package si.matjazcerkvenik.dtools.tools.docker;

import java.util.List;

import si.matjazcerkvenik.dtools.web.beans.DockerBean;

public class DockerTest {

	public static void main(String[] args) {

		String containers = "CONTAINER ID        IMAGE                        COMMAND                  CREATED             STATUS                    PORTS               NAMES\n"
				+ "49808de129b0        prom/alertmanager:v0.15.2    \"/bin/alertmanager -…\"   14 minutes ago      Up 14 minutes             9093/tcp            monis_alertmanager.1.851eq2d4m4old07q6cnxxpdph\n"
				+ "8c0730ee52af        prom/snmp-exporter:v0.13.0   \"/bin/snmp_exporter …\"   14 minutes ago      Up 14 minutes             9116/tcp            monis_snmpexporter.1.kfq4fd204xbmhzldmz5mf1l1j\n"
				+ "977b26e35e17        prom/node-exporter:v0.16.0   \"/bin/node_exporter …\"   14 minutes ago      Up 14 minutes             9100/tcp            monis_nodex.mw547g4rsk5ql96kddj45pwxt.qk22ngrs7f214ss0ogvlv5kcz\n"
				+ "297278aec36f        google/cadvisor:v0.31.0      \"/usr/bin/cadvisor -…\"   14 minutes ago      Up 14 minutes             8080/tcp            monis_cadvisor.mw547g4rsk5ql96kddj45pwxt.pan0cke9fath3owp424lnyg6c\n"
				+ "0bc63f261527        portainer/portainer          \"/portainer\"             23 hours ago        Exited (2) 16 hours ago                       portainer\n";

		List<DockerContainer> containersList = DockerBean.createContainerObjects(containers);
		
		for (DockerContainer dockerContainer : containersList) {
			System.out.println(dockerContainer.toString());
		}
		
		String images = "REPOSITORY            TAG                 IMAGE ID            CREATED             SIZE\n"
				+ "portainer/portainer   latest              00ead811e8ae        6 weeks ago         58.7MB\n"
				+ "prom/snmp-exporter    <none>              4731be0fc5cf        7 weeks ago         18.6MB\n"
				+ "google/cadvisor       <none>              a38f1319a420        7 weeks ago         73.8MB";

		List<DockerImage> imagesList = DockerBean.createImageObjects(images);

		for (DockerImage dockerImage : imagesList) {
			System.out.println(dockerImage.toString());
		}

	}

}
