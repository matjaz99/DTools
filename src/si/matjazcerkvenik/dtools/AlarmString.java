package si.matjazcerkvenik.dtools;

import java.util.ArrayList;
import java.util.List;

import si.matjazcerkvenik.dtools.tools.snmp.SnmpTrap;
import si.matjazcerkvenik.dtools.xml.VarBind;

public class AlarmString {
	
	public String alarmText = "";
	
	public SnmpTrap parseAlarm() {
		
		String s = alarmText.replace("]]", "");
		String[] split = s.split("VBS\\[");
		
		// 1.3.6.1.2.1.1.3.0 = 491 days, 21:14:12.03; 1.3.6.1.6.3.1.1.4.1.0 = 1.3.6.1.4.1.161.3.10.104.1; 1.3.6.1.4.1.161.3.10.105.3.0 = 10539464;
		// System.out.println(split[1]);
		
		String[] varbinds = split[1].split("; 1.3.6");
		
		List<VarBind> vbList = new ArrayList<VarBind>();
		
		for (int i = 0; i < varbinds.length; i++) {
			
			String[] oid = varbinds[i].trim().split(".0 = ");
			System.out.println(ParseTraps.count + "/" + i + "/ " + "1.3.6" + oid[0].trim() + ".0" + " ===> " + oid[1].trim());
			
			VarBind vb = new VarBind("oidName" + i, "1.3.6" + oid[0].trim() + ".0", VarBind.TYPE_OCTET_STRING, oid[1].trim());
			vbList.add(vb);
			
		}
		
		SnmpTrap trap = new SnmpTrap();
		trap.setCommunity("public");
		trap.setSourceIp("127.0.0.1");
		trap.setTrapName("dimetra_trap");
		trap.setVersion("v2c");
		trap.setSysUpTime("0000");
		trap.setSnmpTrapOid("1.3.6.1.4.1.161.3.10.104.1");
		trap.setVarbind(vbList);
		
		return trap;
		
	}
	
}
