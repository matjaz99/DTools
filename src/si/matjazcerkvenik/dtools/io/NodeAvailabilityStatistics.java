package si.matjazcerkvenik.dtools.io;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import org.knowm.xchart.PieChart;
import org.knowm.xchart.PieChartBuilder;

import si.matjazcerkvenik.dtools.context.DToolsContext;
import si.matjazcerkvenik.dtools.tools.ping.PingStatus;

public class NodeAvailabilityStatistics {
	
	private List<DataBox> aa = new ArrayList<DataBox>();

	public PieChart getChart(String node, String location, int history) {
		
		aa.clear();
		
		// Create empty boxes
		createEmptyBoxes(history);
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

		return chart;
	}
	
	
	private void createEmptyBoxes(int history) {
		
		int numOfBoxes = history * 60;
		long now = System.currentTimeMillis();
		long before = now - (history * 3600 * 1000);
		
		for (int i = 0; i < numOfBoxes; i++) {
			
			DataBox box = new DataBox();
			box.boxId = i;
			box.time = before;
			box.available = false;
			aa.add(box);
			
			before = before + (60 * 1000);
			
		}
		
		
		
	}
	
	public static void main(String[] args) {
		NodeAvailabilityStatistics n = new NodeAvailabilityStatistics();
		n.createEmptyBoxes(1);
		n.parseLog("/Users/matjaz/Developer/Projekti/DTools/log/ping.log", "Node #105", "aaa");
		
		int size = n.aa.size();
		int numberOfUps = 0;
		for (int i = 0; i < size; i++) {
			System.out.println(n.aa.get(i).toString());
			if (n.aa.get(i).available) {
				numberOfUps++;
			}
		}
		
		int upTime = (int) ((float)numberOfUps / size * 100);
		System.out.println("upTime: " + upTime);
		
	}
	
	
	private void parseLog(String logfile, String node, String location) {
		
		File f = new File(logfile);
		if (!f.exists()) {
			return;
		}
		
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
			e.printStackTrace();
		}
		
	}
	
	private void findBox(PingStatus status) {
		
		for (int i = 0; i < aa.size(); i++) {
			
			DataBox db = aa.get(i);
			long start = db.time;
			long end = start + (60 * 1000);
			if (status.getErrorCode() == PingStatus.EC_OK) {
				
				if (status.getStartTime() >= start && status.getStartTime() <= end) {
					db.available = true;
					System.out.println("set UP for box " + db.boxId);
				}
				
			}
			
		}
		
	}
	
	private int calculateUpTime() {
		
		int size = aa.size();
		int numberOfUps = 0;
		for (int i = 0; i < size; i++) {
			System.out.println(aa.get(i).toString());
			if (aa.get(i).available) {
				numberOfUps++;
			}
		}
		
		int upTime = (int) ((float)numberOfUps / size * 100);
		
		if (upTime > 100) upTime = 100;
		
		return upTime;
		
	}
	
	
	class DataBox {
		
		private int boxId;
		private long time;
		private boolean available = false;
		
		@Override
		public String toString() {
			return "Box[" + boxId + "]: time=" + time + ", status=" + available;
		}
		
	}

}
