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

package si.matjazcerkvenik.dtools.web.beans;

import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;

public class Growl {
	
	public static void addGrowlMessage(String title) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, title,  null);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }
	
	public static void addGrowlMessage(String title, Severity severity) {
        FacesMessage message = new FacesMessage(severity, title,  null);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }
	
	public static void addGrowlMessage(String title, String detail, Severity severity) {
        FacesMessage message = new FacesMessage(severity, title,  detail);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }
	
}
