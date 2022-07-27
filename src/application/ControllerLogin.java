 package application;


import java.io.IOException;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoField;
import application.Model.AvvisiDaoImpl;
import application.Model.Avviso;
import application.Model.Credenziali;
import application.Model.PazienteDaoImpl;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;


public class ControllerLogin {

	@FXML private TextField usernameText;
	@FXML private TextField pswText;
	@FXML private Label errorMessage;
	private Stage stage;
	private Scene scene;
	private Parent root;
	private String usrT; 
	private String pswT; 
	private AvvisiDaoImpl avvisi;
	
	@FXML
	public void initialize() {
		avvisi = new AvvisiDaoImpl();
	}
	
	public void checkButton(ActionEvent event) throws IOException {
		usrT = usernameText.getText();
		pswT = pswText.getText();
		
		//recupero le credenziali
		Credenziali[] credenzialiInserite = Credenziali.getCredentials();
		
		for (Credenziali x : credenzialiInserite) {
			if(x.getUsername().equals(usrT) && x.getPassword().equals(pswT) && x.getUsername().charAt(0)=='M') {
				//login medico
				Credenziali credenziali = new Credenziali(usrT, pswT);
				errorMessage.setText("");
				new PazienteDaoImpl().setUsr(credenziali);
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
			}else if (x.getUsername().equals(usrT) && x.getPassword().equals(pswT) && ((Credenziali)x).getUsername().charAt(0)=='F') {
				//login viginate vaccini
				Credenziali credenziali = new Credenziali(usrT, pswT);
				controlloAvvisi();		
				new PazienteDaoImpl().setUsr(credenziali);
				avvisi.avvisiSerialize();
				errorMessage.setText("");
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
			}else {
				errorMessage.setText("Password o username non corretti");
			}
		}
	}
	
	private void controlloAvvisi() {
		if(isWeekend() == true)
			avvisi.aggiungiAvvisoNonLetto(new Avviso("Avviso settimanale"));
	}

	private boolean isWeekend(){
		LocalDate ld = LocalDate.now();
        DayOfWeek day = DayOfWeek.of(ld.get(ChronoField.DAY_OF_WEEK));
        return day == DayOfWeek.SUNDAY || day == DayOfWeek.SATURDAY;
    }
}
