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

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;

import si.matjazcerkvenik.dtools.tools.icmp.EPingStatus;
import si.matjazcerkvenik.dtools.tools.icmp.IcmpPing;

public class Node {

	private String name;
	private String hostname;
	private String description;
	private String type = "IP_NODE";
	private boolean favorite = false;
	private NodeServices nodeServices;
	private EPingStatus icmpPingStatus = EPingStatus.UNKNOWN;

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
	
	public NodeServices getNodeServices() {
		return nodeServices;
	}

	@XmlElement(name="services")
	public void setNodeServices(NodeServices nodeServices) {
		this.nodeServices = nodeServices;
	}
	
	public void addService(Service service) {
		if (nodeServices == null) {
			nodeServices = new NodeServices();
		}
		nodeServices.addService(service);
	}
	
	public void deleteService(Service service) {
		nodeServices.getServices().remove(service);
	}

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

	public EPingStatus getIcmpPingStatus() {
		return icmpPingStatus;
	}

	@XmlTransient
	public void setIcmpPingStatus(EPingStatus status) {
		this.icmpPingStatus = status;
	}
	
	public boolean isFavorite() {
		return favorite;
	}

	@XmlElement
	public void setFavorite(boolean favorite) {
		this.favorite = favorite;
	}

	/**
	 * Send ping to this node
	 */
	public void updateIcmpStatus() {
		IcmpPing p = new IcmpPing();
		icmpPingStatus = p.ping(hostname);
	}

}
