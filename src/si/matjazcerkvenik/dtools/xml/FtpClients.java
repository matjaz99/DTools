package si.matjazcerkvenik.dtools.xml;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class FtpClients {
	
	private List<FtpClient> ftpClientList;

	public List<FtpClient> getFtpClientList() {
		return ftpClientList;
	}

	@XmlElement(name="ftpClient")
	public void setFtpClientList(List<FtpClient> ftpClient) {
		this.ftpClientList = ftpClient;
	}
	
	public void addFtpClientAction(FtpClient c) {
		ftpClientList.add(c);
	}
	
	public void deleteFtpClient(FtpClient c) {
		ftpClientList.remove(c);
	}
	
}
