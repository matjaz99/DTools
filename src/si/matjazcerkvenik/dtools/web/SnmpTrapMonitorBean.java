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
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import si.matjazcerkvenik.dtools.io.DAO;
import si.matjazcerkvenik.dtools.tools.snmp.impl.TrapNotification;
import si.matjazcerkvenik.dtools.tools.snmp.impl.TrapReceiver;

@ManagedBean
@ViewScoped
public class SnmpTrapMonitorBean implements Serializable {
	
	private static final long serialVersionUID = -3589597671351602203L;
	
	private List<TrapNotification> list;
	private TrapNotification activeTrap;
	private String receivedTrapsAsString;

	
	private TrapReceiver trapReceiver;
		
	
	@PostConstruct
	public void init() {
//		Map<String, Object> requestParameterMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
//		trapReceiver = (TrapReceiver) requestParameterMap.get("trapRecv");
//		
//		Map<String, String> requestParameterMap2 = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
//		if (requestParameterMap2.containsKey("name")) {
//			System.out.println("found " + requestParameterMap2.get("name"));
//		}
		
		Map<String, String> requestParameterMap = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		if (requestParameterMap.containsKey("recv")) {
			String name = requestParameterMap.get("recv");
			trapReceiver = DAO.getInstance().findtrapReceiver(name);
		}
	}
	
	
	/**
	 * Get trap to show info
	 * @return
	 */
	public TrapNotification getActiveTrap() {
		return activeTrap;
	}

	/**
	 * Set trap to show info
	 * @param activeTrap
	 */
	public void setActiveTrap(TrapNotification activeTrap) {
		this.activeTrap = activeTrap;
	}
	
	/**
	 * Make active trap null
	 */
	public void nullActiveTrap() {
		this.activeTrap = null;
	}
	
	/**
	 * Update list of traps (inside bean) in descending order
	 */
	public void updateTrapNotifications() {
		if (trapReceiver == null) {
			return;
		}
		list.clear();
		Object[] array = trapReceiver.getReceivedTrapNotifications().toArray();
		for (int i = array.length - 1; i >= 0; i--) {
			list.add((TrapNotification) array[i]);
		}
	}
	
	/**
	 * Get list of traps
	 * @return
	 */
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
		if (trapReceiver == null) {
			return rowClasses.toString();
		}
		Object[] array = trapReceiver.getReceivedTrapNotifications().toArray();

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
	
	/**
	 * Convert severity to style class
	 * @param severity
	 * @return styleClass
	 */
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
	
	
	/**
	 * Return traps in raw format
	 * @return traps
	 */
	public String getReceivedTrapsAsString() {
		
		if (trapReceiver == null) {
			return receivedTrapsAsString;
		}
		receivedTrapsAsString = "";
		Object[] array = trapReceiver.getReceivedTrapNotifications().toArray();
		for (int i = 0; i < array.length; i++) {
			TrapNotification tn = (TrapNotification) array[i];
			receivedTrapsAsString += tn.toStringRaw() + "\n";
		}
//		receivedTrapsAsString = receivedTrapsAsString.replaceAll(", ", "\n");
		return receivedTrapsAsString;
	}

	public void setReceivedTrapsAsString(String receivedTrapsAsString) {
		this.receivedTrapsAsString = receivedTrapsAsString;
	}
	
	public void clearData() {
		if (trapReceiver != null) {
			trapReceiver.clearReceivedTraps();
		}
		receivedTrapsAsString = null;
	}
	
	public void saveData() {
		String file = DAO.getInstance().saveReceivedTrapsAsTxt("snmp-traps", getReceivedTrapsAsString());
		Growl.addGrowlMessage("Saved as " + file, FacesMessage.SEVERITY_INFO);
	}
	
}
