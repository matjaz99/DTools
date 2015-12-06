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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.xml.bind.annotation.XmlTransient;

import si.matjazcerkvenik.dtools.io.DAO;
import si.matjazcerkvenik.dtools.tools.snmp.ColumnMetadata;
import si.matjazcerkvenik.dtools.tools.snmp.SnmpAgent;
import si.matjazcerkvenik.dtools.tools.snmp.SnmpTable;

@ManagedBean
@ViewScoped
public class SnmpTableBean implements Serializable {
	
	private static final long serialVersionUID = 1388947034491411923L;
	
	private SnmpAgent agent;
	private SnmpTable table;
	
//	private String newColumnType;
	
	@PostConstruct
	public void init() {
		
		Map<String, String> requestParameterMap = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		if (requestParameterMap.containsKey("agent")) {
			String name = requestParameterMap.get("agent");
			agent = DAO.getInstance().findSnmpAgent(name);
//			SnmpSimulator sim = DAO.getInstance().loadSnmpSimulator();
//			for (SnmpAgent a : sim.getSnmpAgentsList()) {
//				if (a.getName().equals(name)) {
//					agent = a;
//					break;
//				}
//			}
		}
		if (requestParameterMap.containsKey("snmpTableName")) {
			String name = requestParameterMap.get("snmpTableName");
			table = agent.findSnmpTable(name);
			table.setAgent(agent);
		}
		
		populateColumns();
		
	}

	public SnmpAgent getAgent() {
		return agent;
	}

	public void setAgent(SnmpAgent agent) {
		this.agent = agent;
	}

	public SnmpTable getTable() {
		return table;
	}

	public void setTable(SnmpTable table) {
		this.table = table;
	}
	
	
//	public String getNewColumnType() {
//		return newColumnType;
//	}
//
//	public void setNewColumnType(String newColumnType) {
//		this.newColumnType = newColumnType;
//	}





	private List<String> columns = new ArrayList<String>();
	private List<Map<String, Object>> rows = new ArrayList<Map<String,Object>>();
	
	
	public void populateColumns() {
		
		columns.clear();
		rows.clear();
		
		for (int i = 0; i < table.getMetadata().getColumnsMetaList().size(); i++) {
			columns.add(table.getMetadata().getColumnsMetaList().get(i).getName());
		}
		
		
		for (int i = 0; i < table.getRowsList().size(); i++) {
			
			Map<String, Object> m = new HashMap<String, Object>();
//			if (table.getRowsList().get(i).getValuesList() == null || table.getRowsList().get(i).getValuesList().isEmpty()) {
//				continue;
//			}
			for (int j = 0; j < table.getMetadata().getColumnsMetaList().size(); j++) {
				m.put(table.getMetadata().getColumnsMetaList().get(j).getName(), table.getRowsList().get(i).getValuesList().get(j));
			}
			rows.add(m);
			
		}
		
	}

	public List<String> getColumns() {
		return columns;
	}

	@XmlTransient
	public void setColumns(List<String> columns) {
		this.columns = columns;
	}

	public List<Map<String, Object>> getRows() {
		return rows;
	}

	@XmlTransient
	public void setRows(List<Map<String, Object>> rows) {
		this.rows = rows;
	}
	
	

	public void addNewRow() {
		if (table.getMetadata().getColumnsMetaList().isEmpty()) {
			Growl.addGrowlMessage("Create columns first!", FacesMessage.SEVERITY_WARN);
			return;
		}
		table.addNewRowWithDefaultValues();
		populateColumns();
	}
	
	public void deleteRow(Map<String, Object> row) {
		int removeRowIndex = 99;
		for (int i = 0; i < rows.size(); i++) {
			if (rows.get(i).equals(row)) {
				removeRowIndex = i;
				break;
			}
		}
		table.getRowsList().remove(removeRowIndex);
		populateColumns();
	}
	
	public void addNewColumn() {
		if (table.getMetadata().getColumnsMetaList().isEmpty()) {
			List<ColumnMetadata> col = new ArrayList<ColumnMetadata>();
			table.getMetadata().setColumnsMetaList(col);
		}
		ColumnMetadata cm = new ColumnMetadata();
		cm.setAccess("ro");
		cm.setIndex(false);
		cm.setName("colName" + table.getMetadata().getColumnsMetaList().size());
		cm.setOid(table.getMetadata().getTableOid());
		cm.setType("OCTET_STRING");
		table.getMetadata().getColumnsMetaList().add(cm);
		
		for (int i = 0; i < table.getRowsList().size(); i++) {
			table.getRowsList().get(i).getValuesList().add("value");
		}
		
		populateColumns();
	}
	
	public void deleteColumn(ColumnMetadata cm) {
		int removeRowsIndex = 99;
		for (int i = 0; i < table.getMetadata().getColumnsMetaList().size(); i++) {
			if (table.getMetadata().getColumnsMetaList().get(i).getName().equals(cm.getName())) {
				removeRowsIndex = i;
				break;
			}
		}
		table.getMetadata().getColumnsMetaList().remove(cm);
		for (int i = 0; i < table.getRowsList().size(); i++) {
			table.getRowsList().get(i).getValuesList().remove(removeRowsIndex);
		}
	}
	
	
	public void saveTable() {
		DAO.getInstance().saveSnmpDataTable(table);
		Growl.addGrowlMessage("Table saved", FacesMessage.SEVERITY_INFO);
	}
	
	
	
	public void tableValueChanged(ValueChangeEvent e) {
		String s = e.getNewValue().toString();
//		System.out.println("tableValueChanged: " + s);
		
		int rowIndex = 0;
		Map<String, Object> rowComponent = (Map<String, Object>) e.getComponent().getAttributes().get("row"); // Map: ifIndex=12, ifDesc=dsds
		for (int i = 0; i < rows.size(); i++) {
			if (rows.get(i).equals(rowComponent)) {
				// warning: equals method compares values in row, so it might find more than one row if values are identical!!
//				System.out.println("row: " + i);
				rowIndex = i;
			}
		}
		
		int colIndex = 0;
		String col = (String) e.getComponent().getAttributes().get("col"); // ifIndex
		for (int i = 0; i < table.getMetadata().getColumnsMetaList().size(); i++) {
			if (table.getMetadata().getColumnsMetaList().get(i).getName().equals(col)) {
//				System.out.println("col: " + i);
				colIndex = i;
			}
		}
		table.getRowsList().get(rowIndex).getValuesList().set(colIndex, s);
	}
	
	
	// $ snmpget -v 2c -c public 192.168.1.100:6161 1.3.6.1.4.1.444.1.8.3.2
	// $ snmpbulkwalk -v 2c -c public 192.168.1.100:6161 1.3.6.1.4.1.444.1.8
	// $ snmpbulkget -v 2c -C r16 -c public 192.168.1.100:6161 1.3.6.1.4.1.444.1.8
	public String generateNetSnmpHelp() {
		String s = "$ snmpget -v 2c -c public " + agent.getLocalIp() + ":" + agent.getLocalPort() + " " + table.getMetadata().getTableOid() + ".index.col\n";
		s += "$ snmpbulkwalk -v 2c -c public " + agent.getLocalIp() + ":" + agent.getLocalPort() + " " + table.getMetadata().getTableOid() + "\n";
		s+= "$ snmpbulkget -v 2c -C r16 -c public " + agent.getLocalIp() + ":" + agent.getLocalPort() + " " + table.getMetadata().getTableOid() + "\n";
		return s;
	}

}
