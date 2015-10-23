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

package si.matjazcerkvenik.dtools.web;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;

import si.matjazcerkvenik.dtools.tools.localhost.LocalhostInfo;
import si.matjazcerkvenik.dtools.tools.snmp.SnmpAgent;
import si.matjazcerkvenik.dtools.tools.snmp.SnmpTrap;
import si.matjazcerkvenik.dtools.xml.DAO;

//@ManagedBean
//@ViewScoped
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
		
		destinationIp = agent.getTrapDestinationsList().get(0).getDestinationIp();
		destinationPort = agent.getTrapDestinationsList().get(0).getDestinationPort();
		sendInterval = agent.getTrapDestinationsList().get(0).getSendInterval();
		
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
//		return destinationIp;
		return agent.getTrapDestinationsList().get(0).getDestinationIp();
	}

	public void setDestinationIp(String destinationIp) {
		this.destinationIp = destinationIp;
		agent.getTrapDestinationsList().get(0).setDestinationIp(destinationIp);
	}

	public int getDestinationPort() {
//		return destinationPort;
		return agent.getTrapDestinationsList().get(0).getDestinationPort();
	}

	public void setDestinationPort(int port) {
		this.destinationPort = port;
		agent.getTrapDestinationsList().get(0).setDestinationPort(port);
	}
	
	public int getSendInterval() {
		return sendInterval;
	}

	public void setSendInterval(int sendInterval) {
		this.sendInterval = sendInterval;
		agent.getTrapDestinationsList().get(0).setSendInterval(sendInterval);
	}
	
	@Deprecated
	public void changedDestIp(ValueChangeEvent e) {
		if (e.getOldValue().toString().equalsIgnoreCase(e.getNewValue().toString())) {
			return;
		}
		destinationIp = e.getNewValue().toString();
		agent.getTrapDestinationsList().get(0).setDestinationIp(destinationIp);
		DAO.getInstance().saveAgentMetadata(agent);
	}
	
	@Deprecated
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
	
	@Deprecated
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

//	public List<SnmpTrap> getSnmpTrapsList() {
//		return DAO.getInstance().loadSnmpTraps().getTraps();
//		return agent.getSnmpTraps().getTraps();
//	}
	
	
	/**
	 * Send selected trap to configured destination IP and port.
	 * @param trap
	 */
	@Deprecated
	public void sendTrap(SnmpTrap trap) {
		if (agent.getTrapSender() == null) {
			Growl.addGrowlMessage("Agent is not running", FacesMessage.SEVERITY_WARN);
			return;
		}
		agent.getTrapSender().sendTrap(destinationIp, destinationPort, trap);
		Growl.addGrowlMessage("Trap sent to " + destinationIp + ":" + destinationPort, FacesMessage.SEVERITY_INFO);
	}
	
	@Deprecated
	public void deleteTrap(SnmpTrap trap) {
		DAO.getInstance().deleteSnmpTrap(trap);
		Growl.addGrowlMessage("Trap deleted", FacesMessage.SEVERITY_INFO);
	}
	
	@Deprecated
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
	
	@Deprecated
	public String getSenderThreadStatus() {
		if (agent.getSenderThread() == null) {
			return "Start";
		} else {
			return "Stop";
		}
	}
	
}
