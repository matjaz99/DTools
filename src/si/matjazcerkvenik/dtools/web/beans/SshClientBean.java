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

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import si.matjazcerkvenik.dtools.context.DToolsContext;
import si.matjazcerkvenik.dtools.io.DAO;
import si.matjazcerkvenik.dtools.tools.ssh.SshClient;
import si.matjazcerkvenik.dtools.tools.ssh.SshResponse;

@ManagedBean
@ViewScoped
public class SshClientBean implements Serializable {
	
	private static final long serialVersionUID = -5044656422250893827L;
	
	private SshClient client;
	
	private String response;
	
	private String command = "pwd";
	
	private List<SshResponse> sshResponsesList;
	
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
	
	public List<String> getCommands() {
		return DAO.getInstance().loadCommands().getCommands();
	}
	
	public String getCommand() {
		return command;
	}

	public void setCommand(String newCommand) {
		this.command = newCommand;
	}
	
	public void handleKeyEvent() {
//        System.out.println("handleKeyEvent: " + command);
    }
	
	public void addCommand() {
		DAO.getInstance().addCommand(command);
	}
	
	public void switchCommand(ActionEvent actionEvent) {
		command = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("cmd");
//		System.out.println("switchCommand: " + command);
    }
	
	public void deleteCommand(String cmd) {
		DAO.getInstance().deleteCommand(cmd);
	}
	

	
	
	public void execute() {
		
		Growl.addGrowlMessage("Command sent", FacesMessage.SEVERITY_INFO);
		response = client.execute(command);
		saveResponse();
		
	}
	

	
	
	public String getStatusColor() {
		return client.getStatusColor();
	}
	
	public String getClientIcon() {
		return "bullet_" + client.getStatusColor() + "-mini.png";
	}
	
	public String getStatusText() {
		return client.getStatusText();
	}
	
	
	public void disconnect() {
		client.disconnect();
	}
	
	
	public void clearResponse() {
		response = null;
	}
	
	
	
	public void saveResponse() {
		
		SshResponse r = new SshResponse();
		r.setDate(DToolsContext.getCurrentDate());
		r.setFilename(System.currentTimeMillis() + "@" + client.getHostname());
		r.setRemark(null);
		r.setSshClient(client);
		r.setCommand(command);
		r.setResponse(response);
		
		DAO.getInstance().saveSshResponse(r);
		r.saveTxt();
		
		sshResponsesList.add(r);
		
		Growl.addGrowlMessage("Saved", FacesMessage.SEVERITY_INFO);
		
	}
	
	
	
	
	public List<SshResponse> getSshResponsesList() {
		if (sshResponsesList == null) {
			sshResponsesList = DAO.getInstance().loadAllSshResponses(client.getHostname());
		}
		return sshResponsesList;
	}
	
	public String openSshResponse(SshResponse response) {
		// FIXME
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("sshResponse", response);
		return "viewSshResponse";
	}
	
	public void toggleFavoriteResponse(SshResponse response) {
		response.setFavorite(!response.isFavorite());
		DAO.getInstance().saveSshResponse(response);
	}
	
	public void deleteSshResponse(SshResponse response) {
		sshResponsesList.remove(response);
		DAO.getInstance().deleteSshResponse(response);
	}
	
}
