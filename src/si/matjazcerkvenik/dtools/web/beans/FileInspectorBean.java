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

package si.matjazcerkvenik.dtools.web.beans;

import java.io.FileNotFoundException;
import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import si.matjazcerkvenik.dtools.tools.MissingValueException;
import si.matjazcerkvenik.dtools.tools.fileinspector.DuplicateFilesFinder;

@ManagedBean
@SessionScoped
public class FileInspectorBean implements Serializable {
	
	private static final long serialVersionUID = 978539936948768708L;
	
	private String directory;
	private String result;

	public String getDirectory() {
		return directory;
	}

	public void setDirectory(String directory) {
		this.directory = directory;
	}
	
	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public void searchDirectoryForDuplicates() {
		System.out.println("scan: " + directory);
		result = null;
		try {
			DuplicateFilesFinder dff = new DuplicateFilesFinder();
			dff.scanDir(directory);
			result = dff.printDuplicateFiles();
		} catch (FileNotFoundException e) {
			result = "ERROR: Directory not found";
		} catch (MissingValueException e) {
			result = "ERROR: Missing directory";
		}
	}
	
}
