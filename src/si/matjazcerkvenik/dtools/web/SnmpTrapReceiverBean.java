package si.matjazcerkvenik.dtools.web;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

import org.snmp4j.PDU;

import si.matjazcerkvenik.dtools.tools.snmp.SnmpTrapReceiver;
import si.matjazcerkvenik.dtools.xml.DAO;

@ManagedBean
@ApplicationScoped
public class SnmpTrapReceiverBean {
	
	private SnmpTrapReceiver trapReceiver = null;
	private String ip = "localhost";
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
			trapReceiver = new SnmpTrapReceiver();
			trapReceiver.start(ip, port);
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
	
	
	public String getReceivedTrapsAsString() {
		
		if (trapReceiver == null) {
			return receivedTrapsAsString;
		}
		receivedTrapsAsString = "";
		Object[] array = trapReceiver.getReceivedTraps().toArray();
		for (int i = 0; i < array.length; i++) {
			PDU pdu = (PDU) array[i];
			receivedTrapsAsString += PDU.getTypeString(pdu.getType()) + " ";
			receivedTrapsAsString += pdu.getVariableBindings() + "\n";
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
