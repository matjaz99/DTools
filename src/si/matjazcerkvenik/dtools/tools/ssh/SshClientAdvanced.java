package si.matjazcerkvenik.dtools.tools.ssh;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.UnknownHostException;

import si.matjazcerkvenik.dtools.context.DToolsContext;
import si.matjazcerkvenik.dtools.tools.AuthenticationException;
import si.matjazcerkvenik.simplelogger.SimpleLogger;

import com.trilead.ssh2.ChannelCondition;
import com.trilead.ssh2.Connection;
import com.trilead.ssh2.Session;

public class SshClientAdvanced {
	
	private SimpleLogger logger;

	private Connection sshConnection;
	private Session sshSession;

	private InputStream in;
	private OutputStream out;
	
	
	public SshClientAdvanced() {
		logger = DToolsContext.getInstance().getLogger();
	}
	
	public boolean connect(String host, int port, String username, String password) 
			throws UnknownHostException, AuthenticationException, IOException {
		
		createConnection(host, port);
		authenticate(username, password);
		openSession();
		
		logger.info("SshClientAdvanced:connect(): connected: ssh://" + username + "@" + host + ":" + port);
		
		return true;
		
	}

	
	private void createConnection(String host, int port) 
			throws UnknownHostException, IOException {
		
		sshConnection = new Connection(host, port);
		sshConnection.connect(null, 10000, 0);
		logger.debug("SshClientAdvanced:createConnection(): connection established: " + host + ":" + port);

	}

	private void authenticate(String username, String password) 
			throws AuthenticationException, IOException {
		
		boolean b = false;
		b = sshConnection.authenticateWithPassword(username, password);
		if (!b) {
			throw new AuthenticationException();
		}
		
		logger.debug("SshClientAdvanced:authenticate(): success");

	}

	private void openSession() throws IOException {
		
		sshSession = sshConnection.openSession();
		sshSession.requestPTY("dumb", 0, 0, 0, 0, null);
		sshSession.startShell();
		in = sshSession.getStdout();
		out = sshSession.getStdin();
		sshSession.waitForCondition(ChannelCondition.STDOUT_DATA, 3000);
		
		logger.debug("SshClientAdvanced:openSession(): success");

	}

	public void sendCommand(String cmd) {
		
		logger.info("SshClientAdvanced:sendCommand(): " + cmd);

		try {
			for (int i = 0; i < cmd.length(); i++) {
				char ch = cmd.charAt(i);
				int z = (int) ch;
				out.write(z);
			}

			out.write(13); // ascii code for <enter>

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String getResponse() {
		
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
			e.printStackTrace();
		}
		
		logger.debug("SshClientAdvanced:getResponse(): " + response);
		
		return response;
	}

	public void disconnect() {
		sshSession.close();
		sshConnection.close();
		logger.info("SshClientAdvanced:disconnect(): disconnected");
	}
	
	
	
	public static void main(String[] args) throws Exception {
		
		SshClientAdvanced ssh2 = new SshClientAdvanced();
		
		ssh2.connect("192.168.1.110", 22, "root", "object00");
//		ssh2.createConnection();
//		ssh2.authenticate();
//		ssh2.openSession();
		
		String s = "";
		
		ssh2.sendCommand("pwd");
		s=ssh2.getResponse();
		System.out.println(s);
		
		ssh2.sendCommand("ls -l");
		s=ssh2.getResponse();
		System.out.println(s);
		
		ssh2.disconnect();
		
	}
	

}
