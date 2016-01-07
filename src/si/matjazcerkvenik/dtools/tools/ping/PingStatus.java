package si.matjazcerkvenik.dtools.tools.ping;


public class PingStatus {
	
	public final static int EC_UNKN 			= 0;
	public final static String EM_UNKN 			= "Unknown";
	public final static int EC_OK 				= 1;
	public final static String EM_OK 			= "OK";
	public final static int EC_UNKN_HOST 		= 10;
	public final static String EM_UNKN_HOST 	= "Unknown host";
	public final static int EC_CONN_ERROR 		= 11;
	public final static String EM_CONN_ERROR 	= "Connection error";
	public final static int EC_IO_ERROR 		= 12;
	public final static String EM_IO_ERROR 		= "IO error";
	public final static int EC_PROT_ERROR 		= 13;
	public final static String EM_PROT_ERROR 	= "Protocol error";
	public final static int EC_MALF_URL 		= 14;
	public final static String EM_MALF_URL 		= "Malformed URL error";
	
	
	private int errorCode = 0;
	private String errorMessage;
	private String errorDescription;
	private long startTime;
	private long endTime;
	
	public PingStatus() {
		
	}

	public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getErrorDescription() {
		return errorDescription;
	}

	public void setErrorDescription(String errorDescription) {
		this.errorDescription = errorDescription;
	}

	public long getStartTime() {
		return startTime;
	}

	public long getEndTime() {
		return endTime;
	}
	
	public void started() {
		startTime = System.currentTimeMillis();
	}
	
	public void ended() {
		endTime = System.currentTimeMillis();
	}
	
	@Override
	public String toString() {
		int delta = (int) (endTime - startTime);
		return "[eC=" + errorCode + ", eM=" + errorMessage
				+ ", eD=" + errorDescription + ", sT=" + startTime 
				+ ", eT=" + endTime + ", dT=" + delta + "]";
	}
	
}
