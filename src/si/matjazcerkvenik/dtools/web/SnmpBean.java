package si.matjazcerkvenik.dtools.web;

import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import si.matjazcerkvenik.dtools.xml.DAO;
import si.matjazcerkvenik.dtools.xml.SnmpManager;

@ManagedBean
@SessionScoped
public class SnmpBean {

	private String name;
	private String hostname;
	private String snmpVersion = "v2c";
	private String community = "public";
	private int port = 161;
	

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getHostname() {
		return hostname;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	public String getSnmpVersion() {
		return snmpVersion;
	}

	public void setSnmpVersion(String snmpVersion) {
		this.snmpVersion = snmpVersion;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getCommunity() {
		return community;
	}

	public void setCommunity(String community) {
		this.community = community;
	}

	
	
	public void addSnmpManagerAction() {

		SnmpManager m = new SnmpManager();
		m.setName(name);
		m.setHostname(hostname);
		m.setPort(port);
		m.setSnmpVersion(snmpVersion);
		m.setCommunity(community);

		DAO.getInstance().addSnmpManager(m);

		name = null;
		hostname = null;
		port = 161;
		community = "public";
		snmpVersion = "v2c";

	}

	public void deleteSnmpManagerAction(SnmpManager m) {
		DAO.getInstance().deleteSnmpManager(m);
	}

	public List<SnmpManager> getSnmpManagersList() {
		return DAO.getInstance().loadSnmpManagers().getSnmpManagerList();
	}

	

}
