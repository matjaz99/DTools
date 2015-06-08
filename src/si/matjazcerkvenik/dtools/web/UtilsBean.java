package si.matjazcerkvenik.dtools.web;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import si.matjazcerkvenik.dtools.tools.localhost.LocalhostInfo;

@ManagedBean
@SessionScoped
public class UtilsBean {
	
	public String getLocalIpAddress() {
		return LocalhostInfo.getLocalIpAddress();
	}
	
	public String getSystemUser() {
		return LocalhostInfo.getSystemUser();
	}
	
	

	public List<String> getLocalhostInfo() {
		
		List<String> list = new ArrayList<String>();
		list.add("Local IP address: " + getLocalIpAddress());
		
		InetAddress ip;
		try {
	 
			ip = InetAddress.getLocalHost();
			NetworkInterface network = NetworkInterface.getByInetAddress(ip);
			byte[] mac = network.getHardwareAddress();
			
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < mac.length; i++) {
				sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));		
			}
			list.add("MAC address: " + sb.toString());
	 
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (SocketException e){
			e.printStackTrace();
		}
		
		
		
		// Get Java Runtime
	    Runtime runtime = Runtime.getRuntime();
	    list.add("Runtime: " + Runtime.getRuntime());

	    // Get Number of Processor availaible to JVM
	    int numberOfProcessors = runtime.availableProcessors();
	    list.add(numberOfProcessors + " Processors ");

	    // Get FreeMemory, Max Memory and Total Memory
	    long freeMemory = runtime.freeMemory();
	    list.add("Bytes=" + freeMemory + " |KB=" + freeMemory / 1024 + " |MB=" + (freeMemory / 1024) / 1024+" Free Memory in JVM");

	    long maxMemory = runtime.maxMemory();
	    list.add(maxMemory + "-Bytes " + maxMemory / 1024 + "-KB  " + (maxMemory / 1024) / 1024 + "-MB " + " Max Memory Availaible in JVM");

	    long totalMemory = runtime.totalMemory();
	    list.add(totalMemory + "-Bytes " + totalMemory / 1024 + "-KB " + (totalMemory / 1024) / 1024 + "-MB " + " Total Memory Availaible in JVM");
		
	    // Get OS Name
	    String strOSName = System.getProperty("os.name");
	    list.add("Operating system: "+strOSName);

	    // Get JVM Spec
	    String strJavaVersion = System.getProperty("java.specification.version");
	    list.add("JVM Spec : " + strJavaVersion);
	    // Get Class Path
	    String strClassPath = System.getProperty("java.class.path");
	    list.add("Classpath: " + strClassPath);

	    // Get File Separator
	    String strFileSeparator = System.getProperty("file.separator");
	    list.add("File separator: " + strFileSeparator);

	    // Get System Properties
	    Properties prop = System.getProperties();
//	    list.add("System Properties(detail): " + prop);
	    @SuppressWarnings("rawtypes")
		Enumeration keys = prop.keys();
	    while (keys.hasMoreElements()) {
	    	String key = (String) keys.nextElement();
	    	String value = (String) prop.get(key);
	    	list.add(key + ": " + value);
	    }

	    // Get System Time
	    long lnSystemTime = System.currentTimeMillis();
	    list.add("Milliseconds since midnight, January 1, 1970 UTC : " + lnSystemTime + "\nSecond=" + lnSystemTime / 1000 + "\nMinutes=" + (lnSystemTime / 1000) / 60 + ""
	            + "\nHours=" + ((lnSystemTime / 1000) / 60) / 60 + "\nDays=" +   (((lnSystemTime / 1000) / 60) / 60) / 24 + "\nYears=" + ((((lnSystemTime / 1000) / 60) /  60) / 24) / 365);
	    
	    return list;
	    
	}
	
}
