package si.matjazcerkvenik.dtools.xml;

public class VarBind {
	
	public enum TYPE { OCTET_STRING, INTEGER, OID, GAUGE, COUNTER32, 
		IP_ADDRESS, TIMETICKS, COUNTER64, UNSIGNED_INTEGER, BITS  }
	
	private String oid;
	private TYPE type = TYPE.OCTET_STRING;
	private String value;
	
	public String getOid() {
		return oid;
	}
	public void setOid(String oid) {
		this.oid = oid;
	}
	public TYPE getType() {
		return type;
	}
	public void setType(TYPE type) {
		this.type = type;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
	
	
}
