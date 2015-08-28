package si.matjazcerkvenik.dtools.tools.snmp;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class SnmpContext {

	int someCtx;
	
	public static final int UNDEFINED = 0;
	public static final int CRITICAL = 1;
	public static final int MAJOR = 2;
	public static final int MINOR = 3;
	public static final int WARNING = 4;
	public static final int CLEAR = 5;
	public static final int INFO = 6;

	public int getSomeCtx() {
		return someCtx;
	}

	public void setSomeCtx(int someCtx) {
		this.someCtx = someCtx;
	}

	public void resolveHostname(String ip) {
		
		String hostname = null;
		try {
			InetAddress addr = InetAddress.getByName(ip);
			String host = addr.getHostName();
			System.out.println(host);
		} catch (UnknownHostException e) {
			e.printStackTrace();
			hostname = ip;
		}
		

	}
	
	public static void main(String[] args) {
		SnmpContext c = new SnmpContext();
		c.resolveHostname("centos6");
		c.resolveHostname("127.0.0.1");
		c.resolveHostname("192.168.1.110");
		c.resolveHostname("centos66");
		c.resolveHostname("192.168.1.123");
	}
	

}
