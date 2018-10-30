package si.matjazcerkvenik.dtools.web.beans;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.primefaces.event.TabChangeEvent;
import org.primefaces.event.TabCloseEvent;

import si.matjazcerkvenik.dtools.tools.console.Console;

@ManagedBean
@SessionScoped
public class DockerBean {
	
	private String tabChangedName = "Containers";
	
	private String containersData;
	private String volumesData;
	private String networksData;
	private String configData;
	private String serviceData;
	private String stackData;
	private String nodeData;
	private String imagesData;
	private String infoData;

	public void onTabChange(TabChangeEvent event) {
		tabChangedName = event.getTab().getTitle();
//		FacesMessage msg = new FacesMessage("Active Tab: " + tabChangedName, "Tab Changed");
//		FacesContext.getCurrentInstance().addMessage(null, msg);
		getData(tabChangedName);
	}

	public void onTabClose(TabCloseEvent event) {
//		FacesMessage msg = new FacesMessage("Closed tab: " + event.getTab().getTitle(), "Tab Closed");
//		FacesContext.getCurrentInstance().addMessage(null, msg);
	}
	
	
	public void getData(String title) {
		
		if (title.equals("Containers")) {
			containersData = null;
		} else if (title.equals("Volumes")) {
			volumesData = null;
		} else if (title.equals("Networks")) {
			networksData = null;
		} else if (title.equals("Configs")) {
			configData = null;
		} else if (title.equals("Services")) {
			serviceData = null;
		} else if (title.equals("Stacks")) {
			stackData = null;
		} else if (title.equals("Sworm")) {
			nodeData = null;
		} else if (title.equals("Images")) {
			infoData = null;
		} else if (title.equals("Info")) {
			infoData = null;
		}
		
	}

	public String getContainersData() {
		if (containersData == null) {
			String[] cmd = {"docker", "ps", "-a"};
			containersData = Console.runLinuxCommand(cmd);
		}
		return containersData;
	}

	public void setContainersData(String containersData) {
		this.containersData = containersData;
	}

	public String getVolumesData() {
		if (volumesData == null) {
			String[] cmd = {"docker", "volume", "ls"};
			volumesData = Console.runLinuxCommand(cmd);
		}
		return volumesData;
	}

	public void setVolumesData(String volumesData) {
		this.volumesData = volumesData;
	}

	public String getNetworksData() {
		if (networksData == null) {
			String[] cmd = {"docker", "network", "ls"};
			volumesData = Console.runLinuxCommand(cmd);
		}
		return networksData;
	}

	public void setNetworksData(String networksData) {
		this.networksData = networksData;
	}

	public String getConfigData() {
		if (configData == null) {
			String[] cmd = {"docker", "config", "ls"};
			configData = Console.runLinuxCommand(cmd);
		}
		return configData;
	}

	public void setConfigData(String configData) {
		this.configData = configData;
	}

	public String getServiceData() {
		if (serviceData == null) {
			String[] cmd = {"docker", "service", "ls"};
			serviceData = Console.runLinuxCommand(cmd);
		}
		return serviceData;
	}

	public void setServiceData(String serviceData) {
		this.serviceData = serviceData;
	}

	public String getStackData() {
		if (stackData == null) {
			String[] cmd = {"docker", "stack", "ls"};
			stackData = Console.runLinuxCommand(cmd);
		}
		return stackData;
	}

	public void setStackData(String stackData) {
		this.stackData = stackData;
	}

	public String getNodeData() {
		if (nodeData == null) {
			String[] cmd = {"docker", "node", "ls"};
			nodeData = Console.runLinuxCommand(cmd);
		}
		return nodeData;
	}

	public void setNodeData(String nodeData) {
		this.nodeData = nodeData;
	}

	public String getImagesData() {
		if (imagesData == null) {
			String[] cmd = {"docker", "image", "ls"};
			imagesData = Console.runLinuxCommand(cmd);
		}
		return imagesData;
	}

	public void setImagesData(String imagesData) {
		this.imagesData = imagesData;
	}

	public String getInfoData() {
		if (infoData == null) {
			String[] cmd = {"docker", "info"};
			infoData = Console.runLinuxCommand(cmd);
		}
		return infoData;
	}

	public void setInfoData(String infoData) {
		this.infoData = infoData;
	}

	public String getTabChangedName() {
		return tabChangedName;
	}

	public void setTabChangedName(String tabChangedName) {
		this.tabChangedName = tabChangedName;
	}
	

}
