package application.View;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class IllegalWindowException{

	public IllegalWindowException(String string) {
		
		String message = string.replace("java.lang.IllegalArgumentException:", "");
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Informazioni errate");
		alert.setHeaderText(null);
		alert.setContentText(message);
		alert.showAndWait();
	}
}
