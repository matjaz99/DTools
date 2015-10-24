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

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import si.matjazcerkvenik.dtools.tools.snmp.impl.SenderThread;

@XmlRootElement
public class TrapsTable {
	
	private String filePath;
	
	private String name;
	private List<SnmpTrap> trapsList;
	private List<TrapDestination> trapDestinationsList;
	
	private SnmpAgent agent; // just a reference to agent
	private SenderThread senderThread;
	
	
	public TrapsTable() {
	}
	
	public TrapsTable(SnmpAgent a) {
		this.agent = a;
	}

	public String getName() {
		return name;
	}

	@XmlAttribute(name = "name")
	public void setName(String name) {
		this.name = name;
	}

	public List<SnmpTrap> getTrapsList() {
		return trapsList;
	}

	@XmlElement(name = "trap")
	public void setTrapsList(List<SnmpTrap> list) {
		this.trapsList = list;
	}
	
	public List<TrapDestination> getTrapDestinationsList() {
		return trapDestinationsList;
	}

	@XmlElement(name="trapDestination")
	public void setTrapDestinationsList(List<TrapDestination> trapDestinationsList) {
		this.trapDestinationsList = trapDestinationsList;
	}
	
	public void addTrap(SnmpTrap t) {
		trapsList.add(t);
	}
	
	public void deleteTrap(SnmpTrap t) {
		trapsList.remove(t);
	}

	public String getFilePath() {
		return filePath;
	}

	@XmlTransient
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	
	public SenderThread getSenderThread() {
		return senderThread;
	}

	@XmlTransient
	public void setSenderThread(SenderThread senderThread) {
		this.senderThread = senderThread;
	}
	
	public void startSenderThread() {
		if (senderThread == null) {
			senderThread = new SenderThread(agent, this);
			senderThread.startThread();
		}
	}
	
	public void stopSenderThread() {
		if (senderThread != null) {
			senderThread.stopThread();
			try {
				senderThread.interrupt();
				senderThread.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			senderThread = null;
		}
	}
	
}
