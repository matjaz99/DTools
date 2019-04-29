package si.matjazcerkvenik.dtools.context;

import java.util.List;

import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.Counter;
import io.prometheus.client.Gauge;
import si.matjazcerkvenik.dtools.io.DAO;
import si.matjazcerkvenik.dtools.tools.NetworkLocation;

public class DMetrics {
	
	public static long startTimestamp = System.currentTimeMillis();
	public static CollectorRegistry registry = CollectorRegistry.defaultRegistry;
	public static DMetricsRegistry dMetricsRegistry = new DMetricsRegistry();
	
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
	
	
	
	public static void collectMetrics() {
		
		DMetrics.dtools_build_info.labels("DTools", DToolsContext.version, System.getProperty("os.name"), startTimestamp + "").set(1);
		
		List<NetworkLocation> locs = DAO.getInstance().loadNetworkLocations();
		for (NetworkLocation nl : locs) {
			DMetrics.dtools_monitored_nodes_count.labels(nl.getLocationName()).set(nl.getNetworkNodes().getNodesList().size());
		}
		
		// simulate other metrics
		
		DMetrics.dMetricsRegistry.simulateMetrics();
		
	}
	
	
	
}


