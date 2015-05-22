package si.matjazcerkvenik.dtools.web;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Properties;

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
		getLocalhostInfo();
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
	
	
	// TODO move this somewhere else
	public List<String> getLocalhostInfo() {
		
		
		InetAddress ip;
		try {
	 
			ip = InetAddress.getLocalHost();
			System.out.println("Current IP address : " + ip.getHostAddress());
	 
			NetworkInterface network = NetworkInterface.getByInetAddress(ip);
	 
			byte[] mac = network.getHardwareAddress();
	 
			System.out.print("Current MAC address : ");
	 
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < mac.length; i++) {
				sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));		
			}
			System.out.println(sb.toString());
	 
		} catch (UnknownHostException e) {
	 
			e.printStackTrace();
	 
		} catch (SocketException e){
	 
			e.printStackTrace();
	 
		}
		
		
		//1.Get Java Runtime
	    Runtime runtime = Runtime.getRuntime();
	    System.out.println("Runtime=" + Runtime.getRuntime());

	    //2. Get Number of Processor availaible to JVM
	    int numberOfProcessors = runtime.availableProcessors();
	    System.out.println(numberOfProcessors + " Processors ");

	    //2. Get FreeMemory, Max Memory and Total Memory
	    long freeMemory = runtime.freeMemory();
	    System.out.println("Bytes=" + freeMemory + " |KB=" + freeMemory / 1024 + " |MB=" + (freeMemory / 1024) / 1024+" Free Memory in JVM");

	    long maxMemory = runtime.maxMemory();
	    System.out.println(maxMemory + "-Bytes " + maxMemory / 1024 + "-KB  " + (maxMemory / 1024) / 1024 + "-MB " + " Max Memory Availaible in JVM");

	    long totalMemory = runtime.totalMemory();
	    System.out.println(totalMemory + "-Bytes " + totalMemory / 1024 + "-KB " + (totalMemory / 1024) / 1024 + "-MB " + " Total Memory Availaible in JVM");
		
	  //6. Get OS Name
	    String strOSName = System.getProperty("os.name");
	    System.out.println("This is "+strOSName);

	    //7. Get JVM Spec
	    String strJavaVersion = System.getProperty("java.specification.version");
	    System.out.println("JVM Spec : " + strJavaVersion);
	    //8. Get Class Path
	    String strClassPath = System.getProperty("java.class.path");
	    System.out.println("Classpath: " + strClassPath);

	    //9. Get File Separator
	    String strFileSeparator = System.getProperty("file.separator");
	    System.out.println("File separator: " + strFileSeparator);

	    //10. Get System Properties
	    Properties prop = System.getProperties();
	    System.out.println("System Properties(detail): " + prop);

	    //11. Get System Time
	    long lnSystemTime = System.currentTimeMillis();
	    System.out.println("Milliseconds since midnight, January 1, 1970 UTC : " + lnSystemTime + "\nSecond=" + lnSystemTime / 1000 + "\nMinutes=" + (lnSystemTime / 1000) / 60 + ""
	            + "\nHours=" + ((lnSystemTime / 1000) / 60) / 60 + "\nDays=" +   (((lnSystemTime / 1000) / 60) / 60) / 24 + "\nYears=" + ((((lnSystemTime / 1000) / 60) /  60) / 24) / 365);
	    
	    return null;
	    
	}
	
}
