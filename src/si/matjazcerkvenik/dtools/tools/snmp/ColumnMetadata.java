package si.matjazcerkvenik.dtools.tools.snmp;

import javax.xml.bind.annotation.XmlAttribute;

public class ColumnMetadata {
	
	private String name;
	private String type;
	private String oid;
	private String access;
	
	
	public String getName() {
		return name;
	}
	@XmlAttribute
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	@XmlAttribute
	public void setType(String type) {
		this.type = type;
	}
	public String getOid() {
		return oid;
	}
	@XmlAttribute
	public void setOid(String oid) {
		this.oid = oid;
	}
	public String getAccess() {
		return access;
	}
	@XmlAttribute
	public void setAccess(String access) {
		this.access = access;
	}
	
}
