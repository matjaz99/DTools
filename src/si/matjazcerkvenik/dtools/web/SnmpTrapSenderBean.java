package si.matjazcerkvenik.dtools.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

import org.primefaces.context.RequestContext;

import si.matjazcerkvenik.dtools.tools.snmp.SnmpTrapSender;
import si.matjazcerkvenik.dtools.xml.DAO;
import si.matjazcerkvenik.dtools.xml.SnmpTrap;

@ManagedBean
@ApplicationScoped
public class SnmpTrapSenderBean {
	
	private String localIp = "localhost";
	private int localPort = 6161;
	
	private String destinationIp = "localhost";
	private int destinationPort = 6162;
	
	private SnmpTrapSender trapSender;
	

	public String getLocalIp() {
		return localIp;
	}

	public void setLocalIp(String localIp) {
		this.localIp = localIp;
	}

	public int getLocalPort() {
		return localPort;
	}

	public void setLocalPort(int localPort) {
		this.localPort = localPort;
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

	
	
	
	
	




	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

	
	
	
	public List<SnmpTrap> getSnmpTrapsList() {
		return DAO.getInstance().loadSnmpTraps().getTraps();
	}
	
	
	public void sendTrap(SnmpTrap trap) {
		if (trapSender == null) {
			Growl.addGrowlMessage("Agent is not running", FacesMessage.SEVERITY_WARN);
			return;
		}
		trapSender.sendTrap(destinationIp, destinationPort, trap);
		Growl.addGrowlMessage("Trap sent", FacesMessage.SEVERITY_INFO);
	}
	
//	public void modify(SnmpTrap trap) {
//		
//		if (trap.getVersion().equals("v1")) {
//			trapNameV1 = new String(trap.getTrapName());
//			community = new String(trap.getCommunity());
//			genericTrap = new String(trap.getGenericTrap());
//			specificTrap = new String(trap.getSpecificTrap());
//			enterpriseOid = new String(trap.getEnterpriseOid());
//			sourceIp = new String(trap.getSourceIp());
//			timestamp = new String(trap.getTimestamp());
//			varbindsV1 = new ArrayList<VarBind>(trap.getVarbind());
//		} else {
//			trapNameV2C = new String(trap.getTrapName());
//			community = new String(trap.getCommunity());
//			sourceIp = new String(trap.getSourceIp());
//			varbindsV2C = new ArrayList<VarBind>(trap.getVarbind());
//		}
//		
//	}
	
	public void deleteTrap(SnmpTrap trap) {
		DAO.getInstance().deleteSnmpTrap(trap);
		Growl.addGrowlMessage("Trap deleted", FacesMessage.SEVERITY_INFO);
	}
	
	
	
	public void toggleRunning() {
		
		if (trapSender == null) {
			trapSender = new SnmpTrapSender();
			trapSender.start(localIp, localPort);
			Growl.addGrowlMessage("Agent running", FacesMessage.SEVERITY_INFO);
		} else {
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
	
	
	
	// TODO use dialog to create new server of client...
	public void newTrapV1() {
        Map<String,Object> options = new HashMap<String, Object>();
        options.put("modal", true);
        options.put("draggable", true);
        options.put("resizable", true);
        options.put("contentWidth", 1024);
         
        RequestContext.getCurrentInstance().openDialog("snmpTrapV1Composer", options, null);
    }
	
	
}
