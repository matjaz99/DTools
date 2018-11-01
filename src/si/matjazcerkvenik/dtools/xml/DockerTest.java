package si.matjazcerkvenik.dtools.xml;

import java.util.List;

import si.matjazcerkvenik.dtools.web.beans.DockerBean;

public class DockerTest {

	public static void main(String[] args) {

		String images = "REPOSITORY            TAG                 IMAGE ID            CREATED             SIZE\n"
				+ "portainer/portainer   latest              00ead811e8ae        6 weeks ago         58.7MB\n"
				+ "prom/snmp-exporter    <none>              4731be0fc5cf        7 weeks ago         18.6MB\n"
				+ "google/cadvisor       <none>              a38f1319a420        7 weeks ago         73.8MB";
		
		
		List<DockerImage> list = DockerBean.createImageObjects(images);
		
		for (DockerImage dockerImage : list) {
			System.out.println(dockerImage.toString());
		}
		
	}
	
	

}
