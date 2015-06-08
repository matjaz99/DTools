package si.matjazcerkvenik.dtools.tools.localhost;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class LocalhostInfo {
	
	public static String getLocalIpAddress() {
		try {
			return InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			return "UnknownHost";
		}
	}
	
	public static String getSystemUser() {
		String u = System.getProperty("user.name");
		if (u == null) {
			return "user";
		}
		return u;
	}
	
}
