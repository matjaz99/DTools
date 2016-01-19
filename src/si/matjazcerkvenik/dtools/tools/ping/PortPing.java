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

import java.io.IOException;
import java.io.Serializable;
import java.net.InetSocketAddress;
import java.net.Socket;

import si.matjazcerkvenik.dtools.xml.Service;

public class PortPing implements IPing, Serializable {
	
	private static final long serialVersionUID = -4560770564153981136L;
	
	private Service service;
	private PingStatus status = new PingStatus();
	
	@Override
	public void configure(Service service) {
		this.service = service;
	}
	
	@Override
	public void ping() {
		
		status = new PingStatus();
		status.started();
		
		try {
            Socket socket = new Socket();
            socket.connect(new InetSocketAddress(service.getNode().getHostname(), 
            		Integer.parseInt(service.getParam("monitoring.port"))), 10000);
            socket.close();
            status.setErrorCode(PingStatus.EC_OK);
            status.setErrorMessage(PingStatus.EM_OK);
        } catch (IOException e) {
        	status.setErrorCode(PingStatus.EC_IO_ERROR);
        	status.setErrorMessage(PingStatus.EM_IO_ERROR);
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
		case PingStatus.EC_IO_ERROR:
			return "bullet_red.png";
		case PingStatus.EC_CONN_ERROR:
			return "bullet_red.png";
		default:
			break;
		}
		return "bullet_black.png";
		
	}
	
	@Override
	public PingStatus getStatus() {
		return status;
	}
	
	@Override
	public void resetStatus() {
		status = new PingStatus();
	}
	
	@Deprecated
	public PingStatus ping(String hostname, int port) {
		
		PingStatus ps = new PingStatus();
		ps.started();
		
		try {
            Socket socket = new Socket();
            socket.connect(new InetSocketAddress(hostname, port), 10000);
            socket.close();
            ps.setErrorCode(PingStatus.EC_OK);
			ps.setErrorMessage(PingStatus.EM_OK);
        } catch (IOException e) {
        	ps.setErrorCode(PingStatus.EC_IO_ERROR);
			ps.setErrorMessage(PingStatus.EM_IO_ERROR);
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
