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
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

import si.matjazcerkvenik.dtools.tools.NetworkLocation;
import si.matjazcerkvenik.dtools.xml.Node;

@FacesValidator(value="newNodeValidator")
public class NewNodeValidator implements Validator {

	@Override
	public void validate(FacesContext ctx, UIComponent comp, Object value)
			throws ValidatorException {
		
		String newNodeName = ((String) value).trim();
		ValidatorUtils.validateIfEmpty(newNodeName);
		ValidatorUtils.validateSpecialCharacters(newNodeName);
        
		NetworkLocation location = (NetworkLocation) comp.getAttributes().get("networkLocation");
		
		for (Node node : location.getNetworkNodes().getNodesList()) {
			if (node.getName().equalsIgnoreCase(newNodeName)) {
				FacesMessage message = new FacesMessage();
				message.setSummary(newNodeName + " already exists");
				message.setSeverity(FacesMessage.SEVERITY_ERROR);
				throw new ValidatorException(message);
			}
		}
		
	}

}
