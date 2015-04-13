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
