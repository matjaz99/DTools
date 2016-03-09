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

package si.matjazcerkvenik.dtools.web.beans;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

import org.primefaces.context.RequestContext;

import si.matjazcerkvenik.dtools.tools.ping.AutoDiscoverThread;
import si.matjazcerkvenik.dtools.tools.ping.PingScheduler;

@ManagedBean
@ApplicationScoped
public class AppBean {
	
	
	/* AUTO DISCOVERY */
	
	private String fromIp = "192.168.1.0";
	private String toIp = "192.168.1.100";
	
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
		if (adThread.isRunning()) {
			return;
		} else {
			adThread = null;
			adThread = new AutoDiscoverThread();
		}
		adThread.startAutoDiscover(fromIp, toIp);
		Growl.addGrowlMessage("AutoDiscover started", FacesMessage.SEVERITY_INFO);
		RequestContext context = RequestContext.getCurrentInstance();
		context.addCallbackParam("success", true);
	}
	
	public void stopAutoDiscover() {
		adThread.stopAutoDiscover();
		adThread = null;
		Growl.addGrowlMessage("AutoDiscover terminated", FacesMessage.SEVERITY_INFO);
	}
	
	public void toggleAutodiscovery() {
		
	}
	
	public boolean isAutoDiscoveryActive() {
		if (adThread != null && adThread.isRunning()) {
			return true;
		}
		return false;
	}
	
	public int getActiveWorkersCount() {
		if (adThread == null) {
			return 0;
		}
		return adThread.getActiveWorkersCount();
	}

	public int getTotalCount() {
		if (adThread == null) {
			return 0;
		}
		return adThread.getTotalCount();
	}

	public int getDiscoveredNodesCount() {
		if (adThread == null) {
			return 0;
		}
		return adThread.getDiscoveredNodesCount();
	}
	
	
	
	
	
	/* MONITORING - PING SCHEDULER */
	
	
	
	private PingScheduler pingScheduler = null;
	private boolean monitoringActive;

	public boolean isMonitoringActive() {
		return monitoringActive;
	}

	public void setMonitoringActive(boolean monitoringActive) {
		this.monitoringActive = monitoringActive;
	}

	/**
	 * Start or stop ping scheduler
	 */
	public void togglePingScheduler() {
		
		if (monitoringActive) {
			if (pingScheduler == null) {
				pingScheduler = new PingScheduler();
			}
			pingScheduler.startPingScheduler();
			Growl.addGrowlMessage("Monitoring started", FacesMessage.SEVERITY_INFO);
		} else {
			// already listening
			pingScheduler.stopPingScheduler();
			Growl.addGrowlMessage("Monitoring stopped", FacesMessage.SEVERITY_INFO);
		}
	}
	
	
	
	
	
}
