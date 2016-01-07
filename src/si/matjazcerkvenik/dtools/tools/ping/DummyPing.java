package si.matjazcerkvenik.dtools.tools.ping;

import si.matjazcerkvenik.dtools.xml.Service;

public class DummyPing implements IPing {
	
	private PingStatus status = new PingStatus();
	
	@Override
	public void configure(Service service) {
		// not applicable
	}
	
	@Override
	public void ping() {
		// not applicable
	}
	
	@Override
	public PingStatus getStatus() {
		return status;
	}
	
	@Override
	public String getStatusIcon() {
		return "bullet_disabled.png";
	}
	
}
