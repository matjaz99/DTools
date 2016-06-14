package si.matjazcerkvenik.dtools.io.statistics;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.knowm.xchart.BitmapEncoder;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYSeries;
import org.knowm.xchart.BitmapEncoder.BitmapFormat;
import org.knowm.xchart.style.markers.SeriesMarkers;

import si.matjazcerkvenik.dtools.context.DProps;
import si.matjazcerkvenik.dtools.context.DToolsContext;
import si.matjazcerkvenik.dtools.tools.ping.PingStatus;

/**
 * Draw timeline of ICMP ping response times.
 * 
 * @author matjaz
 *
 */
public class PingResponseTime2 {
	
	private List<XY> upData = new ArrayList<XY>();
	private List<XY> downData = new ArrayList<XY>();
	
	private int samplingInterval = 60;
	private long now = System.currentTimeMillis();
	
	
	private void prepareDummyData(int history) {
		
		samplingInterval = DProps.getPropertyInt(DProps.NETWORK_MONITORING_PING_INTERVAL);
		int numOfIntervals = history * 3600 / samplingInterval;
//		long now = System.currentTimeMillis();
		long start = now - (history * 3600 * 1000);
		
		for (int i = 0; i < numOfIntervals; i++) {
			
			XY up = new XY();
			up.x = start;
			up.y = 0;
			upData.add(up);
			
			XY dn = new XY();
			dn.x = start;
			dn.y = 0;
			downData.add(dn);
			
			start = start + (samplingInterval * 1000);
			
		}
		
	}
	
	private void parseLog(String logfile, String node, String location, String pingType) {
		
		File f = new File(logfile);
		if (!f.exists()) return;
		
		try {
			FileReader fr = new FileReader(f);
			BufferedReader br = new BufferedReader(fr);
			String line;
			while ((line = br.readLine()) != null) {
				if (line.contains("|" + location + "|")
						&& line.contains("|" + node + "|")
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
					
					PingStatus status = new PingStatus(tempArray[9]);
					
					findInterval(status);
					
				}
				
			}
			br.close();
		} catch (Exception e) {
			DToolsContext.getInstance().getLogger().error("PingResponseTime2:parseLog(): error parsing log: " + e.getMessage());
		}
		
	}
	
	
	private void findInterval(PingStatus status) {
		
		for (int i = 0; i < upData.size(); i++) {
			
			XY xy = upData.get(i);
			long start = xy.x;
			long end = start + (samplingInterval * 1000);
			
			if (status.getStartTime() >= start && status.getStartTime() < end) {
				
				if (status.getErrorCode() == PingStatus.EC_OK) {
					xy.y = (int) (status.getEndTime() - status.getStartTime());
				}
				
			}
			
		}
		
	}
	
	private long recalculateTime(int history, long timeInMillis) {
		
		int time = (int) (timeInMillis - now);
		
		if (history < 5) {
			// return minutes
			time = time / 1000 / 60;
		} else {
			// return hours
			time = time / 1000 / 60 / 60;
		}
		return time;
	}
	
	public void generateChart(String imageId, String node, String location, int history) {
		
		prepareDummyData(history);
		
		parseLog(DToolsContext.HOME_DIR + "/log/ping.log.1", node, location, "ICMP_PING");
		parseLog(DToolsContext.HOME_DIR + "/log/ping.log", node, location, "ICMP_PING");
		
		List<Long> xUpData = new ArrayList<Long>();
		List<Integer> yUpData = new ArrayList<Integer>();
		List<Long> xDnData = new ArrayList<Long>();
		List<Integer> yDnData = new ArrayList<Integer>();
		
		for (int i = 0; i < downData.size(); i++) {
//			xDnData.add(downData.get(i).x / 1000);
			xDnData.add(recalculateTime(history, downData.get(i).x));
			yDnData.add(downData.get(i).y);
		}
		
		for (int i = 0; i < upData.size(); i++) {
//			xUpData.add(upData.get(i).x / 1000);
			xUpData.add(recalculateTime(history, upData.get(i).x));
			yUpData.add(upData.get(i).y);
		}
		
		int width = DProps.getPropertyInt(DProps.NETWORK_STATISTICS_CHART_WIDTH);
		int height = DProps.getPropertyInt(DProps.NETWORK_STATISTICS_CHART_HEIGHT);

		// Create Chart
		XYChart xychart = new XYChart(width, height);
		xychart.setTitle("ICMP Ping Response Time: " + node);
		String s = "hours";
		if (history < 5) s = "minutes";
		xychart.setXAxisTitle("History (" + s + ")");
		xychart.setYAxisTitle("Response time (ms)");
		XYSeries series1 = xychart.addSeries("Up", xUpData, yUpData);
		XYSeries series2 = xychart.addSeries("Down", xDnData, yDnData);
		series1.setMarker(SeriesMarkers.NONE);
		series2.setMarker(SeriesMarkers.NONE);
		
		String tempFileName = DToolsContext.HOME_DIR + "/temp/" + imageId;
		try {
			BitmapEncoder.saveBitmap(xychart, tempFileName, BitmapFormat.PNG);
		} catch (IOException e) {
			DToolsContext.getInstance().getLogger().error("PingResponseTime2:generateChart(): error generating chart: " + e.getMessage());
		}
		
		
	}
	
	private class XY {
		private long x;
		private int y;
	}
	
}
