package si.matjazcerkvenik.dtools.xml;

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;

import si.matjazcerkvenik.dtools.tools.icmp.EPingStatus;
import si.matjazcerkvenik.dtools.tools.icmp.IcmpPing;
import si.matjazcerkvenik.dtools.tools.icmp.PortPing;

public class Service {
	
	private String name;
	private List<Param> params;
	private EPingStatus status = EPingStatus.UNKNOWN;

	public String getName() {
		return name;
	}

	@XmlAttribute
	public void setName(String name) {
		this.name = name;
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
	
	public void pingService(Node node) {
		// TODO solve this with interface or something
		if (name.equals("ICMP")) {
			IcmpPing p = new IcmpPing();
			status = p.ping(node.getHostname());
		} else {
			PortPing p = new PortPing();
			status = p.ping(node.getHostname(), 21);
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
	
}
