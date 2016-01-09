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

import si.matjazcerkvenik.dtools.io.DAO;
import si.matjazcerkvenik.dtools.xml.Node;

public class PingThread extends Thread {
	
	private boolean running = false;
	
	@Override
	public void run() {
		
		while (running) {
			
			List<Node> nodesList = DAO.getInstance().loadNetworkNodes().getNodesList();
			
			try {
				
				IcmpPing p = new IcmpPing();
				
				for (int i = 0; i < nodesList.size(); i++) {
					
					Node node = nodesList.get(i);
//					node.setIcmpPingStatus(p.ping(node.getHostname()));
					
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
