package si.matjazcerkvenik.dtools.web.webhook;

import java.util.Arrays;
import java.util.List;

public class AmAlert {
	
	private String receiver;
	private String status;
	private List<Alert> alerts;
	//private Alert[] alerts;
//	private String alerts;
	
	public String getReceiver() {
		return receiver;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

//	public Alert[] getAlerts() {
//		return alerts;
//	}
//
//	public void setAlerts(Alert[] alerts) {
//		this.alerts = alerts;
//	}
	
	

	@Override
	public String toString() {
		return "AmAlert [receiver=" + receiver + ", status=" + status + ", alerts=" + alerts + "]";
	}

	public List<Alert> getAlerts() {
		return alerts;
	}

	public void setAlerts(List<Alert> alerts) {
		this.alerts = alerts;
	}

//	public String getAlerts() {
//		return alerts;
//	}
//
//	public void setAlerts(String alerts) {
//		this.alerts = alerts;
//	}
	
	
	
//	private String alertsToString() {
//		String s = "";
//		for (int i = 0; i < alerts.length; i++) {
//			s += "\t" + alerts[i].toString() + "\n";
//		}
//		return s;
//	}
	
	
}
