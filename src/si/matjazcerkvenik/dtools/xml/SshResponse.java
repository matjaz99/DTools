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
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@XmlRootElement
public class SshResponse {

	private SshClient sshClient;
	private String command;
	private String remark;
	private String date;
	private String filename;
	private String response;

	public String getCommand() {
		return command;
	}

	@XmlElement
	public void setCommand(String command) {
		this.command = command;
	}

	public SshClient getSshClient() {
		return sshClient;
	}

	@XmlElement
	public void setSshClient(SshClient sshClient) {
		this.sshClient = sshClient;
	}

	public String getRemark() {
		return remark;
	}

	@XmlElement
	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getDate() {
		return date;
	}

	@XmlElement
	public void setDate(String date) {
		this.date = date;
	}

	public String getFilename() {
		return filename;
	}

	@XmlElement
	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getResponse() {
		return response;
	}

	@XmlTransient
	public void setResponse(String response) {
		this.response = response;
	}

}
