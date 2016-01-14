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

import si.matjazcerkvenik.dtools.io.DAO;
import si.matjazcerkvenik.dtools.tools.ftp.FtpClient;
import si.matjazcerkvenik.dtools.tools.ftp.FtpClients;

// http://www.mkyong.com/jsf2/multi-components-validator-in-jsf-2-0/
@FacesValidator(value="newFtpClientValidator")
public class NewFtpClientValidator implements Validator {
	
	@Override
	public void validate(FacesContext ctx, UIComponent comp, Object value)
			throws ValidatorException {
		
		String host = (String) value;
		ValidatorUtils.validateFileName(host);
		
		UIInput portComponent = (UIInput) comp.getAttributes().get("port");
        String port = (String) portComponent.getSubmittedValue();
        ValidatorUtils.validateInteger(port);
        
        UIInput userComponent = (UIInput) comp.getAttributes().get("user");
        String user = (String) userComponent.getSubmittedValue();
        ValidatorUtils.validateFileName(user);
        
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
