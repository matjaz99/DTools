package si.matjazcerkvenik.dtools.web;

import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import si.matjazcerkvenik.dtools.xml.DAO;
import si.matjazcerkvenik.dtools.xml.FtpClient;
import si.matjazcerkvenik.dtools.xml.FtpTransfer;

@ManagedBean
@ViewScoped
public class FtpClientBean {
	
	private FtpClient ftpClient;
	
	private String source;
	private String destination;
	private String direction = "Download";
	
	
	
	@PostConstruct
	public void init() {
		Map<String, Object> requestParameterMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
		ftpClient = (FtpClient) requestParameterMap.get("client");
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
		// TODO
	}
	
	
}
