package si.matjazcerkvenik.dtools.tools.console;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Console {

	public static String runLinuxCommand(String[] command) {
		
		String s = null;
		
		Runtime rt = Runtime.getRuntime();
		try {
			Process p = rt.exec(command);

			BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
			BufferedReader errbr = new BufferedReader(new InputStreamReader(p.getErrorStream()));
			while ((s = br.readLine()) != null) {
				System.out.println("Response : " + s);
				s += s + "\n";
			}
			while ((s = errbr.readLine()) != null) {
				System.out.println("ErrResponse : " + s);
			}

			// wait for ending command
			p.waitFor();

		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return s;

	}

}
