package si.matjazcerkvenik.dtools.tools.snmp;

public interface ISnmpManager {
	
	public void processTrap();
	
	public void sendGet();
	
	public void sendSet();
	
}
