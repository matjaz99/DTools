package si.matjazcerkvenik.dtools.web;

import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import si.matjazcerkvenik.dtools.tools.snmp.SimpleSnmpClient;
import si.matjazcerkvenik.dtools.xml.SnmpClient;

@ManagedBean
@ViewScoped
public class SnmpClientBean {
	
	private SnmpClient snmpClient;
	
	private String oid = ".1.3.6.1.2.1.1.1.0";
	private String result;
	
	@PostConstruct
	public void init() {
		Map<String, Object> requestParameterMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
		snmpClient = (SnmpClient) requestParameterMap.get("snmpClient");
	}
	
	public SnmpClient getSnmpClient() {
		return snmpClient;
	}

	public void setSnmpClient(SnmpClient client) {
		this.snmpClient = client;
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
		System.out.println("host: " + snmpClient.getHostname() + ", " + snmpClient.getPort() + ", " + oid);
		SimpleSnmpClient snmp = new SimpleSnmpClient(snmpClient.getHostname(), snmpClient.getPort());
		result = "SNMP-GET " + oid + " = " + snmp.getAsString(oid);
		
	}
	
}
