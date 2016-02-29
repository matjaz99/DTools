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
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import si.matjazcerkvenik.dtools.io.DAO;
import si.matjazcerkvenik.dtools.tools.ftp.FtpClient;
import si.matjazcerkvenik.dtools.tools.ftp.FtpTransfer;
import si.matjazcerkvenik.dtools.tools.ftp.VfsFtpSftpClient;

@ManagedBean
@ViewScoped
public class FtpClientBean {
	
	private FtpClient ftpClient;
	
	private String source;
	private String destination;
	private String direction = "Download";
	
	
	
	@PostConstruct
	public void init() {
		Map<String, String> requestParameterMap = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		if (requestParameterMap.containsKey("clientUrl")) {
			String name = requestParameterMap.get("clientUrl");
			ftpClient = DAO.getInstance().findFtpClient(name);
		}
	}

	public FtpClient getFtpClient() {
		return ftpClient;
	}

	public void setFtpClient(FtpClient ftpClient) {
		this.ftpClient = ftpClient;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public String getDirection() {
		return direction;
	}

	public void setDirection(String direction) {
		this.direction = direction;
	}

	public List<FtpTransfer> getTransfersList() {
		return DAO.getInstance().loadTransfers().getTransfersList();		
	}
	
	public String getIcon(String d) {
		if (d.equals("Download")) {
			return "download";
		}
		return "upload";
	}
	
	public void addTransfer() {
		FtpTransfer t = new FtpTransfer();
		t.setFrom(source);
		t.setTo(destination);
		t.setDirection(direction);
		DAO.getInstance().addFtpTransfer(t);
		source = null;
		destination = null;
		direction = "Download";
	}
	
	public void deleteTransfer(FtpTransfer t) {
		DAO.getInstance().deleteFtpTransfer(t);
	}
	
	public void execute(FtpTransfer t) {
		
		VfsFtpSftpClient c = new VfsFtpSftpClient(ftpClient.getHostname(), 
				ftpClient.getPort(), 
				ftpClient.getUsername(), 
				ftpClient.getPassword(), 
				ftpClient.getProtocol().toLowerCase());
		
		if (t.getDirection().equals("Upload")) {
			c.upload(t.getFrom(), t.getTo());
		} else {
			c.download(t.getTo(), t.getFrom());
		}
		
	}
	
	
}
