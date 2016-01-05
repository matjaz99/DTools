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

import java.net.InetAddress;
import java.net.UnknownHostException;

public class IcmpPing {
	
	public PingStatus ping(String hostname) {
		
		PingStatus ps = new PingStatus();
		ps.started();
		
		try {
			InetAddress address = InetAddress.getByName(hostname);
			if (address.isReachable(3000)) {
				ps.setErrorCode(PingStatus.EC_OK);
				ps.setErrorMessage(PingStatus.EM_OK);
			} else {
				ps.setErrorCode(PingStatus.EC_CONN_ERROR);
				ps.setErrorMessage(PingStatus.EM_CONN_ERROR);
			}

		} catch (UnknownHostException e) {
			ps.setErrorCode(PingStatus.EC_UNKN_HOST);
			ps.setErrorMessage(PingStatus.EM_UNKN_HOST);
			ps.setErrorDescription(e.getMessage());
		} catch (Exception e) {
			ps.setErrorCode(PingStatus.EC_CONN_ERROR);
			ps.setErrorMessage(PingStatus.EM_CONN_ERROR);
			ps.setErrorDescription(e.getMessage());
		}
		
		ps.ended();
		return ps;
		
	}
	
}
