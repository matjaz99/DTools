package si.matjazcerkvenik.dtools.web;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import si.matjazcerkvenik.dtools.tools.snmp.SimpleSnmpManager;

@ManagedBean
@SessionScoped
public class SnmpBean {

	private String hostname = "centos6";
	private String port = "161";
	private String oid = ".1.3.6.1.2.1.1.1.0";
	private String result;

	public String getHostname() {
		return hostname;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getOid() {
		return oid;
	}

	public void setOid(String oid) {
		this.oid = oid;
	}
	
	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public void execute() {
		System.out.println("host: " + hostname + ", " + port + ", " + oid);
		SimpleSnmpManager snmp = new SimpleSnmpManager(hostname, port);
		result = "SNMP-GET " + oid + " = " + snmp.getAsString(oid);
		
	}

}
