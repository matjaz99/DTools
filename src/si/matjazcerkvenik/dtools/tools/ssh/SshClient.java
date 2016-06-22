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

package si.matjazcerkvenik.dtools.tools.ssh;

import java.io.IOException;
import java.io.Serializable;
import java.net.UnknownHostException;

import javax.xml.bind.annotation.XmlElement;

import si.matjazcerkvenik.dtools.tools.AuthenticationException;

public class SshClient implements Serializable {

	private static final long serialVersionUID = -9108386214429876662L;
	
	private String username;
	private String password;
	private String hostname;
	private int port;
	private boolean favorite = false;
	
	private SshImpl sshImpl;
	private String statusColor = "black";
	private String statusText = "DISCONNECTED";
	

	public String getUsername() {
		return username;
	}

	@XmlElement
	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	@XmlElement
	public void setPassword(String password) {
		this.password = password;
	}

	public String getHostname() {
		return hostname;
	}

	@XmlElement
	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	public int getPort() {
		return port;
	}

	@XmlElement
	public void setPort(int port) {
		this.port = port;
	}
	
	public boolean isFavorite() {
		return favorite;
	}

	@XmlElement
	public void setFavorite(boolean favorite) {
		this.favorite = favorite;
	}
	
	
	/**
	 * Execute given command. The method also sets <code>statusColor</code> and 
	 * <code>statusText</code> which provide additional data about connection state.
	 * @param command
	 * @return response
	 */
	public String execute(String command) {
		
		String response = "";
		
		try {
			
			if (sshImpl == null) {
				sshImpl = new SshImpl(this);
			}
			if (!sshImpl.isConnected()) {
				sshImpl.connect();
			}
			
			sshImpl.sendCommand(command);
			response = sshImpl.getResponse();
			
			statusColor = "green";
			statusText = "CONNECTED";
			
		} catch (UnknownHostException e) {
			statusColor = "red";
			statusText = "Unknown host!";
		} catch (AuthenticationException e) {
			statusColor = "yellow";
			statusText = "Authentication failed!";
		} catch (IOException e) {
			statusColor = "red";
			statusText = "Connection failed!";
		} catch (Exception e) {
			statusColor = "red";
			statusText = e.getMessage();
		}
		
		return response;
		
	}
	
	public void disconnect() {
		sshImpl.disconnect();
		statusColor = "black";
		statusText = "DISCONNECTED";
	}
	
	public String getStatusColor() {
		return statusColor;
	}

	public String getStatusText() {
		return statusText;
	}
	
	/**
	 * This method is called to reset <code>statusColor</code> and 
	 * <code>statusText</code> when client is disconnected by timeout.
	 */
	public void resetStatus() {
		statusColor = "black";
		statusText = "DISCONNECTED";
	}

	/**
	 * Return URL presentation of this client
	 * @return url string
	 */
	public String toUrlString() {
		return "ssh://" + username + "@" + hostname + ":" + port;
	}

}
