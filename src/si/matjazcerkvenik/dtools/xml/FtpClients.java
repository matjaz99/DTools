package si.matjazcerkvenik.dtools.xml;

import java.util.ArrayList;
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
	
	public List<FtpClient> getCustomFtpClientsList(String hostname) {
		
		List<FtpClient> list = new ArrayList<FtpClient>();
		
		for (int i = 0; i < getFtpClientList().size(); i++) {
			if (getFtpClientList().get(i).getHostname().equals(hostname)) {
				list.add(getFtpClientList().get(i));
			}
		}
		
		return list;
		
	}
	
}
