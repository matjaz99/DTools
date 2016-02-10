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
import si.matjazcerkvenik.dtools.tools.ssh.SshClient;

@ManagedBean
@SessionScoped
public class SshClientsBean implements Serializable {
	
	private static final long serialVersionUID = -1511672829559602579L;
	
	private String username;
	private String password;
	private String hostname;
	private int port = 22;
	
	
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getHostname() {
		return hostname;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public void addClientAction() {

		SshClient c = new SshClient();
		c.setUsername(username);
		c.setPassword(password);
		c.setHostname(hostname);
		c.setPort(port);

		DAO.getInstance().addSshClient(c);
		Growl.addGrowlMessage("Created SSH client: " + c.toUrlString(), FacesMessage.SEVERITY_INFO);

		username = null;
		password = null;
		hostname = null;
		port = 22;
		
		RequestContext context = RequestContext.getCurrentInstance();
		context.addCallbackParam("success", true);

	}

	public void deleteSshClientAction(SshClient c) {
		DAO.getInstance().deleteSshClient(c);
	}

	public List<SshClient> getSshClientsList() {
		return DAO.getInstance().loadSshClients().getSshClientList();
	}
	
	public void toggleFavorite(SshClient client) {
		client.setFavorite(!client.isFavorite());
		DAO.getInstance().saveSshClients();
	}
	
	

}
