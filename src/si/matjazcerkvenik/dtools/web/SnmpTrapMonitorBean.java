package si.matjazcerkvenik.dtools.web;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;

import si.matjazcerkvenik.dtools.tools.snmp.TrapNotification;

@ManagedBean
@SessionScoped
public class SnmpTrapMonitorBean {
	
	@ManagedProperty(value="#{snmpTrapReceiverBean}")
	private SnmpTrapReceiverBean snmpTrapReceiverBean;
	
	
	// only setter is needed to inject
	public void setSnmpTrapReceiverBean(SnmpTrapReceiverBean snmpTrapReceiverBean) {
		this.snmpTrapReceiverBean = snmpTrapReceiverBean;
	}



	public List<TrapNotification> getTrapNotifications() {
		Object[] array = snmpTrapReceiverBean.getTrapReceiver().getReceivedTrapNotifications().toArray();
		List<TrapNotification> list = new ArrayList<TrapNotification>();
		for (int i = 0; i < array.length; i++) {
			list.add((TrapNotification) array[i]);
		}
		return list;
		
	}
	
}
