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
		
		logger.info("Console: response:\n" + sb.toString() + "\n");
		
		return sb.toString();

	}

}
