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

package si.matjazcerkvenik.dtools.tools.snmp;

import javax.xml.bind.annotation.XmlAttribute;

public class TrapDestination {

	private String destinationIp;
	private int destinationPort;
	private int sendInterval = 13000;
	
	public TrapDestination() {
	}

	public TrapDestination(String destinationIp, int port) {
		this.destinationIp = destinationIp;
		this.destinationPort = port;
	}

	public String getDestinationIp() {
		return destinationIp;
	}

	@XmlAttribute(name="ip")
	public void setDestinationIp(String destinationIp) {
		this.destinationIp = destinationIp;
	}

	public int getDestinationPort() {
		return destinationPort;
	}

	@XmlAttribute(name="port")
	public void setDestinationPort(int port) {
		this.destinationPort = port;
	}
	
	public int getSendInterval() {
		return sendInterval;
	}

	@XmlAttribute
	public void setSendInterval(int sendInterval) {
		this.sendInterval = sendInterval;
	}

}
