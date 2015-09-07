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

package si.matjazcerkvenik.dtools.xml;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

import org.snmp4j.smi.Counter32;
import org.snmp4j.smi.Counter64;
import org.snmp4j.smi.Gauge32;
import org.snmp4j.smi.Integer32;
import org.snmp4j.smi.IpAddress;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.TimeTicks;
import org.snmp4j.smi.UnsignedInteger32;
import org.snmp4j.smi.VariableBinding;

public class VarBind implements Cloneable {

	private String oid;
	private String oidName;
	private String type = TYPE_OCTET_STRING;
	private String value;
	
	public static final String TYPE_OCTET_STRING = "OCTET_STRING";
	public static final String TYPE_INTEGER = "INTEGER";
	public static final String TYPE_OID = "OID";
	public static final String TYPE_GAUGE = "GAUGE";
	public static final String TYPE_COUNTER32 = "COUNTER32";
	public static final String TYPE_IP_ADDRESS = "IP_ADDRESS";
	public static final String TYPE_TIMETICKS = "TIMETICKS";
	public static final String TYPE_COUNTER64 = "COUNTER64";
	public static final String TYPE_UNSIGNED_INTEGER = "UNSIGNED_INTEGER";
	
	public VarBind() {
	}

	public VarBind(String oidName, String oid, String type, String value) {
		this.oid = oid;
		this.oidName = oidName;
		this.type = type;
		this.value = value;
	}

	public String getOid() {
		return oid;
	}

	@XmlAttribute
	public void setOid(String oid) {
		this.oid = oid;
	}

	public String getOidName() {
		return oidName;
	}

	@XmlAttribute
	public void setOidName(String oidName) {
		this.oidName = oidName;
	}

	public String getType() {
		return type;
	}

	@XmlAttribute
	public void setType(String type) {
		this.type = type;
	}

	public String getValue() {
		return value;
	}

	@XmlAttribute
	public void setValue(String value) {
		this.value = value;
	}
	
	public VariableBinding getSnmp4jVarBind() {
		if (type.equals(TYPE_OCTET_STRING)) {
			return new VariableBinding(new OID(oid), new OctetString(value));
		} else if (type.equals(TYPE_INTEGER)) {
			return new VariableBinding(new OID(oid), new Integer32(Integer.parseInt(value)));
		} else if (type.equals(TYPE_OID)) {
			return new VariableBinding(new OID(oid), new OID(value));
		} else if (type.equals(TYPE_GAUGE)) {
			return new VariableBinding(new OID(oid), new Gauge32(Long.parseLong(value)));
		} else if (type.equals(TYPE_COUNTER32)) {
			return new VariableBinding(new OID(oid), new Counter32(Long.parseLong(value)));
		} else if (type.equals(TYPE_IP_ADDRESS)) {
			return new VariableBinding(new OID(oid), new IpAddress(value));
		} else if (type.equals(TYPE_TIMETICKS)) {
			return new VariableBinding(new OID(oid), new TimeTicks(Long.parseLong(value)));
		} else if (type.equals(TYPE_COUNTER64)) {
			return new VariableBinding(new OID(oid), new Counter64(Long.parseLong(value)));
		} else if (type.equals(TYPE_UNSIGNED_INTEGER)) {
			return new VariableBinding(new OID(oid), new UnsignedInteger32(Integer.parseInt(value)));
		}
		return new VariableBinding(new OID(oid), new OctetString(value));
	}
	
	@Override
	public String toString() {
		return oid + "=" + value;
	}
	
	
	public VarBind makeClone() {
		VarBind vb = null;
		try {
			vb = (VarBind) this.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return vb;
	}

}
