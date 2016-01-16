package si.matjazcerkvenik.dtools.xml;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;

import si.matjazcerkvenik.dtools.tools.ping.DummyPing;
import si.matjazcerkvenik.dtools.tools.ping.HttpPing;
import si.matjazcerkvenik.dtools.tools.ping.IPing;
import si.matjazcerkvenik.dtools.tools.ping.IcmpPing;
import si.matjazcerkvenik.dtools.tools.ping.PingStatus;
import si.matjazcerkvenik.dtools.tools.ping.PortPing;

public class Service implements Serializable {
	
	private static final long serialVersionUID = -4021275493603174833L;
	
	private String name;
	private String monitoringClass;
	private List<Param> params;
	private Node node;  // just a reference to node object
	private IPing ping;
//	private PingStatus status = new PingStatus();
	
	public void init(Node node) {
		this.node = node;
		initPing();
	}

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
		return ping.getStatus();
	}

//	@XmlTransient
//	public void setStatus(PingStatus status) {
//		this.status = status;
//	}
	
	public Node getNode() {
		return node;
	}

	@XmlTransient
	public void setNode(Node node) {
		this.node = node;
	}

	public void addParam(String key, String value) {
		if (params == null) {
			params = new ArrayList<Param>();
		}
		params.add(new Param(key, value));
	}
	
	/**
	 * Return param value according to selected key. If null is returned 
	 * no key is found.
	 * @param key
	 * @return value
	 */
	public String getParam(String key) {
		for (Param p : params) {
			if (p.getKey().equals(key)) {
				return p.getValue();
			}
		}
		return null;
	}
	
	public void pingService() {
		
		if (ping == null) {
			initPing();
		}
		System.out.println(Thread.currentThread().getName() + " ping " + node.getName() + "(" + node.getHostname() + ")\t" + " [" + new Date() + "] " + ping.getClass().getName());
		ping.ping();
		
	}
	
	private void initPing() {
		
		if (ping != null) {
			return;
		}
		
		if (monitoringClass.equals("DISABLED")) {
			ping = new DummyPing();
		} else if (monitoringClass.equals("ICMP_PING")) {
			ping = new IcmpPing();
		} else if (monitoringClass.equals("PORT_PING")) {
			ping = new PortPing();
		} else if (monitoringClass.equals("HTTP_PING")) {
			ping = new HttpPing();
		}
		ping.configure(this);
		
	}
	
	
	/**
	 * Convert ping status to appropriate icon.
	 * @return icon
	 */
	public String getStatusIcon() {
		return ping.getStatusIcon();
	}
	
	
	/**
	 * Return all params in single string (aka toString)
	 * @return params string
	 */
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
