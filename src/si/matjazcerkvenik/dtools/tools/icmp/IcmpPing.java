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

package si.matjazcerkvenik.dtools.tools.icmp;

import java.net.Inet4Address;
import java.net.UnknownHostException;

import si.matjazcerkvenik.dtools.context.DToolsContext;
import si.matjazcerkvenik.simplelogger.SimpleLogger;

public class IcmpPing {
	
	private SimpleLogger logger;
	
	public IcmpPing() {
		logger = DToolsContext.getInstance().getLogger();
	}
	
	public EPingStatus ping(String hostname) {
		
		try {
			if (Inet4Address.getByName(hostname).isReachable(3000)) {
				logger.info("IcmpPing: ping " + hostname + " [" + EPingStatus.UP + "]");
				return EPingStatus.UP;
			} else {
				logger.info("IcmpPing: ping " + hostname + " [" + EPingStatus.DOWN + "]");
				return EPingStatus.DOWN;
			}

		} catch (UnknownHostException e) {
			logger.error("IcmpPing:UnknownHostException", e);
			return EPingStatus.DOWN;
		} catch (Exception e) {
			logger.error("IcmpPing:Exception", e);
			return EPingStatus.DOWN;
		}
		
	}
	
}
