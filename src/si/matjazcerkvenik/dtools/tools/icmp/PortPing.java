package si.matjazcerkvenik.dtools.tools.icmp;

import java.net.InetSocketAddress;
import java.net.Socket;

import si.matjazcerkvenik.dtools.context.DToolsContext;
import si.matjazcerkvenik.simplelogger.SimpleLogger;

public class PortPing {
	
private SimpleLogger logger;
	
	public PortPing() {
		logger = DToolsContext.getInstance().getLogger();
	}
	
	public EPingStatus ping(String hostname, int port) {
		try {
            Socket socket = new Socket();
            socket.connect(new InetSocketAddress(hostname, port), 10000);
            logger.info("PortPing: ping " + hostname + ":" + port + " [" + EPingStatus.UP + "]");
            socket.close();
            return EPingStatus.UP;
        } catch (Exception e) {
        	logger.error("PortPing:Exception: " + e.getMessage());
			return EPingStatus.DOWN;
        }
	}
	
}
