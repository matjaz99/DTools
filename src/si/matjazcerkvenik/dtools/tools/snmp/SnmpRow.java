package si.matjazcerkvenik.dtools.tools.snmp;

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;

public class SnmpRow {

	private int index;
	private List<String> valuesList;
//	private TableMetadata metadata;

	public int getIndex() {
		return index;
	}

	@XmlAttribute
	public void setIndex(int index) {
		this.index = index;
	}

	public List<String> getValuesList() {
		return valuesList;
	}

	@XmlElement(name = "value")
	public void setValuesList(List<String> valuesList) {
		this.valuesList = valuesList;
	}

//	public TableMetadata getMetadata() {
//		return metadata;
//	}
//
//	@XmlTransient
//	public void setMetadata(TableMetadata metadata) {
//		this.metadata = metadata;
//	}
	
//	public String getHeader() {
//		String s = "";
//		for (int i = 0; i < metadata.getColumnsMetaList().size(); i++) {
//			s += metadata.getColumnsMetaList().get(i).getName();
//		}
//		return s;
//	}
	
	public void applyMetadata(ColumnMetadata columnMeta) {
//		for (int i = 0; i < valuesList.size(); i++) {
//			valuesList.get(i).setColumnMetadata(columnMeta);
//		}
	}

}
