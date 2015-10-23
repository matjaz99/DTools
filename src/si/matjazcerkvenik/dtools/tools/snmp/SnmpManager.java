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

import si.matjazcerkvenik.dtools.tools.localhost.LocalhostInfo;
import si.matjazcerkvenik.dtools.tools.snmp.impl.TrapReceiver;

@XmlRootElement
public class SnmpManager {
	
	private List<TrapReceiver> trapReceiversList;
	
	public List<TrapReceiver> getTrapReceiversList() {
		return trapReceiversList;
	}

	@XmlElement(name="trapReceiver")
	public void setTrapReceiversList(List<TrapReceiver> trapReceiversList) {
		this.trapReceiversList = trapReceiversList;
	}

	public void addTrapReceiver(TrapReceiver receiver) {
		trapReceiversList.add(receiver);
	}
	
	public void removeTrapReceiver(TrapReceiver receiver) {
		trapReceiversList.remove(receiver);
	}
	
	
	
	public void createDefaultTrapReceiver() {
		if (trapReceiversList == null) {
			trapReceiversList = new ArrayList<TrapReceiver>();
		}
		TrapReceiver tr = new TrapReceiver("TrapReceiver0", LocalhostInfo.getLocalIpAddress(), 6162);
		trapReceiversList.add(tr);
	}
	
	
}
