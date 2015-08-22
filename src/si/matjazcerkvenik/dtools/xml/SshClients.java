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
