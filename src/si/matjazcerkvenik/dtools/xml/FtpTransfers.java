package si.matjazcerkvenik.dtools.xml;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class FtpTransfers {
	
	private List<FtpTransfer> transfersList;

	public List<FtpTransfer> getTransfersList() {
		return transfersList;
	}

	@XmlElement(name="transfer")
	public void setTransfersList(List<FtpTransfer> transfersList) {
		this.transfersList = transfersList;
	}
	
	public void addFtpTransfer(FtpTransfer t) {
		transfersList.add(t);
	}
	
	public void deleteFtpTransfer(FtpTransfer t) {
		transfersList.remove(t);
	}
	
}
