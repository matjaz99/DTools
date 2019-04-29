package si.matjazcerkvenik.dtools.xml;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;

import io.prometheus.client.Collector;
import io.prometheus.client.Counter;
import io.prometheus.client.Gauge;
import si.matjazcerkvenik.dtools.io.randomizer.RandomGenerator;

public class CustomMetric implements Serializable {
	
	private static final long serialVersionUID = -5446465109046421826L;
	
	private String name;
	private String help;
	private Double currentValue = 20.0;
	private String type = "GAUGE";
	private List<MetricLabel> labels = new ArrayList<MetricLabel>();
	private RandomGenerator randomGenerator = new RandomGenerator();
	private Collector collector = null;
	

	public String getName() {
		return name;
	}

	@XmlElement
	public void setName(String name) {
		this.name = name;
	}

	public String getHelp() {
		return help;
	}

	@XmlElement
	public void setHelp(String help) {
		this.help = help;
	}
	
	public Double simulateValue() {
		if (type.equals("GAUGE")) {
			currentValue = new Double(randomGenerator.getNextInt());
			Gauge g = (Gauge) collector;
			g.labels(getValuesAsArray()).set(currentValue);
		} else if (type.equals("COUNTER")) {
			currentValue = currentValue + RandomGenerator.abs(new Double(randomGenerator.getNextInt()));
			Counter c = (Counter) collector;
			c.labels(getValuesAsArray()).inc(currentValue);
		}
		return currentValue;
	}

	public Double getCurrentValue() {
		return currentValue;
	}

	@XmlTransient
	public void setCurrentValue(Double currentValue) {
		this.currentValue = currentValue;
	}

	public String getType() {
		return type;
	}

	@XmlElement
	public void setType(String type) {
		this.type = type;
	}

	public List<MetricLabel> getLabels() {
		return labels;
	}

	@XmlElement(name = "label")
	public void setLabels(List<MetricLabel> labels) {
		this.labels = labels;
	}

	public RandomGenerator getRandomGenerator() {
		return randomGenerator;
	}

	@XmlElement
	public void setRandomGenerator(RandomGenerator randomGenerator) {
		this.randomGenerator = randomGenerator;
	}

	public Collector getCollector() {
		return collector;
	}

	@XmlTransient
	public void setCollector(Collector collector) {
		this.collector = collector;
	}

	public void addLabel(MetricLabel label) {
		labels.add(label);
	}

	public void removeLabel(MetricLabel label) {
		labels.remove(label);
	}
	
	public String getLabelValue(String key) {
		for (MetricLabel l : labels) {
			if (l.getKey().equals(key)) {
				return l.getValue();
			}
		}
		return null;
	}
	
	public String[] getKeysAsArray() {
		String[] keys = new String[labels.size()];
		for (int i = 0; i < labels.size(); i++) {
			keys[i] = labels.get(i).getKey();
		}
		return keys;
	}
	
	public String[] getValuesAsArray() {
		String[] values = new String[labels.size()];
		for (int i = 0; i < labels.size(); i++) {
			values[i] = labels.get(i).getValue();
		}
		return values;
	}

	public String toPromString() {
		String s = name;
		if (labels != null && !labels.isEmpty()) {
			s += "{";
			for (MetricLabel lbl : labels) {
				s += lbl.getKey() + "=\"" + lbl.getValue() + "\", ";
			}
			s += "}";
		}
		return s;
	}

}
