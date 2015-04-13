package si.matjazcerkvenik.dtools.xml;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Commands {
	
	private List<String> commands;

	public List<String> getCommands() {
		return commands;
	}

	@XmlElement(name="command")
	public void setCommands(List<String> commands) {
		this.commands = commands;
	}
	
	public void addCommand(String cmd) {
		commands.add(cmd);
	}
	
	public void deleteCommand(String cmd) {
		commands.remove(cmd);
	}
	
	
}
