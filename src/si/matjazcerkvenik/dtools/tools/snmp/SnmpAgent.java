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

import si.matjazcerkvenik.dtools.tools.snmp.impl.SenderThread;
import si.matjazcerkvenik.dtools.tools.snmp.impl.SnmpTrapSender;
import si.matjazcerkvenik.dtools.web.Growl;
import si.matjazcerkvenik.dtools.xml.DAO;


public class SnmpAgent implements ISnmpAgent {
	
	private String name;
	
	private SnmpTrapSender trapSender;
	private SenderThread senderThread;
	
	private List<TrapDestination> trapDestinationsList;
	
	public SnmpAgent() {
	}
	
	public SnmpAgent(String name, String ip, int port) {
		this.name = name;
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

	@Override
	public void sendTrap() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sendResponse() {
		// TODO Auto-generated method stub
		
	}
	
	public void toggleRunning() {
		
		if (trapSender == null) {
			trapSender = new SnmpTrapSender(null, 6161);
			trapSender.start();
			Growl.addGrowlMessage("Agent running on port " + trapSender.getLocalPort(), FacesMessage.SEVERITY_INFO);
		} else {
			
			if (senderThread != null) {
				toggleSendingAll(); // stop thread before stopping agent
			}
			
			// already listening
			trapSender.stop();
			trapSender = null;
			Growl.addGrowlMessage("Agent stopped", FacesMessage.SEVERITY_INFO);
		}
		
	}
	
	public boolean isListening() {
		if (trapSender != null) {
			return true;
		}
		return false;
	}
	
	public void toggleSendingAll() {
		// TODO
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
		
	}
	
	public void changedName(ValueChangeEvent e) {
		if (e.getOldValue().toString().equalsIgnoreCase(e.getNewValue().toString())) {
			return;
		}
		name = e.getNewValue().toString();
		DAO.getInstance().saveSnmpSimulator();
	}

}
