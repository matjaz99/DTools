package si.matjazcerkvenik.dtools.context;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.prometheus.client.Collector;
import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.Counter;
import io.prometheus.client.Gauge;
import si.matjazcerkvenik.dtools.io.DAO;
import si.matjazcerkvenik.dtools.tools.NetworkLocation;
import si.matjazcerkvenik.dtools.xml.CustomMetric;

public class DMetrics {
	
	public static final Gauge dtools_build_info = Gauge.build()
			.name("dtools_build_info")
			.help("DTools version")
			.labelNames("app", "version","os","starttime")
			.register();
	
	public static final Gauge dtools_monitored_nodes_count = Gauge.build()
			.name("dtools_monitored_nodes_count")
			.help("Number of monitored nodes")
			.labelNames("networkLocation")
			.register();
	
	public static final Counter dtools_webhook_messages_received_total = Counter.build()
			.name("dtools_webhook_messages_received_total")
			.help("Number of received webhook messages.")
			.labelNames("remoteHost", "method")
			.register();
	
	public static final Counter dtools_am_alerts_received_total = Counter.build()
			.name("dtools_am_alerts_received_total")
			.help("Number of received alerts from alertmanager.")
			.labelNames("remoteHost", "alerttype", "severity")
			.register();
	
	public static final Counter dtools_ssh_commands_executed_total = Counter.build()
			.name("dtools_ssh_commands_executed_total")
			.help("Number of commands executed by SSH clients.")
			.labelNames("remoteHost", "status")
			.register();
	
	private static Map<String, Collector> simulatedMetrics = new HashMap<String, Collector>();
	public static long startTimestamp = System.currentTimeMillis();
	public static CollectorRegistry registry = CollectorRegistry.defaultRegistry;
	
	public static void registerMetric(CustomMetric m) {
		if (simulatedMetrics.containsKey(m.getName())) {
			return;
		}
		if (m.getType().equals("GAUGE")) {
			Gauge g = Gauge.build()
					.name(m.getName())
					.help(m.getHelp())
					.labelNames(m.getKeysAsArray())
					.register();
			simulatedMetrics.put(m.getName(), g);
		} else if (m.getType().equals("COUNTER")) {
			Counter c = Counter.build()
					.name(m.getName())
					.help(m.getHelp())
					.labelNames(m.getKeysAsArray())
					.register();
			simulatedMetrics.put(m.getName(), c);
		}
	}
	
	public static void collectMetrics() {
		
		DMetrics.dtools_build_info.labels("DTools", DToolsContext.version, System.getProperty("os.name"), startTimestamp + "").set(1);
		
		List<NetworkLocation> locs = DAO.getInstance().loadNetworkLocations();
		for (NetworkLocation nl : locs) {
			DMetrics.dtools_monitored_nodes_count.labels(nl.getLocationName()).set(nl.getNetworkNodes().getNodesList().size());
		}
		
		// simulate other metrics
		
		if (simulatedMetrics.isEmpty()) {
			for (CustomMetric c : DAO.getInstance().loadCustomMetrics().getMetricsList()) {
				DMetrics.registerMetric(c);
			}
		}
		
		for (CustomMetric m : DAO.getInstance().loadCustomMetrics().getMetricsList()) {
			if (m.getType().equals("GAUGE")) {
				Gauge g = (Gauge) simulatedMetrics.get(m.getName());
				g.labels(m.getValuesAsArray()).set(m.simulateValue());
			} else if (m.getType().equals("COUNTER")) {
				Counter c = (Counter) simulatedMetrics.get(m.getName());
				c.labels(m.getValuesAsArray()).inc(Math.abs(m.simulateValue()));
			}
		}
		
	}
	
}
