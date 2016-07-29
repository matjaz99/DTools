package si.matjazcerkvenik.dtools.io.influxdb;

import java.util.concurrent.TimeUnit;

import org.influxdb.InfluxDB;
import org.influxdb.InfluxDB.ConsistencyLevel;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.BatchPoints;
import org.influxdb.dto.Point;
import org.influxdb.dto.Query;

import si.matjazcerkvenik.dtools.context.DToolsContext;
import si.matjazcerkvenik.dtools.tools.ping.PingStatus;

public class InfluxDbClient {
	
	public static final String url = "http://192.168.1.115:8086";
	public static final String user = "root";
	public static final String pass = "root";
	
	public static void test1() {
		
		InfluxDB influxDB = InfluxDBFactory.connect(url, user, pass);
//		String dbName = "aTimeSeries";
		String dbName = "animals";
		influxDB.createDatabase(dbName);

		BatchPoints batchPoints = BatchPoints
		                .database(dbName)
		                .tag("async", "true")
		                .retentionPolicy("default")
		                .consistency(ConsistencyLevel.ALL)
		                .build();
		Point point1 = Point.measurement("cpu")
		                    .time(System.currentTimeMillis(), TimeUnit.MILLISECONDS)
		                    .addField("idle", 90L)
		                    .addField("user", 9L)
		                    .addField("system", 1L)
		                    .build();
		Point point2 = Point.measurement("disk")
		                    .time(System.currentTimeMillis(), TimeUnit.MILLISECONDS)
		                    .addField("used", 80L)
		                    .addField("free", 1L)
		                    .build();
		batchPoints.point(point1);
		batchPoints.point(point2);
		influxDB.write(batchPoints);
		Query query = new Query("SELECT idle FROM cpu", dbName);
		influxDB.query(query);
		influxDB.deleteDatabase(dbName);
		
	}
	
	public static void test2() {
		InfluxDB influxDB = InfluxDBFactory.connect(url, user, pass);
		String dbName = "animals";
//		influxDB.createDatabase(dbName);

		// Flush every 2000 Points, at least every 100ms
		influxDB.enableBatch(2000, 100, TimeUnit.MILLISECONDS);

		Point point1 = Point.measurement("cpu")
		                    .time(System.currentTimeMillis(), TimeUnit.MILLISECONDS)
		                    .addField("idle", 90L)
		                    .addField("user", 9L)
		                    .addField("system", 1L)
		                    .build();
//		Point point2 = Point.measurement("disk")
//		                    .time(System.currentTimeMillis(), TimeUnit.MILLISECONDS)
//		                    .addField("used", 80L)
//		                    .addField("free", 1L)
//		                    .build();

		influxDB.write(dbName, "default", point1);
//		influxDB.write(dbName, "default", point2);
		Query query = new Query("SELECT idle FROM cpu", dbName);
		influxDB.query(query);
//		influxDB.deleteDatabase(dbName);
	}
	
	public static void test3() {
		InfluxDB influxDB = InfluxDBFactory.connect(url, user, pass);
		String dbName = "animals";

		// Flush every 2000 Points, at least every 100ms
		influxDB.enableBatch(2000, 100, TimeUnit.MILLISECONDS);
		
		for (int i = 0; i < 1000000; i++) {
			
			long val = getNextCpuValue();
			
			Point point1 = Point.measurement("cpu")
                    .time(System.currentTimeMillis(), TimeUnit.MILLISECONDS)
                    .addField("idle", val)
                    .addField("user", 9L)
                    .addField("system", 1L)
                    .build();

			influxDB.write(dbName, "default", point1);
			
			System.out.println("Inserted[" + i + "]: " + val);
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		
		
	}
	
	
	public static long cpuValue = 50L;
	
	public static long getNextCpuValue() {
		
		int x = (int) (Math.random() * 10);
		
		if (x % 2 == 0) {
			cpuValue = cpuValue + 2;
		} else {
			cpuValue = cpuValue - 2;
		}
		
		if (cpuValue < 1) {
			cpuValue = 1;
		}
		if (cpuValue > 100) {
			cpuValue = 100;
		}
		
		return cpuValue;
		
	}
	
	private static InfluxDB _influxDB = null;
	
	public static synchronized void insertPingStatus(String pingType, String location, 
			String nodeName, String hostname, String service, PingStatus status) {
		
		try {
			if (_influxDB == null) {
				_influxDB = InfluxDBFactory.connect("http://192.168.1.115:8086", "root", "root");
				
				// Flush every 2000 Points, at least every 100ms
				_influxDB.enableBatch(2000, 100, TimeUnit.MILLISECONDS);
				
				System.out.println("INFLUX CONNECTED");
			}

			
			
			Point point1 = Point.measurement("ping")
//                .time(System.currentTimeMillis(), TimeUnit.MILLISECONDS)
					.tag("pingType", pingType)
					.tag("location", location)
					.tag("nodeName", nodeName)
					.tag("hostname", hostname)
					.tag("service", service)
			        .addField("eC", status.getErrorCode())
			        .addField("eM", status.getErrorMessage())
			        .addField("eD", status.getErrorDescription() == null ? "" : status.getErrorDescription())
			        .addField("sT", status.getStartTime())
			        .addField("eT", status.getEndTime())
			        .addField("dT", status.getDeltaTime())
			        .build();

			_influxDB.write("mySmartHome", "default", point1);
			System.out.println("INFLUX insert");
		} catch (Exception e) {
			DToolsContext.getInstance().getLogger().error("Exception", e);
		}
		
		//2016.05.27 03:25:08:483 - INFO  |pool-4-thread-15|ICMP_PING|aaa|Node #100|192.168.1.100||||[eC=1, eM=OK, eD=null, sT=1464312308483, eT=1464312308483, dT=0]
	}
	
}
