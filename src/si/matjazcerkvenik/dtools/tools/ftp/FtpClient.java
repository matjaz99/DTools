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

package si.matjazcerkvenik.dtools.tools.ftp;

import javax.xml.bind.annotation.XmlElement;

public class FtpClient {

	private String username;
	private String password;
	private String hostname;
	private int port;
	private String protocol;
	private boolean favorite = false;

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

	public String getProtocol() {
		return protocol;
	}

	@XmlElement
	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}
	
	public boolean isFavorite() {
		return favorite;
	}

	@XmlElement
	public void setFavorite(boolean favorite) {
		this.favorite = favorite;
	}
	
	public String getProtocolLowercase() {
		return protocol.toLowerCase();
	}
	
	public String toUrlString() {
		return protocol.toLowerCase() + "://" + username + "@" + hostname + ":" + port;
	}

}
