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

import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import si.matjazcerkvenik.dtools.web.webhook.AmAlertMessage;
import si.matjazcerkvenik.dtools.web.webhook.DNotification;
import si.matjazcerkvenik.dtools.web.webhook.HttpMessage;
import si.matjazcerkvenik.dtools.web.webhook.WebhookServlet;

@ManagedBean
@SessionScoped
public class WebhookBean {
	
	public List<HttpMessage> getMessages() {
		return WebhookServlet.messages;
	}
	
	public List<AmAlertMessage> getAmMessages() {
		return WebhookServlet.amMessages;
	}
	
	public List<DNotification> getDNotifs() {
		return WebhookServlet.dNotifs;
	}
	
}
