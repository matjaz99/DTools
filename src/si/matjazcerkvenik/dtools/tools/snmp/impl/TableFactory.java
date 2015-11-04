package si.matjazcerkvenik.dtools.tools.snmp.impl;

import org.snmp4j.agent.mo.DefaultMOMutableRow2PC;
import org.snmp4j.agent.mo.DefaultMOTable;
import org.snmp4j.agent.mo.MOAccessImpl;
import org.snmp4j.agent.mo.MOColumn;
import org.snmp4j.agent.mo.MOMutableColumn;
import org.snmp4j.agent.mo.MOMutableTableModel;
import org.snmp4j.agent.mo.MOTableIndex;
import org.snmp4j.agent.mo.MOTableModel;
import org.snmp4j.agent.mo.MOTableRow;
import org.snmp4j.agent.mo.MOTableSubIndex;
import org.snmp4j.smi.Gauge32;
import org.snmp4j.smi.Integer32;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.SMIConstants;
import org.snmp4j.smi.Variable;

import si.matjazcerkvenik.dtools.tools.snmp.ColumnMetadata;
import si.matjazcerkvenik.dtools.tools.snmp.SnmpTable;

public class TableFactory {
	
	public static DefaultMOTable createStaticIfTable() {
		MOTableSubIndex[] subIndexes = new MOTableSubIndex[] { new MOTableSubIndex(
				SMIConstants.SYNTAX_INTEGER) };
		MOTableIndex indexDef = new MOTableIndex(subIndexes, false);
		MOColumn[] columns = new MOColumn[8];
		int c = 0;
		columns[c++] = new MOColumn(c, SMIConstants.SYNTAX_INTEGER,
				MOAccessImpl.ACCESS_READ_ONLY); // ifIndex
		columns[c++] = new MOColumn(c, SMIConstants.SYNTAX_OCTET_STRING,
				MOAccessImpl.ACCESS_READ_ONLY);// ifDescr
		columns[c++] = new MOColumn(c, SMIConstants.SYNTAX_INTEGER,
				MOAccessImpl.ACCESS_READ_ONLY); // ifType
		columns[c++] = new MOColumn(c, SMIConstants.SYNTAX_INTEGER,
				MOAccessImpl.ACCESS_READ_ONLY); // ifMtu
		columns[c++] = new MOColumn(c, SMIConstants.SYNTAX_GAUGE32,
				MOAccessImpl.ACCESS_READ_ONLY); // ifSpeed
		columns[c++] = new MOColumn(c, SMIConstants.SYNTAX_OCTET_STRING,
				MOAccessImpl.ACCESS_READ_ONLY);// ifPhysAddress
		columns[c++] = new MOMutableColumn(c, SMIConstants.SYNTAX_INTEGER, // ifAdminStatus
				MOAccessImpl.ACCESS_READ_WRITE, null);
		columns[c++] = new MOColumn(c, SMIConstants.SYNTAX_INTEGER,
				MOAccessImpl.ACCESS_READ_ONLY); // ifOperStatus

		DefaultMOTable ifTable = new DefaultMOTable(
				new OID("1.3.6.1.2.1.2.2.1"), indexDef, columns);
		MOMutableTableModel model = (MOMutableTableModel) ifTable.getModel();
		Variable[] rowValues1 = new Variable[] { new Integer32(1),
				new OctetString("eth0"), new Integer32(6), new Integer32(1500),
				new Gauge32(100000000), new OctetString("00:00:00:00:01"),
				new Integer32(1), new Integer32(1) };
		Variable[] rowValues2 = new Variable[] { new Integer32(2),
				new OctetString("loopback"), new Integer32(24),
				new Integer32(1500), new Gauge32(10000000),
				new OctetString("00:00:00:00:02"), new Integer32(1),
				new Integer32(1) };
		Variable[] rowValues3 = new Variable[] { new Integer32(3),
				new OctetString("test0"), new Integer32(9),
				new Integer32(1111), new Gauge32(123456789),
				new OctetString("00:00:00:00:03"), new Integer32(1),
				new Integer32(1) };
		model.addRow(new DefaultMOMutableRow2PC(new OID("1"), rowValues1));
		model.addRow(new DefaultMOMutableRow2PC(new OID("3"), rowValues2));
		model.addRow(new DefaultMOMutableRow2PC(new OID("5"), rowValues3));
		ifTable.setVolatile(true);
		return ifTable;
	}
	
	
	// $ snmpget -v 2c -c public 192.168.1.100:6161 1.3.6.1.4.1.444.1.8.2.1
	
	public static DefaultMOTable<MOTableRow<Variable>, MOColumn<Variable>, MOTableModel<MOTableRow<Variable>>> createMyCustomTable() {

		MOTableSubIndex[] subIndexes = new MOTableSubIndex[] { new MOTableSubIndex(
				SMIConstants.SYNTAX_INTEGER) };
		MOTableIndex indexDef = new MOTableIndex(subIndexes, false);
		MOColumn<Variable>[] columns = new MOColumn[3];
		int c = 0;
		columns[c++] = new MOColumn<Variable>(c, SMIConstants.SYNTAX_INTEGER,
				MOAccessImpl.ACCESS_READ_ONLY); // index
		columns[c++] = new MOColumn<Variable>(c,
				SMIConstants.SYNTAX_OCTET_STRING, MOAccessImpl.ACCESS_READ_ONLY); // descr
		columns[c++] = new MOColumn<Variable>(c, SMIConstants.SYNTAX_INTEGER,
				MOAccessImpl.ACCESS_READ_ONLY); // number

		DefaultMOTable<MOTableRow<Variable>, MOColumn<Variable>, MOTableModel<MOTableRow<Variable>>> myTable = new DefaultMOTable<MOTableRow<Variable>, MOColumn<Variable>, MOTableModel<MOTableRow<Variable>>>(
				new OID("1.3.6.1.4.1.444.1.1"), indexDef, columns);
		MOMutableTableModel<MOTableRow<Variable>> model = (MOMutableTableModel<MOTableRow<Variable>>) myTable.getModel();
		
		Variable[] rowValues1 = new Variable[] { new Integer32(1),
				new OctetString("port 1"), new Integer32(6) };
		Variable[] rowValues2 = new Variable[] { new Integer32(1),
				new OctetString("port 2"), new Integer32(2) };
		Variable[] rowValues3 = new Variable[] { new Integer32(1),
				new OctetString("port 3"), new Integer32(4) };
		
		model.addRow(new DefaultMOMutableRow2PC(new OID("1"), rowValues1));
		model.addRow(new DefaultMOMutableRow2PC(new OID("2"), rowValues2));
		model.addRow(new DefaultMOMutableRow2PC(new OID("3"), rowValues3));

		myTable.setVolatile(true);
		return myTable;

	}
	
	
	
	public static DefaultMOTable<MOTableRow<Variable>, MOColumn<Variable>, MOTableModel<MOTableRow<Variable>>> createTable(SnmpTable table) {
		
		int numberOfColumns = table.getMetadata().getColumnsMetaList().size();
		
		MOTableSubIndex[] subIndexes = new MOTableSubIndex[] { new MOTableSubIndex(
				SMIConstants.SYNTAX_INTEGER) };
		MOTableIndex indexDef = new MOTableIndex(subIndexes, false);
		MOColumn<Variable>[] columns = new MOColumn[numberOfColumns];
		for (int i = 0; i < numberOfColumns; i++) {
			columns[i] = new MOColumn<Variable>(i+1, getSmiConstant(table.getMetadata().getColumnsMetaList().get(i).getType()),
					MOAccessImpl.ACCESS_READ_ONLY);
		}

		DefaultMOTable<MOTableRow<Variable>, MOColumn<Variable>, MOTableModel<MOTableRow<Variable>>> myTable 
		= new DefaultMOTable<MOTableRow<Variable>, MOColumn<Variable>, MOTableModel<MOTableRow<Variable>>>(
				new OID(table.getMetadata().getTableOid()), indexDef, columns);
		MOMutableTableModel<MOTableRow<Variable>> model = (MOMutableTableModel<MOTableRow<Variable>>) myTable.getModel();
		
		for (int i = 0; i < table.getRowsList().size(); i++) {
			Variable[] rowValues = new Variable[numberOfColumns];
			for (int j = 0; j < numberOfColumns; j++) {
				rowValues[j] = getVariable(table.getMetadata().getColumnsMetaList().get(j).getType(), table.getRowsList().get(i).getValuesList().get(j));
			}
			model.addRow(new DefaultMOMutableRow2PC(new OID("" + (i+1)), rowValues));
		}

		myTable.setVolatile(true);
		return myTable;
		
	}
	
	private static int getSmiConstant(String type) {
		if (type.equals("INTEGER")) {
			return SMIConstants.SYNTAX_INTEGER;
		} else if (type.equals("OCTET_STRING")) {
			return SMIConstants.SYNTAX_OCTET_STRING;
		}
		return SMIConstants.SYNTAX_OCTET_STRING;
	}
	
	private static Variable getVariable(String type, String value) {
		
		if (type.equals("INTEGER")) {
			Integer i = Integer.parseInt(value);
			return new Integer32(i);
		} else if (type.equals("OCTET_STRING")) {
			return new OctetString(value);
		}
		return new OctetString(value);
		
	}
	
}
