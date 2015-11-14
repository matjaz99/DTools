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

import java.io.Serializable;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

import si.matjazcerkvenik.dtools.tools.localhost.LocalhostInfo;
import si.matjazcerkvenik.dtools.tools.snmp.SnmpAgent;
import si.matjazcerkvenik.dtools.xml.DAO;

@ManagedBean
@ApplicationScoped
public class SnmpSimulatorBean implements Serializable {
	
	private static final long serialVersionUID = 6126035339817141055L;


	/**
	 * Add new SNMP agent with default settings
	 */
	public void addNewSnmpAgent() {
		
		SnmpAgent a = new SnmpAgent("SnmpAgent" + DAO.getInstance().loadSnmpSimulator().getSnmpAgentsList().size(), 
				LocalhostInfo.getLocalIpAddress(), 161);
		
		DAO.getInstance().createNewSnmpAgent(a);
		Growl.addGrowlMessage("Created SNMP agent: " + a.toString(), FacesMessage.SEVERITY_INFO);
		
	}
	
	public void deleteSnmpAgent(SnmpAgent a) {
		a.destroyAgent();
		DAO.getInstance().deleteSnmpAgent(a);
		Growl.addGrowlMessage(a.getName() + " deleted", FacesMessage.SEVERITY_INFO);
	}
	
	public List<SnmpAgent> getSnmpAgents() {
		return DAO.getInstance().loadSnmpSimulator().getSnmpAgentsList();
	}
	
	
	public void toggleSnmpAgent(SnmpAgent a) {
		
		if (a.isActive()) {
			boolean b = a.start();
			if (b) {
				Growl.addGrowlMessage("Start agent on port " + a.getLocalPort(), FacesMessage.SEVERITY_INFO);
			} else {
				Growl.addGrowlMessage("Error starting agent", FacesMessage.SEVERITY_WARN);
			}
		} else {
			// already listening
			a.stop();
			Growl.addGrowlMessage("Stop agent", FacesMessage.SEVERITY_INFO);
		}
	}

	
	
}
