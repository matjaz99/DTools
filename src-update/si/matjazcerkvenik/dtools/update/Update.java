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
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;

public class Update {
	
	private static String lastVersionUrl = "http://www.matjazcerkvenik.si/projects/dtools/getLastVersion.php";
	
	
	public static void main(String[] args) {
		
		Update u = new Update();
		String currentVersion = u.getCurrentVersionFromTxt();
		String lastVersion = u.getLastVersionFromTheServer();
		boolean proceed = u.isUpdateRequired(currentVersion, lastVersion);
		if (!proceed) {
			System.out.println("\tUpdate is not required");
			System.exit(0);
		}
		u.deleteDirectory(new File("server/apache-tomcat-7.0.57/work/Catalina/localhost/DTools"));
		u.deleteDirectory(new File("server/apache-tomcat-7.0.57/webapps/DTools"));
		u.deleteFile(new File("server/apache-tomcat-7.0.57/webapps/DTools.war"));
		u.downloadWar(lastVersion);
		u.updateVersion(lastVersion);
		System.out.println("\tSuccessfully updated to " + lastVersion);
		
	}
	
	
	
	/**
	 * Read version.txt file
	 */
	public String getCurrentVersionFromTxt() {

		String ver = "0.0.0";

		try {

			FileInputStream fis = new FileInputStream("config/version.txt");
			DataInputStream dis = new DataInputStream(fis);
			BufferedReader br = new BufferedReader(new InputStreamReader(dis));
			ver = br.readLine().trim();
			dis.close();
			
			System.out.println("\tCurrent version: " + ver);

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("\tError: cannot read config/version.txt");
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

			// print result
			System.out.println("\tLast version: " + response.toString());
			lastVersion = response.toString();

		} catch (MalformedURLException e) {
			System.out.println("\tError: MalformedURLException: cannot read version from the server");
			System.exit(0);
		} catch (IOException e) {
			System.out.println("\tError: IOException: cannot read version from the server");
			System.exit(0);
		}
		
		return lastVersion;

	}
	
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
			System.out.println("\tCannot update developer version!");
			System.exit(0);
			return false;
		}
		
		int currVer = convertToInteger(currentVersion);
		int lastVer = convertToInteger(lastVersion);
		
		if (lastVer == currVer) {
			System.out.println("\tDTools is up to date");
			System.exit(0);
			return false;
		}
		
		if (lastVer > currVer) {
			return true;
		}
		
		return false;
		
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
	 * Download last war from the mc.si server into /server/apache-tomcat-7.0.57/webapps directory.
	 * War will be automatically deployed when server starts.
	 */
	private void downloadWar(String version) {
		
		String url = "http://www.matjazcerkvenik.si/projects/download/DTools/" + version + "/DTools.war";
		
		System.out.println("\tDownloading..... please wait......");
		try {
			ReadableByteChannel in = Channels.newChannel(new URL(url).openStream());
			FileOutputStream fos = new FileOutputStream("server/apache-tomcat-7.0.57/webapps/DTools.war");
			FileChannel channel = fos.getChannel();
			channel.transferFrom(in, 0, Long.MAX_VALUE);
			channel.close();
			fos.close();
		} catch (MalformedURLException e) {
			System.out.println("\tError: MalformedURLException: cannot download " + url);
			System.exit(0);
		} catch (IOException e) {
			System.out.println("\tError: IOException: cannot download " + url);
			System.exit(0);
		}
		
		System.out.println("\tDownload complete");
		
	}
	
	
	
	
	/**
	 * Write last version to version.txt file.
	 */
	private void updateVersion(String version) {
		try {
			FileWriter fw = new FileWriter("config/version.txt");
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(version);
			bw.close();
		} catch (IOException e) {
			System.out.println("\tError: IOException: cannot update config/version.txt");
		}
	}
	
}
