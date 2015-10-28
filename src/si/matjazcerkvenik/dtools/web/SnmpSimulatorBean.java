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

import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;

import org.primefaces.context.RequestContext;

import si.matjazcerkvenik.dtools.tools.localhost.LocalhostInfo;
import si.matjazcerkvenik.dtools.tools.snmp.SnmpAgent;
import si.matjazcerkvenik.dtools.tools.snmp.SnmpSimulator;
import si.matjazcerkvenik.dtools.tools.snmp.TrapsTable;
import si.matjazcerkvenik.dtools.xml.DAO;

@ManagedBean
@ApplicationScoped
public class SnmpSimulatorBean {
	
	private String newObjectName;
	
	/**
	 * Add new SNMP agent with default settings
	 */
	public void addNewSnmpAgent() {
		
		SnmpAgent a = new SnmpAgent("SnmpAgent" + DAO.getInstance().loadSnmpSimulator().getSnmpAgentsList().size(), 
				LocalhostInfo.getLocalIpAddress(), 161);
		
		DAO.getInstance().addSnmpAgent(a);
		Growl.addGrowlMessage("Created SNMP agent: " + a.toString(), FacesMessage.SEVERITY_INFO);
		
	}
	
	public void deleteSnmpAgent(SnmpAgent a) {
		a.stop();
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

	
	
//	public String getNewObjectName() {
//		return newObjectName;
//	}
//
//	public void setNewObjectName(String newObjectName) {
//		this.newObjectName = newObjectName;
//	}
//	
//	
//	public void addTrapScenarioAction(SnmpAgent a) {
////		Map<String, String> requestParameterMap = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
////		if (requestParameterMap.containsKey("agent")) {
////			String name = requestParameterMap.get("agent");
////			System.out.println("=="+name);
////			SnmpSimulator sim = DAO.getInstance().loadSnmpSimulator();
////			for (SnmpAgent a : sim.getSnmpAgentsList()) {
////				if (a.getName().equals(name)) {
//////					agent = a;
////					break;
////				}
////			}
////		}
//		
//		System.out.println("=="+a.getName());
//		TrapsTable tt = new TrapsTable();
//		tt.setName(newObjectName);
//		tt.setFilePath(a.getDirectoryPath() + "/traps/" + newObjectName + "-" + System.currentTimeMillis() + ".xml");
//		
//		a.getTrapsTableList().add(tt);
//		DAO.getInstance().saveSnmpTraps(tt);
//		
//		newObjectName = null;
//		
//		RequestContext context = RequestContext.getCurrentInstance();
//		context.addCallbackParam("success", true);
//	}
	
}
