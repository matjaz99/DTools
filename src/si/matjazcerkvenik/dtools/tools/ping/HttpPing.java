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
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.UnknownHostException;

import si.matjazcerkvenik.dtools.xml.Service;

public class HttpPing implements IPing, Serializable {
	
	private static final long serialVersionUID = 592229742297135552L;
	
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
			URL url = new URL(service.getParam("monitoring.url"));
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			connection.connect();
			int code = connection.getResponseCode();
			if (("" + code).startsWith("2")) {
				status.setErrorCode(PingStatus.EC_OK);
				status.setErrorMessage(PingStatus.EM_OK);
			} else {
				status.setErrorCode(PingStatus.EC_ERROR_RESPONSE);
				status.setErrorMessage(PingStatus.EM_ERROR_RESPONSE);
			}
			status.setErrorDescription("HTTP code: " + code);
		} catch (UnknownHostException e) {
			status.setErrorCode(PingStatus.EC_UNKN_HOST);
			status.setErrorMessage(PingStatus.EM_UNKN_HOST);
			status.setErrorDescription(e.getMessage());
		} catch (MalformedURLException e) {
			status.setErrorCode(PingStatus.EC_MALF_URL);
			status.setErrorMessage(PingStatus.EM_MALF_URL);
			status.setErrorDescription(e.getMessage());
		} catch (ProtocolException e) {
			status.setErrorCode(PingStatus.EC_PROT_ERROR);
			status.setErrorMessage(PingStatus.EM_PROT_ERROR);
			status.setErrorDescription(e.getMessage());
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
		case PingStatus.EC_ERROR_RESPONSE:
			return "bullet_yellow.png";
		case PingStatus.EC_UNKN_HOST:
			return "bullet_red_question.png";
		case PingStatus.EC_MALF_URL:
			return "bullet_red.png";
		case PingStatus.EC_PROT_ERROR:
			return "bullet_red.png";
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
	public PingStatus ping(String s) {

		PingStatus ps = new PingStatus();
		ps.started();

		try {
			URL url = new URL(s);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			connection.connect();
			int code = connection.getResponseCode();
			ps.setErrorCode(PingStatus.EC_OK);
			ps.setErrorMessage(PingStatus.EM_OK);
			ps.setErrorDescription("HTTP code: " + code);
		} catch (UnknownHostException e) {
			ps.setErrorCode(PingStatus.EC_UNKN_HOST);
			ps.setErrorMessage(PingStatus.EM_UNKN_HOST);
			ps.setErrorDescription(e.getMessage());
		} catch (MalformedURLException e) {
			ps.setErrorCode(PingStatus.EC_MALF_URL);
			ps.setErrorMessage(PingStatus.EM_MALF_URL);
			ps.setErrorDescription(e.getMessage());
		} catch (ProtocolException e) {
			ps.setErrorCode(PingStatus.EC_PROT_ERROR);
			ps.setErrorMessage(PingStatus.EM_PROT_ERROR);
			ps.setErrorDescription(e.getMessage());
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
