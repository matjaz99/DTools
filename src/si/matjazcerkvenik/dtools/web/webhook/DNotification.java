package si.matjazcerkvenik.dtools.web.webhook;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DNotification {
	
	private String uid;
	private long timestamp;
	private String alertdomain;
	private String alertname;
	private String alerttype;
	private String instance;
	private String severity;
	private String summary;
	private String description;
	private String status;
	
	
	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
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
