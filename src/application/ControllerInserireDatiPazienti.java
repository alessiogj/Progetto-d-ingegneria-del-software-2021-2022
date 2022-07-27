package application;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import application.Model.FattoreDiRischio;
import application.Model.Paziente;
import application.Model.PazienteDaoImpl;
import application.Model.Rischi;
import application.Model.RiskLevel;
import application.View.IllegalWindowException;

public class ControllerInserireDatiPazienti /*implements Initializable*/{
	
	@FXML private TextField IdTextField; //ID paziente
	@FXML private ComboBox<String> provComb; //Provincia
	@FXML private TextField professioneField; //Professione
	@FXML private DatePicker dateField; //Data di nascita
	@FXML private Button submitButton; //Conferma
	@FXML private TextArea descrizioneFattoreField;
	@FXML private Button aggiungereButton;
	@FXML private ComboBox<String> comLivGravita;
	@FXML private ComboBox<String> comRischio; //fattore di rischio
	private List<FattoreDiRischio> fattidirischio = new ArrayList<>();
	private Stage stage;
	private Scene scene;
	private Parent root;
	private PazienteDaoImpl pazienti;

	@FXML
	public void initialize() {
		descrizioneFattoreField.setWrapText(true);
		try(BufferedReader csvReader = new BufferedReader(new FileReader("src/application/Model/Files/province-sigle.csv"))) {
			ObservableList<String> listP = FXCollections.observableArrayList();
			String r;
			while ((r = csvReader.readLine()) != null) {
			    listP.add(r);
			}
			provComb.setItems(listP);
		}catch (Exception e) {
			System.out.println(e);
		}	
		pazienti = new PazienteDaoImpl();
		dateField.getEditor().setDisable(true);
		provComb.getEditor().setDisable(true);
		comLivGravita.getEditor().setDisable(true);
		comRischio.getEditor().setDisable(true);
		ObservableList<String> listGravita = FXCollections.observableArrayList("UNO", "DUE", "TRE",
				"QUATTRO", "CINQUE");
		comLivGravita.setItems(listGravita);
        ObservableList<String> list = FXCollections.observableArrayList("Fumatore","Iperteso","Sovrappeso",
        		"Patologie cardiovascolari","Patologie oncologiche","Altro");
        comRischio.setItems(list);
	}
	
	public void aggiungiFattore(ActionEvent event) {
		//blocco dei campi relativi ai dati dei pazienti
		String idAsString = IdTextField.getText();
		String provincia = provComb.getValue();
		String professione = professioneField.getText();
		LocalDate myDate = dateField.getValue();
		String descrizione = descrizioneFattoreField.getText();
		String gravita = comLivGravita.getValue();
		String fattore = comRischio.getValue();
		if(idAsString != "" && !provComb.getSelectionModel().isEmpty() 
				&& professione != "" && myDate != null && myDate.isBefore(LocalDate.now()) 
				&& descrizione != null && !comLivGravita.getSelectionModel().isEmpty()
				&& !comRischio.getSelectionModel().isEmpty() && isNumeric(idAsString) &&
				!pazienti.containsPatient(Integer.parseInt(idAsString)) && isString(professione) && descrizione.trim() != "") {
			try {
				FattoreDiRischio fatt = new FattoreDiRischio
								(Rischi.values()[(comRischio.getSelectionModel().getSelectedIndex())],
								descrizione, 
								RiskLevel.valueOf(gravita));
				checkFattore(fatt);
				fattidirischio.add(fatt);
				clearFields();
				IdTextField.setDisable(true);
				provComb.setDisable(true);
				professioneField.setDisable(true);
				dateField.setDisable(true);
			}catch (IllegalArgumentException e) {
				new IllegalWindowException(e.toString());
			}
		}else {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Informazioni errate");
			alert.setHeaderText(null);
			alert.setContentText("Inserire dati corretti!");
			alert.showAndWait();
		}
	}
 
	private void checkFattore(FattoreDiRischio fatt) {
		for (FattoreDiRischio x : fattidirischio) {
			if (x.getRischio().equals(fatt.getRischio())) {
				throw new IllegalArgumentException("Il fattore di rischio è presente per tale paziente");
			}		
		}
	}

	private void clearFields() {
		descrizioneFattoreField.clear();
		comLivGravita.getSelectionModel().clearSelection();
		comRischio.getSelectionModel().clearSelection();
		
	}

	public void submitReazione(ActionEvent event) throws IOException {
		//dati relativi alle caselle
		String idAsString = IdTextField.getText();
		String provincia = provComb.getValue();
		String professione = professioneField.getText();
		LocalDate myDate = dateField.getValue();
		String descrizione = descrizioneFattoreField.getText();
		String gravita = comLivGravita.getValue();
		String fattore = comRischio.getValue();

		try {
			if(idAsString != null && !provComb.getSelectionModel().isEmpty() 
					&& professione != null && myDate != null 
					&& descrizione != "" && !comLivGravita.getSelectionModel().isEmpty() 
					&& !provComb.getSelectionModel().isEmpty() 
					&& isNumeric(idAsString) && isString(professione) && descrizione.trim() != ""){
				//creo il fattore di rischio e lo controllo
				FattoreDiRischio fatt = new FattoreDiRischio
						(Rischi.values()[(comRischio.getSelectionModel().getSelectedIndex())],
						descrizione, 
						RiskLevel.valueOf(gravita));
				checkFattore(fatt);
				fattidirischio.add(fatt);	
				//aggiungo il paziente creato
				int id = Integer.parseInt(idAsString);
				pazienti.addPatient(new Paziente(id, myDate, professione, provincia));	
				//aggiungo i dati al paziente
				pazienti.updateListaRischi(id, fattidirischio);
				//salvo i dati su file
				pazienti.pazientiSerialize();
				//Scena successiva
				FXMLLoader loader = new FXMLLoader(getClass().getResource("View/InserireSegnalazione.fxml"));
				root = loader.load();
				ControllerInserireSegnalazione cs = loader.getController();
				cs.displayID(idAsString, false);
				stage = (Stage)((Node)event.getSource()).getScene().getWindow();
				scene = new Scene(root);
				stage.setScene(scene);
				stage.show();
				stage.centerOnScreen();
				stage.setMinWidth(600);
				stage.setMaxWidth(600);
				stage.setMinHeight(650);
				stage.setMaxHeight(650);
			}else if (idAsString != null && !provComb.getSelectionModel().isEmpty() 
					&& professione != null && myDate != null
					&& descrizione == "" && comRischio.getSelectionModel().isEmpty() 
					&& comLivGravita.getSelectionModel().isEmpty() && isNumeric(idAsString) 
					&& isString(professione)){
				int id = Integer.parseInt(idAsString);
				
				pazienti.addPatient(new Paziente(id, myDate, professione, provincia));
				pazienti.updateListaRischi(id, fattidirischio);
				pazienti.pazientiSerialize();
				//Scena successiva
				FXMLLoader loader = new FXMLLoader(getClass().getResource("View/InserireSegnalazione.fxml"));
				root = loader.load();
				ControllerInserireSegnalazione cs = loader.getController();
				cs.displayID(idAsString, false);
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
				new IllegalWindowException("Compilare i campi!");
			}
		}catch (IllegalArgumentException e) {
			new IllegalWindowException("Compilare i campi!");
		}
	}
	
	public void reset(ActionEvent event) throws IOException {
		clearFields();
	}
	
	public void annulla (ActionEvent event) throws IOException {
		//elimino il paziente se esiste
		if(isNumeric(IdTextField.getText()))
			pazienti.deletePatients(Integer.parseInt(IdTextField.getText()));
		//home
		pazienti.pazientiSerialize();
		FXMLLoader loader = new FXMLLoader(getClass().getResource("View/MainSceneMedico.fxml"));
		root = loader.load();
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
	    if (Integer.parseInt(strNum) <= 0)
	    	return false;
	    return true;
	}
	
	private boolean isString(String s) {
		boolean t = true;
		int i = 0;
		
		while(i < s.length()) {
			t = Character.isAlphabetic(s.charAt(i));
			i++;
			if(t == false) {
				break;
			}
		}
		return t;
	}


}
