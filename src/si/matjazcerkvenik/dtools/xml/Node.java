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

package si.matjazcerkvenik.dtools.xml;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

import si.matjazcerkvenik.dtools.tools.ping.PingStatus;

public class Node implements Serializable, Runnable {
	
	private static final long serialVersionUID = 69849387589275333L;
	
	private String name;
	private String hostname;
	private String description;
	private String type = "IP_NODE";
	private boolean favorite = false;
	private NodeServices nodeServices;
	
	/**
	 * Initialize node: configure services
	 */
	public void init() {
		if (nodeServices == null) {
			nodeServices = new NodeServices();
		}
		for (Service service : nodeServices.getServices()) {
			service.init(this);
		}
	}

	public String getName() {
		return name;
	}

	@XmlElement
	public void setName(String name) {
		this.name = name;
	}

	public String getHostname() {
		return hostname;
	}

	@XmlElement
	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	public String getDescription() {
		return description;
	}

	@XmlElement
	public void setDescription(String description) {
		this.description = description;
	}

	public String getType() {
		return type;
	}

	@XmlAttribute
	public void setType(String type) {
		this.type = type;
	}
	
	public boolean isFavorite() {
		return favorite;
	}

	@XmlElement
	public void setFavorite(boolean favorite) {
		this.favorite = favorite;
	}
	
	public NodeServices getNodeServices() {
		return nodeServices;
	}

	@XmlElement(name="services")
	public void setNodeServices(NodeServices nodeServices) {
		this.nodeServices = nodeServices;
	}
	
	
	/**
	 * Add new service
	 * @param service
	 */
	public void addService(Service service) {
		if (nodeServices == null) {
			nodeServices = new NodeServices();
		}
		nodeServices.addService(service);
	}
	
	/**
	 * Delete selected service
	 * @param service
	 */
	public void deleteService(Service service) {
		nodeServices.getServices().remove(service);
	}

	/**
	 * Return node icon (.png)
	 * @return icon
	 */
	public String getIcon() {
		if (type.equals("SERVER")) {
			return "server-icon.png";
		} else if (type.equals("ROUTER")) {
			return "router-icon.png";
		} else if (type.equals("APPLICATION")) {
			return "application-icon.png";
		}
		return "drive_network.png"; // IP_NODE
	}

	/**
	 * Return ICMP service icon (.png)
	 * @return icon
	 */
	public String getIcmpServiceStatusIcon() {
		if (nodeServices.getServices().isEmpty()) {
			return "bullet_disabled.png";
		}
		for (Service s : nodeServices.getServices()) {
			if (s.getMonitoringClass().equals("ICMP_PING")) {
				return s.getStatusIcon();
			}
		}
		return "bullet_black.png";
	}
	
	public PingStatus getIcmpServiceStatus() {
		for (Service s : nodeServices.getServices()) {
			if (s.getMonitoringClass().equals("ICMP_PING")) {
				return s.getStatus();
			}
		}
		return new PingStatus();
	}
	
	

	/**
	 * Send ICMP ping to this node
	 */
	public void testConnection() {
		
		for (Service s : nodeServices.getServices()) {
			if (s.getMonitoringClass().equals("ICMP_PING")) {
				s.pingService();
			}
		}
		
	}
	
	
	@Override
	public void run() {
		for (Service s : nodeServices.getServices()) {
			if (s.isMonitoringActive()) {
				s.pingService();
			}
		}
	}

}
