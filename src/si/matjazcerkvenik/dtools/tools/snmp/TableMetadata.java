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

public class TableMetadata {
	
	private String tableOid;
	private boolean enabled = true;
	private List<ColumnMetadata> columnsMetaList;

	public String getTableOid() {
		return tableOid;
	}

	@XmlAttribute(name="oid")
	public void setTableOid(String tableOid) {
		this.tableOid = tableOid;
	}

	public boolean isEnabled() {
		return enabled;
	}

	@XmlAttribute(name="enabled")
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public List<ColumnMetadata> getColumnsMetaList() {
		if (columnsMetaList == null) {
			columnsMetaList = new ArrayList<ColumnMetadata>();
		}
		return columnsMetaList;
	}

	@XmlElement(name="column")
	public void setColumnsMetaList(List<ColumnMetadata> columnsMetaList) {
		this.columnsMetaList = columnsMetaList;
	}
	
}
