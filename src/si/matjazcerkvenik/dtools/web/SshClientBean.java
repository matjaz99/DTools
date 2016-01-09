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

package si.matjazcerkvenik.dtools.web;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;

import si.matjazcerkvenik.dtools.context.DToolsContext;
import si.matjazcerkvenik.dtools.io.DAO;
import si.matjazcerkvenik.dtools.tools.AuthenticationException;
import si.matjazcerkvenik.dtools.tools.ssh.SshClientAdvanced;
import si.matjazcerkvenik.dtools.xml.SshClient;
import si.matjazcerkvenik.dtools.xml.SshResponse;

@ManagedBean
@ViewScoped
public class SshClientBean {
	
	private SshClient client;
	private SshClientAdvanced sshClientImpl;
	private String response;
	
	private String selectedCommand;
	private String newCommand;
	private String execStatusColor;
	
	private String filename;
	private String remark;
	
	@PostConstruct
	public void init() {
		Map<String, String> requestParameterMap = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		if (requestParameterMap.containsKey("clientUrl")) {
			String name = requestParameterMap.get("clientUrl");
			client = DAO.getInstance().findSshClient(name);
		}
	}

	public SshClient getSshClient() {
		return client;
	}

	public void setSshClient(SshClient sshClient) {
		this.client = sshClient;
	}
	
	
	
	public String getSshResponse() {
		return response;
	}

	public void setSshResponse(String sshResponse) {
		this.response = sshResponse;
	}
	
	public String getSelectedCommand() {
		return selectedCommand;
	}

	public void setSelectedCommand(String selectedCommand) {
		this.selectedCommand = selectedCommand;
	}
	
	public void commandValueChanged(ValueChangeEvent e) {
		selectedCommand = e.getNewValue().toString();
		response = null;
	}

	public String getNewCommand() {
		return newCommand;
	}

	public void setNewCommand(String newCommand) {
		this.newCommand = newCommand;
	}

	public List<String> getCommands() {
		return DAO.getInstance().loadCommands().getCommands();
	}
	
	public void addCommand() {
		DAO.getInstance().addCommand(newCommand);
		newCommand = null;
	}
	
	public void executeOne() {
		execute(newCommand);
	}
	
	public void executeSelected() {
		execute(selectedCommand);
	}

	private void execute(String command) {
		
		try {
			
			sshClientImpl = new SshClientAdvanced();
			sshClientImpl.connect(client.getHostname(), client.getPort(), client.getUsername(), client.getPassword());
			sshClientImpl.sendCommand(command);
			response = sshClientImpl.getResponse();
			sshClientImpl.disconnect();
			
			execStatusColor = "green";
			FacesContext.getCurrentInstance().addMessage("exec", new FacesMessage("Succes!"));
			
		} catch (UnknownHostException e) {
			execStatusColor = "red";
			FacesContext.getCurrentInstance().addMessage("exec", new FacesMessage("Unknown host!"));
		} catch (AuthenticationException e) {
			execStatusColor = "red";
			FacesContext.getCurrentInstance().addMessage("exec", new FacesMessage("Authentication failed!"));
		} catch (IOException e) {
			execStatusColor = "red";
			FacesContext.getCurrentInstance().addMessage("exec", new FacesMessage("Connection failed!"));
		} catch (Exception e) {
			execStatusColor = "red";
			FacesContext.getCurrentInstance().addMessage("exec", new FacesMessage(e.getMessage()));
		}
		
	}
	
	public String getExecStatusColor() {
		return execStatusColor;
	}
	
	
	public void saveResponse() {
		
		SshResponse r = new SshResponse();
		r.setDate(DToolsContext.getCurrentDate());
		r.setFilename(filename);
		r.setRemark(remark);
		r.setSshClient(client);
		r.setCommand(selectedCommand);
		r.setResponse(response);
		
		DAO.getInstance().saveSshResponse(filename, r);
		
		filename = null;
		remark = null;
		
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	
}
