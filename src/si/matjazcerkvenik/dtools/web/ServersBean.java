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

import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.primefaces.context.RequestContext;

import si.matjazcerkvenik.dtools.io.DAO;
import si.matjazcerkvenik.dtools.tools.icmp.EPingStatus;
import si.matjazcerkvenik.dtools.xml.Server;


/**
 * Methods that end with 'Action' are called on button or link click.
 * 
 * @author matjaz
 *
 */
@ManagedBean
@SessionScoped
public class ServersBean {

	private String name;
	private String hostname;
	private String description;

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

	public void addServerAction() {
		
		Server s = new Server();
		s.setName(name);
		s.setHostname(hostname);
		s.setDescription(description);
		s.setIcmpPingStatus(EPingStatus.UNKNOWN);
		
		DAO.getInstance().addServer(s);
		Growl.addGrowlMessage("Created: " + s.getName(), FacesMessage.SEVERITY_INFO);
		
		name = null;
		hostname = null;
		description = null;
		
		RequestContext context = RequestContext.getCurrentInstance();
		context.addCallbackParam("success", true);
		
	}
	
	public void deleteServerAction(Server server) {
		DAO.getInstance().deleteServer(server);
		Growl.addGrowlMessage("Deleted: " + server.getHostname(), FacesMessage.SEVERITY_INFO);
	}
	
	public List<Server> getServersList() {
		return DAO.getInstance().loadServers().getServerList();
	}
	
	
	
	/**
	 * Convert IcmpPingStatus to appropriate icon.
	 * @param status
	 * @return icon
	 */
	public String getStatusIcon(EPingStatus status) {
		
		switch (status) {
		case UP:
			return "bullet_green";
		case DOWN:
			return "bullet_red";
		case UNKNOWN:
			return "bullet_black";
		default:
			break;
		}
		
		return "bullet_black";
		
	}
	
	public void toggleFavorite(Server server) {
		server.setFavorite(!server.isFavorite());
		DAO.getInstance().saveServers();
	}
	
//	public String getFavoriteIcon(boolean isFavorite) {
//		
//		if (isFavorite) {
//			return "star-full";
//		}
//		
//		return "star-empty";
//		
//	}
	
	/**
	 * Send ICMP ping on selected server.
	 * @param server
	 */
	public void sendIcmpPingAction(Server server) {
		server.updateIcmpStatus();
	}
	

}
