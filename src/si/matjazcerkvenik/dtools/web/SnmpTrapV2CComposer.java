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

import si.matjazcerkvenik.dtools.context.DToolsContext;
import si.matjazcerkvenik.dtools.tools.localhost.LocalhostInfo;
import si.matjazcerkvenik.dtools.tools.snmp.SnmpAgent;
import si.matjazcerkvenik.dtools.tools.snmp.SnmpSimulator;
import si.matjazcerkvenik.dtools.tools.snmp.SnmpTrap;
import si.matjazcerkvenik.dtools.tools.snmp.TrapsTable;
import si.matjazcerkvenik.dtools.xml.DAO;
import si.matjazcerkvenik.dtools.xml.VarBind;

@ManagedBean
@ViewScoped
public class SnmpTrapV2CComposer implements Serializable {
	
	private static final long serialVersionUID = 533428296431557631L;
	
	private String trapName;
	private String community = "public";
	private String sourceIp = LocalhostInfo.getLocalIpAddress();
	private List<VarBind> varbinds;
	private boolean modifyMode = false;
	
	private SnmpAgent agent;
	private TrapsTable trapsList;
	private SnmpTrap originalTrap;
	
	@PostConstruct
	public void init() {
//		Map<String, Object> requestParameterMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
//		SnmpTrap trap = (SnmpTrap) requestParameterMap.get("trap");
		
		Map<String, String> requestParameterMap = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		
		// find agent
		if (requestParameterMap.containsKey("agent")) {
			String name = requestParameterMap.get("agent");
			SnmpSimulator sim = DAO.getInstance().loadSnmpSimulator();
			for (SnmpAgent a : sim.getSnmpAgentsList()) {
				if (a.getName().equals(name)) {
					agent = a;
					break;
				}
			}
		}
		
		// find trapList
		if (requestParameterMap.containsKey("trapsTableName")) {
			String name = requestParameterMap.get("trapsTableName");
			for (TrapsTable a : agent.getTrapsTableList()) {
				if (a.getName().equals(name)) {
					trapsList = a;
					break;
				}
			}
		}
		
		// find trap
		SnmpTrap trap = null;
		if (requestParameterMap.containsKey("trapName")) {
			String name = requestParameterMap.get("trapName");
			for (SnmpTrap a : trapsList.getTrapsList()) {
				if (a.getTrapName().equals(name)) {
					trap = a;
					break;
				}
			}
		}
		
		if (trap != null) {
			modifyMode = true;
			originalTrap = trap;
			trapName = trap.getTrapName();
			community = trap.getCommunity();
			sourceIp = trap.getSourceIp();
			varbinds = trap.cloneVarbinds(); // always use copy of varbinds (in case they are changed)
		}
	}

	public String getTrapName() {
		return trapName;
	}

	public void setTrapName(String trapName) {
		this.trapName = trapName;
	}

	public String getCommunity() {
		return community;
	}

	public void setCommunity(String community) {
		this.community = community;
	}

	public String getSourceIp() {
		return sourceIp;
	}

	public void setSourceIp(String sourceIp) {
		this.sourceIp = sourceIp;
	}

	public List<VarBind> getVarbinds() {
		if (varbinds == null) {
			varbinds = new ArrayList<VarBind>();
			varbinds.add(new VarBind("sysUpTime", "1.3.6.1.2.1.1.3.0", VarBind.TYPE_TIMETICKS, "" + DToolsContext.getSysUpTime()/10));
			varbinds.add(new VarBind("snmpTrapOid", "1.3.6.1.6.3.1.1.4.1.0", VarBind.TYPE_OCTET_STRING, "9.9.9"));
			varbinds.add(new VarBind("sourceIp", "1.3.6.1.6.3.18.1.3.0", VarBind.TYPE_IP_ADDRESS, LocalhostInfo.getLocalIpAddress()));
		}
		return varbinds;
	}

	public void setVarbinds(List<VarBind> varbinds) {
		this.varbinds = varbinds;
	}
	
	public void addNewOid() {
		varbinds.add(new VarBind("oidName", "1.", VarBind.TYPE_OCTET_STRING, "value"));
	}
	
	public void removeOid(VarBind vb) {
		varbinds.remove(vb);
	}
	
	public String saveTrap() {
		
		if (modifyMode) {
			SnmpTrap trap = findTrap(trapName);
			if (trap == null) {
				// trap not found because trapName is changed in modify mode
				trap = originalTrap.makeClone(); // clone original trap with deep copy of varbinds
				trap.setTrapName(trapName);
				trap = populateTrap(trap);
				DAO.getInstance().addSnmpTrap(trap);
				Growl.addGrowlMessage("Trap " + trapName + " saved", FacesMessage.SEVERITY_INFO);
			} else {
				trap = populateTrap(trap);
				DAO.getInstance().saveSnmpTraps(trapsList);
				Growl.addGrowlMessage("Trap " + trapName + " modified", FacesMessage.SEVERITY_INFO);
			}
		} else {
			SnmpTrap trap = new SnmpTrap();
			trap.setTrapName(trapName);
			trap = populateTrap(trap);
			DAO.getInstance().addSnmpTrap(trap);
			Growl.addGrowlMessage("Trap saved", FacesMessage.SEVERITY_INFO);
		}
		
		resetTrap();
		modifyMode = false;
		
		return "snmpTrapsTable";
	}
	
	/**
	 * Find trap according to trap name. Return null if trap not found.
	 * @param trapName
	 * @return trap
	 */
	private SnmpTrap findTrap(String trapName) {
		List<SnmpTrap> list = DAO.getInstance().loadSnmpTraps().getTrapsList();
		for (SnmpTrap trap : list) {
			if (trap.getTrapName().equals(trapName)) {
				return trap;
			}
		}
		return null;
	}
	
	/**
	 * Fill the values from GUI into trap (except trapName!)
	 * @param trap
	 * @return trap
	 */
	private SnmpTrap populateTrap(SnmpTrap trap) {
		trap.setVersion("v2c");
		trap.setCommunity(community);
		trap.setSourceIp(sourceIp);
		trap.setVarbind(varbinds);
		return trap;
	}
	
	/**
	 * Reset trap values to default values and remove trap object from session.
	 */
	public void resetTrap() {
		trapName = null;
		community = "public";
		sourceIp = LocalhostInfo.getLocalIpAddress();
		varbinds = null;
		originalTrap = null;
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("trap");
	}
	

}
