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
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import si.matjazcerkvenik.dtools.tools.snmp.impl.TrapNotification;

@ManagedBean
@ViewScoped
public class SnmpTrapMonitorBean implements Serializable {
	
	private static final long serialVersionUID = -3589597671351602203L;

	@ManagedProperty(value="#{snmpTrapReceiverBean}")
	private SnmpTrapReceiverBean snmpTrapReceiverBean;
	
	private List<TrapNotification> list;
	private TrapNotification activeTrap;



	// only setter is needed to inject
	public void setSnmpTrapReceiverBean(SnmpTrapReceiverBean snmpTrapReceiverBean) {
		this.snmpTrapReceiverBean = snmpTrapReceiverBean;
	}



	public TrapNotification getActiveTrap() {
		return activeTrap;
	}



	public void setActiveTrap(TrapNotification activeTrap) {
		this.activeTrap = activeTrap;
	}
	
	public void nullActiveTrap() {
		this.activeTrap = null;
	}
	
	public void updateTrapNotifications() {
		if (snmpTrapReceiverBean.getTrapReceiver() == null) {
			return;
		}
		Object[] array = snmpTrapReceiverBean.getTrapReceiver().getReceivedTrapNotifications().toArray();
		for (int i = array.length - 1; i >= 0; i--) {
			list.add((TrapNotification) array[i]);
		}
	}



	public List<TrapNotification> getTrapNotifications() {
		if (list == null) {
			list = new ArrayList<TrapNotification>();
			updateTrapNotifications();
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
		if (snmpTrapReceiverBean.getTrapReceiver() == null) {
			return rowClasses.toString();
		}
		Object[] array = snmpTrapReceiverBean.getTrapReceiver().getReceivedTrapNotifications().toArray();

		// sort ascending
//	    for (int i = 0; i < array.length; i++) {
//	    	TrapNotification tn = (TrapNotification) array[i];
//	    	String s = getStyleClass(tn.severity);
//	    	if (s == null) {
//	    		if (i % 2 == 0) {
//					rowClasses.append("tableEvenRow");
//				} else {
//					rowClasses.append("tableOddRow");
//				}
//			} else {
//				rowClasses.append(s);
//			}
//	        if (i < array.length - 1) rowClasses.append(",");
//	    }
		
		// sort descending
		for (int i = array.length - 1; i >= 0; i--) {
	    	TrapNotification tn = (TrapNotification) array[i];
	    	String s = getStyleClass(tn.severity);
	    	if (s == null) {
	    		if (i % 2 == 0) {
					rowClasses.append("tableEvenRow");
				} else {
					rowClasses.append("tableOddRow");
				}
			} else {
				rowClasses.append(s);
			}
	        if (i > 0) rowClasses.append(",");
	    }

	    return rowClasses.toString();
		
	}
	
	private String getStyleClass(int severity) {
		switch (severity) {
		case 1:
			return "bgTrapColor-Critical";
		case 2:
			return "bgTrapColor-Major";
		case 3:
			return "bgTrapColor-Minor";
		case 4:
			return "bgTrapColor-Warning";
		case 5:
			return "bgTrapColor-Clear";
		case 6:
			return "bgTrapColor-Info";
		default:
			break;
		}
		return null;
	}
	
	
	



	

	
	
}
