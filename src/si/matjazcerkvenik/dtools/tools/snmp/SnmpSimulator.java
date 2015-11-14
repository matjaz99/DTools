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

/**
 * This class contains a list of all agents.
 * 
 * @author matjaz
 *
 */
public class SnmpSimulator {
	
	private List<SnmpAgent> snmpAgentsList;

	public List<SnmpAgent> getSnmpAgentsList() {
		return snmpAgentsList;
	}
	
	public void setSnmpAgentsList(List<SnmpAgent> snmpAgentsList) {
		this.snmpAgentsList = snmpAgentsList;
	}
	
	public void addSnmpAgent(SnmpAgent a) {
		if (snmpAgentsList == null) {
			snmpAgentsList = new ArrayList<SnmpAgent>();
		}
		snmpAgentsList.add(a);
	}
	
	public void removeSnmpAgent(SnmpAgent a) {
		snmpAgentsList.remove(a);
	}
	
}
