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

import si.matjazcerkvenik.dtools.tools.ping.PingStatus;

public class PingResponseTime3 {
	
	private List<Serija> listOfSeries = new ArrayList<Serija>();
	private int samplingInterval = 60;
	private long endTime = 0;
	private long startTime = 0;
	private List<PingStatus> pingStatusList = new ArrayList<PingStatus>();
	
	private List<XY> upData = new ArrayList<XY>();
	private List<XY> downData = new ArrayList<XY>();
	
	
	public static void main(String[] args) {
		
		PingResponseTime3 s = new PingResponseTime3();
		s.init(3);
		s.parseLog("/Users/matjaz/Developer/Projekti/DTools/log/ping.log", "Node #105", "aaa", "ICMP_PING");
		s.generateChart();
		
	}
	
	private void init(int history) {
		
		endTime = System.currentTimeMillis();
		startTime = endTime - (history * 3600 * 1000);
		
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
					
					if (status.getStartTime() > startTime) {
						pingStatusList.add(status);
					}
					
				}
				
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println("pingStatusList contains " + pingStatusList.size() + " statuses");
		
	}
	
	
	private void organizeStatuses() {
		
//		Serija serija = new Serija();
//		
//		for (int i = 0; i < pingStatusList.size(); i++) {
//			
//			PingStatus status = pingStatusList.get(i);
//			
//			if (status.getErrorCode() == 0) {
//				
//			} else {
//				
//			}
//			
//		}
		
		
		
		
	}
	
	
	
	
	public void generateChart() {
		
		List<Long> xUpData = new ArrayList<Long>();
		List<Integer> yUpData = new ArrayList<Integer>();
		List<Long> xDnData = new ArrayList<Long>();
		List<Integer> yDnData = new ArrayList<Integer>();
		
		for (int i = 0; i < downData.size(); i++) {
			xDnData.add(downData.get(i).x);
			yDnData.add(downData.get(i).y);
		}
		
		for (int i = 0; i < upData.size(); i++) {
			xUpData.add(upData.get(i).x);
			yUpData.add(upData.get(i).y);
		}
		

		// Create Chart
		XYChart xychart = new XYChart(700, 400);
		xychart.setTitle("Chart");
		xychart.setXAxisTitle("Time");
		xychart.setYAxisTitle("Response time");
		XYSeries series1 = xychart.addSeries("Up", xUpData, yUpData);
		XYSeries series2 = xychart.addSeries("Down", xDnData, yDnData);
		series1.setMarker(SeriesMarkers.NONE);
		series2.setMarker(SeriesMarkers.NONE);
		
		String imageId = "" + System.currentTimeMillis();
		String tempFileName = "/Users/matjaz/Desktop/" + imageId;
		try {
			BitmapEncoder.saveBitmap(xychart, tempFileName, BitmapFormat.PNG);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
	}
	
	private class Serija {
		
		private List<XY> series = new ArrayList<XY>();
		private boolean isUpSerija = false;
		
	}
	
	private class XY {
		private long x;
		private int y;
	}
	
}
