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

package si.matjazcerkvenik.dtools.web.validators;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

import si.matjazcerkvenik.dtools.xml.SshClients;
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
        
        SshClients clients = DAO.getInstance().loadSshClients();
        
        for (SshClient sshClient : clients.getSshClientList()) {
			
        	if (sshClient.getHostname().equalsIgnoreCase(host)
        			&& (sshClient.getPort() + "").equalsIgnoreCase(port)
        			&& sshClient.getUsername().equalsIgnoreCase(user)) {
				
        		portComponent.setValid(false); // So that it's marked invalid.
        		userComponent.setValid(false); // So that it's marked invalid.
        		FacesMessage message = new FacesMessage();
				message.setSummary(sshClient.getHostname() + " already exists");
				message.setSeverity(FacesMessage.SEVERITY_ERROR);
				throw new ValidatorException(message);        		
			}
        	
		}
		
	}
	
}
