package si.matjazcerkvenik.dtools.web;

import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import si.matjazcerkvenik.dtools.context.DToolsContext;
import si.matjazcerkvenik.dtools.xml.DAO;
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
