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
