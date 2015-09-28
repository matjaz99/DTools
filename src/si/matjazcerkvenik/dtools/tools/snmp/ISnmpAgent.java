package si.matjazcerkvenik.dtools.tools.snmp;

public interface ISnmpAgent {
	
	public void sendTrap();
	
	public void	sendResponse();
	
}
