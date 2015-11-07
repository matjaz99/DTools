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
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import si.matjazcerkvenik.dtools.tools.snmp.SnmpAgent;
import si.matjazcerkvenik.dtools.tools.snmp.SnmpSimulator;
import si.matjazcerkvenik.dtools.tools.snmp.SnmpTable;
import si.matjazcerkvenik.dtools.tools.snmp.TrapsTable;
import si.matjazcerkvenik.dtools.xml.DAO;

@ManagedBean
@ViewScoped
public class SnmpAgentBean implements Serializable {
	
	private static final long serialVersionUID = 7581142519721287280L;
	
	private SnmpAgent agent;
	
	@PostConstruct
	public void init() {
		
		Map<String, String> requestParameterMap = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		if (requestParameterMap.containsKey("agent")) {
			String name = requestParameterMap.get("agent");
			SnmpSimulator sim = DAO.getInstance().loadSnmpSimulator();
			for (SnmpAgent a : sim.getSnmpAgentsList()) {
				if (a.getName().equals(name)) {
					agent = a;
					break;
				}
			}
		}
	}
	
	public SnmpAgent getAgent() {
		return agent;
	}

	public void setAgent(SnmpAgent agent) {
		this.agent = agent;
	}

	public List<TrapsTable> getTrapsTableList() {
		return agent.getTrapsTableList();
	}

	public List<SnmpTable> getSnmpTablesList() {
		return agent.getSnmpTablesList();
	}
	
	public void deleteDataTable(SnmpTable table) {
		DAO.getInstance().deleteSnmpDataTable(agent, table);
	}
	
	public void deleteTrapsTable(TrapsTable table) {
		DAO.getInstance().deleteTrapsTable(agent, table);
	}
	
}
