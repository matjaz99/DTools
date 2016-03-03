/* 
 * Copyright (C) 2015 Matjaz Cerkvenik
 * 
 * DTools is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * DTools is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with DTools. If not, see <http://www.gnu.org/licenses/>.
 * 
 */

package si.matjazcerkvenik.dtools.tools.ping;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import si.matjazcerkvenik.dtools.context.DProps;
import si.matjazcerkvenik.dtools.io.DAO;
import si.matjazcerkvenik.dtools.xml.Node;

/**
 * This class schedules periodic ping requests.
 * 
 * @author matjaz
 *
 */
public class PingScheduler {
	
	private ScheduledExecutorService scheduledThreadPool = null;
	
	public PingScheduler() {
	}
	
	public void startPingScheduler() {
		
		int threadPoolSize = DProps.getPropertyInt(DProps.NETWORK_MONITORING_PING_POOL_SIZE);
		int interval = DProps.getPropertyInt(DProps.NETWORK_MONITORING_PING_INTERVAL);
		
		if (scheduledThreadPool == null) {
			scheduledThreadPool = Executors.newScheduledThreadPool(threadPoolSize);
		}
		
		List<Node> nodesList = DAO.getInstance().loadNetworkNodes().getNodesList();
		
		for (int i = 0; i < nodesList.size(); i++) {
			ScheduledFuture<?> fut = scheduledThreadPool.scheduleWithFixedDelay(nodesList.get(i), 0, interval, TimeUnit.SECONDS);
		}
		
	}
	
	public void stopPingScheduler() {
		scheduledThreadPool.shutdown();
		while (!scheduledThreadPool.isTerminated()) {
			
		}
		scheduledThreadPool = null;
		System.out.println("PingScheduler finished");
	}
	
}
