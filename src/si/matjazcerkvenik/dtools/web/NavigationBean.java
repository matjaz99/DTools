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

package si.matjazcerkvenik.dtools.web;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import si.matjazcerkvenik.dtools.tools.snmp.impl.TrapReceiver;
import si.matjazcerkvenik.dtools.xml.FtpClient;
import si.matjazcerkvenik.dtools.xml.Server;
import si.matjazcerkvenik.dtools.xml.SnmpClient;
import si.matjazcerkvenik.dtools.xml.SnmpTrap;
import si.matjazcerkvenik.dtools.xml.SshClient;

@ManagedBean
@SessionScoped
public class NavigationBean {
	
	public String openServer(Server s) {
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("server", s);
		return "server.xhtml?redirect=true";
	}
	
	public String openSshClient(SshClient client) {
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("client", client);
		return "sshClient?redirect=true";
	}
	
	public String openFtpClient(FtpClient client) {
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("client", client);
		return "ftpClient";
	}
	
	public String openSnmpClient(SnmpClient c) {
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("snmpClient", c);
		return "snmpClient";
	}
	
	public String openSnmpTrapReceiver(TrapReceiver trap) {
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("trapRecv", trap);
		return "snmpViewTraps";
	}
	
	public String openTrapMonitor(TrapReceiver trap) {
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("trapRecv", trap);
		return "snmpTrapMonitor";
	}
	
	public String openSnmpTrap(SnmpTrap trap) {
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("trap", trap);
		if (trap.getVersion().equals("v1")) {
			return "snmpTrapV1Composer";
		}
		return "snmpTrapV2CComposer";
	}
	
}
