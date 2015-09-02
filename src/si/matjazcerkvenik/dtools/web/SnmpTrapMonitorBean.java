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

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;

import si.matjazcerkvenik.dtools.tools.snmp.TrapNotification;

@ManagedBean
@SessionScoped
public class SnmpTrapMonitorBean {
	
	@ManagedProperty(value="#{snmpTrapReceiverBean}")
	private SnmpTrapReceiverBean snmpTrapReceiverBean;
	
	
	// only setter is needed to inject
	public void setSnmpTrapReceiverBean(SnmpTrapReceiverBean snmpTrapReceiverBean) {
		this.snmpTrapReceiverBean = snmpTrapReceiverBean;
	}



	public List<TrapNotification> getTrapNotifications() {
		Object[] array = snmpTrapReceiverBean.getTrapReceiver().getReceivedTrapNotifications().toArray();
		List<TrapNotification> list = new ArrayList<TrapNotification>();
		for (int i = 0; i < array.length; i++) {
			list.add((TrapNotification) array[i]);
		}
		return list;
		
	}
	
	/**
	 * Return coma-separated list of row classes.
	 * @return
	 */
	public String getBackgroundColors() {
		// FIXME this should be done differently in JSF 2 - style for each row
		StringBuilder rowClasses = new StringBuilder();
		Object[] array = snmpTrapReceiverBean.getTrapReceiver().getReceivedTrapNotifications().toArray();

	    for (int i = 0; i < array.length; i++) {
	    	TrapNotification tn = (TrapNotification) array[i];
	    	switch (tn.severity) {
			case 1:
				rowClasses.append("bgTrapColor-Critical");
				break;
			case 2:
				rowClasses.append("bgTrapColor-Major");
				break;
			case 3:
				rowClasses.append("bgTrapColor-Minor");
				break;
			case 4:
				rowClasses.append("bgTrapColor-Warning");
				break;
			case 5:
				rowClasses.append("bgTrapColor-Clear");
				break;
			case 6:
				rowClasses.append("bgTrapColor-Info");
				break;
			default:
				if (i % 2 == 0) {
					rowClasses.append("tableEvenRow");
				} else {
					rowClasses.append("tableOddRow");
				}
				break;
			}
	        if (i < array.length - 1) rowClasses.append(",");
	    }

	    return rowClasses.toString();
		
	}
	
}
