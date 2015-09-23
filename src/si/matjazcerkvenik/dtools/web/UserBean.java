package si.matjazcerkvenik.dtools.web;

import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

@ManagedBean
@SessionScoped
public class UserBean implements Serializable {
	
	private static final long serialVersionUID = -6549647281175214450L;
	
	private String username;
	private String password;
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String authenticate() {
		if (username == null || username.isEmpty()) {
			FacesContext.getCurrentInstance().addMessage("loginBtn", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Warning!", "Missing username"));
			return "login";
		}
		if (username.equalsIgnoreCase("test")) {
			return "web/test";
		}
		FacesContext.getCurrentInstance().addMessage("loginBtn", new FacesMessage(FacesMessage.SEVERITY_WARN, "Warning!", "Login failed"));
		return "login";
		
	}
	
}
