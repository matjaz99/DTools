package si.matjazcerkvenik.dtools.tools.ping;

import si.matjazcerkvenik.dtools.xml.Service;

public interface IPing {
	
	public void ping();
	
	public void configure(Service service);
	
	public String getStatusIcon();
	
	public PingStatus getStatus();
	
}
