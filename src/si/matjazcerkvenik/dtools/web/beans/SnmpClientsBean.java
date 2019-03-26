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

import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.primefaces.context.RequestContext;

import si.matjazcerkvenik.dtools.io.DAO;
import si.matjazcerkvenik.dtools.tools.snmp.SnmpClient;

@ManagedBean
@SessionScoped
public class SnmpClientsBean {

	private String name;
	private String hostname;
	private String snmpVersion = "v2c";
	private String community = "public";
	private int port = 161;
	

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getHostname() {
		return hostname;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	public String getSnmpVersion() {
		return snmpVersion;
	}

	public void setSnmpVersion(String snmpVersion) {
		this.snmpVersion = snmpVersion;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getCommunity() {
		return community;
	}

	public void setCommunity(String community) {
		this.community = community;
	}

	
	
	public void addSnmpClient() {

		SnmpClient c = new SnmpClient();
		c.setName(name);
		c.setHostname(hostname);
		c.setPort(port);
		c.setSnmpVersion(snmpVersion);
		c.setCommunity(community);

		DAO.getInstance().addSnmpClient(c);

		name = null;
		hostname = null;
		port = 161;
		community = "public";
		snmpVersion = "v2c";
		
		RequestContext context = RequestContext.getCurrentInstance();
		context.addCallbackParam("success", true);

	}

	public void deleteSnmpClientAction(SnmpClient c) {
		DAO.getInstance().deleteSnmpClients(c);
	}

	public List<SnmpClient> getSnmpClientsList() {
		return DAO.getInstance().loadSnmpClients().getSnmpClientsList();
	}
	
	public void toggleFavorite(SnmpClient client) {
		client.setFavorite(!client.isFavorite());
		DAO.getInstance().saveSnmpClients();
	}
	

}
