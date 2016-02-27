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

package si.matjazcerkvenik.dtools.context;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class DProps {
	
	private static Properties props;
	
	public static final String SIMPLELOGGER_FILENAME = "simplelogger.filename";
	public static final String SIMPLELOGGER_LEVEL = "simplelogger.level";
	public static final String SIMPLELOGGER_APPEND = "simplelogger.append";
	public static final String SIMPLELOGGER_VERBOSE = "simplelogger.verbose";
	public static final String SIMPLELOGGER_MAXFILESIZE = "simplelogger.maxFileSize";
	public static final String SIMPLELOGGER_MAXBACKUPFILES = "simplelogger.maxBackupFiles";
	public static final String SIMPLELOGGER_DATEFORMAT = "simplelogger.dateFormat";
	public static final String SNMP_RECEIVER_QUEUE_SIZE = "snmp.receiver.queue.size";
	public static final String SNMP_RECEIVER_RULES_FILE = "snmp.rules.file";
	public static final String DTOOLS_GUI_CSS_THEME = "dtools.gui.css.theme";
	public static final String NETWORK_MONITORING_PING_POOL_SIZE = "network.monitoring.ping.pool.size";
	public static final String NETWORK_MONITORING_PING_INTERVAL = "network.monitoring.ping.interval";
	public static final String NETWORK_MONITORING_PING_TIMEOUT = "network.monitoring.ping.timeout";
	public static final String AUTO_DISCOVERY_THREAD_POOL_SIZE = "network.autodiscovery.thread.pool.size";
	
	private static Map<String, String> defaultValues = new HashMap<String, String>();
	
	// Default values
	static {
		defaultValues.put(SIMPLELOGGER_FILENAME, "dtools.log");
		defaultValues.put(SIMPLELOGGER_LEVEL, "INFO");
		defaultValues.put(SIMPLELOGGER_APPEND, "true");
		defaultValues.put(SIMPLELOGGER_VERBOSE, "false");
		defaultValues.put(SIMPLELOGGER_MAXFILESIZE, "1");
		defaultValues.put(SIMPLELOGGER_MAXBACKUPFILES, "2");
		defaultValues.put(SIMPLELOGGER_DATEFORMAT, "yyyy.MM.dd hh:mm:ss:SSS");
		defaultValues.put(SNMP_RECEIVER_QUEUE_SIZE, "100");
		defaultValues.put(SNMP_RECEIVER_RULES_FILE, "/config/users/default/snmp/manager/rules/trap-rules-default.js");
		defaultValues.put(DTOOLS_GUI_CSS_THEME, "default");
		defaultValues.put(NETWORK_MONITORING_PING_POOL_SIZE, "5");
		defaultValues.put(NETWORK_MONITORING_PING_INTERVAL, "60");
		defaultValues.put(NETWORK_MONITORING_PING_TIMEOUT, "5000");
		defaultValues.put(AUTO_DISCOVERY_THREAD_POOL_SIZE, "10");
	}
	
	/**
	 * Read dtools.properties and load parameters. Check if all parameters are present in 
	 * properties file. If property is missing, write it's default value into dtools.properties file.
	 */
	public static void loadProperties() {
		
		props = new Properties();
		
		try {
			props.load(new FileInputStream(DToolsContext.HOME_DIR + "/config/dtools.properties"));
			
			// check if all properties exist
			boolean propsModified = false;
			for (String key : defaultValues.keySet()) {
				if (!props.containsKey(key)) {
					propsModified = true;
					props.setProperty(key, defaultValues.get(key));
				}
			}
			
			// write default value if property didn't exist
			if (propsModified) {
				writeProperties();
			}
			
		} catch (IOException e) {
			props = null;
			e.printStackTrace();
		}
	}
	
	public static void writeProperties() {
		try {
			props.store(new FileOutputStream(DToolsContext.HOME_DIR + "/config/dtools.properties"), null);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static Properties getProperties() {
		if (props == null) {
			loadProperties();
		}
		return props;
	}
	
	/**
	 * Return property form dtools.properties
	 * @param key
	 * @return
	 */
	public static String getProperty(String key) {
		if (props == null) {
			loadProperties();
		}
		return props.getProperty(key);
	}
	
	/**
	 * Return property form dtools.properties as integer
	 * @param key
	 * @return int value
	 */
	public static int getPropertyInt(String key) {
		if (props == null) {
			loadProperties();
		}
		int x = 0;
		try {
			x = Integer.parseInt(props.getProperty(key));
		} catch (NumberFormatException e) {
			x = 0;
		}
		return x;
	}
	
	/**
	 * Set new property value and immediately save to file
	 * @param key
	 * @param string value
	 * @param true to write properties file
	 */
	public static void setProperty(String key, String value, boolean writeToFile) {
		props.put(key, value);
		if (writeToFile) {
			writeProperties();
		}
	}
	
}
