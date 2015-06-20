package si.matjazcerkvenik.dtools.web;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import si.matjazcerkvenik.dtools.context.DToolsContext;
import si.matjazcerkvenik.dtools.tools.localhost.LocalhostInfo;
import si.matjazcerkvenik.dtools.xml.DAO;
import si.matjazcerkvenik.dtools.xml.SnmpTrap;
import si.matjazcerkvenik.dtools.xml.VarBind;

@ManagedBean
@ViewScoped
public class SnmpTrapV1Composer {

	private String trapName;
	private String community = "public";
	private String sourceIp = LocalhostInfo.getLocalIpAddress();
	// Generic trap types: coldStart trap (0), warmStart trap (1),
	// linkDown trap(2), linkUp trap (3), authenticationFailure trap (4),
	// egpNeighborLoss trap (5), enterpriseSpecific trap (6)
	private String genericTrap = "6";
	private String specificTrap = "0";
	private String enterpriseOid = "1.";
	private String timestamp = "0";
	private List<VarBind> varbinds;
	private boolean modifyMode = false;
	
	@PostConstruct
	public void init() {
		Map<String, Object> requestParameterMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
		SnmpTrap trap = (SnmpTrap) requestParameterMap.get("trap");
		if (trap != null) {
			modifyMode = true;
			trapName = trap.getTrapName();
			community = trap.getCommunity();
			genericTrap = trap.getGenericTrap();
			specificTrap = trap.getSpecificTrap();
			enterpriseOid = trap.getEnterpriseOid();
			sourceIp = trap.getSourceIp();
			timestamp = trap.getTimestamp();
			varbinds = trap.getVarbind();
		}
	}

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

	public String getGenericTrap() {
		return genericTrap;
	}

	public void setGenericTrap(String genericTrap) {
		this.genericTrap = genericTrap;
	}

	public String getSpecificTrap() {
		return specificTrap;
	}

	public void setSpecificTrap(String specificTrap) {
		this.specificTrap = specificTrap;
	}

	public String getEnterpriseOid() {
		return enterpriseOid;
	}

	public void setEnterpriseOid(String enterpriseOid) {
		this.enterpriseOid = enterpriseOid;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public List<VarBind> getVarbinds() {
		if (varbinds == null) {
			varbinds = new ArrayList<VarBind>();
			varbinds.add(new VarBind("customVarBind", "1.2.3.4", VarBind.TYPE_OCTET_STRING, "abcd"));
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
		
		if (modifyMode) {
			List<SnmpTrap> list = DAO.getInstance().loadSnmpTraps().getTraps();
			for (SnmpTrap snmpTrap : list) {
				if (snmpTrap.getTrapName().equals(trapName)) {
					snmpTrap.setVersion("v1");
					snmpTrap.setCommunity(community);
					snmpTrap.setGenericTrap(genericTrap);
					snmpTrap.setSpecificTrap(specificTrap);
					snmpTrap.setEnterpriseOid(enterpriseOid);
					snmpTrap.setSourceIp(sourceIp);
					snmpTrap.setTimestamp(timestamp);
					snmpTrap.setVarbind(varbinds);
					DAO.getInstance().saveSnmpTraps();
					Growl.addGrowlMessage("Trap " + trapName + " modified", FacesMessage.SEVERITY_INFO);
					break;
				}
			}
		} else {
			SnmpTrap trap = new SnmpTrap();
			trap.setTrapName(trapName);
			trap.setVersion("v1");
			trap.setCommunity(community);
			trap.setGenericTrap(genericTrap);
			trap.setSpecificTrap(specificTrap);
			trap.setEnterpriseOid(enterpriseOid);
			trap.setSourceIp(sourceIp);
			trap.setTimestamp(timestamp);
			trap.setVarbind(varbinds);
			DAO.getInstance().addSnmpTrap(trap);
			Growl.addGrowlMessage("Trap saved", FacesMessage.SEVERITY_INFO);
		}
		
		resetTrap();
		modifyMode = false;
		
		return "snmpAgent";
	}
	
	public void resetTrap() {
		trapName = null;
		community = "public";
		sourceIp = LocalhostInfo.getLocalIpAddress();
		genericTrap = "6";
		specificTrap = "0";
		enterpriseOid = "1.";
		timestamp = "" + DToolsContext.getSysUpTime()/1000;
		varbinds = null;
	}
	
//	public void sendCustomV1Trap() {
//		
//		SnmpTrap trap = new SnmpTrap();
//		trap.setTrapName(trapNameV1);
//		trap.setVersion("v1");
//		trap.setCommunity(community);
//		trap.setGenericTrap(genericTrap);
//		trap.setSpecificTrap(specificTrap);
//		trap.setEnterpriseOid(enterpriseOid);
//		trap.setSourceIp(sourceIp);
//		trap.setTimestamp(timestamp);
//		trap.setVarbind(varbindsV1);
//		
//		trapSender.sendTrap(destinationIp, destinationPort, trap);
//		
//	}

}
