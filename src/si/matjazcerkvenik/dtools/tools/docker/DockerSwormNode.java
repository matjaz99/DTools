package si.matjazcerkvenik.dtools.tools.docker;

public class DockerSwormNode {

	private String id;
	private String hostname;
	private String status;
	private String availability;
	private String managerStatus;
	private String engineVersion;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getHostname() {
		return hostname;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getAvailability() {
		return availability;
	}

	public void setAvailability(String availability) {
		this.availability = availability;
	}

	public String getManagerStatus() {
		return managerStatus;
	}

	public void setManagerStatus(String managerStatus) {
		this.managerStatus = managerStatus;
	}

	public String getEngineVersion() {
		return engineVersion;
	}

	public void setEngineVersion(String engineVersion) {
		this.engineVersion = engineVersion;
	}

	@Override
	public String toString() {
		return "DockerSwormNode [id=" + id + ", hostname=" + hostname + ", status=" + status + ", availability="
				+ availability + ", managerStatus=" + managerStatus + ", engineVersion=" + engineVersion + "]";
	}

}
