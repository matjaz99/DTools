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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.event.ValueChangeEvent;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;

import si.matjazcerkvenik.dtools.context.DToolsContext;
import si.matjazcerkvenik.dtools.io.DAO;
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
	private boolean monitoringEnabled = true;
	private List<Param> params;
	private Node node;  // just a reference to node object
	private IPing ping;
	
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

	public boolean isMonitoringEnabled() {
		return monitoringEnabled;
	}

	@XmlAttribute(name="monitoringActive")
	public void setMonitoringEnabled(boolean monitoringEnabled) {
		this.monitoringEnabled = monitoringEnabled;
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
	 * Return service parameters according to selected key. If null is returned 
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
		
//		System.out.println(Thread.currentThread().getName() + " ping " + node.getName() + "(" + node.getHostname() + ")\t");
		ping.ping();
		
		// format: thread, ping class, node, ping status
		String s = Thread.currentThread().getName() + " " + ping.getClass().getSimpleName() + " " + node.getName()
				+ " " + ping.getStatus().toString();
		DToolsContext.getInstance().getPingLogger().info(s);
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
	
	public void changedMonitoringEnabled(ValueChangeEvent e) {
		if (e.getOldValue().toString().equalsIgnoreCase(e.getNewValue().toString())) {
			return;
		}
		monitoringEnabled = (Boolean) e.getNewValue();
		DAO.getInstance().saveNetworkNodes();
		if (!monitoringEnabled) {
			ping.resetStatus();
		}
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
