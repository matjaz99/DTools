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

import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import si.matjazcerkvenik.dtools.xml.DAO;
import si.matjazcerkvenik.dtools.xml.SshResponse;

@ManagedBean
@SessionScoped
public class HistoryBean {
	
	public List<SshResponse> getSshResponsesList() {
		return DAO.getInstance().loadAllSshResponses();
	}
	
	
	public String openSshResponse(SshResponse response) {
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("sshResponse", response);
		return "viewSshResponse";
	}
	
	public void deleteSshResponse(SshResponse response) {
		DAO.getInstance().deleteSshResponse(response);
	}
	
}
