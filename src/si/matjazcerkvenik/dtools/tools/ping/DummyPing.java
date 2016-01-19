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

package si.matjazcerkvenik.dtools.tools.ping;

import java.io.Serializable;

import si.matjazcerkvenik.dtools.xml.Service;

public class DummyPing implements IPing, Serializable {
	
	private static final long serialVersionUID = 8838986224704641300L;
	
	private PingStatus status = new PingStatus();
	
	@Override
	public void configure(Service service) {
		// not applicable
	}
	
	@Override
	public void ping() {
		// not applicable
	}
	
	@Override
	public PingStatus getStatus() {
		return status;
	}
	
	@Override
	public String getStatusIcon() {
		return "bullet_disabled.png";
	}
	
	@Override
	public void resetStatus() {
		status = new PingStatus();
	}
	
}
