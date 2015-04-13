package si.matjazcerkvenik.dtools.context;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class DToolsServletContextListener implements ServletContextListener {
	
	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		DToolsContext.getInstance();
		System.out.println("DToolsServletContextListener:contextInitialized()");
	}
	
	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		System.out.println("DToolsServletContextListener:contextDestroyed()");
		DToolsContext.getInstance().getLogger().close();
	}
}
