package si.matjazcerkvenik.dtools.xml;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class SnmpTraps {
	
	private List<SnmpTrap> traps;

	public List<SnmpTrap> getTraps() {
		return traps;
	}

	@XmlElement(name = "trap")
	public void setTraps(List<SnmpTrap> traps) {
		this.traps = traps;
	}
	
}
