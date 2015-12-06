package si.matjazcerkvenik.dtools.web;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.primefaces.event.FileUploadEvent;

import si.matjazcerkvenik.dtools.io.DAO;
import si.matjazcerkvenik.dtools.tools.snmp.SnmpAgent;

@ManagedBean
@ViewScoped
public class SnmpMibBrowserBean implements Serializable {
	
	private static final long serialVersionUID = -1292510637321149925L;
	
	private SnmpAgent agent;
	
	@PostConstruct
	public void init() {
		
		Map<String, String> requestParameterMap = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		if (requestParameterMap.containsKey("agent")) {
			String name = requestParameterMap.get("agent");
			agent = DAO.getInstance().findSnmpAgent(name);
		}
	}
	
	public List<File> getMibFiles() {
		return DAO.getInstance().loadAgentMibs(agent);
	}
	
	public void deleteMibFile(File mib) {
		mib.delete();
	}
	
	public void handleFileUpload(FileUploadEvent event) {
		System.out.println("Upload: " + event.getFile().getFileName());
		
	    try {
	    	InputStream input = event.getFile().getInputstream();
			OutputStream output = new FileOutputStream(new File(agent.getDirectoryPath() + "/mibs/" + event.getFile().getFileName()));
			
			int read = 0;
			byte[] bytes = new byte[1024];

			while ((read = input.read(bytes)) != -1) {
				output.write(bytes, 0, read);
			}
			output.flush();
			output.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Growl.addGrowlMessage("Upload complete", FacesMessage.SEVERITY_INFO);
    }
	
}
