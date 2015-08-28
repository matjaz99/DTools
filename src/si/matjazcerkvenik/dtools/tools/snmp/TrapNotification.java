package si.matjazcerkvenik.dtools.tools.snmp;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import org.snmp4j.PDU;

public class TrapNotification {
		
	private int no;
	private String uid;
	private String trapReceiverName;
	private String peerAddress;
	private String nodeName;
	private String trapName;
	private String name;
	private int severity;
	private String sourceInfo;
	private String group;
	private String location;
	private long timestamp;
	
	private String extDat1;
	private String extDat2;
	private String extDat3;
	private String extDat4;
	private String extDat5;
	private String extDat6;
	private String extDat7;
	private String extDat8;
	private String extDat9;
	
	private PDU pdu;
	
	
	
	private String customText;
	
	public TrapNotification(int no, String trapReceiverName, PDU pdu) {
		this.no = no;
		this.trapReceiverName = trapReceiverName;
		this.pdu = pdu;
		timestamp = System.currentTimeMillis();
	}
	


	public int getNo() {
		return no;
	}



	public String getUid() {
		return uid;
	}



	/**
	 * Unique ID of trap
	 * @param uid
	 */
	public void setUid(String uid) {
		this.uid = uid;
	}



	/**
	 * SNMP manager that received trap
	 * @return trapReceiverName
	 */
	public String getTrapReceiverName() {
		return trapReceiverName;
	}



	public String getPeerAddress() {
		return peerAddress;
	}

	public void setPeerAddress(String address) {
		this.peerAddress = address;
	}

	public String getPeerIp() {
		return peerAddress.substring(0, peerAddress.indexOf("/"));
	}
	
	/**
	 * Return resolved hostname from IP.
	 * @return hostname
	 */
	public String getPeerHostname() {
		// TODO
		return null;
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



	/**
	 * Trap name (eg. linkDown)
	 * @param trapName
	 */
	public void setTrapName(String trapName) {
		this.trapName = trapName;
	}



	public String getName() {
		return name;
	}



	/**
	 * Symbolic name of trap (eg. Link is down)
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}



	public int getSeverity() {
		return severity;
	}


	/**
	 * Severity (0 - unkn, 1 - critical, 2, 3, 4 - warning, 5 - clear, 6 - info)
	 * @param severity
	 */
	public void setSeverity(int severity) {
		this.severity = severity;
	}
	
	public String getSourceInfo() {
		return sourceInfo;
	}



	public void setSourceInfo(String sourceInfo) {
		this.sourceInfo = sourceInfo;
	}



	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
	
	public String getDate() {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(timestamp);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd H:mm:ss");
		return sdf.format(cal.getTime());
	}
	
	public String getGroup() {
		return group;
	}


	/**
	 * Alarm group (Network, Links, Equipment, Power, Fan...)
	 * @param group
	 */
	public void setGroup(String group) {
		this.group = group;
	}



	public String getLocation() {
		return location;
	}



	/**
	 * Location (Moscow, London...)
	 * @param location
	 */
	public void setLocation(String location) {
		this.location = location;
	}



	public boolean isV1() {
		return pdu.getType() == PDU.V1TRAP;
	}
	
	public boolean isV2C() {
		return pdu.getType() == PDU.TRAP;
	}
	
	public String getVersion() {
		switch (pdu.getType()) {
		case PDU.V1TRAP:
			return "v1";
		case PDU.TRAP:
			return "v2c";
		default:
			return "unkn";
		}
	}

	
	
	

	public PDU getPdu() {
		return pdu;
	}

	public void setPdu(PDU pdu) {
		this.pdu = pdu;
	}

	/**
	 * Return map of OIDs (OID - value)
	 * @return
	 */
	public HashMap<String, String> getVarbinds() {
		// TODO
		return null;
	}


	


	public String getExtDat1() {
		return extDat1;
	}



	/**
	 * Custom data from OIDs
	 * @param extDat1
	 */
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
	
	@Override
	public String toString() {
		return "PDU from " + peerAddress + "@" + timestamp + "severity=" + severity + " " + pdu.toString() + " customText=" + customText;
	}
	
}
