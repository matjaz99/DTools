package si.matjazcerkvenik.dtools.tools;

import java.net.Inet4Address;
import java.net.UnknownHostException;

import si.matjazcerkvenik.dtools.context.DToolsContext;
import si.matjazcerkvenik.dtools.xml.EPingStatus;
import si.matjazcerkvenik.simplelogger.SimpleLogger;

public class IcmpPing {
	
	private SimpleLogger logger;
	
	public IcmpPing() {
		logger = DToolsContext.getInstance().getLogger();
	}
	
	public EPingStatus ping(String hostname) {
		
		try {
			if (Inet4Address.getByName(hostname).isReachable(3000)) {
				logger.info("IcmpPing: ping " + hostname + " [" + EPingStatus.UP + "]");
				return EPingStatus.UP;
			} else {
				logger.info("IcmpPing: ping " + hostname + " [" + EPingStatus.DOWN + "]");
				return EPingStatus.DOWN;
			}

		} catch (UnknownHostException e) {
			logger.error("IcmpPing:UnknownHostException", e);
			return EPingStatus.DOWN;
		} catch (Exception e) {
			logger.error("IcmpPing:Exception", e);
			return EPingStatus.DOWN;
		}
		
	}
	
}
