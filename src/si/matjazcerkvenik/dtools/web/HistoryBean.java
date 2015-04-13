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
		// TODO
	}
	
}
