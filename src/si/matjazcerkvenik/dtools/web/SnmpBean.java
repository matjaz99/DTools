package si.matjazcerkvenik.dtools.web;

import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import si.matjazcerkvenik.dtools.xml.DAO;
import si.matjazcerkvenik.dtools.xml.SnmpClient;

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

	
	
	public void addSnmpClient() {

		SnmpClient c = new SnmpClient();
		c.setName(name);
		c.setHostname(hostname);
		c.setPort(port);
		c.setSnmpVersion(snmpVersion);
		c.setCommunity(community);

		DAO.getInstance().addSnmpClient(c);

		name = null;
		hostname = null;
		port = 161;
		community = "public";
		snmpVersion = "v2c";

	}

	public void deleteSnmpClientAction(SnmpClient c) {
		DAO.getInstance().deleteSnmpClients(c);
	}

	public List<SnmpClient> getSnmpClientsList() {
		return DAO.getInstance().loadSnmpClients().getSnmpClientsList();
	}

	

}
