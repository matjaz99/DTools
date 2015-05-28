package si.matjazcerkvenik.dtools.web;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import si.matjazcerkvenik.dtools.xml.FtpClient;
import si.matjazcerkvenik.dtools.xml.Server;
import si.matjazcerkvenik.dtools.xml.SnmpManager;
import si.matjazcerkvenik.dtools.xml.SshClient;

@ManagedBean
@SessionScoped
public class NavigationBean {
	
	public String openServer(Server s) {
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("server", s);
		return "server";
	}
	
	public String openSshClient(SshClient client) {
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("client", client);
		return "sshClient";
	}
	
	public String openFtpClient(FtpClient client) {
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("client", client);
		return "ftpClient";
	}
	
	public String openSnmpMng(SnmpManager mng) {
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("snmpMng", mng);
		return "snmpManager";
	}
	
	
	
}
