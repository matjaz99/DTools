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

package si.matjazcerkvenik.dtools.tools.snmp;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@XmlRootElement
public class SnmpTable {

	// see
	// http://stackoverflow.com/questions/13970285/creating-and-populating-a-datatable-dynamically-in-jsf2-0

	private String filePath;

	private String name;
	private TableMetadata metadata;
	private List<SnmpRow> rowsList;

	private SnmpAgent agent; // reference to agent

	public String getName() {
		return name;
	}

	@XmlAttribute
	public void setName(String name) {
		this.name = name;
	}

	public TableMetadata getMetadata() {
		return metadata;
	}

	@XmlElement(name = "metadata")
	public void setMetadata(TableMetadata metadata) {
		this.metadata = metadata;
	}

	public List<SnmpRow> getRowsList() {
		return rowsList;
	}

	@XmlElement(name = "row")
	public void setRowsList(List<SnmpRow> rowsList) {
		this.rowsList = rowsList;
	}

	public String getFilePath() {
		return filePath;
	}

	@XmlTransient
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public SnmpAgent getAgent() {
		return agent;
	}

	@XmlTransient
	public void setAgent(SnmpAgent agent) {
		this.agent = agent;
	}
	
	public void addNewRowWithDefaultValues() {
		
		List<String> values = new ArrayList<String>();
		for (int i = 0; i < metadata.getColumnsMetaList().size(); i++) {
			if (metadata.getColumnsMetaList().get(i).getType().equals("INTEGER")) {
				if (metadata.getColumnsMetaList().get(i).isIndex()) {
					values.add("" + (rowsList.size() + 1)); // automatically increase index column
				} else {
					values.add("0");
				}
				
			} else if (metadata.getColumnsMetaList().get(i).getType().equals("OCTET_STRING")) {
				values.add("value" + (rowsList.size() + 1));
			}
		}
		
		SnmpRow row = new SnmpRow();
		row.setIndex(rowsList.size() + 1);
		row.setValuesList(values);
		rowsList.add(row);
	}

}
