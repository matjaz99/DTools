package si.matjazcerkvenik.dtools.web;

import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean
@SessionScoped
public class UtilsBean {
	
	public String getLocalAddress() {
		try {
			return InetAddress.getLocalHost().getHostName();
		} catch (UnknownHostException e) {
			return "UnknownHost";
		}
	}
	
	public String getSystemUser() {
		String u = System.getProperty("user.name");
		if (u == null) {
			return "user";
		}
		return u;
	}
	
}
