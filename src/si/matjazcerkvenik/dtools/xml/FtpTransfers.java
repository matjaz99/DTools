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

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class FtpTransfers {
	
	private List<FtpTransfer> transfersList;

	public List<FtpTransfer> getTransfersList() {
		return transfersList;
	}

	@XmlElement(name="transfer")
	public void setTransfersList(List<FtpTransfer> transfersList) {
		this.transfersList = transfersList;
	}
	
	public void addFtpTransfer(FtpTransfer t) {
		transfersList.add(t);
	}
	
	public void deleteFtpTransfer(FtpTransfer t) {
		transfersList.remove(t);
	}
	
}
