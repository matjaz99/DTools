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

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ValueChangeEvent;

import si.matjazcerkvenik.dtools.xml.DAO;
import si.matjazcerkvenik.dtools.xml.FtpClient;

@ManagedBean
@SessionScoped
public class FtpClientsBean {

	private String username;
	private String password;
	private String hostname;
	private int port = 21;
	private String protocol = "FTP";
	
	
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

	public String getProtocol() {
		return protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	public void addClientAction() {

		FtpClient c = new FtpClient();
		c.setUsername(username);
		c.setPassword(password);
		c.setHostname(hostname);
		c.setPort(port);
		c.setProtocol(protocol);

		DAO.getInstance().addFtpClient(c);

		username = null;
		password = null;
		hostname = null;
		port = 21;
		protocol = "FTP";

	}

	public void deleteFtpClientAction(FtpClient c) {
		DAO.getInstance().deleteFtpClient(c);
	}

	public List<FtpClient> getFtpClientsList() {
		return DAO.getInstance().loadFtpClients().getFtpClientList();
	}
	
	public void protocolValueChanged(ValueChangeEvent e) {
		protocol = e.getNewValue().toString();
		if (protocol.equals("FTP")) {
			port = 21;
		} else if (protocol.equals("SFTP")) {
			port = 22;
		}
	}
	
	public void toggleFavorite(FtpClient client) {
		client.setFavorite(!client.isFavorite());
		DAO.getInstance().saveFtpClients();
	}
	

}
