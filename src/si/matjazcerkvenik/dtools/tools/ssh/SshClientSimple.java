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

package si.matjazcerkvenik.dtools.tools.ssh;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.trilead.ssh2.Connection;
import com.trilead.ssh2.Session;
import com.trilead.ssh2.StreamGobbler;

public class SshClientSimple {
	
	private String username;
	private String password;

	private Connection sshConnection;
	private Session sshSession;

	private String response = "";
	
	public SshClientSimple(String username, String password) {
		this.username = username;
		this.password = password;
	}
	
	public void createConnection(String host) {

		try {
			
			sshConnection = new Connection(host);
			sshConnection.connect();
			
		} catch (IOException e) {
			
		}

	}

	public void authenticate() {
		boolean isAuthenticated = true;
		try {
			isAuthenticated = sshConnection.authenticateWithPassword(username, password);
		} catch (IOException e) {

		}

		if (isAuthenticated == false) {
			
		}

	}
	
	public void openSession() {
		try {
			sshSession = sshConnection.openSession();
		} catch (IOException e) {

		}

	}
	
	public void sendCommand(String cmd) {

		try {
			
			sshSession.execCommand(cmd);

		} catch (IOException e) {

		}
	}

	public String getResponse() {

		try {
			InputStream stdout = new StreamGobbler(sshSession.getStdout());
			BufferedReader br = new BufferedReader(new InputStreamReader(stdout));

			while (true) {

				String line = br.readLine();
				if (line == null) {
					break;
				}

				System.out.println(line);
			}
			br.close();
		} catch (IOException e) {

		}
		return response;
	}

	public void resetResponseString() {
		response = "";
	}

	public void disconnect() {
		sshSession.close();
		sshConnection.close();
	}
	
	
	
	public static void main(String[] args) {
		
		SshClientSimple ssh1 = new SshClientSimple("root", "object00");
		
		ssh1.createConnection("192.168.1.110");
		ssh1.authenticate();
		ssh1.openSession();
		ssh1.sendCommand("ls -l && date && who");
		ssh1.getResponse();
		ssh1.disconnect();
		
	}
	
}
