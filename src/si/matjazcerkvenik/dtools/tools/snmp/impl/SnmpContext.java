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


public class SnmpContext {

	int someCtx;
	
	public static final int UNDEFINED = 0;
	public static final int CRITICAL = 1;
	public static final int MAJOR = 2;
	public static final int MINOR = 3;
	public static final int WARNING = 4;
	public static final int CLEAR = 5;
	public static final int INFO = 6;

	public int getSomeCtx() {
		return someCtx;
	}

	public void setSomeCtx(int someCtx) {
		this.someCtx = someCtx;
	}
	

}
