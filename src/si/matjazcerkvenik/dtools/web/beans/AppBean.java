package si.matjazcerkvenik.dtools.web.beans;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

import org.primefaces.context.RequestContext;

import si.matjazcerkvenik.dtools.tools.ping.AutoDiscoverThread;

@ManagedBean
@ApplicationScoped
public class AppBean {
	
	private String fromIp = "192.168.1.0";
	private String toIp = "192.168.30.0";
	
	private AutoDiscoverThread adThread;
	
	
	public String getFromIp() {
		return fromIp;
	}

	public void setFromIp(String fromIp) {
		this.fromIp = fromIp;
	}

	public String getToIp() {
		return toIp;
	}

	public void setToIp(String toIp) {
		this.toIp = toIp;
	}

	public void startAutoDiscover() {
		if (adThread == null) {
			adThread = new AutoDiscoverThread();
		}
		if (adThread.isThreadFinished()) {
			adThread = null;
			adThread = new AutoDiscoverThread();
		}
		adThread.startAutoDiscover(fromIp, toIp);
		RequestContext context = RequestContext.getCurrentInstance();
		context.addCallbackParam("success", true);
	}
	
	public void stopAutoDiscover() {
		adThread.stopAutoDiscover();
		adThread = null;
	}
	
}
