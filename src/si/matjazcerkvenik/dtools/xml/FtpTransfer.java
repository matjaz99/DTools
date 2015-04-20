package si.matjazcerkvenik.dtools.xml;

import javax.xml.bind.annotation.XmlElement;

public class FtpTransfer {

	private String from;
	private String to;

	public String getFrom() {
		return from;
	}

	@XmlElement
	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	@XmlElement
	public void setTo(String to) {
		this.to = to;
	}

}
