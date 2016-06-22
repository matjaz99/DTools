/* 
 * Copyright (C) 2015 Matjaz Cerkvenik
 * 
 * DTools is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * DTools is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with DTools. If not, see <http://www.gnu.org/licenses/>.
 * 
 */

package si.matjazcerkvenik.dtools.web.beans;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import si.matjazcerkvenik.dtools.context.DProps;
import si.matjazcerkvenik.dtools.context.DToolsContext;
import si.matjazcerkvenik.dtools.io.DAO;
import si.matjazcerkvenik.dtools.tools.localhost.LocalhostInfo;

@ManagedBean
@SessionScoped
public class UtilsBean {
	
	public String getLocalIpAddress() {
		return LocalhostInfo.getLocalIpAddress();
	}
	
	public String getNetworkInterfaces() {
		return LocalhostInfo.printNetworkInterfaces();
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
			
			if (mac != null) {
				StringBuilder sb = new StringBuilder();
				for (int i = 0; i < mac.length; i++) {
					sb.append(String.format("%02X%s", mac[i],
							(i < mac.length - 1) ? "-" : ""));
				}
				list.add("MAC address: " + sb.toString());
			} else {
				list.add("MAC address: n/a");
			}
	 
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
	
	
	
	public void reloadAllData() {
		DAO.getInstance().resetAllDataToNull();
	}
	
	public String getDtoolsVersion() {
		return DToolsContext.version;
	}
	
	private String pingInterval;
	private String pingTimeout;
	private String pingPoolSize;
	private String autodiscoveryPoolSize;
	private String trapReceiverQueueSize;
	private String chartsWidth;
	private String chartsHeight;
	private String statisticsHistoryHours;
	private String sshDisconnectTimeout;
	private String sshHistoryAgingPeriod;

	public String getPingInterval() {
		return DProps.getProperty(DProps.NETWORK_MONITORING_PING_INTERVAL);
	}

	public void setPingInterval(String pingInterval) {
		this.pingInterval = pingInterval;
	}
	
	public String getPingTimeout() {
		return DProps.getProperty(DProps.NETWORK_MONITORING_PING_TIMEOUT);
	}

	public void setPingTimeout(String pingTimeout) {
		this.pingTimeout = pingTimeout;
	}

	public String getPingPoolSize() {
		return DProps.getProperty(DProps.NETWORK_MONITORING_PING_POOL_SIZE);
	}

	public void setPingPoolSize(String pingPoolSize) {
		this.pingPoolSize = pingPoolSize;
	}

	public String getAutodiscoveryPoolSize() {
		return DProps.getProperty(DProps.AUTO_DISCOVERY_THREAD_POOL_SIZE);
	}

	public void setAutodiscoveryPoolSize(String autodiscoveryPoolSize) {
		this.autodiscoveryPoolSize = autodiscoveryPoolSize;
	}

	
	public String getTrapReceiverQueueSize() {
		return DProps.getProperty(DProps.SNMP_RECEIVER_QUEUE_SIZE);
	}

	public void setTrapReceiverQueueSize(String trapReceiverQueueSize) {
		this.trapReceiverQueueSize = trapReceiverQueueSize;
	}

	public String getChartsWidth() {
		return DProps.getProperty(DProps.NETWORK_STATISTICS_CHART_WIDTH);
	}

	public void setChartsWidth(String chartsWidth) {
		this.chartsWidth = chartsWidth;
	}

	public String getChartsHeight() {
		return DProps.getProperty(DProps.NETWORK_STATISTICS_CHART_HEIGHT);
	}

	public void setChartsHeight(String chartsHeight) {
		this.chartsHeight = chartsHeight;
	}

	public String getStatisticsHistoryHours() {
		return DProps.getProperty(DProps.NETWORK_STATISTICS_HISTORY_HOURS);
	}

	public void setStatisticsHistoryHours(String statisticsHistoryHours) {
		this.statisticsHistoryHours = statisticsHistoryHours;
	}

	public String getSshDisconnectTimeout() {
		return DProps.getProperty(DProps.SSH_DISCONNECT_TIMEOUT);
	}

	public void setSshDisconnectTimeout(String sshDisconnectTimeout) {
		this.sshDisconnectTimeout = sshDisconnectTimeout;
	}

	public String getSshHistoryAgingPeriod() {
		return DProps.getProperty(DProps.SSH_HISTORY_AGING_PERIOD);
	}

	public void setSshHistoryAgingPeriod(String sshHistoryAgingPeriod) {
		this.sshHistoryAgingPeriod = sshHistoryAgingPeriod;
	}

	public void saveSettings() {
		DProps.setProperty(DProps.NETWORK_MONITORING_PING_INTERVAL, pingInterval, false);
		DProps.setProperty(DProps.NETWORK_MONITORING_PING_TIMEOUT, pingTimeout, false);
		DProps.setProperty(DProps.NETWORK_MONITORING_PING_POOL_SIZE, pingPoolSize, false);
		DProps.setProperty(DProps.AUTO_DISCOVERY_THREAD_POOL_SIZE, autodiscoveryPoolSize, false);
		DProps.setProperty(DProps.SNMP_RECEIVER_QUEUE_SIZE, trapReceiverQueueSize, false);
		DProps.setProperty(DProps.NETWORK_STATISTICS_CHART_WIDTH, chartsWidth, false);
		DProps.setProperty(DProps.NETWORK_STATISTICS_CHART_HEIGHT, chartsHeight, false);
		DProps.setProperty(DProps.NETWORK_STATISTICS_HISTORY_HOURS, statisticsHistoryHours, false);
		DProps.setProperty(DProps.SSH_DISCONNECT_TIMEOUT, sshDisconnectTimeout, false);
		DProps.setProperty(DProps.SSH_HISTORY_AGING_PERIOD, sshHistoryAgingPeriod, true);
		Growl.addGrowlMessage("Settings saved", FacesMessage.SEVERITY_INFO);
	}
	
}
