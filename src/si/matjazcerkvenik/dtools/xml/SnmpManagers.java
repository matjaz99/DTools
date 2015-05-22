package si.matjazcerkvenik.dtools.xml;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class SnmpManagers {
	
	private List<SnmpManager> snmpManagerList;

	public List<SnmpManager> getSnmpManagerList() {
		return snmpManagerList;
	}

	@XmlElement(name="mng")
	public void setSnmpManagerList(List<SnmpManager> snmpMngList) {
		this.snmpManagerList = snmpMngList;
	}
	
	public void addSnmpManager(SnmpManager m) {
		snmpManagerList.add(m);
	}
	
	public void deleteSnmpManager(SnmpManager m) {
		snmpManagerList.remove(m);
	}
	
	public List<SnmpManager> getCustomSnmpManagersList(String hostname) {
		
		List<SnmpManager> list = new ArrayList<SnmpManager>();
		
		for (int i = 0; i < getSnmpManagerList().size(); i++) {
			if (getSnmpManagerList().get(i).getHostname().equals(hostname)) {
				list.add(getSnmpManagerList().get(i));
			}
		}
		
		return list;
		
	}
	
}
