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

package si.matjazcerkvenik.dtools.xml;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="networkNodes")
public class NetworkNodes {
	
	private List<Node> nodesList;

	public List<Node> getNodesList() {
		return nodesList;
	}

	@XmlElement(name="node") // name of element (instead of nodesList)
	public void setNodesList(List<Node> node) {
		this.nodesList = node;
	}
	
	public void addNode(Node n) {
		if (nodesList == null) {
			nodesList = new ArrayList<Node>();
		}
		nodesList.add(n);
	}
	
	public void deleteNode(Node n) {
		nodesList.remove(n);
	}
	
}
