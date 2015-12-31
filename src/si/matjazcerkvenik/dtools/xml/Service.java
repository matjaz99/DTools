package si.matjazcerkvenik.dtools.xml;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;

import si.matjazcerkvenik.dtools.tools.icmp.EPingStatus;
import si.matjazcerkvenik.dtools.tools.icmp.IcmpPing;
import si.matjazcerkvenik.dtools.tools.icmp.PortPing;

public class Service {
	
	private String name;
	private String monitoringClass;
	private List<Param> params;
	private EPingStatus status = EPingStatus.UNKNOWN;

	public String getName() {
		return name;
	}

	@XmlAttribute
	public void setName(String name) {
		this.name = name;
	}

	public String getMonitoringClass() {
		return monitoringClass;
	}

	@XmlAttribute
	public void setMonitoringClass(String monitoringClass) {
		this.monitoringClass = monitoringClass;
	}

	public List<Param> getParams() {
		return params;
	}

	@XmlElement(name="param")
	public void setParams(List<Param> params) {
		this.params = params;
	}

	public EPingStatus getStatus() {
		return status;
	}

	@XmlTransient
	public void setStatus(EPingStatus status) {
		this.status = status;
	}
	
	public void addParam(String key, String value) {
		if (params == null) {
			params = new ArrayList<Param>();
		}
		params.add(new Param(key, value));
	}
	
	public String getParam(String key) {
		for (Param p : params) {
			if (p.getKey().equals(key)) {
				return p.getValue();
			}
		}
		return null;
	}
	
	public void pingService(Node node) {
		// TODO solve this with interface or something
		if (monitoringClass.equals("ICMP_PING")) {
			IcmpPing p = new IcmpPing();
			status = p.ping(node.getHostname());
		} else if (monitoringClass.equals("PORT_PING")) {
			PortPing p = new PortPing();
			String portStr = getParam("monitoring.port");
			int portInt = Integer.parseInt(portStr);
			status = p.ping(node.getHostname(), portInt);
		} else if (monitoringClass.equals("HTTP_PING")) {
			// TODO
		}
	}
	
	/**
	 * Convert IcmpPingStatus to appropriate icon.
	 * @param status
	 * @return icon
	 */
	public String getStatusIcon() {
		
		switch (status) {
		case UP:
			return "bullet_green";
		case DOWN:
			return "bullet_red";
		case UNKNOWN:
			return "bullet_black";
		default:
			break;
		}
		
		return "bullet_black";
		
	}
	
	public String getParamsString() {
		String s = "";
		if (params == null) {
			return s;
		}
		for (int i = 0; i < params.size(); i++) {
			s += params.get(i).getKey() + "=" + params.get(i).getValue() + "; ";
		}
		return s;
	}
	
}
