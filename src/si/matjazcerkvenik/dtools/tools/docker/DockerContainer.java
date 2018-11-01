package si.matjazcerkvenik.dtools.tools.docker;

public class DockerContainer {

	private String id;
	private String image;
	private String command;
	private String created;
	private String status;
	private String ports;
	private String names;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public String getCreated() {
		return created;
	}

	public void setCreated(String created) {
		this.created = created;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getPorts() {
		return ports;
	}

	public void setPorts(String ports) {
		this.ports = ports;
	}

	public String getNames() {
		return names;
	}

	public void setNames(String names) {
		this.names = names;
	}

	@Override
	public String toString() {
		return "DockerContainer [id=" + id + ", image=" + image + ", command=" + command + ", created=" + created
				+ ", status=" + status + ", ports=" + ports + ", names=" + names + "]";
	}

}
