/* 
 * Copyright (C) 2015 Matjaz Cerkvenik
 * 
 * DTools is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * DTools is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with DTools. If not, see <http://www.gnu.org/licenses/>.
 * 
 */

package si.matjazcerkvenik.dtools.web.beans;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;

import org.primefaces.event.TabChangeEvent;
import org.primefaces.event.TabCloseEvent;

import si.matjazcerkvenik.dtools.tools.console.Console;

@ManagedBean
@ViewScoped
//@SessionScoped
public class DockerBean {
	
	private String tabChangedName = "Volumes";
	
	private String containersData;
	private String volumesData;
	private String networksData;
	private String configData;
	private String serviceData;
	private String stackData;
	private String nodeData;
	private String imagesData;
	private String infoData;
	private String lsData;

	public void onTabChange(TabChangeEvent event) {
		tabChangedName = event.getTab().getTitle();
		getData(tabChangedName);
	}

	public void onTabClose(TabCloseEvent event) {
		// nothing to do
	}
	
	
	public void getData(String title) {
		
		System.out.println("getData: " + title);
		
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
			imagesData = null;
		} else if (title.equals("Info")) {
			System.out.println("info=null");
			infoData = null;
		} else if (title.equalsIgnoreCase("ls")) {
			System.out.println("ls=null");
			lsData = null;
		}
		
	}

	public String getContainersData() {
		System.out.println("getContainersData");
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
		System.out.println("getVolumesData");
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
			networksData = Console.runLinuxCommand(cmd);
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

	public String getLsData() {
		if (lsData == null) {
			String[] cmd = {"ls", "-la", "/Users/matjaz/Desktop"};
			lsData = Console.runLinuxCommand(cmd);
		}
		return lsData;
	}

	public void setLsData(String lsData) {
		this.lsData = lsData;
	}

	public String getTabChangedName() {
		return tabChangedName;
	}

	public void setTabChangedName(String tabChangedName) {
		this.tabChangedName = tabChangedName;
	}
	

}
