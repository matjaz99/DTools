/* 
 * Copyright (C) 2015 Matjaz Cerkvenik
 * 
 * DTools is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * DTools is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with DTools. If not, see <http://www.gnu.org/licenses/>.
 * 
 */

package si.matjazcerkvenik.dtools.tools.snmp;

import java.io.IOException;

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

import si.matjazcerkvenik.dtools.context.DToolsContext;
import si.matjazcerkvenik.dtools.xml.SnmpTrap;
import si.matjazcerkvenik.simplelogger.SimpleLogger;

public class SnmpTrapSender {
	
	private SimpleLogger logger;
	
	private Snmp snmp;
	
	public SnmpTrapSender() {
		logger = DToolsContext.getInstance().getLogger();
	}
	
	/**
	 * Start SNMP agent
	 * @param localIp
	 * @param localPort
	 */
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
	
	/**
	 * Stop SNMP agent
	 */
	public void stop() {
		try {
			snmp.close();
		} catch (IOException e) {
			logger.error("SnmpTrapSender.stop(): IOException", e);
		}
		logger.info("SnmpTrapSender.stop(): stop agent");
	}
	
	/**
	 * Send the SNMP trap to manager at selected ip (hostname) and port.
	 * @param ip
	 * @param port
	 * @param trap
	 */
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
//			pdu.setGenericTrap(PDUv1.ENTERPRISE_SPECIFIC);
			pdu.setGenericTrap(trap.getGenericTrap());
			pdu.setSpecificTrap(trap.getSpecificTrap());
			pdu.setAgentAddress(new IpAddress(trap.getSourceIp()));
			
			for (int i = 0; i < trap.getVarbind().size(); i++) {
				pdu.add(trap.getVarbind().get(i).getSnmp4jVarBind());
			}
			
			sendTrapV1(pdu, target);
			
			logger.info("SnmpTrapSender.sendTrap(): PDU = " + pdu.toString());
			
		}
		
		if (trap.getVersion().equals("v2c")) {
			
			target.setVersion(SnmpConstants.version2c);
			
			// Create PDU for V2
			PDU pdu = new PDU();
			pdu.setType(PDU.NOTIFICATION);
			
			// variable binding for Enterprise Specific objects
			for (int i = 0; i < trap.getVarbind().size(); i++) {
				pdu.add(trap.getVarbind().get(i).getSnmp4jVarBind());
			}
			
			sendTrapV2C(pdu, target);
			
			logger.info("SnmpTrapSender.sendTrap(): PDU = " + pdu.toString());
			
		}
		
		
		
	}

	/**
	 * This methods sends the V1 trap
	 */
	private void sendTrapV1(PDUv1 pdu, CommunityTarget target) {
		try {
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
			System.out.println("Sending V2C Trap to " + target.getAddress());
			snmp.send(pdu, target);
		} catch (Exception e) {
			System.err.println("Error in Sending V2C Trap to " + target.getAddress());
			System.err.println("Exception Message = " + e.getMessage());
		}
	}

}
