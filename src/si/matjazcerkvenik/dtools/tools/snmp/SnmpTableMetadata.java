package si.matjazcerkvenik.dtools.tools.snmp;

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

public class SnmpTableMetadata {
	
	private String tableOid;
	private List<SnmpColumn> columnsMetaList;

	public String getTableOid() {
		return tableOid;
	}

	@XmlAttribute(name="oid")
	public void setTableOid(String tableOid) {
		this.tableOid = tableOid;
	}

	public List<SnmpColumn> getColumnsMetaList() {
		return columnsMetaList;
	}

	@XmlElement
	public void setColumnsMetaList(List<SnmpColumn> columnsMetaList) {
		this.columnsMetaList = columnsMetaList;
	}
	
}
