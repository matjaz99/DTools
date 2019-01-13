package si.matjazcerkvenik.dtools.xml;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="grafanaPanels")
public class GrafanaPanels {
	
	private List<GrafanaPanel> panels;

	public List<GrafanaPanel> getPanels() {
		return panels;
	}

	@XmlElement(name="iframe")
	public void setPanels(List<GrafanaPanel> panels) {
		this.panels = panels;
	}
	
}
