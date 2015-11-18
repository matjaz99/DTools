package si.matjazcerkvenik.dtools.tools.snmp.impl;

import java.io.File;
import java.io.IOException;

import org.snmp4j.TransportMapping;
import org.snmp4j.agent.BaseAgent;
import org.snmp4j.agent.CommandProcessor;
import org.snmp4j.agent.DuplicateRegistrationException;
import org.snmp4j.agent.MOGroup;
import org.snmp4j.agent.ManagedObject;
import org.snmp4j.agent.io.ImportModes;
import org.snmp4j.agent.mo.snmp.RowStatus;
import org.snmp4j.agent.mo.snmp.SnmpCommunityMIB;
import org.snmp4j.agent.mo.snmp.SnmpNotificationMIB;
import org.snmp4j.agent.mo.snmp.SnmpTargetMIB;
import org.snmp4j.agent.mo.snmp.StorageType;
import org.snmp4j.agent.mo.snmp.VacmMIB;
import org.snmp4j.agent.security.MutableVACM;
import org.snmp4j.mp.MPv3;
import org.snmp4j.security.SecurityLevel;
import org.snmp4j.security.SecurityModel;
import org.snmp4j.security.USM;
import org.snmp4j.smi.Address;
import org.snmp4j.smi.GenericAddress;
import org.snmp4j.smi.Integer32;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.Variable;
import org.snmp4j.transport.TransportMappings;

import si.matjazcerkvenik.dtools.tools.snmp.SnmpAgent;
import si.matjazcerkvenik.dtools.tools.snmp.SnmpTable;
import si.matjazcerkvenik.simplelogger.SimpleLogger;

public class SimpleSnmpAgentImpl extends BaseAgent {

	private SimpleLogger logger;
	
	private SnmpAgent agent;

	
	public SimpleSnmpAgentImpl(SnmpAgent agent) {
		super(new File(agent.getDirectoryPath() + "/conf.agent"), 
				new File(agent.getDirectoryPath() + "/bootCounter.agent"),
				new CommandProcessor(
						new OctetString(MPv3.createLocalEngineID())));
		this.agent = agent;
		logger = agent.getLogger();
	}
	

	public void startSnmpAgent() {
		try {

			init();
			logger.info("SimpleSnmpAgentImpl:startSnmpAgent(): initialized");
			unregisterManagedObjects();
			loadConfig(ImportModes.REPLACE_CREATE);
			addShutdownHook();
			getServer().addContext(new OctetString("public"));

			finishInit();
			run();
			sendColdStartNotification();

			while (true) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException ex1) {
					break;
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void initTransportMappings() throws IOException {
		transportMappings = new TransportMapping[1];
		Address addr = GenericAddress.parse(agent.getLocalIp() + "/" + agent.getLocalPort());
		TransportMapping<?> tm = TransportMappings.getInstance()
				.createTransportMapping(addr);
		transportMappings[0] = tm;
	}

	@Override
	protected void registerManagedObjects() {
		// register custom MOs
		registerManagedObject(TableFactory.createStaticIfTable());
		registerManagedObject(TableFactory.createMyCustomTable());
		for (int i = 0; i < agent.getSnmpTablesList().size(); i++) {
			SnmpTable tab = agent.getSnmpTablesList().get(i);
			if (tab.getMetadata().getColumnsMetaList().size() > 0) {
				if (tab.getMetadata().isEnabled()) {
					registerManagedObject(TableFactory.createTable(tab));
				}
			}
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
	protected void unregisterManagedObjects() {
		// here we should unregister those objects previously registered...
		unregisterManagedObject(this.getSnmpv2MIB());
	}

	public void unregisterManagedObject(MOGroup moGroup) {
		moGroup.unregisterMOs(server, getContext(moGroup));
	}

	@Override
	protected void addNotificationTargets(SnmpTargetMIB targetMIB,
			SnmpNotificationMIB notificationMIB) {

		// TODO do i need this
		// targetMIB.addDefaultTDomains();
		//
		// targetMIB.addTargetAddress(new OctetString("notificationV2c"),
		// TransportDomains.transportDomainUdpIpv4, new OctetString(
		// new UdpAddress("127.0.0.1/162").getValue()), 200, 1,
		// new OctetString("notify"), new OctetString("v2c"),
		// StorageType.permanent);

	}

	/**
	 * Minimal View based Access Control
	 * 
	 * http://www.faqs.org/rfcs/rfc2575.html
	 */
	@Override
	protected void addViews(VacmMIB vacm) {

		vacm.addGroup(SecurityModel.SECURITY_MODEL_SNMPv2c, new OctetString(
				"cpublic"), new OctetString("v1v2group"),
				StorageType.nonVolatile);

		vacm.addAccess(new OctetString("v1v2group"), new OctetString("public"),
				SecurityModel.SECURITY_MODEL_ANY, SecurityLevel.NOAUTH_NOPRIV,
				MutableVACM.VACM_MATCH_EXACT, new OctetString("fullReadView"),
				new OctetString("fullWriteView"), new OctetString(
						"fullNotifyView"), StorageType.nonVolatile);

		vacm.addViewTreeFamily(new OctetString("fullReadView"), new OID("1.3"),
				new OctetString(), VacmMIB.vacmViewIncluded,
				StorageType.nonVolatile);
	}

	@Override
	protected void addCommunities(SnmpCommunityMIB communityMIB) {
		Variable[] com2sec = new Variable[] { new OctetString("public"), // community
																			// name
				new OctetString("cpublic"), // security name
				getAgent().getContextEngineID(), // local engine ID
				new OctetString("public"), // default context name
				new OctetString(), // transport tag
				new Integer32(StorageType.nonVolatile), // storage type
				new Integer32(RowStatus.active) // row status
		};

		// MOTableRow<?> row = communityMIB.getSnmpCommunityEntry().createRow(
		// new OctetString("public2public").toSubIndex(true), com2sec);
		// communityMIB.getSnmpCommunityEntry()
		// .addRow((SnmpCommunityEntryRow) row);

		SnmpCommunityMIB.SnmpCommunityEntryRow row = communityMIB
				.getSnmpCommunityEntry().createRow(
						new OctetString("public2public").toSubIndex(true),
						com2sec);
		communityMIB.getSnmpCommunityEntry().addRow(row);
	}

	@Override
	protected void addUsmUser(USM arg0) {
		// no implementation
		// only for v3
	}
	

}