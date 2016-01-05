package si.matjazcerkvenik.dtools.xml;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;

import si.matjazcerkvenik.dtools.tools.ping.IcmpPing;
import si.matjazcerkvenik.dtools.tools.ping.PingStatus;
import si.matjazcerkvenik.dtools.tools.ping.PortPing;

public class Service {
	
	private String name;
	private String monitoringClass;
	private List<Param> params;
	private PingStatus status = new PingStatus();

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

	public PingStatus getStatus() {
		return status;
	}

	@XmlTransient
	public void setStatus(PingStatus status) {
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
		
		switch (status.getErrorCode()) {
		case 0:
			return "bullet_black";
		case 1:
			return "bullet_green";
		default:
//			return "bullet_red";
			break;
		}
		
		return "bullet_red";
		
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
