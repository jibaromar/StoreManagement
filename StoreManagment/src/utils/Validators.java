package utils;

import javafx.scene.control.TextInputControl;

public class Validators {
	public static final String NAME_CHARS = "a-zA-ZàáâäãåąčćęèéêëėįìíîïłńòóôöõøùúûüųūÿýżźñçčšžÀÁÂÄÃÅĄĆČĖĘÈÉÊËÌÍÎÏĮŁŃÒÓÔÖÕØÙÚÛÜŲŪŸÝŻŹÑßÇŒÆČŠŽ∂ð-";
	
	public static boolean isEmpty(String str) {
		boolean isEmpty = false;
		if (str == null || str.isEmpty() || str.matches("^\\s+$")) {
			isEmpty = true;
		}
		return isEmpty;
	}
	
	public static boolean isEmpty(TextInputControl tf) {
		boolean isEmpty = false;
		if (tf.getText() == null || tf.getText().isEmpty() || isEmpty(tf.getText())) {
			isEmpty = true;
		}
		return isEmpty;
	}
	
	public static boolean isName(String name) {
		boolean isValid = true;
		if (isEmpty(name) || !name.matches("^[" + NAME_CHARS+ "]+(\\s*[" + NAME_CHARS + "]+)*$")) {
			isValid = false;
		}
		return isValid;
	}
	
	public static boolean isPrice(String priceString) {
		boolean isValid = true;
		if (!priceString.matches("^\\d+(\\.\\d+)?$") || Double.parseDouble(priceString) < 0.0) {
			isValid = false;
		}
		return isValid;
	}
	
	public static boolean isQuantity(String quantityString) {
		boolean isValid = true;
		if (!quantityString.matches("^\\d+$") || Integer.parseInt(quantityString) < 0) {
			isValid = false;
		}
		return isValid;
	}
	
	public static boolean isPhone(String phoneString) {
		boolean isValid = true;
		String patterns = "((?:\\+|00)[17](?: |\\-)?|(?:\\+|00)[1-9]\\d{0,2}(?: |\\-)?|(?:\\+|00)1\\-\\d{3}(?: |\\-)?)?(0\\d|\\([0-9]{3}\\)|[1-9]{0,3})(?:((?: |\\-)[0-9]{2}){4}|((?:[0-9]{2}){4})|((?: |\\-)[0-9]{3}(?: |\\-)[0-9]{4})|([0-9]{7}))";
		if (!phoneString.matches(patterns)) {
			isValid = false;
		}
		return isValid;
	}
	
	public static boolean isEmail(String emailString) {
		boolean isValid = true;
		String pattern = "^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$"; // RFC 5322
		if (!emailString.matches(pattern)) {
			isValid = false;
		}
		return isValid;
	}
}
