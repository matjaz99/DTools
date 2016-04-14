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

package si.matjazcerkvenik.dtools.tools;

import java.io.File;
import java.io.Serializable;

import javax.faces.application.FacesMessage;

import org.primefaces.context.RequestContext;

import si.matjazcerkvenik.dtools.context.DProps;
import si.matjazcerkvenik.dtools.io.DAO;
import si.matjazcerkvenik.dtools.tools.ping.AutoDiscoverThread;
import si.matjazcerkvenik.dtools.tools.ping.PingScheduler;
import si.matjazcerkvenik.dtools.web.beans.Growl;
import si.matjazcerkvenik.dtools.xml.NetworkNodes;
import si.matjazcerkvenik.dtools.xml.Node;

public class NetworkLocation implements Serializable {
	
	private static final long serialVersionUID = -5145195148253372917L;
	
	private File xmlFile;
	private String locationName;
	private NetworkNodes networkNodes;
	
	private AutoDiscoverThread autoDiscovery;
	private String fromIp = "192.168.1.0";
	private String toIp = "192.168.1.100";
	
	private PingScheduler pingScheduler;
	private boolean monitoringActive;
	
	
	
	public File getXmlFile() {
		return xmlFile;
	}
	public void setXmlFile(File xmlFile) {
		this.xmlFile = xmlFile;
	}
	public String getLocationName() {
		return locationName;
	}
	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}
	public NetworkNodes getNetworkNodes() {
		if (networkNodes == null) {
			networkNodes = DAO.getInstance().loadNetworkNodes(xmlFile);
		}
		return networkNodes;
	}
	public void setNetworkNodes(NetworkNodes networkNodes) {
		this.networkNodes = networkNodes;
	}
	
	public void addNode(Node node) {
		if (networkNodes == null) {
			networkNodes = new NetworkNodes();
		}
		networkNodes.addNode(node);
	}
	
	public void deleteNode(Node n) {
		networkNodes.deleteNode(n);
	}
	
	public Node findNode(String name) {
		for (Node n : networkNodes.getNodesList()) {
			if (n.getName().equals(name)) {
				return n;
			}
		}
		return null;
	}
	
	
	
	
	
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

	public void startAutoDiscovery() {
		if (autoDiscovery == null) {
			autoDiscovery = new AutoDiscoverThread(this);
		}
		if (autoDiscovery.isRunning()) {
			return;
		} else {
			autoDiscovery = null;
			autoDiscovery = new AutoDiscoverThread(this);
		}
		autoDiscovery.startAutoDiscover(fromIp, toIp);
		Growl.addGrowlMessage("AutoDiscover started", FacesMessage.SEVERITY_INFO);
		RequestContext context = RequestContext.getCurrentInstance();
		context.addCallbackParam("success", true);
	}
	
	public void stopAutoDiscovery() {
		autoDiscovery.stopAutoDiscover();
		autoDiscovery = null;
		Growl.addGrowlMessage("AutoDiscover terminated", FacesMessage.SEVERITY_INFO);
	}
	
	public void toggleAutodiscovery() {
		
	}
	
	public AutoDiscoverThread getAutoDiscovery() {
		return autoDiscovery;
	}
	
	public boolean isAutoDiscoveryActive() {
		if (autoDiscovery != null && autoDiscovery.isRunning()) {
			return true;
		}
		return false;
	}
	
	public String getAutoDiscoveryCurrentIp() {
		if (autoDiscovery == null) {
			return "-";
		}
		return autoDiscovery.getNextIp();
	}
	
	/**
	 * Get number of workers in queue
	 * @return number of workers in queue
	 */
	public int getActiveWorkersCount() {
		if (autoDiscovery == null) {
			return 0;
		}
		return autoDiscovery.getActiveWorkersCount();
	}

	/**
	 * Get total number of created workers
	 * @return number of created workers
	 */
	public int getScannedAddressesCount() {
		if (autoDiscovery == null) {
			return 0;
		}
		return autoDiscovery.getLoopCount() - autoDiscovery.getActiveWorkersCount();
	}

	/**
	 * Get number of autodiscovered nodes
	 * @returnnumber of autodiscovered nodes
	 */
	public int getDiscoveredNodesCount() {
		if (autoDiscovery == null) {
			return 0;
		}
		return autoDiscovery.getDiscoveredNodesCount();
	}
	
	/**
	 * Get pool size
	 * @return pool size
	 */
	public String getAutoDiscoveryPoolSize() {
		return DProps.getProperty(DProps.AUTO_DISCOVERY_THREAD_POOL_SIZE);
	}
	
	/**
	 * Get queue delay (delay between creating new worker) 
	 * @return queue delay
	 */
	public int getAutoDiscoveryDelay() {
		if (autoDiscovery == null) {
			return 0;
		}
		return autoDiscovery.determineDelay();
	}
	
	/**
	 * Get total number of IP addresses to scan
	 * @return total number of IP addresses to scan
	 */
	public int getTotalAddressesCount() {
		if (autoDiscovery == null) {
			return 0;
		}
		return autoDiscovery.getTotalCount();
	}
	
	/**
	 * Get progress in percentage
	 * @return progress
	 */
	public Integer getAdProgress() {
		if (autoDiscovery == null) {
			return 0;
		}
		
		Integer progress = (int)(getScannedAddressesCount() / (float)getTotalAddressesCount() * 100);
		
        if (progress > 100) progress = 100;
        
        return progress;
    }
	
	
	
	
	
	
	public PingScheduler getPingScheduler() {
		return pingScheduler;
	}
	
	/**
	 * Return true if ping scheduler is running
	 * @return true if active
	 */
	public boolean isMonitoringActive() {
//		return monitoringActive;
		if (pingScheduler != null && pingScheduler.isRunning()) {
			return true;
		}
		return false;
	}

	/**
	 * MonitoringActive flag is changed when monitoring switch is pressed. 
	 * This method is called before {@link #togglePingScheduler() togglePingScheduler} method.
	 * @param monitoringActive
	 */
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
			pingScheduler.startPingScheduler(networkNodes.getNodesList());
			Growl.addGrowlMessage("Monitoring started", FacesMessage.SEVERITY_INFO);
		} else {
			// already listening
			pingScheduler.stopPingScheduler();
			Growl.addGrowlMessage("Monitoring stopped", FacesMessage.SEVERITY_INFO);
		}
	}
	
}
