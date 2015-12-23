package si.matjazcerkvenik.dtools.web.validators;

import javax.faces.application.FacesMessage;
import javax.faces.validator.ValidatorException;

public class ValidatorUtils {
	
	/**
	 * Throw ValidatorException if name contains invalid characters: /?<>\:*|
	 * @param name
	 */
	public static void validateFileName(String name) throws ValidatorException {
		
		if (name.contains("/") || name.contains("?") || name.contains("<")
				|| name.contains(">") || name.contains("\\") || name.contains(":")
				|| name.contains("*") || name.contains("|") || name.contains("\"")) {
			FacesMessage message = new FacesMessage();
			message.setSummary("Invalid characters [/?<>\\:*|]");
			message.setSeverity(FacesMessage.SEVERITY_ERROR);
			throw new ValidatorException(message);
		}
		
	}
	
	/**
	 * Return Integer if <code>number</code> can be converted to Integer or 
	 * throw ValidatorException if <code>number</code> is not a number.
	 * @param number
	 */
	public static int validateInteger(String number) throws ValidatorException {
		
		Integer i = 0;
		try {
			i = Integer.parseInt(number);
		} catch (NumberFormatException e) {
			FacesMessage message = new FacesMessage();
			message.setSummary("Value not a number");
			message.setSeverity(FacesMessage.SEVERITY_ERROR);
			throw new ValidatorException(message);
		}
		return i.intValue();
		
	}
	
}
