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

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;

import org.primefaces.context.RequestContext;

import si.matjazcerkvenik.dtools.io.DAO;
import si.matjazcerkvenik.dtools.tools.NetworkLocation;
import si.matjazcerkvenik.dtools.tools.ftp.FtpClient;
import si.matjazcerkvenik.dtools.tools.ftp.FtpClients;
import si.matjazcerkvenik.dtools.tools.ssh.SshClient;
import si.matjazcerkvenik.dtools.tools.ssh.SshClients;
import si.matjazcerkvenik.dtools.xml.Node;
import si.matjazcerkvenik.dtools.xml.Service;

@ManagedBean
@ViewScoped
public class NodeBean implements Serializable {
	
	private static final long serialVersionUID = 8600188798586688068L;
	
	private Node node;
	private NetworkLocation networkLocation;
	
	private String newServiceName;
	private String monitoringClass = "DISABLED";
	private String monitoringPort;
	private String monitoringUrl;
	private boolean monitoringPortRendered = false;
	private boolean monitoringUrlRendered = false;
	
	
	@PostConstruct
	public void init() {
		Map<String, String> requestParameterMap = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		if (requestParameterMap.containsKey("location")) {
			networkLocation = DAO.getInstance().findNetworkLocation(requestParameterMap.get("location"));
		}
		if (requestParameterMap.containsKey("nodeName")) {
			node = networkLocation.findNode(requestParameterMap.get("nodeName"));
		}
	}

	public Node getNode() {
		return node;
	}

	public void setNode(Node node) {
		this.node = node;
	}
	
	public NetworkLocation getNetworkLocation() {
		return networkLocation;
	}

	public void setNetworkLocation(NetworkLocation networkLocation) {
		this.networkLocation = networkLocation;
	}

	public String getNewServiceName() {
		return newServiceName;
	}

	public void setNewServiceName(String newServiceName) {
		this.newServiceName = newServiceName;
	}

	public String getMonitoringClass() {
		return monitoringClass;
	}

	public void setMonitoringClass(String monitoringClass) {
		this.monitoringClass = monitoringClass;
	}

	public String getMonitoringPort() {
		return monitoringPort;
	}

	public void setMonitoringPort(String monitoringPort) {
		this.monitoringPort = monitoringPort;
	}

	public String getMonitoringUrl() {
		return monitoringUrl;
	}

	public void setMonitoringUrl(String monitoringUrl) {
		this.monitoringUrl = monitoringUrl;
	}

	public boolean isMonitoringPortRendered() {
		return monitoringPortRendered;
	}

	public void setMonitoringPortRendered(boolean monitoringPortRendered) {
		this.monitoringPortRendered = monitoringPortRendered;
	}

	
	
	public boolean isMonitoringUrlRendered() {
		return monitoringUrlRendered;
	}

	public void setMonitoringUrlRendered(boolean monitoringUrlRendered) {
		this.monitoringUrlRendered = monitoringUrlRendered;
	}

	public List<Service> getListOfServices() {
		return node.getNodeServices().getServices();
	}
	
	public List<SshClient> getListOfSshClients() {
		SshClients allClients = DAO.getInstance().loadSshClients();
		List<SshClient> tempList = allClients.getCustomSshClientsList(node.getHostname());
		return tempList;
	}
	
	public List<FtpClient> getListOfFtpClients() {
		FtpClients allClients = DAO.getInstance().loadFtpClients();
		List<FtpClient> tempList = allClients.getCustomFtpClientsList(node.getHostname());
		return tempList;
	}
	
	
	
	/**
	 * Add new service and set monitoring class
	 */
	public void addServiceAction() {
		
		Service s = new Service();
		s.setName(newServiceName);
		s.setMonitoringClass(monitoringClass);
		if (monitoringClass.equals("PORT_PING")) {
			s.addParam("monitoring.port", monitoringPort);
		} else if (monitoringClass.equals("HTTP_PING")) {
			s.addParam("monitoring.url", monitoringUrl);
		}
		node.addService(s);
		node.init(); // is this needed, maybe just init service
		
		DAO.getInstance().saveNetworkLocation(networkLocation); // saveNetworkNodes();
		
		newServiceName = null;
		monitoringClass = "DISABLED";
		monitoringPort = null;
		monitoringPortRendered = false;
		monitoringUrl = null;
		monitoringUrlRendered = false;
		
		RequestContext context = RequestContext.getCurrentInstance();
		context.addCallbackParam("success", true);
	}
	
	/**
	 * Delete selected service
	 * @param service
	 */
	public void deleteServiceAction(Service service) {
		node.deleteService(service);
		DAO.getInstance().saveNetworkLocation(networkLocation);
	}
	
	
	/**
	 * Return node's resolved IP address
	 * @return IP address
	 */
	public String getResolvedIpAddress() {
		return node.resolveIpAddress();
	}
	
	
	/**
	 * Change node's name
	 * @param e
	 */
	public void changedName(ValueChangeEvent e) {
		if (e.getOldValue().toString().equalsIgnoreCase(e.getNewValue().toString())) {
			return;
		}
		node.setName(e.getNewValue().toString());
		DAO.getInstance().saveNetworkLocation(networkLocation);
	}
	
	/**
	 * Change node's hostname
	 * @param e
	 */
	public void changedHostname(ValueChangeEvent e) {
		if (e.getOldValue().toString().equalsIgnoreCase(e.getNewValue().toString())) {
			return;
		}
		node.setHostname(e.getNewValue().toString());
		DAO.getInstance().saveNetworkLocation(networkLocation);
	}
	
	/**
	 * Change node's description
	 * @param e
	 */
	public void changedDescription(ValueChangeEvent e) {
		if (e.getOldValue().toString().equalsIgnoreCase(e.getNewValue().toString())) {
			return;
		}
		node.setDescription(e.getNewValue().toString());
		DAO.getInstance().saveNetworkLocation(networkLocation);
	}
	
	/**
	 * Prepare components to be rendered in 'Add service' dialog when new type of 
	 * monitoring class is selected from dropdown menu.
	 */
	public void monitoringClassChanged() {
		
		if (monitoringClass.equals("ICMP_PING") || monitoringClass.equals("DISABLED")) {
			monitoringPortRendered = false;
			monitoringUrlRendered = false;
		} else if (monitoringClass.equals("PORT_PING")) {
			monitoringPortRendered = true;
			monitoringUrlRendered = false;
		} else if (monitoringClass.equals("HTTP_PING")) {
			monitoringPortRendered = false;
			monitoringUrlRendered = true;
		}
		
	}
	
	
	public void toggleMonitoringEnabled(Service service) {
		service.setMonitoringEnabled(!service.isMonitoringEnabled());
		DAO.getInstance().saveNetworkLocation(networkLocation);
	}
	
}
