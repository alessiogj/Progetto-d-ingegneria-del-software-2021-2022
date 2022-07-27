package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class Main extends Application {

	@Override
	public void start(Stage stage) {
		try {

			Parent root = FXMLLoader.load(getClass().getResource("View/Login.fxml"));
			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.setTitle("Gestionale reazioni avverse");
			stage.show();
			stage.centerOnScreen();
			stage.setMinWidth(550);
			stage.setMaxWidth(550);
			stage.setMinHeight(300);
			stage.setMaxHeight(300);

		} catch(Exception e) {
			e.printStackTrace();
		}
	} 

	public static void main(String[] args) {
		launch(args);
	}
}
