package si.matjazcerkvenik.dtools.web;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import si.matjazcerkvenik.dtools.tools.localhost.LocalhostInfo;
import si.matjazcerkvenik.dtools.tools.snmp.SnmpAgent;
import si.matjazcerkvenik.dtools.tools.snmp.impl.SenderThread;
import si.matjazcerkvenik.dtools.xml.DAO;
import si.matjazcerkvenik.dtools.xml.SnmpTrap;

@ManagedBean
@ViewScoped
public class SnmpAgentBean implements Serializable {
	
	private static final long serialVersionUID = -4256173518035867174L;
	
	private SnmpAgent agent;
	
	private String destinationIp = LocalhostInfo.getLocalIpAddress();
	private int destinationPort = 6162;
	private int sendInterval = 13000;
	
	@PostConstruct
	public void init() {
		
		Map<String, Object> requestParameterMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
		agent = (SnmpAgent) requestParameterMap.get("agent");
		
//		Map<String, String> requestParameterMap = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
//		if (requestParameterMap.containsKey("name")) {
//			String name = requestParameterMap.get("name");
//			System.out.println("found " + name);
//			SnmpSimulator sim = DAO.getInstance().loadSnmpSimulator();
//			for (SnmpAgent a : sim.getSnmpAgentsList()) {
//				if (a.getName().equals(name)) {
//					agent = a;
//					break;
//				}
//			}
//		}
	}
	
	public String getName() {
		return agent.getName();
	}
	
	public String getDestinationIp() {
		return destinationIp;
	}

	public void setDestinationIp(String destinationIp) {
		this.destinationIp = destinationIp;
	}

	public int getDestinationPort() {
		return destinationPort;
	}

	public void setDestinationPort(int port) {
		this.destinationPort = port;
	}
	
	public int getSendInterval() {
		return sendInterval;
	}

	public void setSendInterval(int sendInterval) {
		this.sendInterval = sendInterval;
	}

	public List<SnmpTrap> getSnmpTrapsList() {
		return DAO.getInstance().loadSnmpTraps().getTraps();
	}
	
	
	public void sendTrap(SnmpTrap trap) {
		if (agent.getTrapSender() == null) {
			Growl.addGrowlMessage("Agent is not running", FacesMessage.SEVERITY_WARN);
			return;
		}
		agent.getTrapSender().sendTrap(destinationIp, destinationPort, trap);
		Growl.addGrowlMessage("Trap sent to " + destinationIp + ":" + destinationPort, FacesMessage.SEVERITY_INFO);
	}
	
	public void deleteTrap(SnmpTrap trap) {
		DAO.getInstance().deleteSnmpTrap(trap);
		Growl.addGrowlMessage("Trap deleted", FacesMessage.SEVERITY_INFO);
	}
	
	public void toggleSendingAll() {
		
		if (agent.getSenderThread() == null) {
			agent.startSenderThread();
		} else {
			// already running
			agent.stopSenderThread();
		}
		
	}
	
	public String getSenderThreadStatus() {
		if (agent.getSenderThread() == null) {
			return "Start";
		} else {
			return "Stop";
		}
	}
	
}
