package si.matjazcerkvenik.dtools.web.validators;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

import si.matjazcerkvenik.dtools.xml.Clients;
import si.matjazcerkvenik.dtools.xml.DAO;
import si.matjazcerkvenik.dtools.xml.SshClient;

// http://www.mkyong.com/jsf2/multi-components-validator-in-jsf-2-0/
@FacesValidator(value="newSshClientValidator")
public class NewSshClientValidator implements Validator {
	
	@Override
	public void validate(FacesContext ctx, UIComponent comp, Object value)
			throws ValidatorException {
		
		String host = (String) value;
		
		UIInput portComponent = (UIInput) comp.getAttributes().get("port");
        String port = (String) portComponent.getSubmittedValue();
        
        UIInput userComponent = (UIInput) comp.getAttributes().get("user");
        String user = (String) userComponent.getSubmittedValue();
        
        Clients clients = DAO.getInstance().loadClients();
        
        for (SshClient sshClient : clients.getSshClientList()) {
			
        	if (sshClient.getHostname().equalsIgnoreCase(host)
        			&& (sshClient.getPort() + "").equalsIgnoreCase(port)
        			&& sshClient.getUsername().equalsIgnoreCase(user)) {
				
        		portComponent.setValid(false); // So that it's marked invalid.
        		userComponent.setValid(false); // So that it's marked invalid.
                throw new ValidatorException(new FacesMessage("Exactly the same SSH client already exists"));
        		
			}
        	
		}
		
	}
	
}
