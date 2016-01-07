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

package si.matjazcerkvenik.dtools.web;

import java.io.Serializable;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.primefaces.context.RequestContext;

import si.matjazcerkvenik.dtools.io.DAO;
import si.matjazcerkvenik.dtools.tools.ping.PingStatus;
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
public class NetworkNodesBean implements Serializable {
	
	private static final long serialVersionUID = -7361094739598080594L;
	
	private String name;
	private String hostname;
	private String description;
	private String type;

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

	/**
	 * Add new node
	 */
	public void addNodeAction() {
		
		Node n = new Node();
		n.setName(name);
		n.setHostname(hostname);
		n.setDescription(description);
		n.setType(type);
		
		// create ICMP ping as default service
		Service s = new Service();
		s.setName("ICMP");
		s.setMonitoringClass("ICMP_PING");
		n.addService(s);
		n.init();
		
		DAO.getInstance().addNode(n);
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
		DAO.getInstance().deleteNode(n);
		Growl.addGrowlMessage("Deleted: " + n.getHostname(), FacesMessage.SEVERITY_INFO);
	}
	
	/**
	 * Let list of nodes
	 * @return list
	 */
	public List<Node> getNodesList() {
		return DAO.getInstance().loadNetworkNodes().getNodesList();
	}
	
	/**
	 * Toggle favorite flag on selected node
	 * @param node
	 */
	public void toggleFavorite(Node node) {
		node.setFavorite(!node.isFavorite());
		DAO.getInstance().saveNetworkNodes();
	}
	
	
	/**
	 * Convert IcmpPingStatus to appropriate icon.
	 * @param status
	 * @return icon
	 */
//	public String getStatusIcon(PingStatus status) {
//		
//		switch (status.getErrorCode()) {
//		case 0:
//			return "bullet_black";
//		case 1:
//			return "bullet_green";
//		default:
////			return "bullet_red";
//			break;
//		}
//		
//		return "bullet_red";
//		
//	}
	
	
	
	
	/**
	 * Send ping on selected node.
	 * @param node
	 */
//	public void testConnection(Node node) {
//		node.testConnection();
//	}
	

}
