package si.matjazcerkvenik.dtools.xml;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.bind.annotation.XmlElement;

public class Todo {

	private String title;
	private String description;
	private Date created;
	private Date deadline;
	private boolean completed = false;

	public String getTitle() {
		return title;
	}

	@XmlElement
	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	@XmlElement
	public void setDescription(String description) {
		this.description = description;
	}

	public Date getCreated() {
		return created;
	}

	@XmlElement
	public void setCreated(Date created) {
		this.created = created;
	}

	public Date getDeadline() {
		return deadline;
	}

	@XmlElement
	public void setDeadline(Date deadline) {
		this.deadline = deadline;
	}

	public boolean isCompleted() {
		return completed;
	}

	@XmlElement
	public void setCompleted(boolean completed) {
		this.completed = completed;
	}
	
	public String getFormattedDate() {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		return sdf.format(deadline);
	}
	
	public String getBackgroundColor() {
		
		if (completed) {
			return "background-color: LimeGreen;";
		}
		
		long start = created.getTime();
		long end = deadline.getTime();
		long now = (new Date()).getTime();
		
		if (end - now < 0) {
			// in the past
			return "background-color: DarkRed;";
		}
		
		
		if ((end - start) < getDaysInMillis(6)) {
			// short term task
			if (end - now < getDaysInMillis(1)) {
				return "background-color: red;";
			}
			if (end - now < getDaysInMillis(2)) {
				return "background-color: orange;";
			}
			if (end - now < getDaysInMillis(3)) {
				return "background-color: yellow;";
			}
			return "";
		}
		
		if ((end - start) < getDaysInMillis(31)) {
			// medium term task
			if (end - now < getDaysInMillis(2)) {
				return "background-color: red;";
			}
			if (end - now < getDaysInMillis(5)) {
				return "background-color: orange;";
			}
			if (end - now < getDaysInMillis(7)) {
				return "background-color: yellow;";
			}
			return "";
		}
		
		// long term task
		if (end - now < getDaysInMillis(3)) {
			return "background-color: red;";
		}
		if (end - now < getDaysInMillis(6)) {
			return "background-color: orange;";
		}
		if (end - now < getDaysInMillis(9)) {
			return "background-color: yellow;";
		}
		return "";
				
	}
	
	private long getDaysInMillis(long days) {
		return days * 24 * 3600 * 1000;
	}

}
