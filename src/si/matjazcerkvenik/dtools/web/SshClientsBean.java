package si.matjazcerkvenik.dtools.web;

import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import si.matjazcerkvenik.dtools.xml.DAO;
import si.matjazcerkvenik.dtools.xml.SshClient;

@ManagedBean
@SessionScoped
public class SshClientsBean {

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

		username = null;
		password = null;
		hostname = null;
		port = 22;

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
