package si.matjazcerkvenik.dtools.web;

import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

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

	public String addServerAction() {
		
		Server s = new Server();
		s.setName(name);
		s.setHostname(hostname);
		s.setDescription(description);
		s.setIcmpPingStatus(EPingStatus.UNKNOWN);
		
		DAO.getInstance().addServer(s);
		
		name = null;
		hostname = null;
		description = null;
		
		addGrowlMessage("Created: " + s.getHostname(), FacesMessage.SEVERITY_INFO);
		
		return "network";
		
	}
	
	public void deleteServerAction(Server server) {
		DAO.getInstance().deleteServer(server);
		addGrowlMessage("Deleted: " + server.getHostname(), FacesMessage.SEVERITY_INFO);
	}
	
	public List<Server> getServersList() {
		return DAO.getInstance().loadServers().getServerList();
	}
	
	public String openServerDetails(Server s) {
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("server", s);
		return "serverDetails";
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
	
	public void toggleFavorite(Server server) {
		server.setFavorite(!server.isFavorite());
		DAO.getInstance().saveServers();
	}
	
//	public String getFavoriteIcon(boolean isFavorite) {
//		
//		if (isFavorite) {
//			return "star-full";
//		}
//		
//		return "star-empty";
//		
//	}
	
	/**
	 * Send ICMP ping on selected server.
	 * @param server
	 */
	public void sendIcmpPingAction(Server server) {
		server.updateIcmpStatus();
	}
	
	public void addGrowlMessage(String summary, Severity severity) {
        FacesMessage message = new FacesMessage(severity, summary,  null);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

}
