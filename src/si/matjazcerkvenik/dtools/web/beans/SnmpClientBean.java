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

package si.matjazcerkvenik.dtools.web.beans;

import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import si.matjazcerkvenik.dtools.io.DAO;
import si.matjazcerkvenik.dtools.tools.snmp.SnmpClient;
import si.matjazcerkvenik.dtools.tools.snmp.impl.SimpleSnmpClient;

@ManagedBean
@ViewScoped
public class SnmpClientBean {
	
	private SnmpClient snmpClient;
	
	private String oid = ".1.3.6.1.2.1.1.1.0";
	private String result;
	
//	@PostConstruct
//	public void init() {
//		Map<String, Object> requestParameterMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
//		snmpClient = (SnmpClient) requestParameterMap.get("snmpClient");
//	}
	
	@PostConstruct
	public void init() {
		Map<String, String> requestParameterMap = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		if (requestParameterMap.containsKey("clientUrl")) {
			String name = requestParameterMap.get("clientUrl");
			snmpClient = DAO.getInstance().findSnmpClient(name);
		}
	}
	
	public SnmpClient getSnmpClient() {
		return snmpClient;
	}

	public void setSnmpClient(SnmpClient client) {
		this.snmpClient = client;
	}

	public String getOid() {
		return oid;
	}

	public void setOid(String oid) {
		this.oid = oid;
	}
	
	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}
	
	
	public void execute() {
		System.out.println("host: " + snmpClient.getHostname() + ", " + snmpClient.getPort() + ", " + oid);
		SimpleSnmpClient snmp = new SimpleSnmpClient(snmpClient.getHostname(), snmpClient.getPort());
		result = "SNMP-GET " + oid + " = " + snmp.getAsString(oid);
		
	}
	
}
