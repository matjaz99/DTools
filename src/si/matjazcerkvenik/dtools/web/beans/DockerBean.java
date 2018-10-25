package si.matjazcerkvenik.dtools.web.beans;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import org.primefaces.event.TabChangeEvent;
import org.primefaces.event.TabCloseEvent;

import si.matjazcerkvenik.dtools.tools.console.Console;

@ManagedBean
@SessionScoped
public class DockerBean {
	
	private String containersData;
	private String volumesData;
	private String configData;
	private String serviceData;
	private String stackData;
	private String nodeData;
	private String infoData;

	public void onTabChange(TabChangeEvent event) {
		FacesMessage msg = new FacesMessage("Active Tab: " + event.getTab().getTitle(), "Tab Changed");
		FacesContext.getCurrentInstance().addMessage(null, msg);
		getData(event.getTab().getTitle());
	}

	public void onTabClose(TabCloseEvent event) {
		FacesMessage msg = new FacesMessage("Closed tab: " + event.getTab().getTitle(), "Tab Closed");
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}
	
	
	public void getData(String title) {
		
		if (title.equals("Containers")) {
			System.out.println("containers selected");
			String[] cmd = {"docker", "ps"};
			containersData = Console.runLinuxCommand(cmd);
		} else if (title.equals("Volumes")) {
			System.out.println("Volumes selected");
			String[] cmd = {"docker", "volume", "ls"};
			volumesData = Console.runLinuxCommand(cmd);
		} else if (title.equals("Configs")) {
			String[] cmd = {"docker", "config", "ls"};
			volumesData = Console.runLinuxCommand(cmd);
		} else if (title.equals("Services")) {
			String[] cmd = {"docker", "service", "ls"};
			volumesData = Console.runLinuxCommand(cmd);
		} else if (title.equals("Stacks")) {
			String[] cmd = {"docker", "stack", "ls"};
			volumesData = Console.runLinuxCommand(cmd);
		} else if (title.equals("Sworm nodes")) {
			String[] cmd = {"docker", "node", "ls"};
			volumesData = Console.runLinuxCommand(cmd);
		} else if (title.equals("Info")) {
			String[] cmd = {"docker", "info"};
			volumesData = Console.runLinuxCommand(cmd);
		}
		
		
	}

	public String getContainersData() {
		return containersData;
	}

	public void setContainersData(String containersData) {
		this.containersData = containersData;
	}

	public String getVolumesData() {
		return volumesData;
	}

	public void setVolumesData(String volumesData) {
		this.volumesData = volumesData;
	}

	public String getConfigData() {
		return configData;
	}

	public void setConfigData(String configData) {
		this.configData = configData;
	}

	public String getServiceData() {
		return serviceData;
	}

	public void setServiceData(String serviceData) {
		this.serviceData = serviceData;
	}

	public String getStackData() {
		return stackData;
	}

	public void setStackData(String stackData) {
		this.stackData = stackData;
	}

	public String getNodeData() {
		return nodeData;
	}

	public void setNodeData(String nodeData) {
		this.nodeData = nodeData;
	}

	public String getInfoData() {
		return infoData;
	}

	public void setInfoData(String infoData) {
		this.infoData = infoData;
	}
	

}
