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

package si.matjazcerkvenik.dtools.xml;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="servers")
public class Servers {
	
	private List<Server> serverList;

	public List<Server> getServerList() {
		return serverList;
	}

	@XmlElement(name="server") // name of element (instead of serverList)
	public void setServerList(List<Server> server) {
		this.serverList = server;
	}
	
	public void addServer(Server s) {
		serverList.add(s);
	}
	
	public void deleteServer(Server s) {
		serverList.remove(s);
	}
	
}
