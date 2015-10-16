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

package si.matjazcerkvenik.dtools.tools.snmp.impl;

import java.util.List;

import si.matjazcerkvenik.dtools.tools.snmp.SnmpAgent;
import si.matjazcerkvenik.dtools.xml.DAO;
import si.matjazcerkvenik.dtools.xml.SnmpTrap;

public class SenderThread extends Thread {
	
	private SnmpAgent agent;
	
	private boolean isRunning = true;
	
	
	
	public SenderThread(SnmpAgent senderBean) {
		this.agent = senderBean;
	}

	public void setSenderBean(SnmpAgent agent) {
		this.agent = agent;
	}

	@Override
	public void run() {
		
		List<SnmpTrap> traps = DAO.getInstance().loadSnmpTraps().getTraps();
		int index = 0;
		
		while (isRunning) {
			
			String ip = agent.getDestinationIp();
			int port = agent.getDestinationPort();
			agent.getTrapSender().sendTrap(ip, port, traps.get(index));
			
			if (index == traps.size() - 1) {
				index = 0;
			} else {
				index++;
			}
			
			int i = (int) agent.getSendInterval();
			try {
				sleep(i);
			} catch (InterruptedException e) {
			}
//			if (i < 1000) {
//				try {
//					sleep(i);
//				} catch (InterruptedException e) {
//				}
//			} else {
//				
//			}
			
//			int remaining = i;
//			while (isRunning) {
//				if (i == 0 || remaining < 0) {
//					break;
//				} else if (remaining < 1000) {
//					try {
//						sleep(remaining);
//					} catch (InterruptedException e) {
//					}
//					remaining = remaining - 1000;
//				} else {
//					try {
//						sleep(1000);
//					} catch (InterruptedException e) {
//					}
//					remaining = remaining - 1000;
//				}
//			}
			
		}
		
	}
	
	
	/**
	 * Start sender thread
	 */
	public void startThread() {
		
		isRunning = true;
		start();
		
	}
	
	
	/**
	 * Stop sender thread
	 */
	public void stopThread() {
		
		isRunning = false;
		
	}
	
}
