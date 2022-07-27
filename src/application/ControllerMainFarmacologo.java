package application;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class ControllerMainFarmacologo {
	
	@FXML private Button logoutButton;
	@FXML private AnchorPane scenePane;

	private Stage stage;
	private Scene scene;
	private Parent root;
	
	public void avvisiLetti(ActionEvent event) throws IOException{
		root = FXMLLoader.load(getClass().getResource("View/AvvisiLetti.fxml"));
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
		stage.centerOnScreen();
		stage.setMinWidth(600);
		stage.setMaxWidth(600);
		stage.setMinHeight(500);
		stage.setMaxHeight(500);
	}
	
	public void analsiDibase(ActionEvent event) throws IOException {
		root = FXMLLoader.load(getClass().getResource("View/SegnalazioniFarmacologo.fxml"));
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
		stage.centerOnScreen();
		stage.setMinWidth(905);
		stage.setMaxWidth(905);
		stage.setMinHeight(500);
		stage.setMaxHeight(500);
	}
	
	public void avvisiNonLetti(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("View/AvvisiNonLetti.fxml"));
		root = loader.load();	
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
		stage.centerOnScreen();
		stage.setMinWidth(610);
		stage.setMaxWidth(610);
		stage.setMinHeight(500);
		stage.setMaxHeight(500);
	}

	public void logout(ActionEvent event) throws IOException {
		root = FXMLLoader.load(getClass().getResource("View/Login.fxml"));
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
		stage.show();
		stage.centerOnScreen();
		stage.setMinWidth(550);
		stage.setMaxWidth(550);
		stage.setMinHeight(300);
		stage.setMaxHeight(300);
	}
}
