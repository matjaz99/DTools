package si.matjazcerkvenik.dtools.tools.docker;

public class DockerService {

	private String id;
	private String name;
	private String mode;
	private String replicas;
	private String image;
	private String ports;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public String getReplicas() {
		return replicas;
	}

	public void setReplicas(String replicas) {
		this.replicas = replicas;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getPorts() {
		return ports;
	}

	public void setPorts(String ports) {
		this.ports = ports;
	}

	@Override
	public String toString() {
		return "DockerService [id=" + id + ", name=" + name + ", mode=" + mode + ", replicas=" + replicas + ", image="
				+ image + ", ports=" + ports + "]";
	}

}
