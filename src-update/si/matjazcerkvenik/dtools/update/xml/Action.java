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
		if (source == null) {
			return source;
		} else if (source.contains("$CURR_VER$")) {
			source = source.replace("$CURR_VER$", Update.currentVersion);
		} else if (source.contains("$DTOOLS_HOME$")) {
			source = source.replace("$DTOOLS_HOME$", Update.DTOOLS_HOME);
		} else if (source.contains("$VERSION$")) {
			source = source.replace("$VERSION$", Update.lastVersion);
		}
		return source;
	}
	@XmlElement
	public void setSource(String source) {
		this.source = source;
	}
	public String getDest() {
		if (dest == null) {
			return dest;
		} else if (dest.contains("$DTOOLS_HOME$")) {
			dest = dest.replace("$DTOOLS_HOME$", Update.DTOOLS_HOME);
		} else if (dest.contains("$CURR_VER$")) {
			dest = dest.replace("$CURR_VER$", Update.currentVersion);
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
	@Override
	public String toString() {
		return "ACTION[" + type + "] source: " + getSource() + "; dest: " + getDest() + "; md5: " + md5;
	}
	
}
