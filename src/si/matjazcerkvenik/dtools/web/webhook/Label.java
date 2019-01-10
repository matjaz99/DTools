package si.matjazcerkvenik.dtools.web.webhook;

public class Label {
	
	private String alertdomain;
	private String alertname;
	private String alerttype;
	private String instance;
	private String job;
	private String monitor;
	private String severity;
	
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
	public String getJob() {
		return job;
	}
	public void setJob(String job) {
		this.job = job;
	}
	public String getMonitor() {
		return monitor;
	}
	public void setMonitor(String monitor) {
		this.monitor = monitor;
	}
	public String getSeverity() {
		return severity;
	}
	public void setSeverity(String severity) {
		this.severity = severity;
	}
	
	@Override
	public String toString() {
		return "Label [alertdomain=" + alertdomain + ", alertname=" + alertname + ", alerttype=" + alerttype
				+ ", instance=" + instance + ", job=" + job + ", monitor=" + monitor + ", severity=" + severity + "]";
	}
	
	
	
	
}
