package application;

import java.io.IOException;

import application.Model.AvvisiDaoImpl;
import application.Model.Avviso;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;

public class ControllerAvvisiNonLetti {

	//@FXML private Label LabelAvvisi;
	@FXML private ScrollPane scrollTest;
	@FXML private ListView<String> listAvvisi = new ListView<>();
	
	private Stage stage;
	private Scene scene;
	private Parent root;
	private AvvisiDaoImpl avvisi;
	
	@FXML 
	public void initialize() {
		avvisi = new AvvisiDaoImpl();	
		for (Avviso x : avvisi.getAvvisiNonLetti()) {
			listAvvisi.getItems().add(x.toString());
		}
	}

	public void indietro(ActionEvent event) throws IOException {
		avvisi.leggiAvvisi();
		avvisi.avvisiSerialize();
		root = FXMLLoader.load(getClass().getResource("View/MainSceneFarmacologo.fxml"));
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
		stage.centerOnScreen();
		stage.setMinWidth(450);
		stage.setMaxWidth(450);
		stage.setMinHeight(300);
		stage.setMaxHeight(300);
	}

}
