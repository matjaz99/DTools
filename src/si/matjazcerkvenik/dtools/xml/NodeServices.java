package si.matjazcerkvenik.dtools.xml;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;

public class NodeServices {
	
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
