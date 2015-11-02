package si.matjazcerkvenik.dtools.tools.snmp;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

public class TableMetadata {
	
	private String tableOid;
	private List<ColumnMetadata> columnsMetaList;

	public String getTableOid() {
		return tableOid;
	}

	@XmlAttribute(name="oid")
	public void setTableOid(String tableOid) {
		this.tableOid = tableOid;
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
