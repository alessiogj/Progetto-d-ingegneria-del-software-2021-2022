package application;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import application.Model.Paziente;
import application.Model.PazienteDaoImpl;
import application.Model.Segnalazione;
import application.Model.SegnalazioneDaoImpl;
import application.Model.Vaccinazione;
import application.View.IllegalWindowException;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ControllerAnalisiDibase {

	@FXML private Label nSegnalazioni;
	@FXML private Label nPerVaccino;
	@FXML private Label nPerSede;
	@FXML private Label nPerProv;
	@FXML private ComboBox<String> provincia;
	@FXML private TextField sede;
	@FXML private ComboBox<String> tipoVaccino;
	private Stage stage;
	private Scene scene;
	private Parent root;
	@FXML private ListView<String> listSegnalazioni = new ListView<String>();
	private SegnalazioneDaoImpl segnalazioni;
	private PazienteDaoImpl pazienti;
	
	
	@FXML
	public void initialize() {
		
		try(BufferedReader csvReader = new BufferedReader(new FileReader("src/application/Model/Files/province-sigle.csv"))) {
			ObservableList<String> listP = FXCollections.observableArrayList();
			String r;
			while ((r = csvReader.readLine()) != null) {
			    listP.add(r);
			}
			provincia.setItems(listP);
			provincia.getEditor().setDisable(true);
		}catch (Exception e) {
			System.out.println(e);
		}
		
		sede.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> arg0, String arg1, String arg2) {
				if (arg2 != "") {
					int contSede=0;
					for (Segnalazione x : segnalazioni.getAllSignal()) {
						for (Vaccinazione t : x.getVaccinazioniDueMesi()) {
							if(t.sede().equals(arg2)) {
								contSede++;
								break;
							}
						}	
					}
					nPerSede.setText(String.valueOf(contSede));
				}
			}
		});	
		
		provincia.getEditor().setDisable(true);
		tipoVaccino.getEditor().setDisable(true);
		segnalazioni = new SegnalazioneDaoImpl();
		pazienti = new PazienteDaoImpl();
		nSegnalazioni.setText(String.valueOf(segnalazioni.calcoloSegnalazioniGravi()));
		
		for(Segnalazione x : segnalazioni.getAllSignal()) {
			listSegnalazioni.getItems().add(x.toString());
		}

		ObservableList<String> vaccinoList = FXCollections.observableArrayList("ASTRAZENECA", "PFIZER", "MODERNA", "SPUTNIK","SINOVAC",
	        		"ANTINFLUENZALE",  "ALTRO", "NONE");
		tipoVaccino.setItems(vaccinoList);
	}
	
	public void vaccinazione() {
		if(tipoVaccino.getValue() != null)
			nPerVaccino.setText(calcoloPerVaccino(tipoVaccino.getValue()));
	}

	public void provincia() {
		int cont=0;
		for (Segnalazione x : segnalazioni.getAllSignal()) {
			if(x.getPatient().getProvincia().equals(provincia.getValue()))
					cont++;
			}	
		nPerProv.setText(String.valueOf(cont));
	}
	
	private String calcoloPerVaccino(String tipo) {
		if(!tipo.equals("NONE")) {
			int i=0;
			for(Segnalazione x : segnalazioni.getAllSignal()) {
				for(Vaccinazione t: x.getVaccinazioniDueMesi())
					if(t.getVaccinazione().toString().equals(tipo))
						i++;
			}
			return String.valueOf(i);
		}
		return "";
	}

	public void cerca(ActionEvent event) {
		//String res="";
		List<String> tmp = new ArrayList<>();
		if (!isString(sede.getText())) {
			new IllegalWindowException("Sede non corretta");
		}else {
			if(tipoVaccino.getValue() != null && sede.getText() != "" && provincia.getValue() != null) {
				if(!tipoVaccino.getSelectionModel().isSelected(7)) {
					for (Paziente x : pazienti.getAllPatients()) {
						if (x.getProvincia().equals(provincia.getValue()))
							for (Segnalazione t : x.getSegnalazioni())
								for(Vaccinazione w: t.getVaccinazioniDueMesi()) {
									if(w.sede().equals(sede.getText()) && w.getVaccinazione().toString().equals(tipoVaccino.getValue())) {
										tmp.add(t.toString());
										break;
									}
								}
					}
				}else {
					for (Paziente x : pazienti.getAllPatients()) {
						if (x.getProvincia().equals(provincia.getValue()))
							for (Segnalazione t : x.getSegnalazioni())
								for(Vaccinazione w: t.getVaccinazioniDueMesi()) {
									if(w.sede().equals(sede.getText())) {
										tmp.add(t.toString());
										break;
									}
								}
					}
				}
				
			}else if(tipoVaccino.getValue() != null && sede.getText() == "" && provincia.getValue() != null) {
				if(tipoVaccino.getSelectionModel().isSelected(7)) {
					//filtro per tipo provincia
					for (Paziente x : pazienti.getAllPatients()) {
						if (x.getProvincia().equals(provincia.getValue())) {
							for (Segnalazione t : x.getSegnalazioni()) {
								tmp.add(t.toString());
								break;
							}
						}
					}
				}else{
					//filtro per provincia e vaccino
					for (Paziente x : pazienti.getAllPatients()) {
						if (x.getProvincia().equals(provincia.getValue()))
							for (Segnalazione t : x.getSegnalazioni())
								for(Vaccinazione w: t.getVaccinazioniDueMesi()) {
									if(w.getVaccinazione().toString().equals(tipoVaccino.getValue())) {
										tmp.add(t.toString());
										break;
									}
								}
					}
				}
			}
			else if(tipoVaccino.getValue() != null && sede.getText() != "" && provincia.getValue() == null) {
				if(tipoVaccino.getSelectionModel().isSelected(7)) {
					for (Segnalazione x : segnalazioni.getAllSignal()) {
						for (Vaccinazione t : x.getVaccinazioniDueMesi()) {
							if(t.sede().equals(sede.getText())) {
								tmp.add(x.toString());
								break; //aggiungo la segnalazione una volta
							}
						}
					}
				}else{
					for (Segnalazione x : segnalazioni.getAllSignal()) {
						for (Vaccinazione t : x.getVaccinazioniDueMesi()) {
							if(t.sede().equals(sede.getText()) && t.getVaccinazione().toString().equals(tipoVaccino.getValue())) {
								tmp.add(x.toString());
								break; //aggiungo la segnalazione una volta
							}
						}
					}
				}
			}
			else if(tipoVaccino.getValue() != null && sede.getText() == "" && provincia.getValue() == null) {
				if(!tipoVaccino.getSelectionModel().isSelected(7)) {
					//filtro per vaccino
					for (Segnalazione x : segnalazioni.getAllSignal()) {
						for (Vaccinazione t : x.getVaccinazioniDueMesi()) {
							if(t.getVaccinazione().toString().equals(tipoVaccino.getValue())) {
								tmp.add(x.toString());
								break; //aggiungo la segnalazione una volta
							}
						}
					}
				}else {
					new IllegalWindowException("Inserire i campi");
					listSegnalazioni.getItems().clear();
					for(Segnalazione x : segnalazioni.getAllSignal()) {
						listSegnalazioni.getItems().add(x.toString());
					}
				}
			}
			else {
				new IllegalWindowException("Inserire i campi");
				listSegnalazioni.getItems().clear();
				for(Segnalazione x : segnalazioni.getAllSignal()) {
					listSegnalazioni.getItems().add(x.toString());
				}
			}
			listSegnalazioni.getItems().clear();
			listSegnalazioni.getItems().addAll(tmp);
		}
	}
	
	public void reset(ActionEvent event) throws IOException {
		provincia.getSelectionModel().clearSelection();
		nPerVaccino.setText("");
		nPerProv.setText("");
		nPerSede.setText("");
		sede.clear();
		tipoVaccino.getSelectionModel().clearSelection();
		for(Segnalazione x : segnalazioni.getAllSignal()) {
			listSegnalazioni.getItems().add(x.toString());
		}
	}
	
	public void proponi(ActionEvent event) throws IOException {
		if(!tipoVaccino.getSelectionModel().isSelected(7) && ! tipoVaccino.getSelectionModel().isEmpty()) {
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Conferma della proposta");
			alert.setHeaderText("Sei sicuro di inviare una proposta per il vaccino " + tipoVaccino.getValue());
			alert.setContentText("Sei d'accordo?");

			Optional<ButtonType> result = alert.showAndWait();
			if (result.get() == ButtonType.OK){
			    // Registro la fase di controllo su un file consultabile
				try(FileWriter fw = new FileWriter("src/application/Model/FIles/Fasi_di_controllo.txt", true);
						BufferedWriter bw = new BufferedWriter(fw);
						PrintWriter out = new PrintWriter(bw)){
					    	out.println("\nSegnalata una fase di controllo per il vaccino: " + tipoVaccino.getValue() + " in"
				        		+ " data " + LocalDate.now() + "\n");
				} catch (IOException e) {
					    
				}
			}
		}else {
			new IllegalWindowException("Selezionare il tipo di vaccino");
		}
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
	
	public void indietro(ActionEvent event) throws IOException {
		root = FXMLLoader.load(getClass().getResource("View/MainSceneFarmacologo.fxml"));
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setTitle("Home Farmacologo");
		stage.setScene(scene);
		stage.show();
		stage.centerOnScreen();
		stage.setMinWidth(450);
		stage.setMaxWidth(450);
		stage.setMinHeight(300);
		stage.setMaxHeight(300);
	}
}
