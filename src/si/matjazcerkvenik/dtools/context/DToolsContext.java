package si.matjazcerkvenik.dtools.context;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import si.matjazcerkvenik.simplelogger.SimpleLogger;

public class DToolsContext {
	
	private static DToolsContext instance;

	public static String HOME_DIR;
	public static String version;
	private Properties props;
	
	private SimpleLogger logger;
	
	
	private DToolsContext() {
		setHomeDir();
		loadProperties();
		readVersion();
		initLogger();
	}
	
	public static DToolsContext getInstance() {
		
		if (instance == null) {
			instance = new DToolsContext();
		}
		return instance;
		
	}
	
	public SimpleLogger getLogger() {
		return logger;
	}
	
	private void setHomeDir() {
		
		String homeDir = System.getProperty("dtools.home");
		
		// if -Ddtools.home VM arg is not set, use ../server/ directory as default
		if (homeDir == null || homeDir.length() == 0) {
			String[] temp = System.getProperty("catalina.home").split("server");
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
	
	/**
	 * Read dtools.properties and load parameters.
	 * @return properties
	 */
	private void loadProperties() {
		
		props = new Properties();
		
		try {
			props.load(new FileInputStream(HOME_DIR + "/config/dtools.properties"));
			// overwrite logger file location
			props.setProperty("simplelogger.filename", HOME_DIR + "/log/" + props.getProperty("simplelogger.filename"));
		} catch (IOException e) {
			props = null;
			e.printStackTrace();
		}
	}
	
	private void initLogger() {
		
		logger = new SimpleLogger(props);
		
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
	
}
