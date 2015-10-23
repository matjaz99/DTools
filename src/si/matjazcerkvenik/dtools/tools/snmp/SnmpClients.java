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
