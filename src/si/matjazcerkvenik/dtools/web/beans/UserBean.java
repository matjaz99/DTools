package si.matjazcerkvenik.dtools.web.beans;

import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;

import si.matjazcerkvenik.dtools.context.DProps;

@ManagedBean
@SessionScoped
public class UserBean implements Serializable {
	
	private static final long serialVersionUID = -6549647281175214450L;
	
	private String username;
	private String password;
	private String cssTheme;
	
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
	
	public void setCssTheme(String cssTheme) {
		// TODO why do i need this if theme can be changed with cssThemeChanged method?
		this.cssTheme = cssTheme;
	}
	public String getCssTheme() {
		return DProps.getProperty(DProps.DTOOLS_GUI_CSS_THEME);
	}
	public void cssThemeChanged(ValueChangeEvent e) {
		DProps.setProperty(DProps.DTOOLS_GUI_CSS_THEME, e.getNewValue().toString(), true);
	}
	
}
