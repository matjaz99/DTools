package si.matjazcerkvenik.dtools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import si.matjazcerkvenik.dtools.tools.snmp.SnmpTrap;
import si.matjazcerkvenik.dtools.tools.snmp.TrapDestination;
import si.matjazcerkvenik.dtools.tools.snmp.TrapsTable;

public class ParseTraps {
	
	public static int count = 0;
	public static List<AlarmString> asList = new ArrayList<AlarmString>();

	public static void main(String[] args) {
		
		readFile("dimetra-traps.log");
		
		TrapsTable table = new TrapsTable();
		table.setTrapsList(new ArrayList<SnmpTrap>());
		table.setFilePath("dimetra-traps.xml");
		table.setName("Dimetra traps");
		
		for (int i = 0; i < asList.size(); i++) {
			SnmpTrap trap = asList.get(i).parseAlarm();
			table.addTrap(trap);
		}
		
		List<TrapDestination> tdList = new ArrayList<TrapDestination>();
		tdList.add(new TrapDestination("192.168.1.101", 162));
		table.setTrapDestinationsList(tdList);
		
		saveSnmpTraps(table);
		
	}

	public static void readFile(String filename) {
		
		AlarmString as = null;
		
		try {
			FileReader fr = new FileReader(filename);
			BufferedReader br = new BufferedReader(fr);
			String line;
			while ((line = br.readLine()) != null) {
				if (line.contains("TRAP")) {
					as = new AlarmString();
					count++;
				}
				as.alarmText += line;
				if (line.contains("]]")) {
					//System.out.println(i + "__trap:" + as.alarmText);
					asList.add(as);
					as = null;
				}
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	public static void saveSnmpTraps(TrapsTable snmpTraps) {
		
		try {
			
			File file = new File(snmpTraps.getFilePath());
			JAXBContext jaxbContext = JAXBContext.newInstance(TrapsTable.class);
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			jaxbMarshaller.marshal(snmpTraps, file);
			
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		
	}
	
}
