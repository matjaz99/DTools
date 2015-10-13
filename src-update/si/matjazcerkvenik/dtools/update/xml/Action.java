/* 
 * Copyright (C) 2015 Matjaz Cerkvenik
 * 
 * DTools is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * DTools is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with DTools. If not, see <http://www.gnu.org/licenses/>.
 * 
 */

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
