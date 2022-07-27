package application;

import java.io.IOException;

import application.Model.PazienteDaoImpl;
import application.View.IllegalWindowException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ControllerSceltaPaziente {
	
	
	
	@FXML private TextField idPaziente;
	private Stage stage;
	private Scene scene;
	private Parent root;
	private PazienteDaoImpl pazienti;
	
	@FXML
	public void initialize() {
		pazienti = new PazienteDaoImpl();
	}
	
	public void nuovoPaziente(ActionEvent event) throws IOException {
		pazienti.pazientiSerialize();
		root = FXMLLoader.load(getClass().getResource("View/InserireDatiPazienti.fxml"));
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
		stage.centerOnScreen();
		stage.setMinWidth(620);
		stage.setMaxWidth(620);
		stage.setMinHeight(600);
		stage.setMaxHeight(600);
	}
	
	public void pazienteEsistente(ActionEvent event) throws IOException {
		if(idPaziente != null && isNumeric(idPaziente.getText()) && pazienti.containsPatient(Integer.parseInt(idPaziente.getText()))
				&& pazienti.getUsr().equals(pazienti.getPatient(Integer.parseInt(idPaziente.getText())).getIdMedico())){
			String id = idPaziente.getText();
			pazienti.pazientiSerialize();
			FXMLLoader loader = new FXMLLoader(getClass().getResource("View/InserireSegnalazione.fxml"));
			root = loader.load();	
			ControllerInserireSegnalazione cs = loader.getController();
			cs.displayID(id, true);
			stage = (Stage)((Node)event.getSource()).getScene().getWindow();
			scene = new Scene(root);
			stage.setScene(scene);
			stage.show();
			stage.centerOnScreen();
			stage.setMinWidth(600);
			stage.setMaxWidth(600);
			stage.setMinHeight(650);
			stage.setMaxHeight(650);
		}else {
			new IllegalWindowException("Inserire paziente esistente");
		}
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
