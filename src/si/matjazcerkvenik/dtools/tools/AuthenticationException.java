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

public class AuthenticationException extends Exception {
	
	private static final long serialVersionUID = -7779239999621010495L;
	
	private static final String MSG = "Authentication failed";

		public AuthenticationException() {
			super(MSG);
		}

		public AuthenticationException(String message, Throwable cause) {
			super(MSG, cause);
		}

		public AuthenticationException(String message) {
			super(MSG);
		}

		public AuthenticationException(Throwable cause) {
			super(MSG, cause);
		}
	
}
