package si.matjazcerkvenik.dtools.xml;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;

public class NodeServices implements Serializable {
	
	private static final long serialVersionUID = 3823011709894724064L;
	
	private List<Service> services;
	
	public NodeServices() {
		if (services == null) {
			services = new ArrayList<Service>();
		}
	}

	public List<Service> getServices() {
		return services;
	}

	@XmlElement(name="service")
	public void setServices(List<Service> services) {
		this.services = services;
	}
	
	public void addService(Service service) {
		if (services == null) {
			services = new ArrayList<Service>();
		}
		services.add(service);
	}
	
}
