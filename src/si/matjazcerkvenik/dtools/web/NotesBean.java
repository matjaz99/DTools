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

package si.matjazcerkvenik.dtools.web;

import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import si.matjazcerkvenik.dtools.context.DToolsContext;
import si.matjazcerkvenik.dtools.io.DAO;
import si.matjazcerkvenik.dtools.xml.Note;

@ManagedBean
@SessionScoped
public class NotesBean {
	
	private String message;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	public List<Note> getNotes() {
		return DAO.getInstance().loadNotes().getNotesList();
	}
	
	public void addNoteAction() {
		
		Note n = new Note();
		n.setTimestamp(DToolsContext.getCurrentDate());
		n.setMessage(message);
		// TODO color
		
		DAO.getInstance().addNote(n);
		
		message = null;
		
	}
	
	public void deleteNote(Note n) {
		DAO.getInstance().deleteNote(n);
	}
	
}
