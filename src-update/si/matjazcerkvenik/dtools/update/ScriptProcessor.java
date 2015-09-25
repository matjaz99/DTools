package si.matjazcerkvenik.dtools.update;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import si.matjazcerkvenik.dtools.update.xml.Action;
import si.matjazcerkvenik.dtools.update.xml.Script;

public class ScriptProcessor {
	
	private Script script;
	
	public void loadScript() {
		
		System.out.println("URL: " + Update.installScriptUrl);
		
		try {
			InputStream iStream = new URL(Update.installScriptUrl).openStream();
			
			try {

				JAXBContext jaxbContext = JAXBContext.newInstance(Script.class);
				Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
				script = (Script) jaxbUnmarshaller.unmarshal(iStream);
				
//				System.out.println("loadScript(): " + Update.installScriptUrl);
				System.out.println(script.toString());

			} catch (JAXBException e) {
				System.out.println("ERROR: JAXBException: cannot read file from server: " + Update.installScriptUrl);
			}

			iStream.close();

		} catch (MalformedURLException e) {
			System.out.println("ERROR: MalformedURLException: cannot read file from server: " + Update.installScriptUrl);
			System.exit(0);
		} catch (IOException e) {
			System.out.println("ERROR: IOException: cannot retrieve data from server: " + Update.installScriptUrl);
			System.exit(0);
		}
		
	}
	
	
	public void processScript() {
		
		boolean proceed = true;
		
		for (int i = 0; i < script.getActionsList().size(); i++) {
			
			Action a = script.getActionsList().get(i);
			
			if (a.getType().equalsIgnoreCase("delete")) {
				
				deleteDirectory(new File(a.getSource()));
				Update.println(a.toString() + " - OK");
				
			} else if (a.getType().equalsIgnoreCase("move")) {
				
				proceed = moveFile(a.getSource(), a.getDest());
				Update.println(a.toString() + " - OK");
				
			} else if (a.getType().equalsIgnoreCase("download")) {
				
				download(a.getSource(), a.getDest());
				Update.println(a.toString() + " - OK");
				if (MD5.getMd5(a.getDest()).equals(a.getMd5())) {
					System.out.println("MD5 checksum: OK");
				} else {
					System.out.println("ERROR: Downloaded file is corrupted, please run update again");
					System.exit(0);
				}
			}
			
			if (!proceed) {
				break;
			}
			
		}
		
	}
	
	/**
	 * Move a source file to chosen destination directory.
	 * @param sourceFile
	 * @param destDir
	 * @return true if successfully moved
	 */
	public boolean moveFile(String sourceFile, String destDir) {
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
	public boolean deleteDirectory(File directory) {
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
	 * Download latest war from the matjazcerkvenik.si server into /server/apache-tomcat-7.0.57/webapps directory.
	 * War will be automatically deployed when server starts.
	 * @throws IOException 
	 */
	public void download(String url, String localFile) {

//		String[] temp = url.split("/");
//		String filename = temp[temp.length - 1];
		
		System.out.print("Downloading " + localFile);

		boolean downloadComplete = false;
		
		try {
			while (!downloadComplete) {
				downloadComplete = transferData(url, localFile);
			}
		} catch (Exception e) {
			System.out.println("ERROR: Failed to download new version, please try again later");
			System.exit(0);
		}

	}
	
	/**
	 * This method does actual reading of data from the file channel
	 * @param url
	 * @param filename
	 * @return true if download completed
	 * @throws IOException
	 */
	private boolean transferData(String url, String filename) throws IOException {
		
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
//			System.out.println("Size: " + (remainingSize + transferedSize)/1000/1000 + " MB");

			if (transferedSize == remainingSize) {
				Update.println(" 100%");
				rbc.close();
				return true;
			}

			FileOutputStream fos = new FileOutputStream(filename, true);
			
			Update.debug("Downloading at " + transferedSize);
			while (remainingSize > 0) {
				long delta = fos.getChannel().transferFrom(rbc, transferedSize, buffer);
				transferedSize += delta;
				Update.debug(transferedSize + " bytes received");
				// TODO show percentage
//				float percent = transferedSize / remainingSize * 100;
//				Update.print("" + percent);
//				if (percent > 80) {
//					Update.print(" 80% ");
//				} else if (percent > 60) {
//					Update.print(" 60% ");
//				} else if (percent > 40) {
//					Update.print(" 40% ");
//				} else if (percent > 20) {
//					Update.print(" 20% ");
//				}
				Update.print(".");
				if (delta == 0) {
					break;
				}
			}
			fos.close();
			Update.debug("Download incomplete, retrying");
			
		} catch (MalformedURLException e) {
			Update.println("ERROR: MalformedURLException: cannot download " + url);
		} catch (IOException e) {
			Update.println("ERROR: IOException: cannot download " + url);
			throw new IOException();
		}
		
		return false;
		
	}
	
	public long getFileSize(String file) {
		File f = new File(file);
		return f.length();
	}
	
}
