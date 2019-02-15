package si.matjazcerkvenik.dtools.web.beans;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.Gauge;
import io.prometheus.client.exporter.PushGateway;
import si.matjazcerkvenik.dtools.tools.PromMetric;

@ManagedBean
@SessionScoped
public class PushgatewayBean {

	private String pgAddress = "localhost:9091";
	private String metric = "test_pushgw_java{directory=\"/opt/docker\",hostname=\"this_host\"}";
	private long value = 1234;

	public String getPgAddress() {
		return pgAddress;
	}

	public void setPgAddress(String pgAddress) {
		this.pgAddress = pgAddress;
	}

	public String getMetric() {
		return metric;
	}

	public void setMetric(String metric) {
		this.metric = metric;
	}

	public long getValue() {
		return value;
	}

	public void setValue(long value) {
		this.value = value;
	}

	public void executeBatchJob() throws Exception {
		CollectorRegistry registry = new CollectorRegistry();
		try {
			PromMetric pm = generateMetric();
			String[] k = new String[pm.getLabels().size()];
			String[] v = new String[pm.getLabels().size()];
			int i = 0;
			for (String s : pm.getLabels().keySet()) {
				k[i] = s;
				v[i] = pm.getLabels().get(s);
				i++;
			}
			Gauge g = Gauge.build().name("test_pushgw_java").help("Sending metrics to pushgateway from Java")
					.labelNames(k).register(registry);
			g.labels(v).set(value);
		} finally {
			PushGateway pg = new PushGateway(pgAddress);
			Map<String, String> map = new LinkedHashMap<String, String>();
			map.put("instance", "this_host");
			map.put("label", "abc");
			pg.pushAdd(registry, "my_batch_job", map);
		}
	}

	private PromMetric generateMetric() {
		
		PromMetric m = new PromMetric();

		if (metric.contains("{")) {
			m.setMetricName(metric.split("\\{")[0]);
			String labelsString = metric.substring(metric.indexOf("{") + 1, metric.length() - 1);
			String labelPairs[] = labelsString.split(",");
			Map<String, String> labelsMap = new LinkedHashMap<String, String>();
			for (int i = 0; i < labelPairs.length; i++) {
				labelsMap.put(labelPairs[i].split("=")[0].trim(), labelPairs[i].split("=")[1].replace("\"", "").trim());
			}
			m.setLabels(labelsMap);
		} else {
			m.setMetricName(metric);
		}
		m.setValue(value);
		System.out.println(m.toString());
		return m;
	}

	public void executeBatchJobOriginal() throws Exception {
		CollectorRegistry registry = new CollectorRegistry();
		Gauge duration = Gauge.build().name("my_batch_job_duration_seconds")
				.help("Duration of my batch job in seconds.").register(registry);
		Gauge.Timer durationTimer = duration.startTimer();
		try {
			// Your code here.

			// This is only added to the registry after success,
			// so that a previous success in the Pushgateway isn't overwritten
			// on failure.
			Gauge lastSuccess = Gauge.build().name("my_batch_job_last_success")
					.help("Last time my batch job succeeded, in unixtime.").register(registry);
			lastSuccess.setToCurrentTime();
		} finally {
			durationTimer.setDuration();
			PushGateway pg = new PushGateway("127.0.0.1:9091");
			pg.pushAdd(registry, "my_batch_job");
		}
	}

}
