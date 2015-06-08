package si.matjazcerkvenik.dtools.web;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

import si.matjazcerkvenik.dtools.tools.localhost.LocalhostInfo;
import si.matjazcerkvenik.dtools.tools.snmp.SnmpTrapSender;
import si.matjazcerkvenik.dtools.xml.DAO;
import si.matjazcerkvenik.dtools.xml.SnmpTrap;
import si.matjazcerkvenik.dtools.xml.VarBind;
import si.matjazcerkvenik.dtools.xml.VarBind.TYPE;

@ManagedBean
@ApplicationScoped
public class SnmpTrapSenderBean {
	
	private String localIp = "localhost";
	private int localPort = 6161;
	
	private String destinationIp = "localhost";
	private int destinationPort = 6162;
	
	private SnmpTrapSender trapSender;
	
	// used to save traps
	private String trapNameV1;
	private String trapNameV2C;
		
	// common parameters
	private String community = "public";
	private String sourceIp = LocalhostInfo.getLocalIpAddress();
	
	// v1 parameters
	// Generic trap types: coldStart trap (0), warmStart trap (1), 
	// linkDown trap(2), linkUp trap (3), authenticationFailure trap (4), 
	// egpNeighborLoss trap (5), enterpriseSpecific trap (6)
	private String genericTrap = "6";
	private String specificTrap = "0";
	private String enterpriseOid = "1.";
	private String timestamp = "0";
	private List<VarBind> varbindsV1;
	
	
	// v2c parameters
	private List<VarBind> varbindsV2C;
	

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

	public String getTrapNameV1() {
		return trapNameV1;
	}

	public void setTrapNameV1(String trapNameV1) {
		this.trapNameV1 = trapNameV1;
	}

	public String getTrapNameV2C() {
		return trapNameV2C;
	}

	public void setTrapNameV2C(String trapNameV2C) {
		this.trapNameV2C = trapNameV2C;
	}

	public List<VarBind> getVarbindsV1() {
		if (varbindsV1 == null) {
			varbindsV1 = new ArrayList<VarBind>();
			varbindsV1.add(new VarBind("Timestamp", "1.2.3.1", TYPE.OCTET_STRING, "999"));
		}
		return varbindsV1;
	}

	public void setVarbindsV1(List<VarBind> varbindsV1) {
		this.varbindsV1 = varbindsV1;
	}

	public List<VarBind> getVarbindsV2C() {
		if (varbindsV2C == null) {
			varbindsV2C = new ArrayList<VarBind>();
			varbindsV2C.add(new VarBind("snmpTrapOid", "1.3.6.1.6.3.1.1.4.1.0", TYPE.OCTET_STRING, "9.9.9"));
			varbindsV2C.add(new VarBind("sysUpTime", "1.3.6.1.2.1.1.3.0", TYPE.OCTET_STRING, "0"));
			varbindsV2C.add(new VarBind("sourceIp", "1.3.6.1.6.3.18.1.3.0", TYPE.OCTET_STRING, LocalhostInfo.getLocalIpAddress()));
		}
		return varbindsV2C;
	}

	public void setVarbindsV2C(List<VarBind> varbindsV2C) {
		this.varbindsV2C = varbindsV2C;
	}
	
	public void addNewOidLineV1() {
		varbindsV1.add(new VarBind("oidName", "1.2.3.4.5", TYPE.OCTET_STRING, "value"));
	}
	
	public void addNewOidLineV2C() {
		varbindsV2C.add(new VarBind("oidName", "1.2.3.4.5", TYPE.OCTET_STRING, "value"));
	}
	
	public void removeOidV1(VarBind vb) {
		varbindsV1.remove(vb);
	}
	
	public void removeOidV2C(VarBind vb) {
		varbindsV2C.remove(vb);
	}
	
	public void saveTrapV1() {
		SnmpTrap trap = new SnmpTrap();
		trap.setTrapName(trapNameV1);
		trap.setVersion("v1");
		trap.setCommunity(community);
		trap.setGenericTrap(genericTrap);
		trap.setSpecificTrap(specificTrap);
		trap.setEnterpriseOid(enterpriseOid);
		trap.setSourceIp(sourceIp);
		trap.setTimestamp(timestamp);
		trap.setVarbind(varbindsV1);
		
		DAO.getInstance().addSnmpTrap(trap);
	}
	
	public void saveTrapV2C() {
		SnmpTrap trap = new SnmpTrap();
		trap.setTrapName(trapNameV2C);
		trap.setVersion("v2c");
		trap.setCommunity(community);
		trap.setSourceIp(sourceIp);
		trap.setVarbind(varbindsV2C);
		
		DAO.getInstance().addSnmpTrap(trap);
	}
	
	public void resetTrapV1() {
		trapNameV1 = null;
		community = "public";
		sourceIp = LocalhostInfo.getLocalIpAddress();
		genericTrap = "6";
		specificTrap = "0";
		enterpriseOid = "1.";
		timestamp = "0";
		varbindsV1 = null;
	}
	
	public void resetTrapV2C() {
		trapNameV2C = null;
		community = "public";
		sourceIp = LocalhostInfo.getLocalIpAddress();
		varbindsV2C = null;
	}


	
	
	
	public List<SnmpTrap> getSnmpTrapsList() {
		return DAO.getInstance().loadSnmpTraps().getTraps();
	}
	
	
	public void sendTrap(SnmpTrap trap) {
		trapSender.sendTrap(destinationIp, destinationPort, trap);
	}
	
	public void deleteTrap(SnmpTrap trap) {
		DAO.getInstance().deleteSnmpTrap(trap);
	}
	
	
	
	public void toggleRunning() {
		
		if (trapSender == null) {
			trapSender = new SnmpTrapSender();
			trapSender.start(localIp, localPort);
		} else {
			// already listening
			trapSender.stop();
			trapSender = null;
		}
		
	}
	
	public boolean isListening() {
		if (trapSender != null) {
			return true;
		}
		return false;
	}
	
	
}
