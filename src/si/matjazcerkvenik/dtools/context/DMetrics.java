package si.matjazcerkvenik.dtools.context;

import io.prometheus.client.Counter;
import io.prometheus.client.Gauge;

public class DMetrics {
	
	public static final Gauge dtools_build_info = Gauge.build()
			.name("dtools_build_info")
			.help("DToold version")
			.labelNames("version","os")
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
	
}
