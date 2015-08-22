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

import javax.xml.bind.annotation.XmlElement;

public class FtpTransfer {

	private String direction;
	private String from;
	private String to;

	public String getFrom() {
		return from;
	}

	@XmlElement
	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	@XmlElement
	public void setTo(String to) {
		this.to = to;
	}

	public String getDirection() {
		return direction;
	}

	@XmlElement
	public void setDirection(String direction) {
		this.direction = direction;
	}

}
