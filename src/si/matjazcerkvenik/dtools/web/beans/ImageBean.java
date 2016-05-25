package si.matjazcerkvenik.dtools.web.beans;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseId;

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import si.matjazcerkvenik.dtools.context.DToolsContext;

@ManagedBean
@ApplicationScoped
public class ImageBean {
	
	public StreamedContent getImage() {
		
		FacesContext context = FacesContext.getCurrentInstance();
		
		StreamedContent image = null;
		
		if (context.getCurrentPhaseId() == PhaseId.RENDER_RESPONSE) {
			System.out.println("loadImage RENDER_RESPONSE");
			return new DefaultStreamedContent();
		}
		
		try {
			
			String imgId = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("imgId");
			
			String tempFileName = DToolsContext.HOME_DIR + "/temp/" + imgId + ".png";
			
			System.out.println("loadImage " + tempFileName);
			
			File imgFile = new File(tempFileName);
			InputStream is = new FileInputStream(imgFile);
			image = new DefaultStreamedContent(is, "image/png");
			
		} catch (FileNotFoundException e) {
			System.err.println(e.getMessage());
		}

		return image;
	}
	
}
