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

import javax.faces.application.FacesMessage;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

import si.matjazcerkvenik.dtools.tools.localhost.LocalhostInfo;
import si.matjazcerkvenik.dtools.tools.snmp.impl.TrapReceiver;
import si.matjazcerkvenik.dtools.tools.snmp.impl.TrapNotification;
import si.matjazcerkvenik.dtools.xml.DAO;

@ManagedBean
@ApplicationScoped
public class SnmpTrapReceiverBean implements Serializable {
	
	private static final long serialVersionUID = 1750695129629929329L;
	
	private TrapReceiver trapReceiver = null;
	private String ip = LocalhostInfo.getLocalIpAddress();
	private int port = 6162;
	
	private String receivedTrapsAsString;
	
	
	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public void toggleListening() {
		
		if (trapReceiver == null) {
			trapReceiver = new TrapReceiver("default", ip, port);
			trapReceiver.start();
			Growl.addGrowlMessage("Start listening for traps", FacesMessage.SEVERITY_INFO);
		} else {
			// already listening
			trapReceiver.stop();
			trapReceiver = null;
			Growl.addGrowlMessage("Stop listening", FacesMessage.SEVERITY_INFO);
		}
		
	}
	
	public boolean isListening() {
		if (trapReceiver != null) {
			return true;
		}
		return false;
	}
	
	
	
	
	public TrapReceiver getTrapReceiver() {
		return trapReceiver;
	}

	public String getReceivedTrapsAsString() {
		
		if (trapReceiver == null) {
			return receivedTrapsAsString;
		}
		receivedTrapsAsString = "";
		Object[] array = trapReceiver.getReceivedTrapNotifications().toArray();
		for (int i = 0; i < array.length; i++) {
			TrapNotification tn = (TrapNotification) array[i];
			receivedTrapsAsString += tn.toStringRaw() + "\n";
		}
//		receivedTrapsAsString = receivedTrapsAsString.replaceAll(", ", "\n");
		return receivedTrapsAsString;
	}

	public void setReceivedTrapsAsString(String receivedTrapsAsString) {
		this.receivedTrapsAsString = receivedTrapsAsString;
	}
	
	public void clearData() {
		if (trapReceiver != null) {
			trapReceiver.clearReceivedTraps();
		}
		receivedTrapsAsString = null;
	}
	
	public void saveData() {
		String file = DAO.getInstance().saveReceivedTrapsAsTxt("snmp-traps", getReceivedTrapsAsString());
		Growl.addGrowlMessage("Saved as " + file, FacesMessage.SEVERITY_INFO);
	}
	
}
