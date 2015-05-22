package si.matjazcerkvenik.dtools.web;

import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import si.matjazcerkvenik.dtools.tools.snmp.SimpleSnmpManager;
import si.matjazcerkvenik.dtools.xml.SnmpManager;

@ManagedBean
@ViewScoped
public class SnmpManagerBean {
	
	private SnmpManager snmpManager;
	
	private String oid = ".1.3.6.1.2.1.1.1.0";
	private String result;
	
	@PostConstruct
	public void init() {
		Map<String, Object> requestParameterMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
		snmpManager = (SnmpManager) requestParameterMap.get("snmpMng");
	}
	
	public SnmpManager getSnmpManager() {
		return snmpManager;
	}

	public void setSnmpManager(SnmpManager snmpManager) {
		this.snmpManager = snmpManager;
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
		System.out.println("host: " + snmpManager.getHostname() + ", " + snmpManager.getPort() + ", " + oid);
		SimpleSnmpManager snmp = new SimpleSnmpManager(snmpManager.getHostname(), snmpManager.getPort());
		result = "SNMP-GET " + oid + " = " + snmp.getAsString(oid);
		
	}
	
}
