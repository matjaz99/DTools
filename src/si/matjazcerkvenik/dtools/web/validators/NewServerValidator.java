package si.matjazcerkvenik.dtools.web.validators;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

import si.matjazcerkvenik.dtools.xml.DAO;
import si.matjazcerkvenik.dtools.xml.Server;
import si.matjazcerkvenik.dtools.xml.Servers;

@FacesValidator(value="newServerValidator")
public class NewServerValidator implements Validator {

	@Override
	public void validate(FacesContext ctx, UIComponent comp, Object value)
			throws ValidatorException {
		
		String newServerName = ((String) value).trim();
		
		Servers servers = DAO.getInstance().loadServers();
		
		for (Server srv : servers.getServerList()) {
			if (srv.getName().equalsIgnoreCase(newServerName)) {
				FacesMessage message = new FacesMessage();
				message.setDetail(newServerName + " already exists");
				message.setSummary("Error");
				message.setSeverity(FacesMessage.SEVERITY_ERROR);
				throw new ValidatorException(message);
			}
		}
		
	}

}
