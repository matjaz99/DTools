package si.matjazcerkvenik.dtools.xml;

import javax.xml.bind.annotation.XmlElement;

public class Note {

	private String timestamp;
	private String message;
	private String color;

	public String getTimestamp() {
		return timestamp;
	}

	@XmlElement
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public String getMessage() {
		return message;
	}

	@XmlElement
	public void setMessage(String message) {
		this.message = message;
	}

	public String getColor() {
		return color;
	}

	@XmlElement
	public void setColor(String color) {
		this.color = color;
	}

}
