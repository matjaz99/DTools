package si.matjazcerkvenik.dtools.io.statistics;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.knowm.xchart.BitmapEncoder;
import org.knowm.xchart.BitmapEncoder.BitmapFormat;
import org.knowm.xchart.CategoryChart;
import org.knowm.xchart.CategoryChartBuilder;

import si.matjazcerkvenik.dtools.context.DProps;
import si.matjazcerkvenik.dtools.context.DToolsContext;
import si.matjazcerkvenik.dtools.tools.ping.PingStatus;

/**
 * Draw histogram of ICMP ping response times.
 * 
 * @author matjaz
 *
 */
public class AverageResponseTime {
	
	private List<PingStatus> statusList = new ArrayList<PingStatus>();
	
	private long startTime = 0;
	
	private List<Box> histogramBoxList = new ArrayList<Box>();
	
	
	public void generateChart(String imageId, String node, String location, int history) {
		
		startTime = System.currentTimeMillis() - (history * 3600 * 1000);
		
		// parse log files
		parseLog(DToolsContext.HOME_DIR + "/log/ping.log.1", node, location);
		parseLog(DToolsContext.HOME_DIR + "/log/ping.log", node, location);
		
		// Create boxes
		createBoxes();
		
		// fill boxes
		fillBoxes();
		
		// prepare data for chart
		List<Integer> xData = new ArrayList<Integer>();
		List<Integer> yData = new ArrayList<Integer>();
		
		for (int i = 0; i < histogramBoxList.size(); i++) {
			xData.add((int) histogramBoxList.get(i).time);
			yData.add(histogramBoxList.get(i).count);
		}
		
		int width = DProps.getPropertyInt(DProps.NETWORK_STATISTICS_CHART_WIDTH);
		int height = DProps.getPropertyInt(DProps.NETWORK_STATISTICS_CHART_HEIGHT);

		// Create Chart
		CategoryChart chart = new CategoryChartBuilder().width(width).height(height).title("Response Time Histogram: " + node).xAxisTitle("Mean Response Time (ms)").yAxisTitle("Number of pings").build();

		// Customize Chart
//		chart.getStyler().setSeriesColors(sliceColors);

		// Series
		chart.addSeries("Pings", xData, yData);

		String tempFileName = DToolsContext.HOME_DIR + "/temp/" + imageId;
		try {
			BitmapEncoder.saveBitmap(chart, tempFileName, BitmapFormat.PNG);
		} catch (IOException e) {
			DToolsContext.getInstance().getLogger().error("AverageResponseTime:generateChart(): error generating chart: " + e.getMessage());
		}
	}
	
	private void createBoxes() {
		
		int numOfBoxes = 15;
		int boxWidth = 100;
		int min = 0;
		int average = Math.round(getAverage());
		
		if (average < 20) {
			numOfBoxes = 20;
			boxWidth = 1;
			min = 0;
		} else if (average < 50) {
			boxWidth = 5;
			min = average - (7 * boxWidth);
		} else if (average < 200) {
			boxWidth = 10;
			min = average - (7 * boxWidth);
		} else if (average < 1000) {
			boxWidth = 50;
			min = average - (7 * boxWidth);
		} else if (average < 2500) {
			boxWidth = 100;
			min = average - (7 * boxWidth);
		} else {
			boxWidth = 500;
			min = 0;
		}
		
		// correct min if negative
		if (min < 0) min = 0;
		
		
		System.out.println("min/max/w: " + min + "/" + 9999999 + "/" + boxWidth);
		System.out.println("average: " + getAverage() + "/" + Math.round(getAverage()));
		
		// create empty boxes
		for (int i = 0; i < numOfBoxes; i++) {
			
			Box box = new Box();
			box.id = i;
			box.time = (i * boxWidth) + min;
			box.width = boxWidth;
			
			histogramBoxList.add(box);
			
		}
		
	}
	
	private void fillBoxes() {
		for (int i = 0; i < statusList.size(); i++) {
			findBox(statusList.get(i));
		}
	}
	
	
	private void findBox(PingStatus status) {
		
		for (int i = 0; i < histogramBoxList.size(); i++) {
			
			Box box = histogramBoxList.get(i);
			long start = box.time;
			long end = start + box.width;
			
			int dt = status.getDeltaTime();
			
			if (dt >= start && dt < end) {
				box.count++;
			}
			
		}
		
	}
	
	
	
	private float getAverage() {
		float x = 0.0f;
		int sum = 0;
		for (int i = 0; i < statusList.size(); i++) {
			sum = sum + statusList.get(i).getDeltaTime();
		}
		x = sum / (float) statusList.size();
		return x;
	}
	
	
	private void parseLog(String logfile, String node, String location) {
		
		File f = new File(logfile);
		if (!f.exists()) return;
		
		try {
			FileReader fr = new FileReader(f);
			BufferedReader br = new BufferedReader(fr);
			String line;
			while ((line = br.readLine()) != null) {
				if (line.contains("|" + location + "|")
						&& line.contains("|" + node + "|")
						&& line.contains("|ICMP_PING|")) {
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
					
					if (status.getStartTime() > startTime && status.getErrorCode() == PingStatus.EC_OK) {
						statusList.add(status);
					}
					
				}
				
			}
			br.close();
		} catch (Exception e) {
			DToolsContext.getInstance().getLogger().error("AverageResponseTime:parseLog(): error parsing log: " + e.getMessage());
		}
		
	}
	
	
	
	private class Box {
		
		private int id;
		private long time;
		private int width;
		private int count = 0;
		
		@Override
		public String toString() {
			return "Box[" + id + "]: time=" + time + ", count=" + count;
		}
		
	}
	
}
