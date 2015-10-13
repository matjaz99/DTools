package si.matjazcerkvenik.dtools.update.xml;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

import si.matjazcerkvenik.dtools.update.Update;

public class Action {

	private String type;
	private String source;
	private String dest;
	private String md5;

	public String getType() {
		return type;
	}

	@XmlAttribute
	public void setType(String type) {
		this.type = type;
	}

	public String getSource() {
		if (source != null) {
			source = replaceUrlParameters(source);
		}
		return source;
	}

	@XmlElement
	public void setSource(String source) {
		this.source = source;
	}

	public String getDest() {
		if (dest != null) {
			dest = replaceUrlParameters(dest);
		}
		return dest;
	}

	@XmlElement
	public void setDest(String dest) {
		this.dest = dest;
	}

	public String getMd5() {
		return md5;
	}

	@XmlElement
	public void setMd5(String md5) {
		this.md5 = md5;
	}
	
	private String replaceUrlParameters(String s) {
		if (s.contains("$DTOOLS_HOME$")) {
			s = s.replace("$DTOOLS_HOME$", Update.DTOOLS_HOME);
		} else if (s.contains("$CURR_VER$")) {
			s = s.replace("$CURR_VER$", Update.currentVersion);
		} else if (s.contains("$VERSION$")) {
			s = s.replace("$VERSION$", Update.lastVersion);
		}
		return s;
	}

	@Override
	public String toString() {
		return "[" + type.toUpperCase() + "] source: " + getSource()
				+ "; dest: " + getDest() + "; md5: " + md5;
	}

}
