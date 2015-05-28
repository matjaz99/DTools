package si.matjazcerkvenik.dtools.xml;

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
