package si.matjazcerkvenik.dtools.tools.snmp;

public class VB {
	
	public String oid;
	public String value;
	
	public VB(String oid, String value) {
		this.oid = oid;
		this.value = value;
	}
	
	@Override
	public String toString() {
		return oid + "=" + value;
	}
	
}
