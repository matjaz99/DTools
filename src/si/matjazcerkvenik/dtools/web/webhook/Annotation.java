package si.matjazcerkvenik.dtools.web.webhook;

public class Annotation {
	
	private String console;
	private String description;
	private String summary;
	
	public String getConsole() {
		return console;
	}
	public void setConsole(String console) {
		this.console = console;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getSummary() {
		return summary;
	}
	public void setSummary(String summary) {
		this.summary = summary;
	}
	
	@Override
	public String toString() {
		return "Annotation [console=" + console + ", description=" + description + ", summary=" + summary + "]";
	}
	
	
	
	
}
