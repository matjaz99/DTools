package si.matjazcerkvenik.dtools.tools.snmp;


public class SnmpMasterAgent implements ISnmpAgent {
	
	// Currently there is no need for master agent
	// Master agent behaves as a proxy
	// Maybe it could implement some adapters for various SBI protocols

	@Override
	public void sendTrap() {
		// no implementation
	}

	@Override
	public void sendResponse() {
		// no implementation
	}

}
