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

package si.matjazcerkvenik.dtools.tools.ftp;

import java.io.File;

import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.FileSystemOptions;
import org.apache.commons.vfs2.Selectors;
import org.apache.commons.vfs2.impl.StandardFileSystemManager;
import org.apache.commons.vfs2.provider.sftp.SftpFileSystemConfigBuilder;

public class VfsFtpSftpClient {

	private String hostname;
	private int port;
	private String username;
	private String password;
	private String protocol;

	public VfsFtpSftpClient(String hostname, int port, String username,
			String password, String protocol) {
		this.hostname = hostname;
		this.port = port;
		this.username = username;
		this.password = password;
		this.protocol = protocol;
	}

	public void upload(String localFile, String remoteFile) {
		
		StandardFileSystemManager manager = new StandardFileSystemManager();

		try {

			// check if the file exists
			File file = new File(localFile);
			if (!file.exists())
				throw new RuntimeException("Error. Local file not found");

			// Initializes the file manager
			manager.init();

			// Setup our SFTP configuration
			FileSystemOptions opts = new FileSystemOptions();
			SftpFileSystemConfigBuilder.getInstance().setStrictHostKeyChecking(
					opts, "no");
			SftpFileSystemConfigBuilder.getInstance().setUserDirIsRoot(opts,
					true);
			SftpFileSystemConfigBuilder.getInstance().setTimeout(opts, 10000);

			System.out.println("copy " + file.getAbsolutePath() + " to");
			// Create the SFTP URI using the host name, userid, password, remote
			// path and file name
			String sftpUri = protocol + "://" + username + ":" + password + "@"
					+ hostname + ":" + port + remoteFile;
			System.out.println(sftpUri);

			// Create local file object
			FileObject localFileObject = manager.resolveFile(file.getAbsolutePath());

			// Create remote file object
			FileObject remoteFileObject = manager.resolveFile(sftpUri, opts);

			// Copy local file to sftp server
			remoteFileObject.copyFrom(localFileObject, Selectors.SELECT_SELF);
			System.out.println("File upload successful");

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			manager.close();
		}
		
	}

	public void download(String localFile, String remoteFile) {

		StandardFileSystemManager manager = new StandardFileSystemManager();
		// Initializes the file manager
		try {
			manager.init();
			// Setup our SFTP configuration
			FileSystemOptions opts = new FileSystemOptions();
			SftpFileSystemConfigBuilder.getInstance().setStrictHostKeyChecking(
					opts, "no");
			SftpFileSystemConfigBuilder.getInstance().setUserDirIsRoot(opts,
					true);
			SftpFileSystemConfigBuilder.getInstance().setTimeout(opts, 10000);

			// Create the SFTP URI using the host name, userid, password, remote
			// path and file name
			String sftpUri = protocol + "://" + username + ":" + password + "@"
					+ hostname + ":" + port + remoteFile;
			System.out.println(sftpUri);
			
			// Create local file object
			File file = new File(localFile);
			FileObject localFileObject = manager.resolveFile(file
					.getAbsolutePath());

			// Create remote file object
			FileObject remoteFileObject = manager.resolveFile(sftpUri, opts);

			// Copy local file to sftp server
			localFileObject.copyFrom(remoteFileObject, Selectors.SELECT_SELF);
			System.out.println("File download successful");
		} catch (FileSystemException e) {
			e.printStackTrace();
		} finally {
			manager.close();
		}

	}

	public static void main(String[] args) {
		

//		VfsFtpSftpClient c = new VfsFtpSftpClient("centos6", 22, "matjaz", "object00", "sftp");
		VfsFtpSftpClient c = new VfsFtpSftpClient("centos6", 21, "ftpuser", "ftpuser", "ftp");
		
		c.upload("ftptestdir/openmp3player.log.1", "/ftptestdir/up-openmp3player.log.1");
//		c.download("down-openmp3player.log", "/ftptestdir/up-openmp3player.log.1");
//		
//		c.upload("ftptestdir/OpenMp3Player-3.0.0-alpha.01.tar.gz", "/ftptestdir/OpenMp3Player-3.0.0-alpha.01.tar.gz");
//		c.download("OpenMp3Player-3.0.0-alpha.01.tar.gz", "/ftptestdir/OpenMp3Player-3.0.0-alpha.01.tar.gz");
		
//		c.upload("/Users/matjaz/DTools/data/AAA.txt", "/ftptestdir/AAA.txt");
//		c.download("/Users/matjaz/DTools/data/BBB.txt", "/ftptestdir/AAA.txt");
		

	}

}
