package si.matjazcerkvenik.dtools.tools;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import si.matjazcerkvenik.dtools.context.DProps;
import si.matjazcerkvenik.dtools.io.DAO;
import si.matjazcerkvenik.dtools.xml.Node;

public class AutoDiscoverThread extends Thread {
	
	private ExecutorService executor;
	
	private int[] fromIpArray;
	private int[] toIpArray;
		
	private int count = 0;
	
	private boolean threadIsFinished = false;
	
	public AutoDiscoverThread() {
		
		int poolSize = Integer.parseInt(DProps.getProperty(DProps.AUTO_DISCOVERY_THREAD_POOL_SIZE));
		executor = Executors.newFixedThreadPool(poolSize);
	}
	
	@Override
	public void run() {
		
		threadIsFinished = false;
		
		while (!(fromIpArray[0] == toIpArray[0] && fromIpArray[1] == toIpArray[1] 
				&& fromIpArray[2] == toIpArray[2] && fromIpArray[3] == toIpArray[3])) {
			
			Runnable worker = new AutoDiscoverWorker(count, fromIpArray[0] + "." + fromIpArray[1] + "."
					+ fromIpArray[2] + "." + fromIpArray[3], this);
			executor.execute(worker);
			count++;
			
			fromIpArray[3]++;
			if (fromIpArray[3] == 256) {
				fromIpArray[3] = 0;
				fromIpArray[2]++;
			}
			
			if (fromIpArray[2] == 256) {
				fromIpArray[2] = 0;
				fromIpArray[1]++;
			}
			
			if (fromIpArray[1] == 256) {
				fromIpArray[1] = 0;
				fromIpArray[0]++;
			}
			
			try {
				Thread.sleep(determineDelay());
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
		}
				
		while (count > 0) {
			System.out.println("count: " + count);
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
		}
		
		executor.shutdown();
		System.out.println("AD thread finished");
		
		executor = null;
		threadIsFinished = true;
	}
	
	public void startAutoDiscover(String startIp, String endIp) {
		
		fromIpArray = convertIpAddressToIntArray(startIp);
		toIpArray = convertIpAddressToIntArray(endIp);
		
		this.start();
		
	}
	
	public static void main(String[] args) {
		String sip = "192.168.0.15";
		String eip = "192.168.5.20";
		
		AutoDiscoverThread adtp = new AutoDiscoverThread();
		
		long sipInt = adtp.getIpAsInt(sip);
		long eipInt = adtp.getIpAsInt(eip);
		
		if ((eipInt - sipInt) < 0) {
			System.out.println("Invalid range: " + (eipInt - sipInt));
			return;
		}
		
//		System.out.println("Number of IPs to scan: " + adtp.getNumberOfIpsInRange());
		
		adtp.startAutoDiscover(sip, eip);
	}
	
	public void stopAutoDiscover() {
		executor.shutdownNow();
		while (!executor.isTerminated()) {
			// wait
		}
		System.out.println("Finished");
	}
	
//	public boolean isTerminated() {
//		return executor.isShutdown();
//	}
	
	public boolean isThreadFinished() {
		return threadIsFinished;
	}

	/**
	 * Convert String IP address (x.x.x.x) to array of four integers ([x, x, x, x]).
	 * @param ip
	 * @return array
	 */
	private int[] convertIpAddressToIntArray(String ip) {
		
		int[] ipInt = new int[4];
		String[] temp = ip.split("\\.");
		
		for (int i = 0; i < temp.length; i++) {
			ipInt[i] = Integer.parseInt(temp[i]);
		}
		
		return ipInt;
		
	}
	
	
	
	private long getIpAsInt(String ip) {
		
		String[] temp = ip.split("\\.");
		
		if (temp[1].length() == 1) {
			temp[1] = "00" + temp[1];
		} else if (temp[1].length() == 2) {
			temp[1] = "0" + temp[1];
		}
		if (temp[2].length() == 1) {
			temp[2] = "00" + temp[2];
		} else if (temp[2].length() == 2) {
			temp[2] = "0" + temp[2];
		}
		if (temp[3].length() == 1) {
			temp[3] = "00" + temp[3];
		} else if (temp[3].length() == 2) {
			temp[3] = "0" + temp[3];
		}
		
		return Long.parseLong(temp[0] + temp[1] + temp[2] + temp[3]);
	}
	
	public synchronized void storeNode(Node n) {
		DAO.getInstance().addNode(n);
//		if (nodes == null) {
//			nodes = new ArrayList<Node>();
//		}
//		nodes.add(n);
		System.out.println("==> Node added: " + n.getHostname());
	}
	
	public synchronized void decreaseCount() {
		count--;
	}
	
	public int determineDelay() {
		int delay = 30;
		delay = 4 * count;
		System.out.println("delay " + delay);
		return delay;
	}
	
}
