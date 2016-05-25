package si.matjazcerkvenik.dtools.web.beans;

import java.io.IOException;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.knowm.xchart.BitmapEncoder;
import org.knowm.xchart.BitmapEncoder.BitmapFormat;
import org.knowm.xchart.internal.chartpart.Chart;

import si.matjazcerkvenik.dtools.context.DToolsContext;
import si.matjazcerkvenik.dtools.io.DAO;
import si.matjazcerkvenik.dtools.io.NodeAvailabilityStatistics;
import si.matjazcerkvenik.dtools.io.PingResponseTimeStatistics;
import si.matjazcerkvenik.dtools.io.StatFilter;
import si.matjazcerkvenik.dtools.tools.NetworkLocation;
import si.matjazcerkvenik.dtools.xml.Node;

@ManagedBean
@ViewScoped
public class NodeStatBean {
	
	private Node node;
	private NetworkLocation networkLocation;
	
	private String imageId = "0";
	
	private String chartType = "Availability";
	private int showLastHours = 3;
	
	@PostConstruct
	public void init() {
		Map<String, String> requestParameterMap = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		if (requestParameterMap.containsKey("location")) {
			networkLocation = DAO.getInstance().findNetworkLocation(requestParameterMap.get("location"));
		}
		if (requestParameterMap.containsKey("nodeName")) {
			node = networkLocation.findNode(requestParameterMap.get("nodeName"));
		}
		System.out.println("init NodeStatBean: node=" + node.getName() + ", loc=" + networkLocation.getLocationName());
	}

	public Node getNode() {
		return node;
	}

	public void setNode(Node node) {
		this.node = node;
	}

	public NetworkLocation getNetworkLocation() {
		return networkLocation;
	}

	public void setNetworkLocation(NetworkLocation networkLocation) {
		this.networkLocation = networkLocation;
	}

	public int getShowLastHours() {
		return showLastHours;
	}

	public void setShowLastHours(int showLastHours) {
		this.showLastHours = showLastHours;
	}
	
	public String getImageId() {
		return imageId;
	}

	public void setImageId(String imageId) {
		this.imageId = imageId;
	}

	public String getChartType() {
		return chartType;
	}

	public void setChartType(String chartType) {
		this.chartType = chartType;
	}
	
	
	
	public void chartTypeChanged() {
		
		if (chartType.equals("Availability")) {
			// TODO
		} else if (chartType.equals("Response Time")) {
			// TODO
		}
		
	}
	
	

	public void doRenderAction(ActionEvent actionEvent) {
		
		imageId = "" + System.currentTimeMillis();
		String tempFileName = DToolsContext.HOME_DIR + "/temp/" + imageId;
		
		StatFilter filter = new StatFilter();
		filter.setNodeName(node.getName());
		filter.setLocationName(networkLocation.getLocationName());
		filter.setHistoryHours(showLastHours);
		
		
		Chart<?, ?> chart = null;
		
		if (chartType.equals("Availability")) {
			
			NodeAvailabilityStatistics nas = new NodeAvailabilityStatistics();
			chart = nas.getChart(node.getName(), networkLocation.getLocationName(), showLastHours);
			
		} else if (chartType.equals("Response Time")) {
			
			filter.setTitle("ICMP Ping response time");
			filter.setxAxisTitle("Time (sec)");
			filter.setyAxisTitle("Response Time (ms)");
			
			PingResponseTimeStatistics stat = new PingResponseTimeStatistics(filter);
			chart = stat.generateXYChart();
			
		}

		try {
			BitmapEncoder.saveBitmap(chart, tempFileName, BitmapFormat.PNG);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
}
