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
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class DProps {
	
	private static Properties props;
	
	public static String SIMPLELOGGER_FILENAME = "simplelogger.filename";
	public static String SIMPLELOGGER_LEVEL = "simplelogger.level";
	public static String SIMPLELOGGER_APPEND = "simplelogger.append";
	public static String SIMPLELOGGER_VERBOSE = "simplelogger.verbose";
	public static String SIMPLELOGGER_MAXFILESIZE = "simplelogger.maxFileSize";
	public static String SIMPLELOGGER_MAXBACKUPFILES = "simplelogger.maxBackupFiles";
	public static String SIMPLELOGGER_DATEFORMAT = "simplelogger.dateFormat";
	public static String SNMP_RECEIVER_QUEUE_SIZE = "snmp.receiver.queue.size";
	public static String SNMP_RECEIVER_RULES_FILE = "snmp.rules.file";
	
	private static Map<String, String> defaultValues = new HashMap<String, String>();
	
	static {
		defaultValues.put(SIMPLELOGGER_FILENAME, "dtools.log");
		defaultValues.put(SIMPLELOGGER_LEVEL, "INFO");
		defaultValues.put(SIMPLELOGGER_APPEND, "true");
		defaultValues.put(SIMPLELOGGER_VERBOSE, "false");
		defaultValues.put(SIMPLELOGGER_MAXFILESIZE, "1");
		defaultValues.put(SIMPLELOGGER_MAXBACKUPFILES, "2");
		defaultValues.put(SIMPLELOGGER_DATEFORMAT, "yyyy.MM.dd hh:mm:ss:SSS");
		defaultValues.put(SNMP_RECEIVER_QUEUE_SIZE, "100");
		defaultValues.put(SNMP_RECEIVER_RULES_FILE, "/config/users/default/snmp/trap-rules-default.js");
	}
	
	/**
	 * Read dtools.properties and load parameters.
	 * @return properties
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
			
			if (propsModified) {
				props.store(new FileOutputStream(DToolsContext.HOME_DIR + "/config/dtools.properties"), null);
			}
			
		} catch (IOException e) {
			props = null;
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
	
}
