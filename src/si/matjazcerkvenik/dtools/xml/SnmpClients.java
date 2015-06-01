package si.matjazcerkvenik.dtools.xml;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class SnmpClients {
	
	private List<SnmpClient> snmpClientsList;

	public List<SnmpClient> getSnmpClientsList() {
		return snmpClientsList;
	}

	@XmlElement(name="client")
	public void setSnmpClientsList(List<SnmpClient> clientsList) {
		this.snmpClientsList = clientsList;
	}
	
	public void addSnmpClient(SnmpClient c) {
		snmpClientsList.add(c);
	}
	
	public void deleteSnmpClient(SnmpClient c) {
		snmpClientsList.remove(c);
	}
	
	public List<SnmpClient> getCustomSnmpClientsList(String hostname) {
		
		List<SnmpClient> list = new ArrayList<SnmpClient>();
		
		for (int i = 0; i < getSnmpClientsList().size(); i++) {
			if (getSnmpClientsList().get(i).getHostname().equals(hostname)) {
				list.add(getSnmpClientsList().get(i));
			}
		}
		
		return list;
		
	}
	
}
