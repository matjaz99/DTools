package si.matjazcerkvenik.dtools.tools.snmp;

import java.util.Date;

import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.TransportMapping;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.IpAddress;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.UdpAddress;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.transport.DefaultUdpTransportMapping;

import si.matjazcerkvenik.dtools.xml.SnmpTrap;

public class SnmpV2cTrapSender {

	/**
	 * This methods sends the V2 trap
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
			comtarget.setVersion(SnmpConstants.version2c);
			comtarget.setAddress(new UdpAddress(hostname + "/" + port));
			comtarget.setRetries(2);
			comtarget.setTimeout(5000);

			// Create PDU for V2
			PDU pdu = new PDU();

			// need to specify the system up time
			pdu.add(new VariableBinding(SnmpConstants.sysUpTime,
					new OctetString(new Date().toString())));
			pdu.add(new VariableBinding(SnmpConstants.snmpTrapOID, new OID(
					)));
			pdu.add(new VariableBinding(SnmpConstants.snmpTrapAddress,
					new IpAddress(trap.getSourceIp())));

			// variable binding for Enterprise Specific objects, Severity
			// (should be defined in MIB file)
			pdu.add(new VariableBinding(new OID("1.2.3"), new OctetString(
					"Major")));
			pdu.setType(PDU.NOTIFICATION);

			// Send the PDU
			Snmp snmp = new Snmp(transport);
			System.out.println("Sending V2 Trap to " + hostname + " on Port "
					+ port);
			snmp.send(pdu, comtarget);
			snmp.close();
		} catch (Exception e) {
			System.err.println("Error in Sending V2 Trap to " + hostname
					+ " on Port " + port);
			System.err.println("Exception Message = " + e.getMessage());
		}
	}

}
