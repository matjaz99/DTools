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

package si.matjazcerkvenik.dtools.web;

import java.io.Serializable;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

import si.matjazcerkvenik.dtools.tools.localhost.LocalhostInfo;
import si.matjazcerkvenik.dtools.tools.snmp.SnmpManager;
import si.matjazcerkvenik.dtools.tools.snmp.impl.TrapReceiver;
import si.matjazcerkvenik.dtools.xml.DAO;

@ManagedBean
@ApplicationScoped
public class SnmpManagerBean implements Serializable {
	
	private static final long serialVersionUID = -8242154775224005611L;

	public void addNewTrapReceiver() {
		
		TrapReceiver r = new TrapReceiver("TrapReceiver" + DAO.getInstance().loadSnmpManager().getTrapReceiversList().size(), 
				LocalhostInfo.getLocalIpAddress(), 6162);
		
		DAO.getInstance().addTrapReceiver(r);
		Growl.addGrowlMessage("Created SNMP manager: " + r.toString(), FacesMessage.SEVERITY_INFO);
		
	}
	
	public void deleteTrapReceiver(TrapReceiver r) {
		r.stop();
		DAO.getInstance().deleteTrapReceiver(r);
		Growl.addGrowlMessage(r.getName() + " deleted", FacesMessage.SEVERITY_INFO);
	}
	
	public List<TrapReceiver> getTrapReceivers() {
		return DAO.getInstance().loadSnmpManager().getTrapReceiversList();
	}
	
	public void toggleTrapReceiver(TrapReceiver r) {
		
		if (r.isActive()) {
			r.start();
			Growl.addGrowlMessage("Start listening for traps", FacesMessage.SEVERITY_INFO);
		} else {
			// already listening
			r.stop();
			Growl.addGrowlMessage("Stop listening", FacesMessage.SEVERITY_INFO);
		}
	}
	
	public void stopAllReceivers() {
		SnmpManager m = DAO.getInstance().loadSnmpManager();
		for (int i = 0; i < m.getTrapReceiversList().size(); i++) {
			m.getTrapReceiversList().get(i).stop();
		}
	}
	

}
