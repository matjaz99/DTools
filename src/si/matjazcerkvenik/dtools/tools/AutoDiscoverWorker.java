package si.matjazcerkvenik.dtools.tools;

import java.util.Date;

import si.matjazcerkvenik.dtools.tools.ping.IcmpPing;
import si.matjazcerkvenik.dtools.tools.ping.PingStatus;
import si.matjazcerkvenik.dtools.xml.Node;
import si.matjazcerkvenik.dtools.xml.Service;

public class AutoDiscoverWorker implements Runnable {
	
	private long id;
	private String command;
	private AutoDiscoverThread adtp;

	public AutoDiscoverWorker(long id, String command, AutoDiscoverThread adtp) {
		this.id = id;
		this.command = command;
		this.adtp = adtp;
	}
	
	@Override
	public void run() {
		System.out.println("Ping: " + command);
		
		IcmpPing p = new IcmpPing();
		PingStatus status = p.ping(command);
		
		if (status.getErrorCode() == PingStatus.EC_OK) {
			Node n = new Node();
			n.setHostname(command);
			n.setName("AD-Node #" + id);
			n.setDescription("AutoDiscovered node @ " + new Date());
			
			Service s = new Service();
			s.setName("ICMP");
			s.setMonitoringClass("ICMP_PING");
			n.addService(s);
			n.init();
			
			adtp.storeNode(n);
		}
		adtp.decreaseCount();
		
	}
	
	@Override
	public String toString() {
		return this.command;
	}
	
}
