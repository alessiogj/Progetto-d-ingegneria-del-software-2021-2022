package application;

import java.io.IOException;

import application.Model.PazienteDaoImpl;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class ControllerMainMedico {
	
	@FXML
	private Button logoutButton;
	@FXML
	private AnchorPane scenePane;
	@FXML
	private Label dettagliPaziente;
	
	private Stage stage;
	private Scene scene;
	private Parent root;

	PazienteDaoImpl pazienti = new PazienteDaoImpl();
	
	@FXML
	public void initialize() {
		pazienti = new PazienteDaoImpl();
	}
	
	public void inserimentoReazione(ActionEvent event) throws IOException {
		pazienti.pazientiSerialize();
		root = FXMLLoader.load(getClass().getResource("View/SceltaPaziente.fxml"));
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
		stage.centerOnScreen();
		stage.setMinWidth(380);
		stage.setMaxWidth(380);
		stage.setMinHeight(230);
		stage.setMaxHeight(230);
	}
	
	public void visualizzaPazienti(ActionEvent event) throws IOException {
		FXMLLoader loader =  new FXMLLoader(getClass().getResource("View/ElencoPazientiMedicoCurante.fxml"));
		root = loader.load();
		pazienti.pazientiSerialize();
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
		stage.show();
		stage.centerOnScreen();
		stage.setMinWidth(1100);
		stage.setMaxWidth(1100);
		stage.setMinHeight(700);
		stage.setMaxHeight(700);
	}
	
	public void logout(ActionEvent event) throws IOException {
		pazienti.pazientiSerialize();
		root = FXMLLoader.load(getClass().getResource("View/Login.fxml"));
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
		stage.centerOnScreen();
		stage.setMinWidth(550);
		stage.setMaxWidth(550);
		stage.setMinHeight(300);
		stage.setMaxHeight(300);
	}
}
