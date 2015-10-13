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

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Script {
	
	private List<Action> actionsList;

	public List<Action> getActionsList() {
		return actionsList;
	}

	@XmlElement(name="action")
	public void setActionsList(List<Action> actionsList) {
		this.actionsList = actionsList;
	}
	
	@Override
	public String toString() {
		String s = "Script:\n";
		for (int i = 0; i < actionsList.size(); i++) {
			s += actionsList.get(i).toString() + "\n";
		}
		return s;
	}
	
}
