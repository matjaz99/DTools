package si.matjazcerkvenik.dtools.update;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

public class Update {
	
	private static String lastVersionUrl = "http://www.matjazcerkvenik.si/projects/dtools/getLastVersion.php";
	private static String lastWarUrl = "http://www.matjazcerkvenik.si/projects/download/DTools/@version/DTools.war";
//	private static String webappsDir = "/Users/matjaz/Desktop/DTools-1.0.0/server/apache-tomcat-7.0.57/webapps";
//	private static String warFile = "/Users/matjaz/Desktop/DTools-1.0.0/server/apache-tomcat-7.0.57/webapps/DTools.war";
//	private static String repositoryDir = "/Users/matjaz/Desktop/DTools-1.0.0/repository";
//	private static String versionTxt = "/Users/matjaz/Desktop/DTools-1.0.0/config/version.txt";
	private static String webappsDir = "server/apache-tomcat-7.0.57/webapps";
	private static String warFile = "server/apache-tomcat-7.0.57/webapps/DTools.war";
	private static String repositoryDir = "repository";
	private static String versionTxt = "config/version.txt";
	
	private String md5Checksum = "0";
	
	public static void main(String[] args) {
		
		Update u = new Update();
		
		if (args.length > 0) {
			
			if (args[0].equalsIgnoreCase("-h") 
					|| args[0].equalsIgnoreCase("--help") 
					|| args[0].equalsIgnoreCase("help")
					|| args[0].equalsIgnoreCase("?")) {
				u.printHelp();
			}
			
		}
		
		// get versions
		u.isTomcatRunning();
		String currentVersion = u.getCurrentVersionFromTxt();
		String lastVersion = u.getLastVersionFromTheServer();
		lastWarUrl = lastWarUrl.replace("@version", lastVersion);
		
		// check versions
		boolean proceed = u.isUpdateRequired(currentVersion, lastVersion);
		if (!proceed) {
			System.out.println("DTools is up to date");
			System.exit(0);
		}
		
		// ask user to proceed
		while (true) {
			
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	        System.out.print("Do you want to upgrade now? [y/n]: ");
	        try {
				String s = br.readLine();
				if (s.equalsIgnoreCase("n") || s.equalsIgnoreCase("no")) {
					System.out.println("Exit");
					System.exit(0);
				} else if (s.equalsIgnoreCase("y") || s.equalsIgnoreCase("yes")) {
					break;
				} else {
					System.out.println("Choose yes or no!");
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		// delete work directories
		u.deleteDirectory(new File("server/apache-tomcat-7.0.57/work/Catalina/localhost/DTools"));
		u.deleteDirectory(new File("server/apache-tomcat-7.0.57/webapps/DTools"));
		
		// download war to home.dir
		try {
			u.download(lastWarUrl);
		} catch (IOException e) {
			System.out.println("ERROR: Failed to download new version, please try again later");
			System.exit(0);
		}
		
		if (MD5Checksum.getMd5Checksum("DTools.war").equals(u.md5Checksum)) {
			System.out.println("MD5 checksum is correct");
		} else {
			System.out.println("ERROR: File is corrupted, please try downloading again");
			System.exit(0);
		}
		
		// move old war to repository
		proceed = u.moveFile(warFile, repositoryDir + "/" + currentVersion);
		if (proceed) {
//			System.out.println("moved old");
		} else {
//			System.out.println("not moved");
		}
		
		// move new war to webapps
		proceed = u.moveFile("DTools.war", webappsDir);
		if (proceed) {
//			System.out.println("moved new");
		} else {
//			System.out.println("not moved");
		}
//		u.deleteFile(new File(warFile));
		
		
		u.updateVersion(lastVersion);
		System.out.println("Successfully updated to " + lastVersion);
		
	}
	
	public void printHelp() {
		
		System.out.println("DTools update help");
		System.out.println("-h or --help\tprint help");
		System.out.println("-c or --check\tcheck for new version only");
		System.out.println("-m or --md5\tmd5 checksum");
		
	}
	
	
	/**
	 * Check weather Tomcat is running by opening connection to localhost:8080
	 * @return true if Tomcat is running
	 */
	public boolean isTomcatRunning() {
		try {
		    new URL("http://localhost:8080").openConnection().connect();
		    System.out.println("WARN: Please stop the server before updating!");
			System.exit(0);
		    return true;
		} catch (IOException e) {
		}
		return false;
	}
	
	
	
	/**
	 * Read version.txt file
	 * @return current version
	 */
	public String getCurrentVersionFromTxt() {

		String ver = "0.0.0";

		try {

			FileInputStream fis = new FileInputStream(versionTxt);
			DataInputStream dis = new DataInputStream(fis);
			BufferedReader br = new BufferedReader(new InputStreamReader(dis));
			ver = br.readLine().trim();
			dis.close();
			
			System.out.println("Current version: " + ver);

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("ERROR: cannot read " + versionTxt);
			System.exit(0);
		}

		return ver;

	}
	
	/**
	 * Send request to the mc.si server and read last version. Return 
	 * version in x.y.z format.
	 * @return version
	 */
	public String getLastVersionFromTheServer() {
		
		String lastVersion = "0.0.0";

		try {
			InputStream iStream = new URL(lastVersionUrl).openStream();
			BufferedReader in = new BufferedReader(new InputStreamReader(iStream));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
			
			String[] array = response.toString().split("#");
			lastVersion = array[0];
			md5Checksum = array[1];

			// print result
			System.out.println("Last version: " + lastVersion);

		} catch (MalformedURLException e) {
			System.out.println("ERROR: MalformedURLException: cannot read version from the server");
			System.exit(0);
		} catch (IOException e) {
			System.out.println("ERROR: IOException: cannot retrieve data from the server");
			System.exit(0);
		}
		
		return lastVersion;

	}
	
	/**
	 * Convert version to integer:<br>
	 * 1. split version string by periods (.)<br>
	 * 2. make every version (major, minor, patch) 3-digit number by adding zeros at the beginning<br>
	 * 3. join the string and convert to integer<br><br>
	 * Example: version 1.20.333 (String) --&gt; 001020333 (String) --&gt; 1020333 (int)<br>
	 * With this principle there are possibilities to support 1000 major, 1000 minor and 1000 patch versions 
	 * for simple comparison of integers. 
	 * @param version
	 * @return version as integer
	 */
	public int convertToInteger(String version) {
		
		String[] tempVer = version.split("\\.");
		String convertString = tempVer[0];
		if (tempVer[1].length() == 1) {
			convertString += "00" + tempVer[1];
		} else if (tempVer[1].length() == 2) {
			convertString += "0" + tempVer[1];
		} else {
			convertString += tempVer[1];
		}
		if (tempVer[2].length() == 1) {
			convertString += "00" + tempVer[2];
		} else if (tempVer[2].length() == 2) {
			convertString += "0" + tempVer[2];
		} else {
			convertString += tempVer[2];
		}
		
		int v = Integer.parseInt(convertString);
		
		return v;
	}
	
	/**
	 * Parse the last version and compare it to installed version. Return true if 
	 * last version is higher than current version.
	 * @param currentVersion
	 * @param lastVersion
	 * @return true if update is required
	 */
	public boolean isUpdateRequired(String currentVersion, String lastVersion) {
		
		if (currentVersion.contains("alpha") || currentVersion.contains("beta")) {
			System.out.println("Cannot update developer version!");
			System.exit(0);
			return false;
		}
		
		int currVer = convertToInteger(currentVersion);
		int lastVer = convertToInteger(lastVersion);
		
		if (lastVer == currVer) {
			System.out.println("DTools is up to date");
			System.exit(0);
			return false;
		}
		
		if (lastVer > currVer) {
			return true;
		}
		
		return false;
		
	}
	
	private boolean moveFile(String sourceFile, String destDir) {
		File srcFile = new File(sourceFile);
		File destFile = new File(destDir + File.separator + srcFile.getName());
		destFile.getParentFile().mkdirs();
		return srcFile.renameTo(destFile);
	}

	
	/**
	 * Delete given directory and all its contents
	 * 
	 * @param directory
	 * @return true on success
	 */
	private boolean deleteDirectory(File directory) {
		if (directory.exists()) {
			File[] files = directory.listFiles();
			if (null != files) {
				for (int i = 0; i < files.length; i++) {
					if (files[i].isDirectory()) {
						deleteDirectory(files[i]);
					} else {
						files[i].delete();
					}
				}
			}
		}
		return directory.delete();
	}
	
	
	/**
	 * Delete single file
	 * @param file
	 * @return true on success
	 */
	private boolean deleteFile(File file) {
		if (file.exists()) {
			return file.delete();
		}
		return false;
	}
	
	
	/**
	 * Download latest war from the matjazcerkvenik.si server into /server/apache-tomcat-7.0.57/webapps directory.
	 * War will be automatically deployed when server starts.
	 * @throws IOException 
	 */
	public void download(String url) throws IOException {

		String[] temp = url.split("/");
		String filename = temp[temp.length - 1];
		
		System.out.print("Downloading " + filename + " ");

		boolean downloadComplete = false;
		
		while (!downloadComplete) {
			downloadComplete = transferData(url, filename);
		}

	}

	public boolean transferData(String url, String filename) throws IOException {
		
		long transferedSize = getFileSize(filename);
		
		try {

			URL website = new URL(url);
			URLConnection connection = website.openConnection();
			connection.setRequestProperty("Range", "bytes="+transferedSize+"-");
			ReadableByteChannel rbc = Channels.newChannel(connection.getInputStream());
			long remainingSize = connection.getContentLength();
			long buffer = remainingSize;
			if (remainingSize > 65536) {
				buffer = 1 << 16;
			}
//			System.out.println("Remaining size: " + remainingSize);

			if (transferedSize == remainingSize) {
				System.out.println("Download complete");
				rbc.close();
				return true;
			}

			FileOutputStream fos = new FileOutputStream(filename, true);
			
//			System.out.println("Downloading at " + transferedSize);
			while (remainingSize > 0) {
				long delta = fos.getChannel().transferFrom(rbc, transferedSize, buffer);
				transferedSize += delta;
//				System.out.println(transferedSize + " bytes received");
				System.out.print(".");
				if (delta == 0) {
					break;
				}
			}
			fos.close();
//			System.out.println("Download incomplete, retrying");
			
		} catch (MalformedURLException e) {
			System.out.println("ERROR: MalformedURLException: cannot download " + url);
		} catch (IOException e) {
			System.out.println("ERROR: IOException: cannot download " + url);
			throw new IOException();
		}
		
		return false;
		
	}

	public long getFileSize(String file) {
		File f = new File(file);
		return f.length();
	}
	
	
	
	
	/**
	 * Write last version to version.txt file.
	 */
	private void updateVersion(String version) {
		try {
			FileWriter fw = new FileWriter(versionTxt);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(version);
			bw.close();
		} catch (IOException e) {
			System.out.println("ERROR: IOException: cannot update config/version.txt");
		}
	}
	
}
