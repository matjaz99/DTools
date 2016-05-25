package si.matjazcerkvenik.dtools.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

@ManagedBean
@SessionScoped
//@ViewScoped
//@RequestScoped
public class TestBean {
	
	private String imgText;
	
	private String imgFile = "/Users/matjaz/Desktop/pig.png";
	
	private StreamedContent image;
	
	@PostConstruct
	public void init() {
		System.out.println("init TestBean");
	}

	public StreamedContent getImage() {
		System.out.println("getImage=" + image + ", imgFile=" + imgFile);
		if (image != null) {
			return image;
		}
		try {
			System.out.println("loading image " + imgFile);
			File chartFile = new File(imgFile);
			InputStream is = new FileInputStream(chartFile);
			image = new DefaultStreamedContent(is, "image/png");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return image;
	}

	public void setImage(StreamedContent image) {
		this.image = image;
	}

	public String getImgFile() {
		return imgFile;
	}

	public void setImgFile(String imgFile) {
		this.imgFile = imgFile;
	}
	
	public void confPig(ActionEvent actionEvent) {
//		String imgName = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("icon");
		image = null;
//		imgFile = "/Users/matjaz/Desktop/" + imgName + ".png";
		imgFile = "/Users/matjaz/Desktop/pig.png";
		System.out.println("confPig");
	}
	
	public void confWarn(ActionEvent actionEvent) {
		String imgName = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("icon");
		image = null;
		imgFile = "/Users/matjaz/Desktop/" + imgName + ".png";
		System.out.println("confWarn");
	}
	
	public void confUsers() {
		imgFile = "/Users/matjaz/Desktop/users.png";
		image = null;
		System.out.println("confUsers");
	}
	
	public void useIcon(String iconName) {
		image = null;
		imgFile = "/Users/matjaz/Desktop/" + iconName + ".png";
		System.out.println("useIcon " + iconName);
	}

	public String getImgText() {
		return imgText;
	}

	public void setImgText(String imgText) {
		this.imgText = imgText;
	}

	public void renderImage() {
		imgFile = "/Users/matjaz/Desktop/" + imgText + ".png";
		System.out.println("renderImage " + imgFile);
		image = null;
		imgText = null;
		
	}
	
}


