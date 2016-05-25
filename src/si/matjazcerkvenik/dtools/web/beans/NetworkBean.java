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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;

import org.primefaces.context.RequestContext;

import si.matjazcerkvenik.dtools.io.DAO;
import si.matjazcerkvenik.dtools.tools.NetworkLocation;
import si.matjazcerkvenik.dtools.xml.NetworkNodes;
import si.matjazcerkvenik.dtools.xml.Node;
import si.matjazcerkvenik.dtools.xml.Service;


/**
 * Methods that end with 'Action' are called on button or link click.
 * 
 * @author matjaz
 *
 */
@ManagedBean
@SessionScoped
public class NetworkBean implements Serializable {
	
	private static final long serialVersionUID = -7361094739598080594L;
	
	private String name;
	private String hostname;
	private String description;
	private String type;
	
	private String fromIp = "192.168.1.0";
	private String toIp = "192.168.2.0";
	
	private NetworkLocation selectedNetworkLocation;
	private String newLocationName;
	
	@PostConstruct
	public void init() {
		Map<String, String> requestParameterMap = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		if (requestParameterMap.containsKey("location")) {
			selectedNetworkLocation = DAO.getInstance().findNetworkLocation(requestParameterMap.get("location"));
		}
		if (selectedNetworkLocation == null) {
			selectedNetworkLocation = DAO.getInstance().loadNetworkLocations().get(0);
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getHostname() {
		return hostname;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
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

	public NetworkLocation getSelectedNetworkLocation() {
		return selectedNetworkLocation;
	}

	public void setSelectedNetworkLocation(String selectedNetworkLocation) {
		this.selectedNetworkLocation = DAO.getInstance().findNetworkLocation(selectedNetworkLocation);
	}
	
	public void changedNetworkLocation(ValueChangeEvent e) {
		selectedNetworkLocation = DAO.getInstance().findNetworkLocation(e.getNewValue().toString());
	}

	public String getNewLocationName() {
		return newLocationName;
	}

	public void setNewLocationName(String newLocationName) {
		this.newLocationName = newLocationName;
	}

	/**
	 * Add new node
	 */
	public void addNodeAction() {
		
		Node n = new Node();
		n.setName(name);
		n.setHostname(hostname);
		n.setDescription(description);
		n.setType(type);
		n.setLocationName(selectedNetworkLocation.getLocationName());
		
		// create ICMP ping as default service
		// TODO move this to new method in Node
		Service s = new Service();
		s.setName("ICMP");
		s.setMonitoringClass("ICMP_PING");
		n.addService(s);
		n.init();
		
		DAO.getInstance().addNode(selectedNetworkLocation, n);
		Growl.addGrowlMessage("Created: " + n.getName(), FacesMessage.SEVERITY_INFO);
		
		name = null;
		hostname = null;
		description = null;
		type = "IP_NODE";
		
		RequestContext context = RequestContext.getCurrentInstance();
		context.addCallbackParam("success", true);
		
	}
	
	/**
	 * Delete selected node
	 * @param n
	 */
	public void deleteNodeAction(Node n) {
		// TODO stop monitoring on node if active
		DAO.getInstance().deleteNode(selectedNetworkLocation, n);
		Growl.addGrowlMessage("Deleted: " + n.getHostname(), FacesMessage.SEVERITY_INFO);
	}
	
	/**
	 * Get list of nodes in selected network location
	 * @return list
	 */
	public List<Node> getNodesList() {
		return selectedNetworkLocation.getNetworkNodes().getNodesList();
	}
	
	/**
	 * Let number of nodes in the list
	 * @return number of nodes
	 */
	public int getNodesListSize() {
		return selectedNetworkLocation.getNetworkNodes().getNodesList().size();
	}
	
	/**
	 * Toggle favorite flag on selected node
	 * @param node
	 */
	public void toggleFavorite(Node node) {
		node.setFavorite(!node.isFavorite());
		DAO.getInstance().saveNetworkLocation(selectedNetworkLocation); // saveNetworkNodes();
	}
	
	/**
	 * Get list of network location names (for dropdown menu)
	 * @return list
	 */
	public List<String> getNetworkLocations() {
		List<String> list = new ArrayList<String>();
		for (int i = 0; i < DAO.getInstance().loadNetworkLocations().size(); i++) {
			list.add(DAO.getInstance().loadNetworkLocations().get(i).getLocationName());
		}
		return list;
	}
	
	/**
	 * Add new location and set focus on this location
	 */
	public void addLocationAction() {
		
		NetworkLocation loc = new NetworkLocation();
		loc.setLocationName(newLocationName);
		loc.setNetworkNodes(new NetworkNodes());
		
		DAO.getInstance().addNetworkLocation(loc);
		Growl.addGrowlMessage("Created: " + loc.getLocationName(), FacesMessage.SEVERITY_INFO);
		
		selectedNetworkLocation = DAO.getInstance().findNetworkLocation(newLocationName);
		newLocationName = null;
		
		RequestContext context = RequestContext.getCurrentInstance();
		context.addCallbackParam("success", true);
		
	}
	
	/**
	 * Delete location. Set the first location in the list as active location.
	 * @return redirect to network page
	 */
	public String deleteLocation() {
		// TODO stop monitoring and autodiscovery before deleting
		DAO.getInstance().deleteNetworkLocation(selectedNetworkLocation);
		selectedNetworkLocation = DAO.getInstance().loadNetworkLocations().get(0);
		return "network?faces-redirect=true";
	}

}
