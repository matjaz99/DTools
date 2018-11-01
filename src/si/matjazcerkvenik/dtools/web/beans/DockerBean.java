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

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.event.TabChangeEvent;
import org.primefaces.event.TabCloseEvent;

import si.matjazcerkvenik.dtools.tools.console.Console;
import si.matjazcerkvenik.dtools.tools.docker.DockerContainer;
import si.matjazcerkvenik.dtools.tools.docker.DockerImage;
import si.matjazcerkvenik.dtools.tools.docker.DockerService;
import si.matjazcerkvenik.dtools.tools.docker.DockerSwormNode;

@ManagedBean
@ViewScoped
public class DockerBean {
		
	private String containersData;
	private String volumesData;
	private String networksData;
	private String configData;
	private String serviceData;
	private String stackData;
	private String nodeData;
	private String imagesData;
	private List<DockerImage> imagesList;
	private String statsData;
	private String infoData;

	public void onTabChange(TabChangeEvent event) {
		resetData(event.getTab().getTitle());
	}

	public void onTabClose(TabCloseEvent event) {
		// nothing to do
	}
	
	
	public void resetData(String title) {
		
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
			infoData = null;
		} else if (title.equalsIgnoreCase("Stats")) {
			statsData = null;
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
			imagesList = createImageObjects(imagesData);
		}
		return imagesData;
	}

	public void setImagesData(String imagesData) {
		this.imagesData = imagesData;
	}

	public List<DockerImage> getImagesList() {
		return imagesList;
	}

	public void setImagesList(List<DockerImage> imagesList) {
		this.imagesList = imagesList;
	}

	public String getStatsData() {
		if (statsData == null) {
			String[] cmd = {"docker", "stats", "--no-stream"};
			statsData = Console.runLinuxCommand(cmd);
		}
		return statsData;
	}

	public void setStatsData(String statsData) {
		this.statsData = statsData;
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
	
	
	public static List<DockerContainer> createContainerObjects(String data) {
		
		List<DockerContainer> list = new ArrayList<DockerContainer>();
		
		String[] lines = data.split("\n");
		System.out.println("found " + lines.length + " lines");
		
		for (int i = 1; i < lines.length; i++) {
			String[] strArray = lines[i].split("\\s{2,}");
			DockerContainer di = new DockerContainer();
			di.setId(strArray[0].trim());
			di.setImage(strArray[1].trim());
			di.setCommand(strArray[2].replace("…", "...").trim());
			di.setCreated(strArray[3].trim());
			di.setStatus(strArray[4].trim());
			di.setPorts(strArray[5].trim()); // TODO there could be no port specified!
			if (strArray.length > 6) {
				di.setNames(strArray[6].trim());
			}
			
			list.add(di);
		}
		
		return list;
		
	}
	
	public static List<DockerService> createServiceObjects(String data) {
		
		List<DockerService> list = new ArrayList<DockerService>();
		
		String[] lines = data.split("\n");
		System.out.println("found " + lines.length + " lines");
		
		for (int i = 1; i < lines.length; i++) {
			String[] strArray = lines[i].split("\\s{2,}");
			DockerService di = new DockerService();
			di.setId(strArray[0].trim());
			di.setName(strArray[1].trim());
			di.setMode(strArray[2].replace("…", "...").trim());
			di.setReplicas(strArray[3].trim());
			di.setImage(strArray[4].trim());
			if (strArray.length > 5) {
				di.setPorts(strArray[5].trim());
			}
			
			list.add(di);
		}
		
		return list;
		
	}
	
	public static List<DockerSwormNode> createSwormNodeObjects(String data) {
		
		List<DockerSwormNode> list = new ArrayList<DockerSwormNode>();
		
		String[] lines = data.split("\n");
		System.out.println("found " + lines.length + " lines");
		
		for (int i = 1; i < lines.length; i++) {
			String[] strArray = lines[i].split("\\s{2,}");
			DockerSwormNode di = new DockerSwormNode();
			di.setId(strArray[0].trim());
			di.setHostname(strArray[1].trim());
			di.setStatus(strArray[2].replace("…", "...").trim());
			di.setAvailability(strArray[3].trim());
			di.setManagerStatus(strArray[4].trim());
			di.setEngineVersion(strArray[5].trim());
			
			list.add(di);
		}
		
		return list;
		
	}
	
	public static List<DockerImage> createImageObjects(String data) {
		
		List<DockerImage> list = new ArrayList<DockerImage>();
		
		String[] lines = data.split("\n");
		System.out.println("found " + lines.length + " lines");
		
		for (int i = 1; i < lines.length; i++) {
			String[] strArray = lines[i].split("\\s{2,}");
			DockerImage di = new DockerImage();
			di.setRepository(strArray[0].trim());
			di.setTag(strArray[1].trim());
			di.setImageId(strArray[2].trim());
			di.setCreated(strArray[3].trim());
			di.setSize(strArray[4].trim());
			
			list.add(di);
		}
		
		return list;
		
	}
	

}
