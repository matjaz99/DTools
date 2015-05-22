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
	

}
