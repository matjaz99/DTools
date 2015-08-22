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

package si.matjazcerkvenik.dtools.update;

import java.io.File;
import java.io.FileInputStream;
import java.security.MessageDigest;

public class MD5Checksum {
	
	public static String getMd5Checksum(String file) {
		
		File f = new File(file);
		if (!f.exists()) {
			return "0";
		}
		
		//convert the byte to hex format
		StringBuffer sb = null;
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			FileInputStream fis = new FileInputStream(file);
			byte[] dataBytes = new byte[1024];
 
			int nread = 0; 
 
			while ((nread = fis.read(dataBytes)) != -1) {
			  md.update(dataBytes, 0, nread);
			}
 
			byte[] mdbytes = md.digest();
 
			sb = new StringBuffer("");
			for (int i = 0; i < mdbytes.length; i++) {
				sb.append(Integer.toString((mdbytes[i] & 0xff) + 0x100, 16).substring(1));
			}
			
			fis.close();
		} catch (Exception e) {
			System.out.println("ERROR: cannot determine MD5 checksum");
		}
		
	    return sb.toString();
	    
	}
	
	
}
