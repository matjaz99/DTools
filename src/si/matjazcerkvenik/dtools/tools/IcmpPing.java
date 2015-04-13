package si.matjazcerkvenik.dtools.tools;

import java.net.Inet4Address;

import si.matjazcerkvenik.dtools.xml.EPingStatus;

public class IcmpPing {
	
	public EPingStatus ping(String hostname) {
		
		try {
			if (Inet4Address.getByName(hostname).isReachable(1000)) {
				return EPingStatus.UP;
			} else {
				return EPingStatus.DOWN;
			}

		} catch (Exception e) {
			e.printStackTrace();
			return EPingStatus.DOWN;
		}
		
	}
	
}
