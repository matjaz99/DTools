package si.matjazcerkvenik.dtools.tools.snmp.impl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.snmp4j.MessageDispatcher;
import org.snmp4j.MessageDispatcherImpl;
import org.snmp4j.TransportMapping;
import org.snmp4j.agent.AgentConfigManager;
import org.snmp4j.agent.DefaultMOContextScope;
import org.snmp4j.agent.DefaultMOServer;
import org.snmp4j.agent.DuplicateRegistrationException;
import org.snmp4j.agent.MOQuery;
import org.snmp4j.agent.MOQueryWithSource;
import org.snmp4j.agent.MOScope;
import org.snmp4j.agent.MOServer;
import org.snmp4j.agent.ManagedObject;
import org.snmp4j.agent.cfg.EngineBootsCounterFile;
import org.snmp4j.agent.io.DefaultMOPersistenceProvider;
import org.snmp4j.agent.io.MOInput;
import org.snmp4j.agent.io.MOInputFactory;
import org.snmp4j.agent.io.prop.PropertyMOInput;
import org.snmp4j.agent.mo.DefaultMOFactory;
import org.snmp4j.agent.mo.DefaultMOTable;
import org.snmp4j.agent.mo.MOAccessImpl;
import org.snmp4j.agent.mo.MOColumn;
import org.snmp4j.agent.mo.MOScalar;
import org.snmp4j.agent.mo.MOTableModel;
import org.snmp4j.agent.mo.MOTableRow;
import org.snmp4j.agent.mo.snmp.TimeStamp;
import org.snmp4j.agent.mo.util.VariableProvider;
import org.snmp4j.agent.request.Request;
import org.snmp4j.agent.request.RequestStatus;
import org.snmp4j.agent.request.SubRequest;
import org.snmp4j.agent.request.SubRequestIterator;
import org.snmp4j.log.Log4jLogFactory;
import org.snmp4j.log.LogAdapter;
import org.snmp4j.log.LogFactory;
import org.snmp4j.log.LogLevel;
import org.snmp4j.mp.MPv3;
import org.snmp4j.security.SecurityProtocols;
import org.snmp4j.smi.Address;
import org.snmp4j.smi.GenericAddress;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.Variable;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.transport.TransportMappings;
import org.snmp4j.util.ThreadPool;

import si.matjazcerkvenik.dtools.tools.snmp.SnmpAgent;
import si.matjazcerkvenik.dtools.tools.snmp.SnmpTable;

public class AdvancedSnmpAgentImpl implements VariableProvider, ISnmpAgentImpl {

	static {
		LogFactory.setLogFactory(new Log4jLogFactory());
		org.apache.log4j.BasicConfigurator.configure();
		LogFactory.getLogFactory().getRootLogger().setLogLevel(LogLevel.ALL);
	}

	private LogAdapter logger = LogFactory
			.getLogger(AdvancedSnmpAgentImpl.class);

	protected AgentConfigManager agent;
	protected MOServer server;
	private String configFile = "conf.agent";
	private File bootCounterFile = new File("bootCounter.agent");

	// supported MIBs
	protected Modules modules;

	protected Properties tableSizeLimits;

	private SnmpAgent dtAgent;

	public AdvancedSnmpAgentImpl(SnmpAgent ag) {
		dtAgent = ag;
		initTestAgent();
	}

	public void initTestAgent() {

		server = new DefaultMOServer();
		MOServer[] moServers = new MOServer[] { server };

		// fill properties
		final Properties props = new Properties();
		InputStream configInputStream = AdvancedSnmpAgentImpl.class
				.getResourceAsStream("DToolsSnmpAgentConfig.properties");
		if (configInputStream != null) {
			// System.out.println("MyTestAgentConfig.properties found");
		}
		try {
			props.load(configInputStream);
		} catch (IOException ex) {
			ex.printStackTrace();
		}

		MOInputFactory configurationFactory = new MOInputFactory() {
			public MOInput createMOInput() {
				return new PropertyMOInput(props, AdvancedSnmpAgentImpl.this);
			}
		};

		InputStream tableSizeLimitsInputStream = AdvancedSnmpAgentImpl.class
				.getResourceAsStream("DToolsSnmpAgentTableSizeLimits.properties");
		tableSizeLimits = new Properties();
		try {
			tableSizeLimits.load(tableSizeLimitsInputStream);
		} catch (IOException ex) {
			ex.printStackTrace();
		}

		// MessageDispatcher
		MessageDispatcher messageDispatcher = new MessageDispatcherImpl();
		List<String> addr = new ArrayList<String>();
		// format: udp:192.168.1.100/6161
		addr.add("udp:" + dtAgent.getLocalIp() + "/" + dtAgent.getLocalPort());
		addListenAddresses(messageDispatcher, addr);

		agent = new AgentConfigManager(new OctetString(
				MPv3.createLocalEngineID()), messageDispatcher, null,
				moServers, ThreadPool.create("Agent", 3),
				configurationFactory, new DefaultMOPersistenceProvider(
						moServers, configFile), new EngineBootsCounterFile(
						bootCounterFile));

	}

	protected void addListenAddresses(MessageDispatcher md,
			List<String> addresses) {
		for (String addressString : addresses) {
			Address address = GenericAddress.parse(addressString);
			if (address == null) {
				logger.fatal("Could not parse address string '" + addressString
						+ "'");
				return;
			}
			TransportMapping tm = TransportMappings.getInstance()
					.createTransportMapping(address);
			if (tm != null) {
				md.addTransportMapping(tm);
			} else {
				logger.warn("No transport mapping available for address '"
						+ address + "'.");
			}
		}
	}

	@Override
	public void startSnmpAgent() {
		agent.initialize();
		registerMIBs();
		// add proxy forwarder
		agent.setupProxyForwarder();
		// apply table size limits
		agent.setTableSizeLimits(tableSizeLimits);
		// now continue agent setup and launch it.
		agent.run();
	}
	
	@Override
	public void stopSnmpAgent() {
		agent.shutdown();
	}

	public void registerMIBs() {
		if (modules == null) {
			modules = new Modules(DefaultMOFactory.getInstance());
			modules.getSnmp4jDemoMib()
					.getSnmp4jDemoEntry()
					.addMOTableRowListener(
							new MyTableRowListener(agent, modules));
			((TimeStamp) modules.getSnmp4jDemoMib().getSnmp4jDemoEntry()
					.getColumn(Snmp4jDemoMib.idxSnmp4jDemoEntryCol4))
					.setSysUpTime(agent.getSysUpTime());
		}
		try {
			modules.registerMOs(server, null);
			// Some alternatives
			// Register a scalar with your OID in your enterprise subtree:
			MOScalar<Variable> myScalar = new MOScalar<Variable>(new OID(
					"1.3.6.1.4.1.4444.1.55.1.1"),
					MOAccessImpl.ACCESS_READ_CREATE, new OctetString("myText"));
			server.register(myScalar, null);
			// Register a table with a string index and a single integer payload 
			// column a row status column to

//			DefaultMOTable customTable1 = TableFactory.createMyCustomTable();
//			server.register(customTable1, null);
			
			for (int i = 0; i < dtAgent.getSnmpTablesList().size(); i++) {
				SnmpTable tab = dtAgent.getSnmpTablesList().get(i);
				if (tab.getMetadata().getColumnsMetaList().size() > 0) {
					if (tab.getMetadata().isEnabled()) {
						registerManagedObject(TableFactory.createTable(tab));
					}
				}
			}

		} catch (DuplicateRegistrationException drex) {
			logger.error("Duplicate registration: " + drex.getMessage() + "."
					+ " MIB object registration may be incomplete!", drex);
		}

	}
	
	public void registerManagedObject(ManagedObject mo) {
		try {
			server.register(mo, null);
		} catch (DuplicateRegistrationException ex) {
			logger.error("SimpleSnmpAgentImpl:registerManagedObject(): DuplicateRegistrationException: " + mo.toString(), ex);
		}
	}

	@Override
	public Variable getVariable(String name) {
		OID oid;
		OctetString context = null;
		int pos = name.indexOf(':');
		if (pos >= 0) {
			context = new OctetString(name.substring(0, pos));
			oid = new OID(name.substring(pos + 1, name.length()));
		} else {
			oid = new OID(name);
		}
		final DefaultMOContextScope scope = new DefaultMOContextScope(context,
				oid, true, oid, true);
		MOQuery query = new MOQueryWithSource(scope, false, this);
		ManagedObject mo = server.lookup(query);
		if (mo != null) {
			final VariableBinding vb = new VariableBinding(oid);
			final RequestStatus status = new RequestStatus();
			SubRequest req = new SubRequest() {
				private boolean completed;
				private MOQuery query;

				public boolean hasError() {
					return false;
				}

				public void setErrorStatus(int errorStatus) {
					status.setErrorStatus(errorStatus);
				}

				public int getErrorStatus() {
					return status.getErrorStatus();
				}

				public RequestStatus getStatus() {
					return status;
				}

				public MOScope getScope() {
					return scope;
				}

				public VariableBinding getVariableBinding() {
					return vb;
				}

				public Request getRequest() {
					return null;
				}

				public Object getUndoValue() {
					return null;
				}

				public void setUndoValue(Object undoInformation) {
				}

				public void completed() {
					completed = true;
				}

				public boolean isComplete() {
					return completed;
				}

				public void setTargetMO(ManagedObject managedObject) {
				}

				public ManagedObject getTargetMO() {
					return null;
				}

				public int getIndex() {
					return 0;
				}

				public void setQuery(MOQuery query) {
					this.query = query;
				}

				public MOQuery getQuery() {
					return query;
				}

				public SubRequestIterator<SubRequest> repetitions() {
					return null;
				}

				public void updateNextRepetition() {
				}

				public Object getUserObject() {
					return null;
				}

				public void setUserObject(Object userObject) {
				}

			};
			mo.get(req);
			return vb.getVariable();
		}
		return null;
	}
}
