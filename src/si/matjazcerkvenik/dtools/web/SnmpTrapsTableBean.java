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
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;

import si.matjazcerkvenik.dtools.io.DAO;
import si.matjazcerkvenik.dtools.tools.snmp.SnmpAgent;
import si.matjazcerkvenik.dtools.tools.snmp.SnmpSimulator;
import si.matjazcerkvenik.dtools.tools.snmp.SnmpTrap;
import si.matjazcerkvenik.dtools.tools.snmp.TrapsTable;

@ManagedBean
@ViewScoped
public class SnmpTrapsTableBean implements Serializable {
	
	private static final long serialVersionUID = 1643857542322934484L;
	
	private SnmpAgent agent;
	private TrapsTable trapsTable;
	
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
		if (requestParameterMap.containsKey("trapsTableName")) {
			String name = requestParameterMap.get("trapsTableName");
			for (TrapsTable t : agent.getTrapsTableList()) {
				if (t.getName().equals(name)) {
					trapsTable = t;
					trapsTable.setAgent(agent);
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

	public TrapsTable getTrapsTable() {
		return trapsTable;
	}

	public void setTrapsTable(TrapsTable table) {
		this.trapsTable = table;
	}

	public String getDestinationIp() {
//		return destinationIp;
		return trapsTable.getTrapDestinationsList().get(0).getDestinationIp();
	}

	public void setDestinationIp(String destinationIp) {
		this.destinationIp = destinationIp;
		trapsTable.getTrapDestinationsList().get(0).setDestinationIp(destinationIp);
		// TODO save to file
	}

	public int getDestinationPort() {
//		return destinationPort;
		return trapsTable.getTrapDestinationsList().get(0).getDestinationPort();
	}

	public void setDestinationPort(int destinationPort) {
		this.destinationPort = destinationPort;
		trapsTable.getTrapDestinationsList().get(0).setDestinationPort(destinationPort);
	}

	public int getSendInterval() {
//		return sendInterval;
		return trapsTable.getTrapDestinationsList().get(0).getSendInterval();
	}

	public void setSendInterval(int sendInterval) {
		this.sendInterval = sendInterval;
		trapsTable.getTrapDestinationsList().get(0).setSendInterval(sendInterval);
	}
	
	public void changedDestIp(ValueChangeEvent e) {
		if (e.getOldValue().toString().equalsIgnoreCase(e.getNewValue().toString())) {
			return;
		}
		destinationIp = e.getNewValue().toString();
		trapsTable.getTrapDestinationsList().get(0).setDestinationIp(destinationIp);
		DAO.getInstance().saveSnmpTraps(trapsTable);
	}
	
	public void changedDestPort(ValueChangeEvent e) {
		if (e.getOldValue().toString().equalsIgnoreCase(e.getNewValue().toString())) {
			return;
		}
		try {
			destinationPort = Integer.parseInt(e.getNewValue().toString());
			trapsTable.getTrapDestinationsList().get(0).setDestinationPort(destinationPort);
		} catch (NumberFormatException ex) {
			ex.printStackTrace();
		}
		DAO.getInstance().saveSnmpTraps(trapsTable);
	}
	
	public void changedSendInterval(ValueChangeEvent e) {
		if (e.getOldValue().toString().equalsIgnoreCase(e.getNewValue().toString())) {
			return;
		}
		try {
			sendInterval = Integer.parseInt(e.getNewValue().toString());
			trapsTable.getTrapDestinationsList().get(0).setSendInterval(sendInterval);
		} catch (NumberFormatException ex) {
			ex.printStackTrace();
		}
		DAO.getInstance().saveSnmpTraps(trapsTable);
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
//		DAO.getInstance().deleteSnmpTrap(agent, trap);
		trapsTable.deleteTrap(trap);
		DAO.getInstance().saveSnmpTraps(trapsTable);
		Growl.addGrowlMessage("Trap deleted", FacesMessage.SEVERITY_INFO);
	}
	
	public void toggleSendingAll() {
		
		if (agent.getTrapSender() == null) {
			Growl.addGrowlMessage("Agent is not running", FacesMessage.SEVERITY_WARN);
			return;
		}
		
		if (trapsTable.getSenderThread() == null) {
			trapsTable.startSenderThread();
			Growl.addGrowlMessage("Send all traps to " + destinationIp + ":" + destinationPort, FacesMessage.SEVERITY_INFO);
		} else {
			// already running
			trapsTable.stopSenderThread();
			Growl.addGrowlMessage("Stopped sending", FacesMessage.SEVERITY_INFO);
		}
		
	}
	
	public String getSenderThreadStatus() {
		if (trapsTable.getSenderThread() == null) {
			return "Start";
		} else {
			return "Stop";
		}
	}
	
}
