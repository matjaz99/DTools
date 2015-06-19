package si.matjazcerkvenik.dtools.web;

import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import si.matjazcerkvenik.dtools.context.DToolsContext;
import si.matjazcerkvenik.dtools.tools.localhost.LocalhostInfo;
import si.matjazcerkvenik.dtools.xml.DAO;
import si.matjazcerkvenik.dtools.xml.SnmpTrap;
import si.matjazcerkvenik.dtools.xml.VarBind;

@ManagedBean
@ViewScoped
public class SnmpTrapV2CComposer {

	private String trapName;
	private String community = "public";
	private String sourceIp = LocalhostInfo.getLocalIpAddress();
	private List<VarBind> varbinds;

	public String getTrapName() {
		return trapName;
	}

	public void setTrapName(String trapName) {
		this.trapName = trapName;
	}

	public String getCommunity() {
		return community;
	}

	public void setCommunity(String community) {
		this.community = community;
	}

	public String getSourceIp() {
		return sourceIp;
	}

	public void setSourceIp(String sourceIp) {
		this.sourceIp = sourceIp;
	}

	public List<VarBind> getVarbinds() {
		if (varbinds == null) {
			varbinds = new ArrayList<VarBind>();
			varbinds.add(new VarBind("sysUpTime", "1.3.6.1.2.1.1.3.0", VarBind.TYPE_TIMETICKS, "" + DToolsContext.getSysUpTime()/10));
			varbinds.add(new VarBind("snmpTrapOid", "1.3.6.1.6.3.1.1.4.1.0", VarBind.TYPE_OCTET_STRING, "9.9.9"));
			varbinds.add(new VarBind("sourceIp", "1.3.6.1.6.3.18.1.3.0", VarBind.TYPE_IP_ADDRESS, LocalhostInfo.getLocalIpAddress()));
		}
		return varbinds;
	}

	public void setVarbinds(List<VarBind> varbinds) {
		this.varbinds = varbinds;
	}
	
	public void addNewOid() {
		varbinds.add(new VarBind("oidName", "1.", VarBind.TYPE_OCTET_STRING, "value"));
	}
	
	public void removeOid(VarBind vb) {
		varbinds.remove(vb);
	}
	
	public String saveTrap() {
		
		if (trapName == null || trapName.trim().isEmpty()) {
			Growl.addGrowlMessage("Missing trap name", FacesMessage.SEVERITY_WARN);
			return null;
		}
		List<SnmpTrap> list = DAO.getInstance().loadSnmpTraps().getTraps();
		for (SnmpTrap snmpTrap : list) {
			if (snmpTrap.getTrapName().equals(trapName)) {
				Growl.addGrowlMessage("Name already exists", FacesMessage.SEVERITY_WARN);
				return null;
			}
		}
		
		SnmpTrap trap = new SnmpTrap();
		trap.setTrapName(trapName);
		trap.setVersion("v2c");
		trap.setCommunity(community);
		trap.setSourceIp(sourceIp);
		trap.setVarbind(varbinds);
		
		DAO.getInstance().addSnmpTrap(trap);
		resetTrap();
		Growl.addGrowlMessage("Trap saved", FacesMessage.SEVERITY_INFO);
		
		return "snmpAgent";
	}
	
	public void resetTrap() {
		trapName = null;
		community = "public";
		sourceIp = LocalhostInfo.getLocalIpAddress();
		varbinds = null;
	}
	
//	public void sendCustomV2CTrap() {
//		
//		SnmpTrap trap = new SnmpTrap();
//		trap.setTrapName(trapNameV2C);
//		trap.setVersion("v2c");
//		trap.setCommunity(community);
//		trap.setSourceIp(sourceIp);
//		trap.setVarbind(varbindsV2C);
//		
//		trapSender.sendTrap(destinationIp, destinationPort, trap);
//	}
	

}