package si.matjazcerkvenik.dtools.tools.ping;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

public class PortPing {
		
	public PingStatus ping(String hostname, int port) {
		
		PingStatus ps = new PingStatus();
		ps.started();
		
		try {
            Socket socket = new Socket();
            socket.connect(new InetSocketAddress(hostname, port), 10000);
            socket.close();
            ps.setErrorCode(PingStatus.EC_OK);
			ps.setErrorMessage(PingStatus.EM_OK);
        } catch (IOException e) {
        	ps.setErrorCode(PingStatus.EC_IO_ERROR);
			ps.setErrorMessage(PingStatus.EM_IO_ERROR);
			ps.setErrorDescription(e.getMessage());
        } catch (Exception e) {
        	ps.setErrorCode(PingStatus.EC_CONN_ERROR);
			ps.setErrorMessage(PingStatus.EM_CONN_ERROR);
			ps.setErrorDescription(e.getMessage());
        }
		
		ps.ended();
		return ps;
		
	}
	
}
