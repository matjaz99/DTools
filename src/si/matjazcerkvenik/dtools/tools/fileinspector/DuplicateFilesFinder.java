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

package si.matjazcerkvenik.dtools.tools.fileinspector;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import si.matjazcerkvenik.dtools.context.DToolsContext;
import si.matjazcerkvenik.dtools.tools.MissingValueException;
import si.matjazcerkvenik.dtools.tools.md5.MD5Checksum;

/**
 * This class searches selected directory for files with equal MD5 checksum - duplicate files.
 * 
 * @author matjaz
 *
 */
public class DuplicateFilesFinder {
	
	private String scanDir;
	private Map<String, List<File>> map = new HashMap<String, List<File>>();
	
	public void scanDir(String directory) throws MissingValueException, FileNotFoundException {
		
		scanDir = directory.trim();
		
		if (scanDir == null || scanDir.length() == 0) {
			throw new MissingValueException();
		}
		DToolsContext.getInstance().getLogger().info("DuplicateFilesFinder:scanDir(): " + scanDir);
		scanDir(new File(scanDir));
		
	}
	
	private void scanDir(File dir) throws FileNotFoundException {
		
		if (dir.exists() && dir.isDirectory()) {
			
			File[] files = dir.listFiles();
			
			for (int i = 0; i < files.length; i++) {
				if (files[i].isFile()) {
					addToMap(files[i]);
				} else if (files[i].isDirectory()) {
					scanDir(files[i]);
				}
			}
			
		} else {
			throw new FileNotFoundException();
		}
		
	}
	
	
	private void addToMap(File f) {
		String md5 = MD5Checksum.getMd5Checksum(f);
		List<File> filesList;
		if (map.containsKey(md5)) {
			filesList = map.get(md5);
		} else {
			filesList = new ArrayList<File>();
		}
		
		filesList.add(f);
		map.put(md5, filesList);
	}
	
	
	public String printDuplicateFiles() {
		
		StringBuffer sb = new StringBuffer();
		sb.append("Duplicate files in: ").append(scanDir).append("\n");
		
		for (List<File> list : map.values()) {
			
			if (list.size() == 1) {
				continue;
			}
			sb.append("------------------------------------------\n");
			for (int i = 0; i < list.size(); i++) {
				sb.append(list.get(i)).append("\n");
			}
			
		}
		
		return sb.toString();
		
	}
	
	public static void main(String[] args) throws Exception {
		DuplicateFilesFinder dff = new DuplicateFilesFinder();
		dff.scanDir("/Users/matjaz/Developer/git-workspace/DTools");
		System.out.println(dff.printDuplicateFiles());
	}

}
