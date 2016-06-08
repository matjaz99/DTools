package si.matjazcerkvenik.dtools.io.statistics;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.knowm.xchart.BitmapEncoder;
import org.knowm.xchart.PieChart;
import org.knowm.xchart.PieChartBuilder;
import org.knowm.xchart.BitmapEncoder.BitmapFormat;

import si.matjazcerkvenik.dtools.context.DProps;
import si.matjazcerkvenik.dtools.context.DToolsContext;
import si.matjazcerkvenik.dtools.tools.ping.PingStatus;

/**
 * This class calculates node availability in the past N hours and generates a pie chart.
 * 
 * @author matjaz
 *
 */
public class NodeAvailability {
	
	private List<Box> boxList = new ArrayList<Box>();
	
	private int samplingInterval = 60;
	

	/**
	 * Calculate ICMP availability of node and generate pie chart.
	 * @param imageId
	 * @param node
	 * @param location
	 * @param history
	 */
	public void generateChart(String imageId, String node, String location, int history) {
		
		boxList.clear();
		
		// Create empty boxes
		createEmptyBoxes(history);
		
		// parse log files
		parseLog(DToolsContext.HOME_DIR + "/log/ping.log.1", node, location);
		parseLog(DToolsContext.HOME_DIR + "/log/ping.log", node, location);
		
		int upTime = calculateUpTime();
		int downTime = 100 - upTime;
		

		// Create Chart
		PieChart chart = new PieChartBuilder().width(700).height(400).title("Node availability: " + node).build();

		// Customize Chart
		Color[] sliceColors = new Color[] { new Color(68, 224, 14), new Color(224, 68, 14) };
		chart.getStyler().setSeriesColors(sliceColors);

		// Series
		chart.addSeries("Up", upTime);
		chart.addSeries("Down", downTime);

		String tempFileName = DToolsContext.HOME_DIR + "/temp/" + imageId;
		try {
			BitmapEncoder.saveBitmap(chart, tempFileName, BitmapFormat.PNG);
		} catch (IOException e) {
			DToolsContext.getInstance().getLogger().error("NodeAvailability:generateChart(): error generating chart: " + e.getMessage());
		}
	}
	
	
	/**
	 * Create intervals with time frame of ping interval (sampling interval), called boxes.<br>
	 * Each box has an id (counter), start time (end time of the box is startTime + samplingInterval),
	 * and status (UP=true or DOWN=false).
	 * Later when parsing log file, each box will be filled with either UP or DOWN status.
	 * @param history (hours)
	 */
	private void createEmptyBoxes(int history) {
		
		samplingInterval = DProps.getPropertyInt(DProps.NETWORK_MONITORING_PING_INTERVAL);
		int numOfBoxes = history * 3600 / samplingInterval;
		long now = System.currentTimeMillis();
		long start = now - (history * 3600 * 1000);
		
		for (int i = 0; i < numOfBoxes; i++) {
			
			Box box = new Box();
			box.id = i;
			box.time = start;
			box.available = false;
			boxList.add(box);
			
			start = start + (samplingInterval * 1000);
			
		}
		
	}
	
	public static void main(String[] args) {
		NodeAvailability n = new NodeAvailability();
		n.createEmptyBoxes(1);
		n.parseLog("/Users/matjaz/Developer/Projekti/DTools/log/ping.log", "Node #105", "aaa");
		
		int size = n.boxList.size();
		int numberOfUps = 0;
		for (int i = 0; i < size; i++) {
			System.out.println(n.boxList.get(i).toString());
			if (n.boxList.get(i).available) {
				numberOfUps++;
			}
		}
		
		int upTime = (int) ((float)numberOfUps / size * 100);
		System.out.println("upTime: " + upTime);
		
	}
	
	
	/**
	 * Parse ping.log file and fill the corresponding boxes with UP/DOWN statuses.
	 * @param logfile
	 * @param node
	 * @param location
	 */
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
					
					findBox(status);
					
				}
				
			}
			br.close();
		} catch (Exception e) {
			DToolsContext.getInstance().getLogger().error("NodeAvailability:parseLog(): error parsing log: " + e.getMessage());
		}
		
	}
	
	/**
	 * Find a corresponding interval (box) and set its status to UP or DOWN.
	 * Only statuses with errorCode=OK are marked as UP.
	 * @param status
	 */
	private void findBox(PingStatus status) {
		
		for (int i = 0; i < boxList.size(); i++) {
			
			Box box = boxList.get(i);
			long start = box.time;
			long end = start + (samplingInterval * 1000);
			if (status.getErrorCode() == PingStatus.EC_OK) {
				
				if (status.getStartTime() >= start && status.getStartTime() < end) {
					box.available = true;
				}
				
			}
			
		}
		
	}
	
	
	/**
	 * Calculate percentage of UP time.
	 * @return up time in %
	 */
	private int calculateUpTime() {
		
		int size = boxList.size();
		int numberOfUps = 0;
		for (int i = 0; i < size; i++) {
			if (boxList.get(i).available) {
				numberOfUps++;
			}
		}
		
		int upTime = (int) ((float)numberOfUps / size * 100);
		
		if (upTime > 100) upTime = 100;
		
		return upTime;
		
	}
	
	
	private class Box {
		
		private int id;
		private long time;
		private boolean available = false;
		
		@Override
		public String toString() {
			return "Box[" + id + "]: time=" + time + ", status=" + available;
		}
		
	}

}
