package si.matjazcerkvenik.dtools.xml;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;

import si.matjazcerkvenik.dtools.io.randomizer.RandomGenerator;

public class CustomMetric implements Serializable {
	
	private static final long serialVersionUID = -5446465109046421826L;
	
	private String name;
	private String help;
	private Double value = 1.0;
	private String type = "GAUGE";
	private List<MetricLabel> labels = new ArrayList<MetricLabel>();;
	private RandomGenerator randomGenerator = new RandomGenerator();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getHelp() {
		return help;
	}

	public void setHelp(String help) {
		this.help = help;
	}

	public Double getValue() {
		value = new Double(randomGenerator.getNextInt());
		return value;
	}

	public void setValue(Double value) {
		this.value = value;
	}

	public String getType() {
		return type;
	}

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

	public void setRandomGenerator(RandomGenerator randomGenerator) {
		this.randomGenerator = randomGenerator;
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
