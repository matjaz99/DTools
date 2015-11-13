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

package si.matjazcerkvenik.dtools.tools.snmp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.event.ValueChangeEvent;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import si.matjazcerkvenik.dtools.tools.snmp.impl.SimpleSnmpAgentImpl;
import si.matjazcerkvenik.dtools.tools.snmp.impl.TrapSender;
import si.matjazcerkvenik.dtools.xml.DAO;

@XmlRootElement
public class SnmpAgent implements Serializable {
	
	private static final long serialVersionUID = -2488608414261579629L;
	
	private String directoryPath;
	
	private String name;
	private String localIp;
	private int localPort = 6161;
	private String description;
	
	private TrapSender trapSender;
	private SimpleSnmpAgentImpl agentImpl;
	private boolean active = false;
	
	private List<TrapsTable> trapsTableList;
	private List<SnmpTable> snmpTablesList;
	
	
	
	public SnmpAgent() {
	}
	
	public SnmpAgent(String name, String ip, int port) {
		this.name = name;
		this.localIp = ip;
		this.localPort = port;
	}
	
	public String getDirectoryPath() {
		return directoryPath;
	}

	@XmlTransient
	public void setDirectoryPath(String directoryPath) {
		this.directoryPath = directoryPath;
	}

	public String getName() {
		return name;
	}

	@XmlAttribute
	public void setName(String name) {
		this.name = name;
	}
	
	public String getLocalIp() {
		return localIp;
	}

	@XmlElement
	public void setLocalIp(String localIp) {
		this.localIp = localIp;
	}

	public int getLocalPort() {
		return localPort;
	}

	@XmlElement
	public void setLocalPort(int localPort) {
		this.localPort = localPort;
	}
	
	public String getDescription() {
		return description;
	}

	@XmlElement
	public void setDescription(String description) {
		this.description = description;
	}
	

	/**
	 * Get sender of traps; only one sender per agent exists.
	 * @return trapSender
	 */
	public TrapSender getTrapSender() {
		return trapSender;
	}

	@XmlTransient
	public void setTrapSender(TrapSender trapSender) {
		this.trapSender = trapSender;
	}
	
	

	public List<TrapsTable> getTrapsTableList() {
		return trapsTableList;
	}

	@XmlTransient
	public void setTrapsTableList(List<TrapsTable> list) {
		this.trapsTableList = list;
	}
	
	public void addNewTrapsTable(TrapsTable tt) {
		if (trapsTableList == null) {
			trapsTableList = new ArrayList<TrapsTable>();
		}
		trapsTableList.add(tt);
	}
	
	

	public List<SnmpTable> getSnmpTablesList() {
		return snmpTablesList;
	}

	@XmlTransient
	public void setSnmpTablesList(List<SnmpTable> snmpTablesList) {
		this.snmpTablesList = snmpTablesList;
	}
	
	public void addNewSnmpTable(SnmpTable table) {
		if (snmpTablesList == null) {
			snmpTablesList = new ArrayList<SnmpTable>();
		}
		snmpTablesList.add(table);
	}
	
	public SnmpTable findSnmpTable(String name) {
		for (SnmpTable t : snmpTablesList) {
			if (t.getName().equals(name)) return t;
		}
		return null;
	}
	

	/**
	 * Return true if trapSender is running
	 * @return active
	 */
	public boolean isActive() {
		return active;
	}

	@XmlTransient
	public void setActive(boolean active) {
		this.active = active;
	}
	

	
	
	
	/**
	 * Start agent on localIp and local Port.
	 * @return true if successfully started
	 */
	public boolean start() {
		if (trapSender == null) {
			trapSender = new TrapSender(localIp, localPort);
			active = trapSender.start();
		}
		if (agentImpl == null) {
			agentImpl = new SimpleSnmpAgentImpl(this);
			agentImpl.startSnmpAgent();
		}
		return active;
	}
	
	
	/**
	 * Stop agent
	 */
	public void stop() {
		if (trapSender != null) {
			trapSender.stop();
			trapSender = null;
			for (TrapsTable tt : trapsTableList) {
				tt.stopSenderThread();
			}
//			active = false;          where is this set to false if not here???
		}
		if (agentImpl != null) {
			agentImpl.stop();
			agentImpl = null;
		}
	}
	
	
	
	
	public void changedName(ValueChangeEvent e) {
		if (e.getOldValue().toString().equalsIgnoreCase(e.getNewValue().toString())) {
			return;
		}
		name = e.getNewValue().toString();
		DAO.getInstance().saveAgentMetadata(this);
		
	}
	
	public void changedIp(ValueChangeEvent e) {
		if (e.getOldValue().toString().equalsIgnoreCase(e.getNewValue().toString())) {
			return;
		}
		localIp = e.getNewValue().toString();
		DAO.getInstance().saveAgentMetadata(this);
	}
	
	public void changedPort(ValueChangeEvent e) {
		if (e.getOldValue().toString().equalsIgnoreCase(e.getNewValue().toString())) {
			return;
		}
		try {
			localPort = Integer.parseInt(e.getNewValue().toString());
		} catch (NumberFormatException ex) {
			ex.printStackTrace();
		}
		DAO.getInstance().saveAgentMetadata(this);
	}

}
