package si.matjazcerkvenik.dtools.web;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

import si.matjazcerkvenik.dtools.tools.ping.PingScheduler;

@ManagedBean
@ApplicationScoped
public class PingSchedulerBean {
	
	private PingScheduler pingScheduler = null;
	private boolean active;
	
	
	
	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public void togglePingScheduler() {
		
		if (active) {
			if (pingScheduler == null) {
				pingScheduler = new PingScheduler();
			}
			pingScheduler.startPingScheduler();
			Growl.addGrowlMessage("Ping scheduler started", FacesMessage.SEVERITY_INFO);
		} else {
			// already listening
			pingScheduler.stopPingScheduler();
			Growl.addGrowlMessage("Ping scheduler stopped", FacesMessage.SEVERITY_INFO);
		}
	}
	
//	public void start() {
//		if (pingScheduler == null) {
//			pingScheduler = new PingScheduler();
//		}
//		pingScheduler.startPingScheduler();
//	}
	
	
	
	
}
