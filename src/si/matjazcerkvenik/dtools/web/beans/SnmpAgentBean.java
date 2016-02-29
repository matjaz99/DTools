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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.primefaces.context.RequestContext;

import si.matjazcerkvenik.dtools.io.DAO;
import si.matjazcerkvenik.dtools.tools.localhost.LocalhostInfo;
import si.matjazcerkvenik.dtools.tools.snmp.ColumnMetadata;
import si.matjazcerkvenik.dtools.tools.snmp.SnmpAgent;
import si.matjazcerkvenik.dtools.tools.snmp.SnmpRow;
import si.matjazcerkvenik.dtools.tools.snmp.SnmpTable;
import si.matjazcerkvenik.dtools.tools.snmp.SnmpTrap;
import si.matjazcerkvenik.dtools.tools.snmp.TableMetadata;
import si.matjazcerkvenik.dtools.tools.snmp.TrapDestination;
import si.matjazcerkvenik.dtools.tools.snmp.TrapsTable;

@ManagedBean
@ViewScoped
public class SnmpAgentBean implements Serializable {
	
	private static final long serialVersionUID = 7581142519721287280L;
	
	private SnmpAgent agent;
	
	private String newTableName;
	private String newTableOID;
	private String newTrapsTableName;
	
	@PostConstruct
	public void init() {
		
		Map<String, String> requestParameterMap = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		if (requestParameterMap.containsKey("agent")) {
			String name = requestParameterMap.get("agent");
			agent = DAO.getInstance().findSnmpAgent(name);
		}
	}
	
	public SnmpAgent getAgent() {
		return agent;
	}

	public void setAgent(SnmpAgent agent) {
		this.agent = agent;
	}

	public String getNewTableName() {
		return newTableName;
	}

	public void setNewTableName(String newTableName) {
		this.newTableName = newTableName;
	}

	public String getNewTableOID() {
		return newTableOID;
	}

	public void setNewTableOID(String tableOID) {
		this.newTableOID = tableOID;
	}
	
	public String getNewTrapsTableName() {
		return newTrapsTableName;
	}

	public void setNewTrapsTableName(String newTrapsTableName) {
		this.newTrapsTableName = newTrapsTableName;
	}

	public List<TrapsTable> getTrapsTableList() {
		return agent.getTrapsTableList();
	}

	public List<SnmpTable> getSnmpTablesList() {
		return agent.getSnmpTablesList();
	}
	
	
	public void addTrapScenarioAction(SnmpAgent a) {
		
		// create new empty table with default trap destination
		TrapsTable tt = new TrapsTable();
		tt.setName(newTrapsTableName);
		tt.setFilePath(a.getDirectoryPath() + "/traps/" + newTrapsTableName + "-" + System.currentTimeMillis() + ".xml");
		tt.setTrapsList(new ArrayList<SnmpTrap>());
		
		TrapDestination td = new TrapDestination(LocalhostInfo.getLocalIpAddress(), 162);
		List<TrapDestination> destList = new ArrayList<TrapDestination>();
		destList.add(td);
		tt.setTrapDestinationsList(destList);
		
		a.addNewTrapsTable(tt);
		DAO.getInstance().saveSnmpTraps(tt);
		
		Growl.addGrowlMessage("Scenario " + newTrapsTableName + " added", FacesMessage.SEVERITY_INFO);
		
		newTrapsTableName = null;
		
		RequestContext context = RequestContext.getCurrentInstance();
		context.addCallbackParam("success", true);
	}
	
	
	/**
	 * Create new empty table. Columns and rows are instanced but they are empty.
	 */
	public void addSnmpTableAction() {
		
		// create new empty table
		SnmpTable tbl = new SnmpTable();
		tbl.setName(newTableName);
		tbl.setFilePath(agent.getDirectoryPath() + "/tables/" + newTableName + "-" + System.currentTimeMillis() + ".xml");
		TableMetadata meta = new TableMetadata();
		meta.setColumnsMetaList(new ArrayList<ColumnMetadata>());
		meta.setTableOid(newTableOID);
		tbl.setMetadata(meta);
		tbl.setRowsList(new ArrayList<SnmpRow>());
		
		agent.addNewSnmpTable(tbl);
		DAO.getInstance().saveSnmpDataTable(tbl);
		
		Growl.addGrowlMessage("Table " + newTableName + " added", FacesMessage.SEVERITY_INFO);
		
		newTableName = null;
		newTableOID = null;
		
		RequestContext context = RequestContext.getCurrentInstance();
		context.addCallbackParam("success", true);
		
	}
	
	
	public void toggleTableEnable(SnmpTable table) {
//		table.getMetadata().setEnabled(!table.getMetadata().isEnabled());
		DAO.getInstance().saveSnmpDataTable(table);
	}
	
	
	public void deleteDataTable(SnmpTable table) {
		DAO.getInstance().deleteSnmpDataTable(agent, table);
		Growl.addGrowlMessage("Table " + table.getName() + " deleted", FacesMessage.SEVERITY_INFO);
	}
	
	public void deleteTrapsTable(TrapsTable table) {
		DAO.getInstance().deleteTrapsTable(agent, table);
		Growl.addGrowlMessage("Table " + table.getName() + " deleted", FacesMessage.SEVERITY_INFO);
	}
	
}
