package si.matjazcerkvenik.dtools.update.xml;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Script {
	
	private List<Action> actionsList;

	public List<Action> getActionsList() {
		return actionsList;
	}

	@XmlElement(name="action")
	public void setActionsList(List<Action> actionsList) {
		this.actionsList = actionsList;
	}
	
	@Override
	public String toString() {
		String s = "Script:\n";
		for (int i = 0; i < actionsList.size(); i++) {
			s += actionsList.get(i).toString() + "\n";
		}
		return s;
	}
	
}
