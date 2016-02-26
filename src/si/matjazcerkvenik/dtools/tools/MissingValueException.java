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

package si.matjazcerkvenik.dtools.tools;

public class MissingValueException extends Exception {
	
	private static final long serialVersionUID = 3064508668471250586L;
	
	private static final String MSG = "Missing value";

		public MissingValueException() {
			super(MSG);
		}

		public MissingValueException(String message, Throwable cause) {
			super(MSG, cause);
		}

		public MissingValueException(String message) {
			super(MSG);
		}

		public MissingValueException(Throwable cause) {
			super(MSG, cause);
		}
	
}
