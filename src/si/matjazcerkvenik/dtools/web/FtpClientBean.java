package si.matjazcerkvenik.dtools.web;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;

import si.matjazcerkvenik.dtools.xml.DAO;
import si.matjazcerkvenik.dtools.xml.FtpClient;
import si.matjazcerkvenik.dtools.xml.FtpTransfer;
import si.matjazcerkvenik.dtools.xml.FtpTransfers;

@ManagedBean
@ViewScoped
public class FtpClientBean {
	
	private FtpClient ftpClient;
	private FtpTransfers transfers;
	
	private String source;
	private String destination;
	
	private String selectedTransfer;

	
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
	
	
	public String getSelectedTransfer() {
		return selectedTransfer;
	}

	public void setSelectedTransfer(String selectedTransfer) {
		this.selectedTransfer = selectedTransfer;
	}

	public List<String> getTransfersAsStrings() {
		transfers = DAO.getInstance().loadTransfers();
		
		List<String> list = new ArrayList<String>();
		for (int i = 0; i < transfers.getTransfersList().size(); i++) {
			String s = transfers.getTransfersList().get(i).getFrom() + " -> " + transfers.getTransfersList().get(i).getTo();
			list.add(s);
		}
		return list;
		
	}
	
	public void transferValueChanged(ValueChangeEvent e) {
		selectedTransfer = e.getNewValue().toString();
	}
	
	public void addTransfer() {
		FtpTransfer t = new FtpTransfer();
		t.setFrom(source);
		t.setTo(destination);
		DAO.getInstance().addFtpTransfer(t);
		source = null;
		destination = null;
	}
	
	public void execute() {
		// TODO
	}
	
	
}
