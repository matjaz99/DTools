package si.matjazcerkvenik.dtools.web.beans;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.faces.application.FacesMessage;
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
	private String job = "test_job";
	private String instance = "this_host";
	private String label;
	private String metric = "test_pushgw_java{directory=\"/opt/docker\",hostname=\"this_host\"}";
	private long value = 100;
	
	private String curlCli;

	public String getPgAddress() {
		return pgAddress;
	}

	public void setPgAddress(String pgAddress) {
		this.pgAddress = pgAddress;
	}

	public String getJob() {
		return job;
	}

	public void setJob(String job) {
		this.job = job;
	}

	public String getInstance() {
		return instance;
	}

	public void setInstance(String instance) {
		this.instance = instance;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
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

	public String getCurlCli() {
		return curlCli;
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
			Gauge g = Gauge.build().name(pm.getMetricName()).help("Sending metrics to pushgateway from Java")
					.labelNames(k).register(registry);
			g.labels(v).set(value);
			
			// generate curl cli command
			curlCli = "echo \"" + pm.getMetricName();
			if (pm.getLabels() != null) {
				curlCli += "{";
				for (String s : pm.getLabels().keySet()) {
					curlCli += s + "=\\\"" + pm.getLabels().get(s) + "\\\", ";
				}
				curlCli = curlCli.substring(0, curlCli.length()-2);
				curlCli += "}";
			}
			curlCli += " " + value + "\"";
			curlCli += " | curl --data-binary @- http://" + pgAddress + "/metrics/job/" + job + "/instance/" + instance;
			System.out.println("curlcli: " + curlCli);			
			
		} finally {
			PushGateway pg = new PushGateway(pgAddress);
			Map<String, String> map = new LinkedHashMap<String, String>();
			if (instance != null && !instance.isEmpty()) {
				map.put("instance", instance);
			}
			if (label != null && !label.isEmpty()) {
				map.put("label", label);
			}
			pg.pushAdd(registry, job, map);
		}
		Growl.addGrowlMessage("Metric pushed");
		
		
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
