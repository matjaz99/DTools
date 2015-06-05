package si.matjazcerkvenik.dtools.web.listeners;

import javax.faces.context.FacesContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import si.matjazcerkvenik.dtools.context.DToolsContext;
import si.matjazcerkvenik.dtools.web.SnmpTrapReceiverBean;

public class ShutdownListener implements ServletContextListener {
	
	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		DToolsContext.getInstance();
		System.out.println("DTools:ShutdownListener:contextInitialized()");
	}
	
	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		System.out.println("DTools:ShutdownListener:contextDestroyed()");
		
		// close snmp trap receiver
		SnmpTrapReceiverBean snmpTR = (SnmpTrapReceiverBean) FacesContext.getCurrentInstance().
				getExternalContext().getApplicationMap().get("snmpTrapReceiverBean");
		if (snmpTR.isListening()) {
			snmpTR.toggleListening();
		}
		
		// close logger
		DToolsContext.getInstance().getLogger().close();
	}
	
}
