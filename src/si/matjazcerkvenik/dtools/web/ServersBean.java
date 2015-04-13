package si.matjazcerkvenik.dtools.web;

import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import si.matjazcerkvenik.dtools.xml.DAO;
import si.matjazcerkvenik.dtools.xml.EPingStatus;
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
		
		name = null;
		hostname = null;
		description = null;
		
	}
	
	public void deleteServerAction(Server server) {
		DAO.getInstance().deleteServer(server);
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
	
	/**
	 * Send ICMP ping on selected server.
	 * @param server
	 */
	public void sendIcmpPingAction(Server server) {
		server.updateIcmpStatus();
	}

}
