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

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import si.matjazcerkvenik.dtools.io.DAO;
import si.matjazcerkvenik.dtools.tools.snmp.SnmpClient;
import si.matjazcerkvenik.dtools.tools.snmp.SnmpClients;
import si.matjazcerkvenik.dtools.xml.FtpClient;
import si.matjazcerkvenik.dtools.xml.FtpClients;
import si.matjazcerkvenik.dtools.xml.Server;
import si.matjazcerkvenik.dtools.xml.SshClient;
import si.matjazcerkvenik.dtools.xml.SshClients;

@ManagedBean
@ViewScoped
public class ServerBean {
	
	private Server server;
	
	@PostConstruct
	public void init() {
		Map<String, String> requestParameterMap = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		if (requestParameterMap.containsKey("serverName")) {
			String name = requestParameterMap.get("serverName");
			server = DAO.getInstance().findServer(name);
		}
	}

	public Server getServer() {
		return server;
	}

	public void setServer(Server server) {
		this.server = server;
	}
	
	public List<SshClient> getListOfSshClients() {
		
		SshClients allClients = DAO.getInstance().loadSshClients();
		List<SshClient> tempList = allClients.getCustomSshClientsList(server.getHostname());
		
		return tempList;
		
	}
	
	public List<FtpClient> getListOfFtpClients() {
		
		FtpClients allClients = DAO.getInstance().loadFtpClients();
		List<FtpClient> tempList = allClients.getCustomFtpClientsList(server.getHostname());
		
		return tempList;
		
	}
	
	public List<SnmpClient> getListOfSnmpClients() {
		
		SnmpClients allMngs = DAO.getInstance().loadSnmpClients();
		List<SnmpClient> tempList = allMngs.getCustomSnmpClientsList(server.getHostname());
		
		return tempList;
		
	}
	
	public String getResolvedIpAddress() {
		String ip = "n/a";
		try {
			InetAddress address = InetAddress.getByName(server.getHostname());
			ip = address.getHostAddress();
		} catch (UnknownHostException e) {
			ip = "n/a";
			e.printStackTrace();
		} 
		return ip;
	}
	
}
