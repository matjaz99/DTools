package si.matjazcerkvenik.dtools.tools.console;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import si.matjazcerkvenik.dtools.context.DToolsContext;
import si.matjazcerkvenik.simplelogger.SimpleLogger;

public class Console {
	
	private static SimpleLogger logger = DToolsContext.getInstance().getLogger();

	public static String runLinuxCommand(String[] command) {
		
		String c = "";
		for (int i = 0; i < command.length; i++) {
			c += command[i] + " ";
		}
		logger.info("Console: exec: " + c);
		
		String s = null;
		StringBuffer sb = new StringBuffer();
		
		Runtime rt = Runtime.getRuntime();
		try {
			Process p = rt.exec(command);

			BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
			BufferedReader errbr = new BufferedReader(new InputStreamReader(p.getErrorStream()));
			while ((s = br.readLine()) != null) {
				sb.append(s).append("\n");
			}
			while ((s = errbr.readLine()) != null) {
				sb.append(s).append("\n");
			}

			// wait for ending command
			p.waitFor();

		} catch (InterruptedException e) {
//			e.printStackTrace();
//			logger.error("InterruptedException", e);
			sb.append(e.getMessage());
		} catch (IOException e) {
//			e.printStackTrace();
//			logger.error("IOException", e);
			sb.append(e.getMessage());
		}
		
		logger.info(sb.toString());
		
		return sb.toString();

	}

}
