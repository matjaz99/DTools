package si.matjazcerkvenik.dtools.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYSeries;
import org.knowm.xchart.style.markers.SeriesMarkers;

import si.matjazcerkvenik.dtools.context.DToolsContext;

public class PingResponseTimeStatistics {
	
	private StatFilter filter;
	
	private List<XY> xyDataList = new ArrayList<XY>();
	
	
	public PingResponseTimeStatistics(StatFilter filter) {
		this.filter = filter;
	}
	

	public XYChart generateXYChart() {
		
		xyDataList.clear();
		
		parseLog(DToolsContext.HOME_DIR + "/log/ping.log.1", filter.getHistoryHours(), filter.getPingType());
		parseLog(DToolsContext.HOME_DIR + "/log/ping.log", filter.getHistoryHours(), filter.getPingType());
		
		List<Integer> xData = new ArrayList<Integer>();
		List<Integer> yData = new ArrayList<Integer>();
		
		for (int i = 0; i < xyDataList.size(); i++) {
			xData.add(xyDataList.get(i).getX());
			yData.add(xyDataList.get(i).getY());
		}
		
		if (xData.isEmpty()) {
			xData.add(0);
		}
		if (yData.isEmpty()) {
			yData.add(0);
		}

		// Create Chart
		XYChart xychart = new XYChart(filter.getWidth(), filter.getHeight());
		xychart.setTitle(filter.getTitle() + ": " + filter.getNodeName());
		xychart.setXAxisTitle(filter.getxAxisTitle());
		xychart.setYAxisTitle(filter.getyAxisTitle());
		XYSeries series = xychart.addSeries("Data", xData, yData);
		series.setMarker(SeriesMarkers.NONE);
		
		return xychart;
		
	}
	
	
	private void parseLog(String logfile, int historyHours, String pingType) {
		
		File f = new File(logfile);
		if (!f.exists()) {
			return;
		}
		
		try {
			FileReader fr = new FileReader(f);
			BufferedReader br = new BufferedReader(fr);
			String line;
			while ((line = br.readLine()) != null) {
				if (line.contains("|" + filter.getLocationName() + "|")
						&& line.contains("|" + filter.getNodeName() + "|")
						&& line.contains("|" + pingType + "|")) {
					String[] tempArray = line.split("\\|");
					// tempArray[0];  // date
					// tempArray[1];  // pool thread name
					// tempArray[2];  // ping type
					// tempArray[3];  // location
					// tempArray[4];  // node name
					// tempArray[5];  // node IP
					// tempArray[6];  // reserved
					// tempArray[7];  // reserved
					// tempArray[8];  // reserved
					// tempArray[9];  // ping status
					
					tempArray[9] = tempArray[9].replace("[", "").replace("]", "");
					String[] array = tempArray[9].split(",");
					XY xyData = new XY();
					
					for (int i = 0; i < array.length; i++) {
						String[] temp = array[i].trim().split("=");
						
						if (temp[0].equals("eC")) {
							
						} else if (temp[0].equals("eM")) {
							
						} else if (temp[0].equals("eD")) {
							
						} else if (temp[0].equals("sT")) {
							long l = Long.parseLong(temp[1]);
							xyData.setX((int)(l/1000));
						} else if (temp[0].equals("eT")) {
							
						} else if (temp[0].equals("dT")) {
							xyData.setY(Integer.parseInt(temp[1]));
						}
					}
					
					int before = (int) ((System.currentTimeMillis() / 1000) - (historyHours * 3600));
					if (xyData.getX() > before) {
						xyDataList.add(xyData);
//						System.out.println("x: " + xyData.getX() + " y: " + xyData.getY());
					}
					
				}
				
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	
}
