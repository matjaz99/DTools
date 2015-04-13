package si.matjazcerkvenik.dtools.tools;

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
