package si.matjazcerkvenik.dtools.xml;

import javax.xml.bind.annotation.XmlAttribute;

public class MetricLabel {

	private String key;
	private String value;

	public MetricLabel() {
	}

	public MetricLabel(String key, String value) {
		this.key = key;
		this.value = value;
	}

	public String getKey() {
		return key;
	}

	@XmlAttribute
	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	@XmlAttribute
	public void setValue(String value) {
		this.value = value;
	}

}
