package si.matjazcerkvenik.dtools.xml;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class SshClients {
	
	private List<SshClient> sshClientList;

	public List<SshClient> getSshClientList() {
		return sshClientList;
	}

	@XmlElement(name="sshClient")
	public void setSshClientList(List<SshClient> sshClient) {
		this.sshClientList = sshClient;
	}
	
	public void addSshClientAction(SshClient c) {
		sshClientList.add(c);
	}
	
	public void deleteSshClient(SshClient c) {
		sshClientList.remove(c);
	}
	
	public List<SshClient> getCustomSshClientsList(String hostname) {
		
		List<SshClient> list = new ArrayList<SshClient>();
		
		for (int i = 0; i < getSshClientList().size(); i++) {
			if (getSshClientList().get(i).getHostname().equals(hostname)) {
				list.add(getSshClientList().get(i));
			}
		}
		
		return list;
		
	}
}
