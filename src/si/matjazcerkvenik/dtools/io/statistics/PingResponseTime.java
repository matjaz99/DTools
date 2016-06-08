package si.matjazcerkvenik.dtools.io.statistics;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.knowm.xchart.BitmapEncoder;
import org.knowm.xchart.BitmapEncoder.BitmapFormat;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYSeries;
import org.knowm.xchart.style.markers.SeriesMarkers;

import si.matjazcerkvenik.dtools.context.DToolsContext;
import si.matjazcerkvenik.dtools.tools.ping.PingStatus;


/**
 * Simple chart that connects all ping statuses in selected time interval, 
 * regardless of sampling interval.
 * 
 * @author matjaz
 *
 */
public class PingResponseTime {
		
	private List<PingStatus> pingStatuses = new ArrayList<PingStatus>();
	
	
	public PingResponseTime() {
	}
	

	public void generateChart(String imageId, String node, String location, int history) {
		
		pingStatuses.clear();
		
		parseLog(DToolsContext.HOME_DIR + "/log/ping.log.1", node, location, history, "ICMP_PING");
		parseLog(DToolsContext.HOME_DIR + "/log/ping.log", node, location, history, "ICMP_PING");
		
		List<Integer> xData = new ArrayList<Integer>();
		List<Integer> yData = new ArrayList<Integer>();
		
		for (int i = 0; i < pingStatuses.size(); i++) {
			xData.add((int) (pingStatuses.get(i).getStartTime() / 1000));
			if (pingStatuses.get(i).getErrorCode() == PingStatus.EC_OK) {
				yData.add((int) (pingStatuses.get(i).getEndTime() - pingStatuses.get(i).getStartTime()));
			} else {
				yData.add(0);
			}
			
		}
		
		if (xData.isEmpty()) {
			xData.add(0);
		}
		if (yData.isEmpty()) {
			yData.add(0);
		}

		// Create Chart
		XYChart xychart = new XYChart(700, 400);
		xychart.setTitle("ICMP Ping response time: " + node);
		xychart.setXAxisTitle("Time (sec)");
		xychart.setYAxisTitle("Response Time (ms)");
		XYSeries series = xychart.addSeries("Data", xData, yData);
		series.setMarker(SeriesMarkers.NONE);
		
		String tempFileName = DToolsContext.HOME_DIR + "/temp/" + imageId;
		try {
			BitmapEncoder.saveBitmap(xychart, tempFileName, BitmapFormat.PNG);
		} catch (IOException e) {
			DToolsContext.getInstance().getLogger().error("PingResponseTime:generateChart(): error generating chart: " + e.getMessage());
		}
		
	}
	
	
	private void parseLog(String logfile, String node, String location, int historyHours, String pingType) {
		
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
					
					long before = (System.currentTimeMillis()) - (historyHours * 3600 * 1000);
					if (status.getStartTime() > before) {
						pingStatuses.add(status);
					}
					
				}
				
			}
			br.close();
		} catch (Exception e) {
			DToolsContext.getInstance().getLogger().error("PingResponseTime:parseLog(): error parsing log: " + e.getMessage());
		}
		
	}
	
	
}
