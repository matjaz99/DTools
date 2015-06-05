package si.matjazcerkvenik.dtools.tools.snmp;

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
import org.snmp4j.transport.DefaultUdpTransportMapping;

import si.matjazcerkvenik.dtools.xml.SnmpTrap;

public class SnmpV1TrapSender {

	/**
	 * This methods sends the V1 trap
	 */
	public void sendTrap(String hostname, int port, SnmpTrap trap) {
		try {
			// Create Transport Mapping
			@SuppressWarnings("rawtypes")
			TransportMapping transport = new DefaultUdpTransportMapping();
			transport.listen();

			// Create Target
			CommunityTarget comtarget = new CommunityTarget();
			comtarget.setCommunity(new OctetString(trap.getCommunity()));
			comtarget.setVersion(SnmpConstants.version1);
			comtarget.setAddress(new UdpAddress(hostname + "/" + port));
			comtarget.setRetries(2);
			comtarget.setTimeout(5000);

			// Create PDU for V1
			PDUv1 pdu = new PDUv1();
			pdu.setType(PDU.V1TRAP);
			pdu.setEnterprise(new OID(trap.getEnterpriseOid()));
			pdu.setGenericTrap(PDUv1.ENTERPRISE_SPECIFIC);
			pdu.setSpecificTrap(1);
			pdu.setAgentAddress(new IpAddress(trap.getSourceIp()));
			// TODO add varbinds

			// Send the PDU
			Snmp snmp = new Snmp(transport);
			System.out.println("Sending V1 Trap to " + hostname + " on Port "
					+ port);
			snmp.send(pdu, comtarget);
			snmp.close();
		} catch (Exception e) {
			System.err.println("Error in Sending V1 Trap to " + hostname
					+ " on Port " + port);
			System.err.println("Exception Message = " + e.getMessage());
		}
	}

}
