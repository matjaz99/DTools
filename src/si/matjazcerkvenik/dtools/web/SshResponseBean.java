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
