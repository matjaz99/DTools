package si.matjazcerkvenik.dtools.web.beans;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.Serializable;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseId;

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import si.matjazcerkvenik.dtools.context.DToolsContext;

/**
 * This class loads an image.
 * 
 * @author matjaz
 *
 */
@ManagedBean
@ApplicationScoped
public class ImageBean implements Serializable {
	
	private static final long serialVersionUID = 4424316814244875557L;

	/**
	 * Load dynamically generated image from $DTOOLS_HOME$/temp directory. The image ID is 
	 * passed as f:param (name=imgId) along with p:graphicImage tag.
	 * @return image stream
	 */
	public StreamedContent getImage() {
		
		FacesContext context = FacesContext.getCurrentInstance();
		
		StreamedContent image = null;
		
		if (context.getCurrentPhaseId() == PhaseId.RENDER_RESPONSE) {
			// this method is called 3 times: 2 times it is called only to render img element
			// and no image is required to be returned
			return new DefaultStreamedContent();
		}
		
		// the last time this method is called, it is requested to load image itself
		
		String imgId = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("imgId");
		String tempFileName = DToolsContext.HOME_DIR + "/temp/" + imgId + ".png";
		
		try {
			
			File imgFile = new File(tempFileName);
			InputStream is = new FileInputStream(imgFile);
			image = new DefaultStreamedContent(is, "image/png");
			
		} catch (FileNotFoundException e) {
			DToolsContext.getInstance().getLogger().warn("ImageBean:getImage(): file not found: " + tempFileName);
			return null;
		}

		return image;
	}
	
}
