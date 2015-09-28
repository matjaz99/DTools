package si.matjazcerkvenik.dtools.tools.snmp;

import javax.xml.bind.annotation.XmlElement;

public class SnmpManager implements ISnmpManager {
	
	// TrapReceiver
	
	private String name;
	
	private String ip;
	
	private int port;

	public String getName() {
		return name;
	}

	@XmlElement
	public void setName(String name) {
		this.name = name;
	}

	public String getIp() {
		return ip;
	}

	@XmlElement
	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getPort() {
		return port;
	}

	@XmlElement
	public void setPort(int port) {
		this.port = port;
	}
	
	@Override
	public void processTrap() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sendGet() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sendSet() {
		// TODO Auto-generated method stub
		
	}
	
	
}
