package si.matjazcerkvenik.dtools.xml;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;

import si.matjazcerkvenik.dtools.tools.IcmpPing;

public class Server {

	private String name;
	private String hostname;
	private String description;
	private EPingStatus icmpPingStatus = EPingStatus.UNKNOWN;

	public String getName() {
		return name;
	}

	@XmlElement
	public void setName(String name) {
		this.name = name;
	}

	public String getHostname() {
		return hostname;
	}

	@XmlElement
	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	public String getDescription() {
		return description;
	}

	@XmlElement
	public void setDescription(String description) {
		this.description = description;
	}

	public EPingStatus getIcmpPingStatus() {
		return icmpPingStatus;
	}

	@XmlTransient
	public void setIcmpPingStatus(EPingStatus status) {
		this.icmpPingStatus = status;
	}
	
	/**
	 * Send ping on this server
	 */
	public void updateIcmpStatus() {
		IcmpPing p = new IcmpPing();
		icmpPingStatus = p.ping(hostname);
	}

}
