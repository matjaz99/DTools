package si.matjazcerkvenik.dtools.tools.snmp.impl;

import org.snmp4j.agent.AgentConfigManager;
import org.snmp4j.agent.mo.MOMutableTableRow;
import org.snmp4j.agent.mo.MOTableRowEvent;
import org.snmp4j.agent.mo.MOTableRowListener;
import org.snmp4j.agent.mo.snmp.TimeStamp;
import org.snmp4j.smi.Counter32;
import org.snmp4j.smi.Integer32;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.VariableBinding;

public class MyTableRowListener implements MOTableRowListener<Snmp4jDemoMib.Snmp4jDemoEntryRow> {
	
	protected AgentConfigManager agent;
	protected Modules modules;
	
	
	
    public MyTableRowListener(AgentConfigManager agent, Modules modules) {
		this.agent = agent;
		this.modules = modules;
	}



	public void rowChanged(MOTableRowEvent event) {
        if ((event.getType() == MOTableRowEvent.CREATE) ||
            (event.getType() == MOTableRowEvent.UPDATED)) {
          // ignore
          return;
        }
        // update counter
        Counter32 counter = (Counter32)
            event.getRow().getValue(Snmp4jDemoMib.idxSnmp4jDemoEntryCol3);
        if (counter == null) {
          counter = new Counter32(0);
          ((MOMutableTableRow)
           event.getRow()).setValue(Snmp4jDemoMib.idxSnmp4jDemoEntryCol3,
                                    counter);
        }
        counter.increment();
        // update timestamp
        TimeStamp timestamp = (TimeStamp)
            event.getTable().getColumn(Snmp4jDemoMib.idxSnmp4jDemoEntryCol4);
        timestamp.update((MOMutableTableRow)event.getRow(),
                         Snmp4jDemoMib.idxSnmp4jDemoEntryCol4);
        // fire notification
        Integer32 type =
            new Integer32(Snmp4jDemoMib.Snmp4jDemoTableRowModificationEnum.updated);
        switch (event.getType()) {
          case MOTableRowEvent.ADD:
            type.setValue(Snmp4jDemoMib.Snmp4jDemoTableRowModificationEnum.created);
            break;
          case MOTableRowEvent.DELETE:
            type.setValue(Snmp4jDemoMib.Snmp4jDemoTableRowModificationEnum.deleted);
            break;
        }
        VariableBinding[] payload = new VariableBinding[2];
        OID table = event.getTable().getOID();
        OID updateCount = new OID(table);
        updateCount.append(Snmp4jDemoMib.colSnmp4jDemoEntryCol3);
        updateCount.append(event.getRow().getIndex());

        OID modifyType = new OID(table);
        modifyType.append(Snmp4jDemoMib.colSnmp4jDemoTableRowModification);
        modifyType.append(event.getRow().getIndex());

        payload[0] = new VariableBinding(updateCount, counter);
        payload[1] = new VariableBinding(modifyType, type);
        modules.getSnmp4jDemoMib().snmp4jDemoEvent(
            agent.getNotificationOriginator(), new OctetString(), payload);
      }
    }
