package si.matjazcerkvenik.dtools.web.beans;

import java.io.Serializable;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import si.matjazcerkvenik.dtools.context.DProps;
import si.matjazcerkvenik.dtools.io.DAO;
import si.matjazcerkvenik.dtools.io.statistics.AverageResponseTime;
import si.matjazcerkvenik.dtools.io.statistics.NodeAvailability;
import si.matjazcerkvenik.dtools.io.statistics.PingResponseTime;
import si.matjazcerkvenik.dtools.io.statistics.PingResponseTime2;
import si.matjazcerkvenik.dtools.io.statistics.PingResponseTime4;
import si.matjazcerkvenik.dtools.tools.NetworkLocation;
import si.matjazcerkvenik.dtools.xml.Node;

@ManagedBean
@ViewScoped
public class NodeStatBean implements Serializable {
	
	private static final long serialVersionUID = -6814048466553906755L;
	
	private Node node;
	private NetworkLocation networkLocation;
	
	private String imageId = "0";
	
	private String chartType = "Availability";
	private int showLastHours = 24;
	
	@PostConstruct
	public void init() {
		Map<String, String> requestParameterMap = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		if (requestParameterMap.containsKey("location")) {
			networkLocation = DAO.getInstance().findNetworkLocation(requestParameterMap.get("location"));
		}
		if (requestParameterMap.containsKey("nodeName")) {
			node = networkLocation.findNode(requestParameterMap.get("nodeName"));
		}
		showLastHours = DProps.getPropertyInt(DProps.NETWORK_STATISTICS_HISTORY_HOURS);
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
		
		if (chartType.equals("Availability")) {
			
			NodeAvailability nas = new NodeAvailability();
			nas.generateChart(imageId, node.getName(), networkLocation.getLocationName(), showLastHours);
			
		} else if (chartType.equals("Response Time")) {
			
//			PingResponseTime stat = new PingResponseTime();
//			stat.generateChart(imageId, node.getName(), networkLocation.getLocationName(), showLastHours);
			PingResponseTime2 stat = new PingResponseTime2();
			stat.generateChart(imageId, node.getName(), networkLocation.getLocationName(), showLastHours);
			
		} else if (chartType.equals("Response Time 2")) {
			
			PingResponseTime2 stat = new PingResponseTime2();
			stat.generateChart(imageId, node.getName(), networkLocation.getLocationName(), showLastHours);
			
		} else if (chartType.equals("Response Time 4")) {
			
			PingResponseTime4 stat = new PingResponseTime4();
			stat.generateChart(imageId, node.getName(), networkLocation.getLocationName(), showLastHours);
			
		} else if (chartType.equals("Average Response Time")) {
			
			AverageResponseTime stat = new AverageResponseTime();
			stat.generateChart(imageId, node.getName(), networkLocation.getLocationName(), showLastHours);
			
		}
		
	}
	
	
}
