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

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;

import si.matjazcerkvenik.simplelogger.SimpleLogger;

public class DToolsContext {
	
	private static DToolsContext instance;

	public static String HOME_DIR;
	public static String version;
	
	private SimpleLogger logger;
	private long startTime;
	
	private DToolsContext() {
		setHomeDir();
		DProps.loadProperties();
		readVersion();
		initLogger();
	}
	
	public static DToolsContext getInstance() {
		
		if (instance == null) {
			instance = new DToolsContext();
			instance.startTime = System.currentTimeMillis();
		}
		return instance;
		
	}
	
	public SimpleLogger getLogger() {
		return logger;
	}
	
	private void setHomeDir() {
		
		String homeDir = System.getProperty("dtools.home");
		
		// if -Ddtools.home VM arg is not set, use ../server/apache-tomcat directory as default
		if (homeDir == null || homeDir.length() == 0) {
			String[] temp = System.getProperty("catalina.home").split("server\\" + File.separator + "+apache-tomcat");
			homeDir = temp[0];
		}
		if (homeDir.endsWith("/") || homeDir.endsWith("\\")) {
			homeDir = homeDir.substring(0, homeDir.length()-1);
		}
		
		HOME_DIR = homeDir;
		
	}
	
	/**
	 * Read version.txt.
	 */
	private void readVersion() {

		try {

			FileInputStream fis = new FileInputStream(HOME_DIR + "/config/version.txt");

			DataInputStream dis = new DataInputStream(fis);
			BufferedReader br = new BufferedReader(new InputStreamReader(dis));

			DToolsContext.version = br.readLine();

			dis.close();

		} catch (Exception e) {
			version = "0.0.0";
			e.printStackTrace();
		}

	}
	
	
	
	private void initLogger() {
		
		logger = new SimpleLogger(DProps.getProperties());
		// overwrite logger file location
		logger.setFilename(HOME_DIR + "/log/" + DProps.getProperty(DProps.SIMPLELOGGER_FILENAME));
		
		logger.info("");
		logger.info("\t+---------------------------------+");
		logger.info("\t|         Start DTools            |");
		logger.info("\t+---------------------------------+");
		logger.info("");
		logger.info("HOME_DIR=" + HOME_DIR);
		logger.info("VERSION=" + version);
		logger.info("OS=" + getOsType());
		logger.info("LOG_FILE=" + logger.getFilename());		
		
	}
	
	/**
	 * Return type of operating system: WINDOWS, OSX, LINUX
	 * @return
	 */
	public OperatingSystem getOsType() {
		String os = System.getProperty("os.name");
		
		if (os.equalsIgnoreCase("Mac os X")) {
			return OperatingSystem.OSX;
		} else if (os.equalsIgnoreCase("Linux")) {
			return OperatingSystem.LINUX;
		} else if (os.contains("Windows")) {
			return OperatingSystem.WINDOWS;
		}
		
		return OperatingSystem.OTHER;
	}
	
	
	public static String getCurrentDate() {
		
		Date d = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd H:mm:ss");
		return sdf.format(d);
		
	}
	
	public static long getSysUpTime() {
		return System.currentTimeMillis() - instance.startTime;
	}
	
}
