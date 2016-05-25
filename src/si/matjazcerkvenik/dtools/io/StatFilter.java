package si.matjazcerkvenik.dtools.io;

public class StatFilter {
	
	private String nodeName;
	private String locationName;
	
	private int width = 700;
	private int height = 400;
	private String title = "Chart";
	private String xAxisTitle = "x";
	private String yAxisTitle = "y";
	
	private int historyHours = 3;
	private String pingType = "ICMP_PING";
	
	
	public String getNodeName() {
		return nodeName;
	}
	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}
	public String getLocationName() {
		return locationName;
	}
	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public int getHeight() {
		return height;
	}
	public void setHeight(int height) {
		this.height = height;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getxAxisTitle() {
		return xAxisTitle;
	}
	public void setxAxisTitle(String xAxisTitle) {
		this.xAxisTitle = xAxisTitle;
	}
	public String getyAxisTitle() {
		return yAxisTitle;
	}
	public void setyAxisTitle(String yAxisTitle) {
		this.yAxisTitle = yAxisTitle;
	}
	public int getHistoryHours() {
		return historyHours;
	}
	public void setHistoryHours(int historyHours) {
		this.historyHours = historyHours;
	}
	public String getPingType() {
		return pingType;
	}
	public void setPingType(String pingType) {
		this.pingType = pingType;
	}
	
}
