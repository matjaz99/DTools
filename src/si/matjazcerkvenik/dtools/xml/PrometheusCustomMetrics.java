package si.matjazcerkvenik.dtools.xml;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class PrometheusCustomMetrics {
	
	private List<CustomMetric> metricsList;

	public List<CustomMetric> getMetricsList() {
		return metricsList;
	}

	@XmlElement(name="metric")
	public void setMetricsList(List<CustomMetric> metricsList) {
		this.metricsList = metricsList;
	}
	
	public void addMetric(CustomMetric c) {
		if (metricsList == null) {
			metricsList = new ArrayList<CustomMetric>();
		}
		metricsList.add(c);
	}
	
	public void deleteMetric(CustomMetric c) {
		metricsList.remove(c);
	}
	
}
