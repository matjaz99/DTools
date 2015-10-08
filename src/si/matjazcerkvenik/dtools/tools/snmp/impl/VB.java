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

package si.matjazcerkvenik.dtools.tools.snmp.impl;

import java.io.Serializable;

public class VB implements Serializable {
	
	private static final long serialVersionUID = 2766510812643098966L;
	
	public String oid;
	public String value;
	
	public VB(String oid, String value) {
		this.oid = oid;
		this.value = value;
	}
	
	@Override
	public String toString() {
		return oid + "=" + value;
	}
	
}
