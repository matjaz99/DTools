package si.matjazcerkvenik.dtools.web;

import java.io.Serializable;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;

import si.matjazcerkvenik.dtools.tools.snmp.SnmpAgent;
import si.matjazcerkvenik.dtools.tools.snmp.SnmpSimulator;
import si.matjazcerkvenik.dtools.tools.snmp.SnmpTrap;
import si.matjazcerkvenik.dtools.tools.snmp.SnmpTraps;
import si.matjazcerkvenik.dtools.xml.DAO;

@ManagedBean
@ViewScoped
public class SnmpAgentTrapListBean implements Serializable {
	
	private static final long serialVersionUID = 1643857542322934484L;
	
	private SnmpAgent agent;
	private SnmpTraps traps;
	
	private String destinationIp;
	private int destinationPort;
	private int sendInterval;
	
	@PostConstruct
	public void init() {
		
		Map<String, String> requestParameterMap = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		if (requestParameterMap.containsKey("agent")) {
			String name = requestParameterMap.get("agent");
			SnmpSimulator sim = DAO.getInstance().loadSnmpSimulator();
			for (SnmpAgent a : sim.getSnmpAgentsList()) {
				if (a.getName().equals(name)) {
					agent = a;
					break;
				}
			}
		}
		if (requestParameterMap.containsKey("name")) {
			String name = requestParameterMap.get("name");
			for (SnmpTraps a : agent.getSnmpTraps()) {
				if (a.getName().equals(name)) {
					traps = a;
					break;
				}
			}
		}
		
	}

	public SnmpAgent getAgent() {
		return agent;
	}

	public void setAgent(SnmpAgent agent) {
		this.agent = agent;
	}

	public SnmpTraps getTraps() {
		return traps;
	}

	public void setTraps(SnmpTraps traps) {
		this.traps = traps;
	}

	public String getDestinationIp() {
//		return destinationIp;
		return agent.getTrapDestinationsList().get(0).getDestinationIp();
	}

	public void setDestinationIp(String destinationIp) {
		this.destinationIp = destinationIp;
		agent.getTrapDestinationsList().get(0).setDestinationIp(destinationIp);
		// TODO save to file
	}

	public int getDestinationPort() {
//		return destinationPort;
		return agent.getTrapDestinationsList().get(0).getDestinationPort();
	}

	public void setDestinationPort(int destinationPort) {
		this.destinationPort = destinationPort;
		agent.getTrapDestinationsList().get(0).setDestinationPort(destinationPort);
	}

	public int getSendInterval() {
//		return sendInterval;
		return agent.getTrapDestinationsList().get(0).getSendInterval();
	}

	public void setSendInterval(int sendInterval) {
		this.sendInterval = sendInterval;
		agent.getTrapDestinationsList().get(0).setSendInterval(sendInterval);
	}
	
	public void changedDestIp(ValueChangeEvent e) {
		if (e.getOldValue().toString().equalsIgnoreCase(e.getNewValue().toString())) {
			return;
		}
		destinationIp = e.getNewValue().toString();
		agent.getTrapDestinationsList().get(0).setDestinationIp(destinationIp);
		DAO.getInstance().saveAgentMetadata(agent);
	}
	
	public void changedDestPort(ValueChangeEvent e) {
		if (e.getOldValue().toString().equalsIgnoreCase(e.getNewValue().toString())) {
			return;
		}
		try {
			destinationPort = Integer.parseInt(e.getNewValue().toString());
			agent.getTrapDestinationsList().get(0).setDestinationPort(destinationPort);
		} catch (NumberFormatException ex) {
			ex.printStackTrace();
		}
		DAO.getInstance().saveAgentMetadata(agent);
	}
	
	public void changedSendInterval(ValueChangeEvent e) {
		if (e.getOldValue().toString().equalsIgnoreCase(e.getNewValue().toString())) {
			return;
		}
		try {
			sendInterval = Integer.parseInt(e.getNewValue().toString());
			agent.getTrapDestinationsList().get(0).setSendInterval(sendInterval);
		} catch (NumberFormatException ex) {
			ex.printStackTrace();
		}
		DAO.getInstance().saveAgentMetadata(agent);
	}
	
	/**
	 * Send selected trap to configured destination IP and port.
	 * @param trap
	 */
	public void sendTrap(SnmpTrap trap) {
		if (agent.getTrapSender() == null) {
			Growl.addGrowlMessage("Agent is not running", FacesMessage.SEVERITY_WARN);
			return;
		}
		agent.getTrapSender().sendTrap(getDestinationIp(), getDestinationPort(), trap);
		Growl.addGrowlMessage("Trap sent to " + getDestinationIp() + ":" + getDestinationPort(), FacesMessage.SEVERITY_INFO);
	}
	
	public String openTrap(SnmpTrap trap) {
		if (trap.getVersion().equals("v1")) {
			return "snmpTrapV1Composer";
		}
		return "snmpTrapV2CComposer";
	}
	
	public void deleteTrap(SnmpTrap trap) {
//		DAO.getInstance().deleteSnmpTrap(agent, trap); TODO
		Growl.addGrowlMessage("Trap deleted", FacesMessage.SEVERITY_INFO);
	}
	
	public void toggleSendingAll() {
		
		if (agent.getTrapSender() == null) {
			Growl.addGrowlMessage("Agent is not running", FacesMessage.SEVERITY_WARN);
			return;
		}
		
		if (agent.getSenderThread() == null) {
			agent.startSenderThread();
			Growl.addGrowlMessage("Send all traps to " + destinationIp + ":" + destinationPort, FacesMessage.SEVERITY_INFO);
		} else {
			// already running
			agent.stopSenderThread();
			Growl.addGrowlMessage("Stopped sending", FacesMessage.SEVERITY_INFO);
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
