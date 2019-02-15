package si.matjazcerkvenik.dtools.tools;

import java.util.Map;

public class PromMetric {
	
	private String metricName;
	private Map<String, String> labels;
	private Long value;
	
	public String getMetricName() {
		return metricName;
	}
	public void setMetricName(String metricName) {
		this.metricName = metricName;
	}
	public Map<String, String> getLabels() {
		return labels;
	}
	public void setLabels(Map<String, String> labels) {
		this.labels = labels;
	}
	public Long getValue() {
		return value;
	}
	public void setValue(Long value) {
		this.value = value;
	}
	@Override
	public String toString() {
		return "PromMetric [metricName=" + metricName + ", labels=" + labels + ", value=" + value + "]";
	}
	
}
