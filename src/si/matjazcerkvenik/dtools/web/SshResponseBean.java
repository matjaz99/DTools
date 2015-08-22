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

import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import si.matjazcerkvenik.dtools.xml.SshResponse;

@ManagedBean
@ViewScoped
public class SshResponseBean {
	
	private SshResponse sshResponse;
	
	@PostConstruct
	public void init() {
		Map<String, Object> requestParameterMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
		sshResponse = (SshResponse) requestParameterMap.get("sshResponse");
	}

	public SshResponse getSshResponse() {
		return sshResponse;
	}

	public void setSshResponse(SshResponse sshResponse) {
		this.sshResponse = sshResponse;
	}
	
	
	public void repeatExecution() {
		// TODO
	}
	
}
