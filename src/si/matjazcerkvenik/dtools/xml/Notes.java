package si.matjazcerkvenik.dtools.xml;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Notes {
	
	private List<Note> notesList;

	public List<Note> getNotesList() {
		return notesList;
	}

	@XmlElement(name="note")
	public void setNotesList(List<Note> notesList) {
		this.notesList = notesList;
	}
	
	public void addNote(Note n) {
		notesList.add(n);
	}
	
	public void deleteNote(Note n) {
		notesList.remove(n);
	}
	
}
