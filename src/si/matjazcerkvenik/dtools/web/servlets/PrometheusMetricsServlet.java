package si.matjazcerkvenik.dtools.web.servlets;

import java.io.IOException;
import java.io.Writer;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.exporter.common.TextFormat;
import si.matjazcerkvenik.dtools.context.DMetrics;
import si.matjazcerkvenik.dtools.context.DToolsContext;
import si.matjazcerkvenik.dtools.io.DAO;
import si.matjazcerkvenik.dtools.tools.NetworkLocation;

public class PrometheusMetricsServlet extends HttpServlet {

	private static final long serialVersionUID = -5776148450627134391L;

	private CollectorRegistry registry;

	/**
	 * Construct a MetricsServlet for the given registry.
	 */
	public PrometheusMetricsServlet() {
		this.registry = CollectorRegistry.defaultRegistry;
	}

	@Override
	protected void doGet(final HttpServletRequest req, final HttpServletResponse resp)
			throws ServletException, IOException {
		resp.setStatus(HttpServletResponse.SC_OK);
		resp.setContentType(TextFormat.CONTENT_TYPE_004);
		
		collectMetrics();

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
		
		DMetrics.dtools_build_info.labels(DToolsContext.version, System.getProperty("os.name")).set(1);
		
		List<NetworkLocation> locs = DAO.getInstance().loadNetworkLocations();
		for (NetworkLocation nl : locs) {
			DMetrics.dtools_monitored_nodes_count.labels(nl.getLocationName()).set(nl.getNetworkNodes().getNodesList().size());
		}
		
	}

}
