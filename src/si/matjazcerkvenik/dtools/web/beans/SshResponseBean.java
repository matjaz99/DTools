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

import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import si.matjazcerkvenik.dtools.context.DToolsContext;
import si.matjazcerkvenik.dtools.io.DAO;
import si.matjazcerkvenik.dtools.tools.ssh.SshResponse;

@ManagedBean
@ViewScoped
public class SshResponseBean {
	
	private SshResponse sshResponse;
	
	@PostConstruct
	public void init() {
		Map<String, String> requestParameterMap = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		if (requestParameterMap.containsKey("filename")) {
			String file = DToolsContext.HOME_DIR + "/config/users/default/ssh/saved/" 
				+ requestParameterMap.get("filename") + ".xml";
			sshResponse = DAO.getInstance().loadSshResponse(file);
			sshResponse.loadTxt();
		}
	}

	public SshResponse getSshResponse() {
		return sshResponse;
	}

	public void setSshResponse(SshResponse sshResponse) {
		this.sshResponse = sshResponse;
	}
	
	
	
}
