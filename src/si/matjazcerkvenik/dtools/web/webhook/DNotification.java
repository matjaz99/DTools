package si.matjazcerkvenik.dtools.web.webhook;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DNotification {
	
	/** Unique ID of notification */
	private String uid;
	/** Notification ID identifies the same events */
	private String nid;
	private long timestamp;
	/** Counter of identical events (according to nid) */
	private int counter;
	/** timestamp of last occurrence */
	private long lastTimestamp;
	/** Source who sent the notification */
	private String source;
	private String userAgent;
	private String alertdomain;
	private String alertname;
	private String alerttype;
	private String instance;
	private String severity;
	private String priority;
	private String summary;
	private String description;
	private String status;
	
	
	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getNid() {
		return nid;
	}

	public void setNid(String nid) {
		this.nid = nid;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
	
	public String getFormatedTimestamp() {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(timestamp);
		String format = "yyyy/MM/dd H:mm:ss";
		if (System.currentTimeMillis() - timestamp > 1 * 60 * 60 * 1000) {
			format = "yyyy/MM/dd H:mm:ss";
		} else {
			format = "H:mm:ss";
		}
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(cal.getTime());
	}

	public int getCounter() {
		return counter;
	}

	public void setCounter(int counter) {
		this.counter = counter;
	}

	public long getLastTimestamp() {
		return lastTimestamp;
	}

	public void setLastTimestamp(long lastTimestamp) {
		this.lastTimestamp = lastTimestamp;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getUserAgent() {
		return userAgent;
	}

	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}

	public String getAlertdomain() {
		return alertdomain;
	}

	public void setAlertdomain(String alertdomain) {
		this.alertdomain = alertdomain;
	}

	public String getAlertname() {
		return alertname;
	}

	public void setAlertname(String alertname) {
		this.alertname = alertname;
	}

	public String getAlerttype() {
		return alerttype;
	}

	public void setAlerttype(String alerttype) {
		this.alerttype = alerttype;
	}

	public String getInstance() {
		return instance;
	}

	public void setInstance(String instance) {
		this.instance = instance;
	}

	public String getSeverity() {
		return severity;
	}

	public void setSeverity(String severity) {
		this.severity = severity;
	}

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
}
