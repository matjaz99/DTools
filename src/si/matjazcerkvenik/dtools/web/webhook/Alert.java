package si.matjazcerkvenik.dtools.web.webhook;

import java.util.Arrays;
import java.util.List;

public class Alert {
	
	private String status;
	//private Label[] labels;
//	private String labels;
	private List<Label> labels;
	//private Annotation[] annotations;
	private String startsAt;
	private String endsAt;
	private String generatorURL;
	
	
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	

//	public Label[] getLabels() {
//		return labels;
//	}
//
//	public void setLabels(Label[] labels) {
//		this.labels = labels;
//	}

//	public Annotation[] getAnnotations() {
//		return annotations;
//	}
//
//	public void setAnnotations(Annotation[] annotations) {
//		this.annotations = annotations;
//	}

//	public String getLabels() {
//		return labels;
//	}
//
//	public void setLabels(String labels) {
//		this.labels = labels;
//	}
	
	

	public String getStartsAt() {
		return startsAt;
	}

	public List<Label> getLabels() {
		return labels;
	}

	public void setLabels(List<Label> labels) {
		this.labels = labels;
	}

	public void setStartsAt(String startsAt) {
		this.startsAt = startsAt;
	}

	public String getEndsAt() {
		return endsAt;
	}

	public void setEndsAt(String endsAt) {
		this.endsAt = endsAt;
	}

	public String getGeneratorURL() {
		return generatorURL;
	}

	public void setGeneratorURL(String generatorURL) {
		this.generatorURL = generatorURL;
	}

	@Override
	public String toString() {
		return "Alert [status=" + status + ", labels=" + labels + ", annotations="
				+ /*annotationsToString() +*/ ", startsAt=" + startsAt + ", endsAt=" + endsAt + ", generatorURL="
				+ generatorURL + "]";
	}
	
//	private String labelsToString() {
//		String s = "";
//		for (int i = 0; i < labels.length; i++) {
//			s += "\t\t" + labels[i].toString() + "\n";
//		}
//		return s;
//	}
	
//	private String annotationsToString() {
//		String s = "";
//		for (int i = 0; i < annotations.length; i++) {
//			s += "\t\t" + annotations[i].toString() + "\n";
//		}
//		return s;
//	}
	
}
