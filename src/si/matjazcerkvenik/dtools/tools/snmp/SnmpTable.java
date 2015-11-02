package si.matjazcerkvenik.dtools.tools.snmp;

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@XmlRootElement
public class SnmpTable {
	
	// see http://stackoverflow.com/questions/13970285/creating-and-populating-a-datatable-dynamically-in-jsf2-0
	
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
	@XmlElement(name="metadata")
	public void setMetadata(TableMetadata metadata) {
		this.metadata = metadata;
	}
	public List<SnmpRow> getRowsList() {
		return rowsList;
	}
	@XmlElement(name="row")
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
	
	public void applyMetadataToRows() {
//		for (int i = 0; i < metadata.getColumnsMetaList().size(); i++) {
//			for (int j = 0; j < rowsList.size(); j++) {
//				rowsList.get(j).applyMetadata(metadata.getColumnsMetaList().get(i));
//			}
//		}
	}
	
	
	
	
	
	
}
