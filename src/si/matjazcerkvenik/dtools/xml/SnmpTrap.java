package si.matjazcerkvenik.dtools.xml;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;

public class SnmpTrap {
	
	private String trapName;
	
	// common parameters
	private String version = "v2c";
	private String community = "public";
	private String sourceIp = "127.0.0.1";
	private List<VarBind> varbind;
	
	// v1 parameters
	private String genericTrap;
	private String specificTrap;
	private String enterpriseOid;
	private String timestamp;
	// coldStart trap (0), warmStart trap (1), linkDown trap(2)
	// linkUp trap(3), authenticationFailure trap(4), egpNeighborLoss trap(5)
	// enterprise specific (6)
	
	// v2c parameters
	private String snmpTrapOid;
	private String sysUpTime;
	

	public String getTrapName() {
		return trapName;
	}

	@XmlElement
	public void setTrapName(String trapName) {
		this.trapName = trapName;
	}

	public String getVersion() {
		return version;
	}

	@XmlElement
	public void setVersion(String version) {
		this.version = version;
	}

	public String getCommunity() {
		return community;
	}

	@XmlElement
	public void setCommunity(String community) {
		this.community = community;
	}

	public String getGenericTrap() {
		return genericTrap;
	}

	@XmlElement
	public void setGenericTrap(String genericTrap) {
		this.genericTrap = genericTrap;
	}

	public String getSpecificTrap() {
		return specificTrap;
	}

	@XmlElement
	public void setSpecificTrap(String specificTrap) {
		this.specificTrap = specificTrap;
	}

	public String getEnterpriseOid() {
		return enterpriseOid;
	}

	@XmlElement
	public void setEnterpriseOid(String enterpriseOid) {
		this.enterpriseOid = enterpriseOid;
	}

	public String getTimestamp() {
		return timestamp;
	}

	@XmlElement
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public String getSourceIp() {
		return sourceIp;
	}

	@XmlElement
	public void setSourceIp(String sourceIp) {
		this.sourceIp = sourceIp;
	}

	public String getSnmpTrapOid() {
		return snmpTrapOid;
	}

	@XmlElement
	public void setSnmpTrapOid(String snmpTrapOid) {
		this.snmpTrapOid = snmpTrapOid;
	}

	public String getSysUpTime() {
		return sysUpTime;
	}

	@XmlElement
	public void setSysUpTime(String sysUpTime) {
		this.sysUpTime = sysUpTime;
	}

	public List<VarBind> getVarbind() {
		return varbind;
	}

	@XmlElement
	public void setVarbind(List<VarBind> varbind) {
		this.varbind = varbind;
	}
	
	public void addVarbind(String oid, String type, String value) {
		
		if (varbind == null) {
			varbind = new ArrayList<VarBind>();
		}
		
		VarBind v = new VarBind();
		v.setOid(oid);
		v.setType(type);
		v.setValue(value);
		
		varbind.add(v);
	}
	
	public String varbindsToString() {
		String s = "[";
		for (int i = 0; i < varbind.size(); i++) {
			s += varbind.get(i).getOid() + "=" + varbind.get(i).getValue();
			if (i < varbind.size()) {
				s += ", ";
			}
		}
		s += "]";
		return s;
	}
	
}
