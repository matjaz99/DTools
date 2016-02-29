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

import java.io.Serializable;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

import org.primefaces.context.RequestContext;

import si.matjazcerkvenik.dtools.io.DAO;
import si.matjazcerkvenik.dtools.tools.snmp.SnmpAgent;

@ManagedBean
@ApplicationScoped
public class SnmpSimulatorBean implements Serializable {
	
	private static final long serialVersionUID = 6126035339817141055L;

	private String newAgentName;
	
	

	public String getNewAgentName() {
		return newAgentName;
	}

	public void setNewAgentName(String newAgentName) {
		this.newAgentName = newAgentName;
	}

	/**
	 * Add new SNMP agent with default settings
	 */
	public void addNewSnmpAgent() {
		
		SnmpAgent a = SnmpAgent.createDefaultAgent(newAgentName);
		
		DAO.getInstance().createNewSnmpAgent(a);
		Growl.addGrowlMessage("Agent created: " + newAgentName, FacesMessage.SEVERITY_INFO);
		
		newAgentName = null;
		
		RequestContext context = RequestContext.getCurrentInstance();
		context.addCallbackParam("success", true);
		
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
				Growl.addGrowlMessage("Agent started on port " + a.getLocalPort(), FacesMessage.SEVERITY_INFO);
			} else {
				Growl.addGrowlMessage("Error starting agent", FacesMessage.SEVERITY_WARN);
			}
		} else {
			// already listening
			a.stop();
			Growl.addGrowlMessage("Agent stopped", FacesMessage.SEVERITY_INFO);
		}
	}

	
	
	
	
}
