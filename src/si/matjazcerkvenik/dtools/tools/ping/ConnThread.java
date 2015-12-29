package si.matjazcerkvenik.dtools.tools.ping;

import java.util.List;

import si.matjazcerkvenik.dtools.io.DAO;
import si.matjazcerkvenik.dtools.tools.icmp.IcmpPing;
import si.matjazcerkvenik.dtools.xml.Node;

public class ConnThread extends Thread {
	
	private boolean running = false;
	
	@Override
	public void run() {
		
		while (running) {
			
			List<Node> nodesList = DAO.getInstance().loadNetworkNodes().getNodesList();
			
			try {
				
				IcmpPing p = new IcmpPing();
				
				for (int i = 0; i < nodesList.size(); i++) {
					
					Node node = nodesList.get(i);
					node.setIcmpPingStatus(p.ping(node.getHostname()));
					
				}
				
				sleep(60 * 1000);
			} catch (InterruptedException e) {
				running = false;
				e.printStackTrace();
			}
			
		}
		
	}
	
	public void startThread() {
		running = true;
		start();
	}
	
	public void stopThread() {
		running = false;
	}
	
}
