package si.matjazcerkvenik.dtools.xml;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;

public class SnmpTrap {
	
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
	
	// v2c parameters
	private String snmpTrapOid;
	private String sysUpTime;
	

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
	
	public void addVarbind(String oid, VarBind.TYPE type, String value) {
		
		if (varbind == null) {
			varbind = new ArrayList<VarBind>();
		}
		
		VarBind v = new VarBind();
		v.setOid(oid);
		v.setType(type);
		v.setValue(value);
		
		varbind.add(v);
	}
	
}
