package application;


import java.io.IOException;

import application.Model.Paziente;
import application.Model.PazienteDaoImpl;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ControllerElencoPazienti {
	

	@FXML private TextField idPaziente;
	@FXML private ListView<Integer> listPazienti = new ListView<>();
	@FXML private Label infoPaziente;
	private Stage stage;
	private Scene scene;
	private Parent root;
	private PazienteDaoImpl pazienti;

	@FXML
	public void initialize() {
		pazienti = new PazienteDaoImpl();
		for (Paziente x : pazienti.getAllPatients()) {
			if(x.getIdMedico().equals(pazienti.getUsr()))
				listPazienti.getItems().add(x.getId());
		}
		
		listPazienti.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Integer>() {
			@Override
			public void changed(ObservableValue<? extends Integer> arg0, Integer arg1, Integer arg2) {
				infoPaziente.setText(pazienti.getPatient(listPazienti.getSelectionModel().getSelectedItem()).toString());	
			}
		});
	}
	
	public void cercaPaziente(ActionEvent event) {
		if(idPaziente != null && isNumeric(idPaziente.getText())) {
			int idPazienteDaCercare = Integer.parseInt(idPaziente.getText());
			String dettagliPaziente = new PazienteDaoImpl().toStringPaziente(idPazienteDaCercare);
			infoPaziente.setText(dettagliPaziente);
		}else {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Informazioni errate");
			alert.setHeaderText(null);
			alert.setContentText("Inserire un identificativo valido!");
			alert.showAndWait();
		}
	}
	
	public void resetElenco(ActionEvent event) {
		infoPaziente.setText("");
	}
	
	public void back(ActionEvent event) throws IOException {
		pazienti.pazientiSerialize();
		root = FXMLLoader.load(getClass().getResource("View/MainSceneMedico.fxml"));
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
		stage.centerOnScreen();
		stage.setMinWidth(480);
		stage.setMaxWidth(480);
		stage.setMinHeight(200);
		stage.setMaxHeight(200);
	}
	
	private boolean isNumeric(String strNum) {
	    if (strNum == null) {
	        return false;
	    }
	    try {
	        int d = Integer.parseInt(strNum);
	    } catch (NumberFormatException nfe) {
	        return false;
	    }
	    return true;
	}
}
