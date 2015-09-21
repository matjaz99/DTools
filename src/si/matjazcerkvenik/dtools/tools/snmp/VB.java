package si.matjazcerkvenik.dtools.tools.snmp;

import java.io.Serializable;

public class VB implements Serializable {
	
	private static final long serialVersionUID = 2766510812643098966L;
	
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
