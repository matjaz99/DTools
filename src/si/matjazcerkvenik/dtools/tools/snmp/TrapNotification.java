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

import java.io.Serializable;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.snmp4j.CommandResponderEvent;
import org.snmp4j.PDU;
import org.snmp4j.PDUv1;
import org.snmp4j.smi.VariableBinding;

import si.matjazcerkvenik.dtools.tools.md5.MD5Checksum;

public class TrapNotification implements Serializable {
	
	private static final long serialVersionUID = 396526466193707880L;

	/** Sequence number of trap */
	public int no;
	
	/** Unique ID of trap */
	public String uid;
	
	/** Name of SNMP manager that received trap */
	public String trapReceiverName;
	
	/** Peer address (IP/port) */
	public String peerAddress;
	
	/** Peer IP */
	public String peerIp;
	
	/** Resolved peer hostname */
	public String peerHostname;
	
	/** True if trap is version V1 */
	public boolean isV1 = false;
	
	/** True if trap is version V2C */
	public boolean isV2C = false;
	
	/** SNMP version */
	public String snmpVersion;
	
	/** Community */
	public String community;
	
	/** V1 generic trap */
	public int genericTrap;
	
	/** V1 specific trap */
	public int specificTrap;
	
	/** V1 enterprise oid */
	public String enterpriseOid;
	
	/** V2C snmpTrapOid */
	public String snmpTrapOid;
	
	/** V2C sysUpTime */
	public String sysUpTime;
	
	public int errorIndex;
	public int errorStatus;
	public String errorStatusText;
	public int requestId;
	
	/** Node name */
	public String nodeName;
	
	/** Trap name (eg. linkDown) */
	public String trapName;
	
	/** Symbolic name of trap (eg. Link is down) */
	public String name;
	
	/** Severity (0 - unkn, 1 - critical, 2 - major, 3 - minor, 4 - warning, 5 - clear, 6 - info) */
	public int severity;
	
	/** Source info of trap notification */
	public String sourceInfo;
	
	/** Alarm group (Network, Links, Equipment, Power, Fan...) */
	public String group;
	
	/** Location (Moscow, London...) */
	public String location;
	
	/** Timestamp of trap reception*/
	public long timestamp;
	
	/** Extended data 1 */
	public String extDat1;
	
	/** Extended data 2 */
	public String extDat2;
	
	/** Extended data 3 */
	public String extDat3;
	
	/** Extended data 4 */
	public String extDat4;
	
	/** Extended data 5 */
	public String extDat5;
	
	/** Extended data 6 */
	public String extDat6;
	
	/** Extended data 7 */
	public String extDat7;
	
	/** Extended data 8 */
	public String extDat8;
	
	/** Extended data 9 */
	public String extDat9;
	
	/** Ignore this trap */
	public boolean ignore = false;
	
	private PDU pdu;
	
	public VB[] varbinds = new VB[0];
	
	
	
	public String customText;
	
	public TrapNotification(int no, String receiverName, CommandResponderEvent cmdRespEvent) {
		
		this.no = no;
		trapReceiverName = receiverName;
		pdu = cmdRespEvent.getPDU();
		peerAddress = cmdRespEvent.getPeerAddress().toString();
		peerIp = peerAddress.substring(0, peerAddress.indexOf("/"));
		community = new String(cmdRespEvent.getSecurityName());
		timestamp = System.currentTimeMillis();
		
		errorIndex = pdu.getErrorIndex();
		errorStatus = pdu.getErrorStatus();
		errorStatusText = pdu.getErrorStatusText();
		requestId = pdu.getRequestID().toInt();
		
		try {
			InetAddress addr = InetAddress.getByName(peerIp);
			peerHostname = addr.getHostName();
		} catch (UnknownHostException e) {
			peerHostname = peerIp;
		}
		
		Object[] array = pdu.getVariableBindings().toArray();
		for (int i = 0; i < array.length; i++) {
			VariableBinding vb = (VariableBinding) array[i];
			String oid = vb.getOid().toString();
			String val = vb.toValueString();
			addVB(oid, val);
		}
		
		switch (pdu.getType()) {
		case PDU.V1TRAP:
			isV1 = true;
			snmpVersion = "v1";
			PDUv1 pduv1 = (PDUv1) pdu;
			requestId = pdu.getRequestID().toInt();
			genericTrap = pduv1.getGenericTrap();
			specificTrap = pduv1.getSpecificTrap();
			enterpriseOid = pduv1.getEnterprise().toDottedString();
			timestamp = pduv1.getTimestamp();
			break;
		case PDU.TRAP:
			isV2C = true;
			snmpVersion = "v2c";
			for (int i = 0; i < varbinds.length; i++) {
				if (varbinds[i].oid.startsWith("1.3.6.1.6.3.1.1.4.1")) {
					snmpTrapOid = varbinds[i].value;
				}
				if (varbinds[i].oid.startsWith("1.3.6.1.2.1.1.3")) {
					sysUpTime = varbinds[i].value;
				}
			}
			break;
		default:
			snmpVersion = "unkn";
			break;
		}
		
		
		
	}
	
	public void addVB(String oid, String value) {
		VB vb = new VB(oid, value);
		VB[] temp = new VB[varbinds.length + 1];
		int i = 0;
		for (i = 0; i < varbinds.length; i++) {
			temp[i] = varbinds[i];
		}
		varbinds = temp;
		varbinds[i] = vb;
	}
	
	public String getDate() {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(timestamp);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd H:mm:ss");
		return sdf.format(cal.getTime());
	}
	
	
	@Override
	public String toString() {
		return "PDU from " + peerAddress + "@" + timestamp + "severity=" + severity + " " + pdu.toString() + " customText=" + customText;
	}
	
	public String toStringRaw() {
		StringBuffer sb = new StringBuffer();
		sb.append(PDU.getTypeString(pdu.getType()) + " ");
		sb.append(peerIp + " ");
		sb.append(community + " ");
		sb.append(pdu.getVariableBindings());
		return sb.toString();
	}
	
	public String toStringVbs() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < varbinds.length; i++) {
			sb.append(varbinds[i].toString() + "\n");
		}
		return sb.toString();
	}
	
	public String toStringProcessed() {
		// TODO improve!!
		StringBuffer sb = new StringBuffer();
		sb.append(PDU.getTypeString(pdu.getType()) + " ");
		sb.append(peerIp + " ");
		for (int i = 0; i < varbinds.length; i++) {
			sb.append(varbinds[i].oid + "=" + varbinds[i].value + ", ");
		}
		return sb.toString();
	}
	
	
	
	
	/* THE FOLLOWING GETTERS/SETTERS ARE ONLY NEEDED FOR JSF */
	
	
	public int getNo() {
		return no;
	}

//	public void setNo(int no) {
//		this.no = no;
//	}

	public String getUid() {
		if (uid == null) {
			// TODO calculate md5 of all data
			uid = MD5Checksum.getMd5Checksum(toStringRaw());
		}
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getTrapReceiverName() {
		return trapReceiverName;
	}

	public void setTrapReceiverName(String trapReceiverName) {
		this.trapReceiverName = trapReceiverName;
	}

	public String getPeerAddress() {
		return peerAddress;
	}

	public void setPeerAddress(String peerAddress) {
		this.peerAddress = peerAddress;
	}

	public String getPeerIp() {
		return peerIp;
	}

	public void setPeerIp(String peerIp) {
		this.peerIp = peerIp;
	}

	public String getPeerHostname() {
		return peerHostname;
	}

	public void setPeerHostname(String peerHostname) {
		this.peerHostname = peerHostname;
	}

	public String getSnmpVersion() {
		return snmpVersion;
	}

	public void setSnmpVersion(String snmpVersion) {
		this.snmpVersion = snmpVersion;
	}

	public String getNodeName() {
		return nodeName;
	}

	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}

	public String getTrapName() {
		return trapName;
	}

	public void setTrapName(String trapName) {
		this.trapName = trapName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getSeverity() {
		return severity;
	}

	public void setSeverity(int severity) {
		this.severity = severity;
	}

	public String getSourceInfo() {
		return sourceInfo;
	}

	public void setSourceInfo(String sourceInfo) {
		this.sourceInfo = sourceInfo;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public String getExtDat1() {
		return extDat1;
	}

	public void setExtDat1(String extDat1) {
		this.extDat1 = extDat1;
	}

	public String getExtDat2() {
		return extDat2;
	}

	public void setExtDat2(String extDat2) {
		this.extDat2 = extDat2;
	}

	public String getExtDat3() {
		return extDat3;
	}

	public void setExtDat3(String extDat3) {
		this.extDat3 = extDat3;
	}

	public String getExtDat4() {
		return extDat4;
	}

	public void setExtDat4(String extDat4) {
		this.extDat4 = extDat4;
	}

	public String getExtDat5() {
		return extDat5;
	}

	public void setExtDat5(String extDat5) {
		this.extDat5 = extDat5;
	}

	public String getExtDat6() {
		return extDat6;
	}

	public void setExtDat6(String extDat6) {
		this.extDat6 = extDat6;
	}

	public String getExtDat7() {
		return extDat7;
	}

	public void setExtDat7(String extDat7) {
		this.extDat7 = extDat7;
	}

	public String getExtDat8() {
		return extDat8;
	}

	public void setExtDat8(String extDat8) {
		this.extDat8 = extDat8;
	}

	public String getExtDat9() {
		return extDat9;
	}

	public void setExtDat9(String extDat9) {
		this.extDat9 = extDat9;
	}

	public String getCustomText() {
		return customText;
	}

	public void setCustomText(String customText) {
		this.customText = customText;
	}

	public int getGenericTrap() {
		return genericTrap;
	}

	public void setGenericTrap(int genericTrap) {
		this.genericTrap = genericTrap;
	}

	public int getSpecificTrap() {
		return specificTrap;
	}

	public void setSpecificTrap(int specificTrap) {
		this.specificTrap = specificTrap;
	}

	public String getEnterpriseOid() {
		return enterpriseOid;
	}

	public void setEnterpriseOid(String enterpriseOid) {
		this.enterpriseOid = enterpriseOid;
	}

	public String getSnmpTrapOid() {
		return snmpTrapOid;
	}

	public void setSnmpTrapOid(String snmpTrapOid) {
		this.snmpTrapOid = snmpTrapOid;
	}

	public String getSysUpTime() {
		return sysUpTime;
	}

	public void setSysUpTime(String sysUpTime) {
		this.sysUpTime = sysUpTime;
	}

	public boolean isIgnore() {
		return ignore;
	}

	public void setIgnore(boolean ignore) {
		this.ignore = ignore;
	}

	public String getCommunity() {
		return community;
	}

	public boolean isV1() {
		return isV1;
	}

	public boolean isV2C() {
		return isV2C;
	}

	public int getRequestId() {
		return requestId;
	}
	
}
