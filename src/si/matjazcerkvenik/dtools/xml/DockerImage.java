package si.matjazcerkvenik.dtools.xml;

public class DockerImage {

	private String repository;
	private String tag;
	private String imageId;
	private String created;
	private String size;

	public String getRepository() {
		return repository;
	}

	public void setRepository(String repository) {
		this.repository = repository;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public String getImageId() {
		return imageId;
	}

	public void setImageId(String imageId) {
		this.imageId = imageId;
	}

	public String getCreated() {
		return created;
	}

	public void setCreated(String created) {
		this.created = created;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	@Override
	public String toString() {
		return "DockerImage [repository=" + repository + ", tag=" + tag + ", imageId=" + imageId + ", created="
				+ created + ", size=" + size + "]";
	}

}
