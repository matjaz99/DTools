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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.UnknownHostException;
import java.util.Timer;

import si.matjazcerkvenik.dtools.context.DProps;
import si.matjazcerkvenik.dtools.context.DToolsContext;
import si.matjazcerkvenik.dtools.tools.AuthenticationException;
import si.matjazcerkvenik.simplelogger.SimpleLogger;

import com.trilead.ssh2.ChannelCondition;
import com.trilead.ssh2.Connection;
import com.trilead.ssh2.Session;

/**
 * Implementation of Trilead SSH client
 * 
 * @author matjaz
 *
 */
public class SshImpl implements Serializable {
	
	private static final long serialVersionUID = -8183194640927583152L;

	private SimpleLogger logger;

	private transient Connection sshConnection;
	private transient Session sshSession;

	private transient InputStream in;
	private transient OutputStream out;
	
	private SshClient sshClient;
	
	private boolean connected = false;
	
	private Timer timer;
	private TimeoutTask timeoutTask;
	
	
	public SshImpl(SshClient client) {
		logger = DToolsContext.getInstance().getLogger();
		this.sshClient = client;
	}
	
	
	/**
	 * This method creates connection, authenticates the user and opens SSH session for
	 * communication.
	 * @return true if successfully connected and authenticated
	 * @throws UnknownHostException
	 * @throws AuthenticationException
	 * @throws IOException
	 */
	public boolean connect() throws UnknownHostException, AuthenticationException, IOException {
		
		createConnection();
		authenticate();
		openSession();
		
		connected = true;
		
		logger.info("SshImpl:connect(): connected: " + sshClient.toUrlString());
		
		return true;
		
	}

	
	/**
	 * Connect to server
	 * @throws UnknownHostException
	 * @throws IOException
	 */
	private void createConnection() throws UnknownHostException, IOException {
		
		try {
			sshConnection = new Connection(sshClient.getHostname(), sshClient.getPort());
			sshConnection.connect(null, 10000, 0);
			logger.debug("SshImpl:createConnection(): connected");
		} catch (UnknownHostException e) {
			logger.error("SshImpl:createConnection(): UnknownHostException");
			throw e;
		} catch (IOException e) {
			logger.error("SshImpl:createConnection(): IOException");
			throw e;
		}

	}

	
	/**
	 * Authenticate with username and password
	 * @throws AuthenticationException
	 * @throws IOException
	 */
	private void authenticate() throws AuthenticationException, IOException {
		
		boolean b = false;
		b = sshConnection.authenticateWithPassword(sshClient.getUsername(), sshClient.getPassword());
		if (!b) {
			logger.error("SshImpl:authenticate(): AuthenticationException");
			sshConnection.close();
			throw new AuthenticationException();
		}
		
		logger.debug("SshImpl:authenticate(): success");

	}

	
	/**
	 * Create SSH session
	 * @throws IOException
	 */
	private void openSession() throws IOException {
		
		try {
			sshSession = sshConnection.openSession();
			sshSession.requestPTY("dumb", 0, 0, 0, 0, null);
			sshSession.startShell();
			in = sshSession.getStdout();
			out = sshSession.getStdin();
			sshSession.waitForCondition(ChannelCondition.STDOUT_DATA, 3000);
			logger.debug("SshImpl:openSession(): success");
		} catch (IOException e) {
			logger.error("SshImpl:openSession(): IOException");
			throw e;
		}

	}
	

	/**
	 * Return true if successfully connected
	 * @return true if successfully connected
	 */
	public boolean isConnected() {
		return connected;
	}

	
	/**
	 * Send command
	 * @param cmd
	 * @throws IOException
	 */
	public void sendCommand(String cmd) throws IOException {
		
		logger.info("SshImpl:sendCommand(): " + cmd);

		try {
			for (int i = 0; i < cmd.length(); i++) {
				char ch = cmd.charAt(i);
				int z = (int) ch;
				out.write(z);
			}

			out.write(13); // ascii code for <enter>

		} catch (IOException e) {
			logger.error("SshImpl:sendCommand(): IOException");
			throw e;
		}
		
		startTimeoutTimer(DProps.getPropertyInt(DProps.SSH_DISCONNECT_TIMEOUT));
	}

	
	/**
	 * Read response from the socket
	 * @return response
	 * @throws IOException
	 */
	public String getResponse() throws IOException {
		
		String response = "";

		sshSession.waitForCondition(ChannelCondition.EOF, 3000);
		byte[] buff = new byte[8192];

		try {
			int len = in.read(buff); // prebere dolzino bufferja
			if (len == -1)
				return null;
			for (int i = 0; i < len; i++) {
				char c = (char) (buff[i] & 0xff);
				response += c;
			}
			
		} catch (IOException e) {
			logger.error("SshImpl:getResponse(): IOException");
			throw e;
		}
		
		logger.debug("SshImpl:getResponse(): " + response);
		
		return response;
	}

	
	/**
	 * Disconnect the client
	 */
	public void disconnect() {
		sshSession.close();
		sshConnection.close();
		connected = false;
		sshClient.resetStatus();
		stopTimeoutTimer();
		logger.info("SshImpl:disconnect(): disconnected");
	}
	
	
	/**
	 * Start timer for disconnection after a period of inactivity. 
	 * The timer is restarted when another command is sent to the server.
	 * @param sec
	 */
	public void startTimeoutTimer(int sec) {

		stopTimeoutTimer();

		timer = new Timer();
		timeoutTask = new TimeoutTask(this);

		timer.schedule(timeoutTask, sec*1000);
	}


	/**
	 * Stop timer for disconnection.
	 */
	public void stopTimeoutTimer() {

		if (timer != null) {
			timer.cancel();
			timer = null;
		}

		if (timeoutTask != null) {
			timeoutTask.cancel();
			timeoutTask = null;
		}
	}

	
//	public boolean isTimeoutTimerRunning() {
//
//		if (timer != null || timeoutTask != null) {
//			return true;
//		} else {
//			return false;
//		}
//	}
	
	
	
	
	
//	public static void main(String[] args) throws Exception {
//		
//		SshImpl ssh2 = new SshImpl("192.168.1.110", 22, "root", "object00");
//		
//		ssh2.connect();
////		ssh2.createConnection();
////		ssh2.authenticate();
////		ssh2.openSession();
//		
//		String s = "";
//		
//		ssh2.sendCommand("pwd");
//		s=ssh2.getResponse();
//		System.out.println(s);
//		
//		ssh2.sendCommand("ls -l");
//		s=ssh2.getResponse();
//		System.out.println(s);
//		
//		ssh2.disconnect();
//		
//	}
	

}
