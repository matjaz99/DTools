package si.matjazcerkvenik.dtools.tools.ssh;

import java.util.TimerTask;

/**
 * This task disconnects the client after specified time of inactivity
 * 
 * @author matjaz
 *
 */
public class TimeoutTask extends TimerTask {
	
	private SshImpl sshClient = null;
	
	
	public TimeoutTask(SshImpl sshClient) {
		this.sshClient = sshClient;
	}
	

	@Override
	public void run() {
		sshClient.disconnect();
	}
	
}
