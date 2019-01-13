package si.matjazcerkvenik.dtools.xml;

import javax.xml.bind.annotation.XmlAttribute;

public class GrafanaPanel {

	private String src;
	private int height;
	private int width;

	@XmlAttribute
	public String getSrc() {
		return src;
	}

	public void setSrc(String src) {
		this.src = src;
	}

	public int getHeight() {
		return height;
	}

	@XmlAttribute
	public void setHeight(int height) {
		this.height = height;
	}

	public int getWidth() {
		return width;
	}

	@XmlAttribute
	public void setWidth(int width) {
		this.width = width;
	}

}
