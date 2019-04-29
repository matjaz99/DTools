package si.matjazcerkvenik.dtools.context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.prometheus.client.Collector;
import io.prometheus.client.Counter;
import io.prometheus.client.Gauge;
import si.matjazcerkvenik.dtools.io.DAO;
import si.matjazcerkvenik.dtools.xml.CustomMetric;

public class DMetricsRegistry {
	
	private Map<String, Collector> registeredMetrics = new HashMap<String, Collector>();
	private List<CustomMetric> registrar = new ArrayList<CustomMetric>();
	
	public DMetricsRegistry() {
		
		for (CustomMetric c : DAO.getInstance().loadCustomMetrics().getMetricsList()) {
			registerMetric(c);
		}
		
	}
	
	
	public void registerMetric(CustomMetric customMetric) {
		
		String key = customMetric.getName();
		
		if (!registeredMetrics.containsKey(key)) {
			System.out.println("Registering: " + key);
			if (customMetric.getType().equals("GAUGE")) {
				Gauge g = Gauge.build()
						.name(customMetric.getName())
						.help(customMetric.getHelp())
						.labelNames(customMetric.getKeysAsArray())
						.register();
				registeredMetrics.put(key, g);
			} else if (customMetric.getType().equals("COUNTER")) {
				Counter c = Counter.build()
						.name(customMetric.getName())
						.help(customMetric.getHelp())
						.labelNames(customMetric.getKeysAsArray())
						.register();
				registeredMetrics.put(key, c);
			}
			
		}
		
		customMetric.setCollector(registeredMetrics.get(key));
		registrar.add(customMetric);
		
	}
	
	public void unregisterMetric(CustomMetric customMetric) {
		registrar.remove(customMetric);
	}
	
	public void simulateMetrics() {
		
		for (CustomMetric m : registrar) {
			m.simulateValue();
		}
		
	}
	
}

