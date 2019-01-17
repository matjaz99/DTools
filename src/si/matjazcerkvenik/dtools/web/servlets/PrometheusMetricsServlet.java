package si.matjazcerkvenik.dtools.web.servlets;

import java.io.IOException;
import java.io.Writer;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.Counter;
import io.prometheus.client.Gauge;
import io.prometheus.client.exporter.common.TextFormat;
import si.matjazcerkvenik.dtools.context.DMetrics;
import si.matjazcerkvenik.dtools.context.DToolsContext;
import si.matjazcerkvenik.dtools.io.DAO;
import si.matjazcerkvenik.dtools.tools.NetworkLocation;

public class PrometheusMetricsServlet extends HttpServlet {

	private static final long serialVersionUID = -5776148450627134391L;
	
	private long startTimestamp = 0;

	private CollectorRegistry registry;

	/**
	 * Construct a MetricsServlet for the given registry.
	 */
	public PrometheusMetricsServlet() {
		this.registry = CollectorRegistry.defaultRegistry;
		startTimestamp = System.currentTimeMillis();
	}

	@Override
	protected void doGet(final HttpServletRequest req, final HttpServletResponse resp)
			throws ServletException, IOException {
		resp.setStatus(HttpServletResponse.SC_OK);
		resp.setContentType(TextFormat.CONTENT_TYPE_004);
		
		collectMetrics();
		simulateMetrics();

		Writer writer = resp.getWriter();
		try {
			TextFormat.write004(writer, registry.filteredMetricFamilySamples(parse(req)));
			writer.flush();
		} finally {
			writer.close();
		}
	}

	private Set<String> parse(HttpServletRequest req) {
		String[] includedParam = req.getParameterValues("name[]");
		if (includedParam == null) {
			return Collections.emptySet();
		} else {
			return new HashSet<String>(Arrays.asList(includedParam));
		}
	}

	@Override
	protected void doPost(final HttpServletRequest req, final HttpServletResponse resp)
			throws ServletException, IOException {
		doGet(req, resp);
	}
	
	private void collectMetrics() {
		
		DMetrics.dtools_build_info.labels(DToolsContext.version, System.getProperty("os.name"), startTimestamp + "").set(1);
		
		List<NetworkLocation> locs = DAO.getInstance().loadNetworkLocations();
		for (NetworkLocation nl : locs) {
			DMetrics.dtools_monitored_nodes_count.labels(nl.getLocationName()).set(nl.getNetworkNodes().getNodesList().size());
		}
		
	}
	
	
	
	/* Metrics simulator */
	
	
	public static final Gauge temperature = Gauge.build()
			.name("test_temperature_current")
			.help("Current temperature.")
			.register();
	
	public static final Gauge tempCity = Gauge.build()
			.name("test_temperature_by_city_current")
			.help("Current temperature in city.")
			.labelNames("city", "country")
			.register();
	
	public static final Gauge customers = Gauge.build()
			.name("test_customers_current")
			.help("Current customers in city.")
			.labelNames("shop", "country")
			.register();
	
	public static final Counter payment = Counter.build()
			.name("test_customers_payment_total")
			.help("Payment.")
			.labelNames("shop", "country")
			.register();
	
	static {
		
		temperature.set(20);
		
		tempCity.labels("London", "England").set(10);
		tempCity.labels("Bristol", "England").set(15);
		tempCity.labels("Liverpool", "England").set(20);
		tempCity.labels("Helsinki", "Finland").set(5);
		tempCity.labels("Dubai", "Uae").set(35);
		tempCity.labels("Paris", "France").set(20);
		tempCity.labels("Toulouse", "France").set(20);
		
		customers.labels("Ikea", "Austria").set(10);
		customers.labels("Ikea", "Germany").set(12);
		customers.labels("Ikea", "France").set(7);
		customers.labels("Hofer", "Austria").set(8);
		customers.labels("Hofer", "Germany").set(7);
		customers.labels("Hofer", "France").set(9);
		customers.labels("Spar", "Austria").set(7);
		customers.labels("Spar", "Germany").set(8);
		customers.labels("Spar", "France").set(9);
		
	}
	
	private void simulateMetrics() {
		
		temperature.set(getNextValue(new Double(temperature.get()).intValue(), -10, 100, 6));
		
		tempCity.labels("London", "England").set(getNextValue(new Double(tempCity.labels("London", "England").get()).intValue(), 0, 35, 10));
		tempCity.labels("Bristol", "England").set(getNextValue(new Double(tempCity.labels("London", "England").get()).intValue(), 0, 35, 10));
		tempCity.labels("Liverpool", "England").set(getNextValue(new Double(tempCity.labels("London", "England").get()).intValue(), 0, 35, 10));
		tempCity.labels("Helsinki", "Finland").set(getNextValue(new Double(tempCity.labels("Helsinki", "Finland").get()).intValue(), -10, 25, 5));
		tempCity.labels("Dubai", "Uae").set(getNextValue(new Double(tempCity.labels("Dubai", "Uae").get()).intValue(), 20, 50, 5));
		tempCity.labels("Paris", "France").set(getNextValue(new Double(tempCity.labels("Paris", "France").get()).intValue(), 10, 35, 7));
		tempCity.labels("Toulouse", "France").set(getNextValue(new Double(tempCity.labels("Paris", "France").get()).intValue(), 10, 35, 7));
		
		
		int ia = getNextValue(new Double(customers.labels("Ikea", "Austria").get()).intValue(), 0, 20, 2);
		int ig = getNextValue(new Double(customers.labels("Ikea", "Germany").get()).intValue(), 0, 20, 2);
		int ifr = getNextValue(new Double(customers.labels("Ikea", "France").get()).intValue(), 0, 20, 2);
		int ha = getNextValue(new Double(customers.labels("Hofer", "Austria").get()).intValue(), 0, 20, 2);
		int hg = getNextValue(new Double(customers.labels("Hofer", "Germany").get()).intValue(), 0, 20, 2);
		int hf = getNextValue(new Double(customers.labels("Hofer", "France").get()).intValue(), 0, 20, 2);
		int sa = getNextValue(new Double(customers.labels("Spar", "Austria").get()).intValue(), 0, 20, 2);
		int sg = getNextValue(new Double(customers.labels("Spar", "Germany").get()).intValue(), 0, 20, 2);
		int sf = getNextValue(new Double(customers.labels("Spar", "France").get()).intValue(), 0, 20, 2);
		customers.labels("Ikea", "Austria").set(ia);
		customers.labels("Ikea", "Germany").set(ig);
		customers.labels("Ikea", "France").set(ifr);
		customers.labels("Hofer", "Austria").set(ha);
		customers.labels("Hofer", "Germany").set(hg);
		customers.labels("Hofer", "France").set(hf);
		customers.labels("Spar", "Austria").set(sa);
		customers.labels("Spar", "Germany").set(sg);
		customers.labels("Spar", "France").set(sf);
		
		payment.labels("Ikea", "Austria").inc(getRandom(0, 10 * ia));
		payment.labels("Ikea", "Germany").inc(getRandom(0, 11 * ig));
		payment.labels("Ikea", "France").inc(getRandom(0, 8 * ifr));
		payment.labels("Hofer", "Austria").inc(getRandom(0, 4 * ha));
		payment.labels("Hofer", "Germany").inc(getRandom(0, 4 * hg));
		payment.labels("Hofer", "France").inc(getRandom(0, 5 * hf));
		payment.labels("Spar", "Austria").inc(getRandom(0, 6 * sa));
		payment.labels("Spar", "Germany").inc(getRandom(0, 5 * sg));
		payment.labels("Spar", "France").inc(getRandom(0, 7 * sf));
		
	}
	
	private int getRandom(int inclusive, int exclusive) {
		if (inclusive >= exclusive || exclusive < 0) {
			return 0;
		}
		Random rand = new Random();
		return rand.nextInt(exclusive - inclusive) + inclusive;
	}
	
	/**
	 * Generate next value based on current value +/- delta.
	 * Delta is random value, but not bigger than maxDeviation.
	 * Value cannot be bigger than maxValue and not less than 0.
	 * @param currentValue
	 * @param maxValue
	 * @param maxDeviation
	 * @return
	 */
	private int getNextValue(int currentValue, int minValue, int maxValue, int maxDeviation) {
		
		if (minValue >= maxValue) {
			return 0;
		}
		
		Random rand = new Random();
		
		int dev = rand.nextInt(maxDeviation);
		
		if (rand.nextBoolean()) {
			currentValue = currentValue + dev;
		} else {
			currentValue = currentValue - dev;
		}
		
		if (currentValue > maxValue) {
			currentValue = maxValue;
		}
		if (currentValue < minValue) {
			currentValue = minValue;
		}
		
		return currentValue;
		
	}
	

}
