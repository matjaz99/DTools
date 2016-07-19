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

package si.matjazcerkvenik.dtools.web.beans;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;

import si.matjazcerkvenik.dtools.io.DAO;
import si.matjazcerkvenik.dtools.tools.NetworkLocation;
import si.matjazcerkvenik.dtools.tools.ssh.SshClient;

@ManagedBean
@ApplicationScoped
public class AppBean {
	
	
	
	/* ACTIVE CONNECTIONS */
	
	
	
	List<ActiveConnection> activeConnectionsList = new ArrayList<ActiveConnection>();
	
	@ManagedProperty(value="#{snmpSimulatorBean}")
	private SnmpSimulatorBean snmpSimulatorBean;


	public void setSnmpSimulatorBean(SnmpSimulatorBean snmpSimulatorBean) {
		this.snmpSimulatorBean = snmpSimulatorBean;
	}
	
	
	public List<ActiveConnection> getActiveConnectionsList() {
		
		activeConnectionsList.clear();
		
		List<NetworkLocation> locations = DAO.getInstance().loadNetworkLocations();
		
		// active autodiscovery tasks
		for (int i = 0; i < locations.size(); i++) {
			if (locations.get(i).isAutoDiscoveryActive()) {
				ActiveConnection ac = new ActiveConnection();
				ac.name = "Autodiscovery; location: " + locations.get(i).getLocationName();
				ac.outcome = "network";
				ac.status = "Running";
				activeConnectionsList.add(ac);
			}
			if (locations.get(i).isMonitoringActive()) {
				ActiveConnection ac = new ActiveConnection();
				ac.name = "Network Monitoring; location: " + locations.get(i).getLocationName();
				ac.outcome = "network.xhtml?location="+locations.get(i).getLocationName();
				ac.status = "Running";
				activeConnectionsList.add(ac);
			}
		}
		
		// active ssh clients
		List<SshClient> sshClientList = DAO.getInstance().loadSshClients().getSshClientList();
		for (int i = 0; i < sshClientList.size(); i++) {
			if (sshClientList.get(i).getStatusText().equals("CONNECTED")) {
				ActiveConnection ac = new ActiveConnection();
				ac.name = "SSH: " + sshClientList.get(i).toUrlString();
				ac.outcome = "sshClient.xhtml?clientUrl=" + sshClientList.get(i).toUrlString();
				ac.status = "Connected";
				activeConnectionsList.add(ac);
			}
		}

		
		// active SNMP agents
		if (snmpSimulatorBean != null) {
			for (int i = 0; i < snmpSimulatorBean.getSnmpAgents().size(); i++) {
				if (snmpSimulatorBean.getSnmpAgents().get(i).isActive()) {
					ActiveConnection ac = new ActiveConnection();
					ac.name = "SNMP agent simulator: " + snmpSimulatorBean.getSnmpAgents().get(i).getName();
					ac.outcome = "snmpSimulator";
					String args[] = {snmpSimulatorBean.getSnmpAgents().get(i).getName()};
					ac.args = args;
					ac.status = "Running";
					activeConnectionsList.add(ac);
				}
			}
			
		}
		
		
		return activeConnectionsList;
	}
	
	
	
	
	
	public class ActiveConnection {
		
		private String name;
		private String outcome;
		private String status;
		private String[] args;
		
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getOutcome() {
			return outcome;
		}
		public void setOutcome(String outcome) {
			this.outcome = outcome;
		}
		public String getStatus() {
			return status;
		}
		public void setStatus(String status) {
			this.status = status;
		}
		public String[] getArgs() {
			return args;
		}
		public void setArgs(String[] args) {
			this.args = args;
		}
		
	}
	
	
	
}
