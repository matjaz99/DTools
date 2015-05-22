package si.matjazcerkvenik.dtools.web;

import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import si.matjazcerkvenik.dtools.xml.DAO;
import si.matjazcerkvenik.dtools.xml.FtpClient;
import si.matjazcerkvenik.dtools.xml.FtpClients;
import si.matjazcerkvenik.dtools.xml.Server;
import si.matjazcerkvenik.dtools.xml.SnmpManager;
import si.matjazcerkvenik.dtools.xml.SnmpManagers;
import si.matjazcerkvenik.dtools.xml.SshClient;
import si.matjazcerkvenik.dtools.xml.SshClients;

@ManagedBean
@ViewScoped
public class ServerBean {
	
	private Server server;
	
	@PostConstruct
	public void init() {
		Map<String, Object> requestParameterMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
		server = (Server) requestParameterMap.get("server");
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
	
	public List<SnmpManager> getListOfSnmpManagers() {
		
		SnmpManagers allMngs = DAO.getInstance().loadSnmpManagers();
		List<SnmpManager> tempList = allMngs.getCustomSnmpManagersList(server.getHostname());
		
		return tempList;
		
	}
	
}
