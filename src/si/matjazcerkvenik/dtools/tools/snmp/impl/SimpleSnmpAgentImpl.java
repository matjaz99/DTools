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
import org.snmp4j.agent.mo.DefaultMOMutableRow2PC;
import org.snmp4j.agent.mo.DefaultMOTable;
import org.snmp4j.agent.mo.MOAccessImpl;
import org.snmp4j.agent.mo.MOColumn;
import org.snmp4j.agent.mo.MOMutableColumn;
import org.snmp4j.agent.mo.MOMutableTableModel;
import org.snmp4j.agent.mo.MOTableIndex;
import org.snmp4j.agent.mo.MOTableModel;
import org.snmp4j.agent.mo.MOTableRow;
import org.snmp4j.agent.mo.MOTableSubIndex;
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
import org.snmp4j.smi.Gauge32;
import org.snmp4j.smi.GenericAddress;
import org.snmp4j.smi.Integer32;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.SMIConstants;
import org.snmp4j.smi.Variable;
import org.snmp4j.transport.TransportMappings;

import si.matjazcerkvenik.dtools.tools.snmp.SnmpAgent;
import si.matjazcerkvenik.dtools.tools.snmp.SnmpTable;

public class SimpleSnmpAgentImpl extends BaseAgent {

	// private String address = "0.0.0.0/6161";
//	private String address = "192.168.1.100/6161";
	private SnmpAgent agent;

	
	public SimpleSnmpAgentImpl(SnmpAgent agent) {
		super(new File(agent.getDirectoryPath() + "/conf.agent"), 
				new File(agent.getDirectoryPath() + "/bootCounter.agent"),
				new CommandProcessor(
						new OctetString(MPv3.createLocalEngineID())));
		this.agent = agent;
	}
	

	public void startSnmpAgent() {
		try {

			init();
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
		try {
			server.register(TableFactory.createStaticIfTable(), null);
			server.register(TableFactory.createMyCustomTable(), null);
			for (int i = 0; i < agent.getSnmpTablesList().size(); i++) {
				SnmpTable tab = agent.getSnmpTablesList().get(i);
				server.register(TableFactory.createTable(tab), null);
			}
		} catch (DuplicateRegistrationException e) {
			e.printStackTrace();
		}
	}

	public void registerManagedObject(ManagedObject mo) {
		try {
			server.register(mo, null);
		} catch (DuplicateRegistrationException ex) {
			throw new RuntimeException(ex);
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

	

	
	
//	public static DefaultMOTable createMyCustomTable() {
//
//		MOTableSubIndex[] subIndexes = new MOTableSubIndex[] { new MOTableSubIndex(
//				SMIConstants.SYNTAX_INTEGER) };
//		MOTableIndex indexDef = new MOTableIndex(subIndexes, false);
//		MOColumn<Variable>[] columns = new MOColumn[3];
//		int c = 0;
//		columns[c++] = new MOColumn<Variable>(c, SMIConstants.SYNTAX_INTEGER,
//				MOAccessImpl.ACCESS_READ_ONLY); // index
//		columns[c++] = new MOColumn<Variable>(c,
//				SMIConstants.SYNTAX_OCTET_STRING, MOAccessImpl.ACCESS_READ_ONLY); // descr
//		columns[c++] = new MOColumn<Variable>(c, SMIConstants.SYNTAX_INTEGER,
//				MOAccessImpl.ACCESS_READ_ONLY); // number
//
//		DefaultMOTable<MOTableRow<Variable>, MOColumn<Variable>, MOTableModel<MOTableRow<Variable>>> myTable = new DefaultMOTable<MOTableRow<Variable>, MOColumn<Variable>, MOTableModel<MOTableRow<Variable>>>(
//				new OID("1.3.6.1.4.1.444.1.8"), indexDef, columns);
//		MOMutableTableModel<MOTableRow<Variable>> model = (MOMutableTableModel<MOTableRow<Variable>>) myTable.getModel();
//		
//		Variable[] rowValues1 = new Variable[] { new Integer32(1),
//				new OctetString("port 1"), new Integer32(6) };
//		Variable[] rowValues2 = new Variable[] { new Integer32(1),
//				new OctetString("port 2"), new Integer32(2) };
//		Variable[] rowValues3 = new Variable[] { new Integer32(1),
//				new OctetString("port 3"), new Integer32(4) };
//		
//		model.addRow(new DefaultMOMutableRow2PC(new OID("1"), rowValues1));
//		model.addRow(new DefaultMOMutableRow2PC(new OID("2"), rowValues2));
//		model.addRow(new DefaultMOMutableRow2PC(new OID("3"), rowValues3));
//
//		myTable.setVolatile(true);
//		return myTable;
//
//	}

}