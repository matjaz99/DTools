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

import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.event.ValueChangeEvent;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;

import si.matjazcerkvenik.dtools.tools.localhost.LocalhostInfo;
import si.matjazcerkvenik.dtools.tools.snmp.impl.SenderThread;
import si.matjazcerkvenik.dtools.tools.snmp.impl.SnmpTrapSender;
import si.matjazcerkvenik.dtools.web.Growl;
import si.matjazcerkvenik.dtools.xml.DAO;


public class SnmpAgent implements ISnmpAgent {
	
	private String name;
	private String localIp;
	private int localPort = 6161;
	
	private String destinationIp = LocalhostInfo.getLocalIpAddress();
	private int destinationPort = 6162;
	
	private SnmpTrapSender trapSender;
	
	private SenderThread senderThread;
	private int sendInterval = 13000;
	
	private boolean active = false;
	
	private List<TrapDestination> trapDestinationsList;
	
	public SnmpAgent() {
	}
	
	public SnmpAgent(String name, String ip, int port) {
		this.name = name;
		this.localIp = ip;
		if (trapDestinationsList == null) {
			trapDestinationsList = new ArrayList<TrapDestination>();
		}
		trapDestinationsList.add(new TrapDestination(ip, port));
	}
	
	public String getName() {
		return name;
	}

	@XmlElement
	public void setName(String name) {
		this.name = name;
	}

	public SnmpTrapSender getTrapSender() {
		return trapSender;
	}

	@XmlElement
	public void setTrapSender(SnmpTrapSender trapSender) {
		this.trapSender = trapSender;
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
	
	public String getDestinationIp() {
		return destinationIp;
	}

	@XmlElement
	public void setDestinationIp(String destinationIp) {
		this.destinationIp = destinationIp;
	}

	public int getDestinationPort() {
		return destinationPort;
	}

	@XmlElement
	public void setDestinationPort(int port) {
		this.destinationPort = port;
	}
	
	public int getSendInterval() {
		return sendInterval;
	}
	
	@XmlElement
	public void setSendInterval(int sendInterval) {
		this.sendInterval = sendInterval;
	}
	
	/**
	 * Return true if receiver is listening
	 * @return active
	 */
	public boolean isActive() {
		return active;
	}

	@XmlTransient
	public void setActive(boolean active) {
		this.active = active;
	}
	

	public SenderThread getSenderThread() {
		return senderThread;
	}

	@XmlTransient
	public void setSenderThread(SenderThread senderThread) {
		this.senderThread = senderThread;
	}

	@Override
	public void sendTrap() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sendResponse() {
		// TODO Auto-generated method stub
		
	}
	
	public boolean start() {
		if (trapSender == null) {
			trapSender = new SnmpTrapSender(localIp, localPort);
			active = trapSender.start();
		}
		return active;
	}
	
	public void stop() {
		if (trapSender != null) {
			trapSender.stop();
			trapSender = null;
			stopSenderThread();
		}
	}
	
	public void startSenderThread() {
		if (senderThread == null) {
			senderThread = new SenderThread(this);
			senderThread.startThread();
		}
	}
	
	public void stopSenderThread() {
		if (senderThread == null) {
			senderThread.stopThread();
			try {
				senderThread.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			senderThread = null;
		}
	}
	
//	public void toggleRunning() {
//		
//		if (trapSender == null) {
//			trapSender = new SnmpTrapSender(localIp, localPort);
//			trapSender.start();
//			Growl.addGrowlMessage("Agent running on port " + localPort, FacesMessage.SEVERITY_INFO);
//		} else {
//			
//			if (senderThread != null) {
//				toggleSendingAll(); // stop thread before stopping agent
//			}
//			
//			// already listening
//			trapSender.stop();
//			trapSender = null;
//			Growl.addGrowlMessage("Agent stopped", FacesMessage.SEVERITY_INFO);
//		}
//		
//	}
	
//	public boolean isListening() {
//		if (trapSender != null) {
//			return true;
//		}
//		return false;
//	}
	
	
	/**
	 * Start sender thread
	 */
//	public void toggleSendingAll() {
//		// TODO
//		if (senderThread == null) {
//			senderThread = new SenderThread(this);
//			senderThread.startThread();
//		} else {
//			// already running
//			senderThread.stopThread();
//			try {
//				senderThread.join();
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
//			senderThread = null;
//		}
//		
//	}
	
	public void changedName(ValueChangeEvent e) {
		if (e.getOldValue().toString().equalsIgnoreCase(e.getNewValue().toString())) {
			return;
		}
		name = e.getNewValue().toString();
		DAO.getInstance().saveSnmpSimulator();
	}
	
	public void changedIp(ValueChangeEvent e) {
		if (e.getOldValue().toString().equalsIgnoreCase(e.getNewValue().toString())) {
			return;
		}
		localIp = e.getNewValue().toString();
		DAO.getInstance().saveSnmpSimulator();
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
		DAO.getInstance().saveSnmpSimulator();
	}
	
	

}
