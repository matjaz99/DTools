package si.matjazcerkvenik.dtools.web.validators;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

import si.matjazcerkvenik.dtools.xml.DAO;
import si.matjazcerkvenik.dtools.xml.FtpClient;
import si.matjazcerkvenik.dtools.xml.FtpClients;

// http://www.mkyong.com/jsf2/multi-components-validator-in-jsf-2-0/
@FacesValidator(value="newFtpClientValidator")
public class NewFtpClientValidator implements Validator {
	
	@Override
	public void validate(FacesContext ctx, UIComponent comp, Object value)
			throws ValidatorException {
		
		String host = (String) value;
		
		UIInput portComponent = (UIInput) comp.getAttributes().get("port");
        String port = (String) portComponent.getSubmittedValue();
        
        UIInput userComponent = (UIInput) comp.getAttributes().get("user");
        String user = (String) userComponent.getSubmittedValue();
        
        FtpClients clients = DAO.getInstance().loadFtpClients();
        
        for (FtpClient client : clients.getFtpClientList()) {
			
        	if (client.getHostname().equalsIgnoreCase(host)
        			&& (client.getPort() + "").equalsIgnoreCase(port)
        			&& client.getUsername().equalsIgnoreCase(user)) {
				
        		portComponent.setValid(false); // So that it's marked invalid.
        		userComponent.setValid(false); // So that it's marked invalid.
                throw new ValidatorException(new FacesMessage("Exactly the same FTP client already exists"));
        		
			}
        	
		}
		
	}
	
}
