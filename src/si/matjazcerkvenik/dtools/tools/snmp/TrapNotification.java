package si.matjazcerkvenik.dtools.tools.snmp;

import org.snmp4j.PDU;

public class TrapNotification {
	
	private String fromIp;
	private PDU pdu;
	private long timestamp;
	
	private int severity;
	private String customText;
	
	public TrapNotification() {
		timestamp = System.currentTimeMillis();
	}
	
	
	public String getFromIp() {
		return fromIp;
	}

	public void setFromIp(String fromIp) {
		this.fromIp = fromIp;
	}

	public PDU getPdu() {
		return pdu;
	}

	public void setPdu(PDU pdu) {
		this.pdu = pdu;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}


	public int getSeverity() {
		return severity;
	}


	public void setSeverity(int severity) {
		this.severity = severity;
	}


	public String getCustomText() {
		return customText;
	}


	public void setCustomText(String customText) {
		this.customText = customText;
	}
	
	@Override
	public String toString() {
		return "PDU from " + fromIp + "@" + timestamp + "severity=" + severity + " " + pdu.toString() + " customText=" + customText;
	}
	
}
