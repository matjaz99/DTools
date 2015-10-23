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

import si.matjazcerkvenik.dtools.xml.VarBind;

public class SnmpTrap implements Cloneable {
	
	private String trapName;
	
	// common parameters
	private String version = "v2c";
	private String community = "public";
	private String sourceIp = "127.0.0.1";
	private List<VarBind> varbind;
	
	// v1 parameters
	private int genericTrap;
	private int specificTrap;
	private String enterpriseOid;
	private String timestamp;
	// coldStart trap (0), warmStart trap (1), linkDown trap(2)
	// linkUp trap(3), authenticationFailure trap(4), egpNeighborLoss trap(5)
	// enterprise specific (6)
	
	// v2c parameters
	private String snmpTrapOid;
	private String sysUpTime;
	

	public String getTrapName() { // TODO rename to 'name'
		return trapName;
	}

	@XmlElement
	public void setTrapName(String trapName) {
		this.trapName = trapName;
	}

	public String getVersion() {
		return version;
	}

	@XmlElement
	public void setVersion(String version) {
		this.version = version;
	}

	public String getCommunity() {
		return community;
	}

	@XmlElement
	public void setCommunity(String community) {
		this.community = community;
	}

	public int getGenericTrap() {
		return genericTrap;
	}

	@XmlElement
	public void setGenericTrap(int genericTrap) {
		this.genericTrap = genericTrap;
	}

	public int getSpecificTrap() {
		return specificTrap;
	}

	@XmlElement
	public void setSpecificTrap(int specificTrap) {
		this.specificTrap = specificTrap;
	}

	public String getEnterpriseOid() {
		return enterpriseOid;
	}

	@XmlElement
	public void setEnterpriseOid(String enterpriseOid) {
		this.enterpriseOid = enterpriseOid;
	}

	public String getTimestamp() {
		return timestamp;
	}

	@XmlElement
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public String getSourceIp() {
		return sourceIp;
	}

	@XmlElement
	public void setSourceIp(String sourceIp) {
		this.sourceIp = sourceIp;
	}

	public String getSnmpTrapOid() {
		return snmpTrapOid;
	}

	@XmlElement
	public void setSnmpTrapOid(String snmpTrapOid) {
		this.snmpTrapOid = snmpTrapOid;
	}

	public String getSysUpTime() {
		return sysUpTime;
	}

	@XmlElement
	public void setSysUpTime(String sysUpTime) {
		this.sysUpTime = sysUpTime;
	}

	public List<VarBind> getVarbind() {
		return varbind;
	}

	@XmlElement
	public void setVarbind(List<VarBind> varbind) {
		this.varbind = varbind;
	}
	
	public void addVarbind(String oid, String type, String value) {
		
		if (varbind == null) {
			varbind = new ArrayList<VarBind>();
		}
		
		VarBind v = new VarBind();
		v.setOid(oid);
		v.setType(type);
		v.setValue(value);
		
		varbind.add(v);
	}
	
	public String varbindsToString() {
		String s = "[";
		for (int i = 0; i < varbind.size(); i++) {
			s += varbind.get(i).toString();
			if (i < varbind.size() - 1) {
				s += ", ";
			}
		}
		s += "]";
		return s;
	}
	
	/**
	 * Clone this trap and make deep copy of varbinds
	 * @return trap
	 */
	public SnmpTrap makeClone() {
		SnmpTrap trap = null;
		try {
			trap = (SnmpTrap) this.clone();
			trap.setVarbind(cloneVarbinds());
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return trap;
	}
	
	/**
	 * Create deep copy of varbinds
	 * @return varbinds
	 */
	public List<VarBind> cloneVarbinds() {
		List<VarBind> copy = new ArrayList<VarBind>();
		for (int i = 0; i < varbind.size(); i++) {
			copy.add(varbind.get(i).makeClone());
		}
		return copy;
	}
	
}
