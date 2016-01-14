package si.matjazcerkvenik.dtools.tools.ping;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import si.matjazcerkvenik.dtools.io.DAO;
import si.matjazcerkvenik.dtools.xml.Node;


public class PingScheduler {
	
	private ScheduledExecutorService scheduledThreadPool = null;
	
	public void startPingScheduler() {
		
//		System.out.println("Current time: " + new Date());
		if (scheduledThreadPool == null) {
			scheduledThreadPool = Executors.newScheduledThreadPool(2);
		}
		
		List<Node> nodesList = DAO.getInstance().loadNetworkNodes().getNodesList();
		
		for (int i = 0; i < nodesList.size(); i++) {
			scheduledThreadPool.scheduleWithFixedDelay(nodesList.get(i), 0, 60, TimeUnit.SECONDS);
		}
		
	}
	
	public void stopPingScheduler() {
		scheduledThreadPool.shutdown();
		while (!scheduledThreadPool.isTerminated()) {
			
		}
		scheduledThreadPool = null;
		System.out.println("Finished");
	}
	
}
