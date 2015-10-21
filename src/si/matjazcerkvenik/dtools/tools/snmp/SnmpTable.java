package si.matjazcerkvenik.dtools.tools.snmp;

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class SnmpTable {
	
	private String name;
	private SnmpTableMetadata metadata;
	private List<SnmpRow> rowsList;
	
	public String getName() {
		return name;
	}
	@XmlAttribute
	public void setName(String name) {
		this.name = name;
	}
	public SnmpTableMetadata getMetadata() {
		return metadata;
	}
	@XmlElement
	public void setMetadata(SnmpTableMetadata metadata) {
		this.metadata = metadata;
	}
	public List<SnmpRow> getRowsList() {
		return rowsList;
	}
	@XmlElement(name="row")
	public void setRowsList(List<SnmpRow> rowsList) {
		this.rowsList = rowsList;
	}
	
}
