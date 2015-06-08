package si.matjazcerkvenik.dtools.tools.snmp;

import java.io.IOException;
import java.util.Date;

import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.PDUv1;
import org.snmp4j.Snmp;
import org.snmp4j.TransportMapping;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.IpAddress;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.UdpAddress;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.transport.DefaultUdpTransportMapping;

import si.matjazcerkvenik.dtools.context.DToolsContext;
import si.matjazcerkvenik.dtools.xml.SnmpTrap;
import si.matjazcerkvenik.dtools.xml.VarBind;
import si.matjazcerkvenik.simplelogger.SimpleLogger;

public class SnmpTrapSender {
	
	private SimpleLogger logger;
	
	private Snmp snmp;
	
	public SnmpTrapSender() {
		logger = DToolsContext.getInstance().getLogger();
	}
	
	public void start(String localIp, int localPort) {
		
		try {
			// Create Transport Mapping
			@SuppressWarnings("rawtypes")
			TransportMapping transport = new DefaultUdpTransportMapping();
			transport.listen();
			
			// Send the PDU
			snmp = new Snmp(transport);
			
			logger.info("SnmpTrapSender.start(): agent started on port " + localPort);
			
		} catch (IOException e) {
			logger.error("SnmpTrapSender.start(): IOException", e);
		}
		
	}
	
	public void stop() {
		try {
			snmp.close();
		} catch (IOException e) {
			logger.error("SnmpTrapSender.stop(): IOException", e);
		}
		logger.info("SnmpTrapSender.stop(): stop agent");
	}
	
	public void sendTrap(String ip, int port, SnmpTrap trap) {
		
		// Create Target
		CommunityTarget target = new CommunityTarget();
		target.setCommunity(new OctetString(trap.getCommunity()));
		target.setAddress(new UdpAddress(ip + "/" + port));
		target.setRetries(2);
		target.setTimeout(5000);
		
		if (trap.getVersion().equals("v1")) {
			
			target.setVersion(SnmpConstants.version1);
			
			// Create PDU for V1
			PDUv1 pdu = new PDUv1();
			pdu.setType(PDU.V1TRAP);
			pdu.setEnterprise(new OID(trap.getEnterpriseOid()));
			pdu.setGenericTrap(PDUv1.ENTERPRISE_SPECIFIC);
			pdu.setSpecificTrap(1);
			pdu.setAgentAddress(new IpAddress(trap.getSourceIp()));
			
			for (int i = 0; i < trap.getVarbind().size(); i++) {
				VarBind vb = trap.getVarbind().get(i);
				// TODO create vb according to type
				pdu.add(new VariableBinding(new OID(vb.getOid()), new OctetString(vb.getValue())));
			}
			
			sendTrapV1(pdu, target);
			
			logger.info("SnmpTrapSender.sendTrap(): PDU = " + pdu.toString());
			
		}
		
		if (trap.getVersion().equals("v2c")) {
			
			target.setVersion(SnmpConstants.version2c);
			
			// Create PDU for V2
			PDU pdu = new PDU();
			pdu.setType(PDU.NOTIFICATION);
			
			// need to specify the system up time
			pdu.add(new VariableBinding(SnmpConstants.sysUpTime, new OctetString(new Date().toString())));
			pdu.add(new VariableBinding(SnmpConstants.snmpTrapOID, new OID()));
			pdu.add(new VariableBinding(SnmpConstants.snmpTrapAddress, new IpAddress(trap.getSourceIp())));
			
			// variable binding for Enterprise Specific objects, Severity
			// (should be defined in MIB file)
			pdu.add(new VariableBinding(new OID("1.2.3"), new OctetString("Major")));
			
			sendTrapV2C(pdu, target);
			
			logger.info("SnmpTrapSender.sendTrap(): PDU = " + pdu.toString());
			
		}
		
		
		
	}

	/**
	 * This methods sends the V1 trap
	 */
	private void sendTrapV1(PDUv1 pdu, CommunityTarget target) {
		try {

			// Send the PDU
			System.out.println("Sending V1 Trap to " + target.getAddress());
			snmp.send(pdu, target);
		} catch (Exception e) {
			System.err.println("Error in Sending V1 Trap to " + target.getAddress());
			System.err.println("Exception Message = " + e.getMessage());
		}
	}
	
	/**
	 * This methods sends the V2C trap
	 */
	private void sendTrapV2C(PDU pdu, CommunityTarget target) {
		try {

			// Send the PDU
			System.out.println("Sending V2C Trap to " + target.getAddress());
			snmp.send(pdu, target);
		} catch (Exception e) {
			System.err.println("Error in Sending V2C Trap to " + target.getAddress());
			System.err.println("Exception Message = " + e.getMessage());
		}
	}

}
