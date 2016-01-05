package si.matjazcerkvenik.dtools.tools.ping;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.UnknownHostException;

public class HttpPing {

	public PingStatus ping(String s) {

		PingStatus ps = new PingStatus();
		ps.started();

		try {
			URL url = new URL(s);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			connection.connect();
			int code = connection.getResponseCode();
			ps.setErrorCode(PingStatus.EC_OK);
			ps.setErrorMessage(PingStatus.EM_OK);
			ps.setErrorDescription("HTTP code: " + code);
		} catch (UnknownHostException e) {
			ps.setErrorCode(PingStatus.EC_UNKN_HOST);
			ps.setErrorMessage(PingStatus.EM_UNKN_HOST);
			ps.setErrorDescription(e.getMessage());
		} catch (MalformedURLException e) {
			ps.setErrorCode(PingStatus.EC_MALF_URL);
			ps.setErrorMessage(PingStatus.EM_MALF_URL);
			ps.setErrorDescription(e.getMessage());
		} catch (ProtocolException e) {
			ps.setErrorCode(PingStatus.EC_PROT_ERROR);
			ps.setErrorMessage(PingStatus.EM_PROT_ERROR);
			ps.setErrorDescription(e.getMessage());
		} catch (IOException e) {
			ps.setErrorCode(PingStatus.EC_IO_ERROR);
			ps.setErrorMessage(PingStatus.EM_IO_ERROR);
			ps.setErrorDescription(e.getMessage());
		} catch (Exception e) {
			ps.setErrorCode(PingStatus.EC_CONN_ERROR);
			ps.setErrorMessage(PingStatus.EM_CONN_ERROR);
			ps.setErrorDescription(e.getMessage());
		}
		
		ps.ended();
		return ps;

	}

}
