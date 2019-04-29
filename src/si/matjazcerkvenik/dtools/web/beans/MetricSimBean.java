package si.matjazcerkvenik.dtools.web.beans;

import java.io.Serializable;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.context.RequestContext;

import si.matjazcerkvenik.dtools.context.DMetrics;
import si.matjazcerkvenik.dtools.io.DAO;
import si.matjazcerkvenik.dtools.xml.CustomMetric;
import si.matjazcerkvenik.dtools.xml.MetricLabel;

@ManagedBean
@ViewScoped
public class MetricSimBean implements Serializable {
	
	private static final long serialVersionUID = -5642411203189721650L;
	
	private CustomMetric newMetric = new CustomMetric();
	
	
	public CustomMetric getNewMetric() {
		return newMetric;
	}

	public void setNewMetric(CustomMetric newMetric) {
		this.newMetric = newMetric;
	}
	
	public void addLabel() {
		newMetric.addLabel(new MetricLabel("label", "value"));
	}
	
	public void removeLabel(MetricLabel label) {
		newMetric.removeLabel(label);
	}
	
	public List<CustomMetric> getCustomMetricsList() {
		return DAO.getInstance().loadCustomMetrics().getMetricsList();
	}
	
	public void removeMetric(CustomMetric metric) {
		DAO.getInstance().deleteCustomMetric(metric);
		DMetrics.dMetricsRegistry.unregisterMetric(metric);
	}

	public void saveMetrics() {
		if (newMetric.getHelp() == null || newMetric.getHelp().isEmpty()) {
			newMetric.setHelp(newMetric.getName());
		}
		DMetrics.dMetricsRegistry.registerMetric(newMetric);
		DAO.getInstance().addCustomMetric(newMetric);
		newMetric = null;
		newMetric = new CustomMetric();
		Growl.addGrowlMessage("Metric registered", FacesMessage.SEVERITY_INFO);
		RequestContext context = RequestContext.getCurrentInstance();
		context.addCallbackParam("success", true);
	}
	
}
