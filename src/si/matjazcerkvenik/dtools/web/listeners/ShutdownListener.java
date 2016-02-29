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

package si.matjazcerkvenik.dtools.web.listeners;

import javax.faces.context.FacesContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import si.matjazcerkvenik.dtools.context.DToolsContext;
import si.matjazcerkvenik.dtools.tools.snmp.SnmpAgent;
import si.matjazcerkvenik.dtools.web.beans.SnmpManagerBean;
import si.matjazcerkvenik.dtools.web.beans.SnmpSimulatorBean;

public class ShutdownListener implements ServletContextListener {
	
	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		DToolsContext.getInstance();
		System.out.println("DTools:ShutdownListener:contextInitialized()");
	}
	
	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		System.out.println("DTools:ShutdownListener:contextDestroyed()");
		
		// close snmp trap sender
		try {
			SnmpSimulatorBean snmpTS = (SnmpSimulatorBean) FacesContext.getCurrentInstance().getExternalContext().getApplicationMap().get("snmpSimulatorBean");
			for (SnmpAgent ag : snmpTS.getSnmpAgents()) {
				ag.stop();
			}
		} catch (Exception e) {
		}
		
		// close snmp trap receiver
		try {
			SnmpManagerBean snmpMng = (SnmpManagerBean) FacesContext.getCurrentInstance().getExternalContext().getApplicationMap().get("snmpManagerBean");
			snmpMng.stopAllReceivers();
		} catch (Exception e) {
		}
		
		// close logger
		DToolsContext.getInstance().getLogger().close();
	}
	
}
