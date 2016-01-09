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

import java.io.Serializable;
import java.net.InetAddress;
import java.net.UnknownHostException;

import si.matjazcerkvenik.dtools.xml.Service;

public class IcmpPing implements IPing, Serializable {
	
	private static final long serialVersionUID = -725608817177247957L;
	
	private String hostname;
	private PingStatus status = new PingStatus();

	@Override
	public void configure(Service service) {
		hostname = service.getNode().getHostname();
	}

	@Override
	public void ping() {
		status = new PingStatus();
		status.started();

		try {
			InetAddress address = InetAddress.getByName(hostname);
			if (address.isReachable(3000)) {
				status.setErrorCode(PingStatus.EC_OK);
				status.setErrorMessage(PingStatus.EM_OK);
			} else {
				status.setErrorCode(PingStatus.EC_CONN_ERROR);
				status.setErrorMessage(PingStatus.EM_CONN_ERROR);
			}

		} catch (UnknownHostException e) {
			status.setErrorCode(PingStatus.EC_UNKN_HOST);
			status.setErrorMessage(PingStatus.EM_UNKN_HOST);
			status.setErrorDescription(e.getMessage());
		} catch (Exception e) {
			status.setErrorCode(PingStatus.EC_CONN_ERROR);
			status.setErrorMessage(PingStatus.EM_CONN_ERROR);
			status.setErrorDescription(e.getMessage());
		}

		status.ended();
	}

	@Override
	public String getStatusIcon() {
		switch (status.getErrorCode()) {
		case PingStatus.EC_OK:
			return "bullet_green.png";
		case PingStatus.EC_CONN_ERROR:
			return "bullet_red.png";
		case PingStatus.EC_UNKN_HOST:
			return "bullet_red_question.png";
		default:
			break;
		}
		return "bullet_black.png";
	}
	
	@Override
	public PingStatus getStatus() {
		return status;
	}

	@Deprecated
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
