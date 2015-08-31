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

package si.matjazcerkvenik.dtools.tools.localhost;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

public class LocalhostInfo {

	/**
	 * Get local IP address. Works good on Windows and OS X. On CentOS 6 it
	 * always returns 'UnknownHost' because many network interfaces could exist
	 * and it doesn't resolve eth0 as default interface.<br>
	 * See method printNetworkInterfaces() below.
	 * 
	 * @return ip address
	 */
	public static String getLocalIpAddress() {
		try {
			return InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			return "UnknownHost";
		}
	}

	public static String printNetworkInterfaces() {
		StringBuffer sb = new StringBuffer();
		try {
			Enumeration<NetworkInterface> ni = NetworkInterface.getNetworkInterfaces();
			while (ni.hasMoreElements()) {
				sb.append("NetworkInterface\n");
				NetworkInterface i = (NetworkInterface) ni.nextElement();
				sb.append("\tDisplayName: " + i.getDisplayName() + "\n");
				sb.append("\tName: " + i.getName() + "\n");
				sb.append("\tHW address: " + convertToHex(i.getHardwareAddress()) + "\n");
				Enumeration<InetAddress> ia = i.getInetAddresses();
				sb.append("\tInetAddresses: " + "\n");
				while (ia.hasMoreElements()) {
					InetAddress a = (InetAddress) ia.nextElement();
					sb.append("\t\tCanonicalHostName: " + a.getCanonicalHostName() + "\n");
					sb.append("\t\tHostAddress: " + a.getHostAddress() + "\n");
					sb.append("\t\tHostName: " + a.getHostName() + "\n");
				}
			}
		} catch (SocketException e1) {
			e1.printStackTrace();
		}
		return sb.toString();
	}
	
	private static String convertToHex(byte[] mac) {
	    if(mac != null) {
	    	StringBuilder sb = new StringBuilder();
	    	for (int i = 0; i < mac.length; i++) {
	    		sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
	    	}
	    	return sb.toString();
	    }
	    return "n/a";
	}
	

	public static String getSystemUser() {
		String u = System.getProperty("user.name");
		if (u == null) {
			return "user";
		}
		return u;
	}

}
