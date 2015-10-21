package si.matjazcerkvenik.dtools.tools.snmp;

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

public class SnmpRow {
	
	private int index;
	private List<String> valuesList;
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
	@XmlElement(name="value")
	public void setValuesList(List<String> valuesList) {
		this.valuesList = valuesList;
	}
	
}
