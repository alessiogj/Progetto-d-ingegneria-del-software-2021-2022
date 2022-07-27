package application;

import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import application.Model.DoseVaccino;
import application.Model.PazienteDaoImpl;
import application.Model.Reazione;
import application.Model.RiskLevel;
import application.Model.Segnalazione;
import application.Model.SegnalazioneDaoImpl;
import application.Model.TipoReazione;
import application.Model.Vaccinazione;
import application.Model.VaccinoTipo;
import application.View.IllegalWindowException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ControllerInserireSegnalazione{
	
	
	@FXML private Label nameLabel;
	@FXML private DatePicker dataReazione;
	@FXML private DatePicker dataVaccinazione;
	@FXML private ComboBox<String>  combSceltaReazione;
	@FXML private ComboBox<String> combVaccino;
	@FXML private Label reazioneLabel;
	@FXML private ComboBox<String> combDose;	
	@FXML private TextField sedeField;
	@FXML private Label sceltaLabel;
	@FXML private ComboBox<String> combGravitaReazione;
	@FXML private TextArea descrizioneReazField;
	@FXML private Label vaccinazioneError;
	private List<Vaccinazione> vaccinazioni = new ArrayList<>(); 
	private boolean checkExisting;
	private Stage stage;
	private Scene scene;
	private Parent root;
	private int id;
	private PazienteDaoImpl pazienti;
	private SegnalazioneDaoImpl segnalazioni;
	
	public void displayID(String idPaziente, boolean check) {
		id=Integer.parseInt(idPaziente);
		checkExisting = check;
	}
	
	@FXML
    public void initialize() {
		descrizioneReazField.setWrapText(true);
		pazienti = new PazienteDaoImpl();
		segnalazioni = new SegnalazioneDaoImpl();
    	dataReazione.getEditor().setDisable(true);
    	dataVaccinazione.getEditor().setDisable(true);
    	combDose.getEditor().setDisable(true);
    	combGravitaReazione.getEditor().setDisable(true);
    	combSceltaReazione.getEditor().setDisable(true);
    	combVaccino.getEditor().setDisable(true);
    	ObservableList<String> gravita = FXCollections.observableArrayList("UNO", "DUE", "TRE",
				"QUATTRO", "CINQUE");
    	combGravitaReazione.setItems(gravita);
        ObservableList<String> list = FXCollections.observableArrayList("TOSSE","ASMA","DERMATITI","INSUFFICIENZA RENALE","MIOCARDIOPATIA",
        		"ALTRO");
        combSceltaReazione.setItems(list);
        
        ObservableList<String> vaccinoList = FXCollections.observableArrayList("ASTRAZENECA", "PFIZER", "MODERNA", "SPUTNIK","SINOVAC",
        		"ANTINFLUENZALE", "ALTRO");
        combVaccino.setItems(vaccinoList);
        
        ObservableList<String> doseList = FXCollections.observableArrayList("I","II","III","IV", "UNICA");
        combDose.setItems(doseList);
       
   }
    
    public void aggiungiVaccinazione(ActionEvent event) throws IOException {
    	try {
    		if(checkAll()) {
        		dataReazione.setDisable(true);
        		combSceltaReazione.setDisable(true);
        		descrizioneReazField.setDisable(true);
        		combGravitaReazione.setDisable(true); 

        		Vaccinazione vaccinoToAdd = new Vaccinazione(id, 
        				dataVaccinazione.getValue(), 
        				VaccinoTipo.valueOf(combVaccino.getValue()), 
        				DoseVaccino.valueOf(combDose.getValue()),
        				sedeField.getText());
        		addVaccinazione(vaccinoToAdd);
        	    vaccinazioni.add(vaccinoToAdd);
        	    clearField();
    		}else {
    			new IllegalWindowException("Campi non corretti");
    		}
    	}catch (IllegalArgumentException e) {
			new IllegalWindowException(e.toString());
		}
    }
    	
    
	private boolean checkAll() {
		return  dataReazione.getValue() != null && descrizioneReazField.getText() != ""  && !combSceltaReazione.getSelectionModel().isEmpty()
				&& !combGravitaReazione.getSelectionModel().isEmpty() && dataVaccinazione.getValue() != null 
				&& !combVaccino.getSelectionModel().isEmpty() 
				&& !combDose.getSelectionModel().isEmpty() &&  sedeField.getText() != "" && isString(sedeField.getText())
				&& ChronoUnit.DAYS.between(pazienti.getPatient(id).birthDay(), dataReazione.getValue()) > 0
				&& ChronoUnit.DAYS.between( dataVaccinazione.getValue(), dataReazione.getValue())  >= 0
				&& ChronoUnit.DAYS.between( dataReazione.getValue(), LocalDate.now())  >= 0 && descrizioneReazField.getText().trim() != "";
	}

	private void addVaccinazione(Vaccinazione vaccinoToAdd) {
		List<Vaccinazione> vaccinazioniPrecendeti = new ArrayList<>(pazienti.getPatient(id).getVaccinazioni());
		
		if(!vaccinoToAdd.getVaccinazione().equals(VaccinoTipo.ANTINFLUENZALE) && !vaccinoToAdd.getDose().equals(DoseVaccino.UNICA)) {
			//vaccinazioni aggiunte in questa esecuzione
			vaccinazioniPrecendeti.addAll(vaccinazioni);
			
			for (Vaccinazione x : vaccinazioniPrecendeti) {
				if(vaccinoToAdd.getDose().ordinal() >= x.getDose().ordinal()) {
					if(ChronoUnit.DAYS.between(x.getDataVaccinazione(),vaccinoToAdd.getDataVaccinazione()) <= 0  
							|| x.getDose().ordinal() == vaccinoToAdd.getDose().ordinal()) {
						if (x.getVaccinazione().ordinal() != VaccinoTipo.ANTINFLUENZALE.ordinal())
							throw new IllegalArgumentException("Ordine delle dosi illegale per vaccini covid");
					}	
				}else{
					if(ChronoUnit.DAYS.between(x.getDataVaccinazione(),vaccinoToAdd.getDataVaccinazione()) >= 0
							|| x.getDose().ordinal() == vaccinoToAdd.getDose().ordinal()) {
						if (x.getVaccinazione().ordinal() != VaccinoTipo.ANTINFLUENZALE.ordinal())
							throw new IllegalArgumentException("Ordine delle dosi illegale per vaccini covid");
					}
				}
				
				//controllo corrispondenza con qualche dose unica precedentemente insierita
				if(vaccinoToAdd.getVaccinazione().ordinal() == x.getVaccinazione().ordinal()
						&& x.getDose().ordinal() == DoseVaccino.UNICA.ordinal())
					throw new IllegalArgumentException("Presente una dose unica");						
			}
			//controllo il numero di vaccinazioni covid, trascurando quelle antinfluenziali
			int nVaccCovid = 0;
			for (Vaccinazione x : vaccinazioniPrecendeti) {
				if(x.getVaccinazione().ordinal() == VaccinoTipo.ANTINFLUENZALE.ordinal())
					nVaccCovid++;
			}
			if (nVaccCovid >= 4) {
				throw new IllegalArgumentException("Non possono essere effettuate più di 4 dosi di vaccini anticovid");
			}
		}else if (!vaccinoToAdd.getVaccinazione().equals(VaccinoTipo.ANTINFLUENZALE) && vaccinoToAdd.getDose().equals(DoseVaccino.UNICA)){
			
			vaccinazioniPrecendeti.addAll(vaccinazioni);
			
			for (Vaccinazione x : vaccinazioniPrecendeti) {
				if (x.getVaccinazione().ordinal() == vaccinoToAdd.getVaccinazione().ordinal()) {
					throw new IllegalArgumentException("In presenza di dose unica non è possibile effettuare tale vaccino");
				}
				if (ChronoUnit.DAYS.between(x.getDataVaccinazione(),vaccinoToAdd.getDataVaccinazione()) == 0)
					throw new IllegalArgumentException("Il vaccino non può essere effettuato nella stessa data");
			}
		}else {
			//antinfluenzale
			if(!vaccinoToAdd.getDose().equals(DoseVaccino.UNICA))
				throw new IllegalArgumentException("Il vaccino antinfluenzale è sempre una dose unica");
			vaccinazioniPrecendeti.addAll(vaccinazioni);
			for (Vaccinazione x : vaccinazioniPrecendeti) {
				if (ChronoUnit.DAYS.between(x.getDataVaccinazione(),vaccinoToAdd.getDataVaccinazione()) == 0)
					throw new IllegalArgumentException("Il vaccino non può essere effettuato nella stessa data");
			}
		}
	}
	
	private void clearField() {
		combDose.getSelectionModel().clearSelection();
		dataVaccinazione.setValue(null);
		combVaccino.getSelectionModel().clearSelection();
		sedeField.clear();
	}


	public void confermaSegnalazione(ActionEvent event) throws IOException {
		try {
			if(checkAll()) {
				//tutti i campi compilati
				
				//aggiungo vaccinazione al paziente
		    	Vaccinazione vaccinoToAdd = new Vaccinazione(id, 
		    			dataVaccinazione.getValue(),
		    			VaccinoTipo.valueOf(combVaccino.getValue()),
		    			DoseVaccino.valueOf(combDose.getValue()),
		    			sedeField.getText());
		    	addVaccinazione(vaccinoToAdd);
				//aggiungo la reazione
		    	Reazione reazione = new Reazione(id, 
		    			TipoReazione.values()[combSceltaReazione.getSelectionModel().getSelectedIndex()], 
		    			dataReazione.getValue(), 
		    			RiskLevel.valueOf(combGravitaReazione.getValue()), 
		    			descrizioneReazField.getText());
		    	
		    	pazienti.updateReazione(id, reazione);
		    	vaccinazioni.add(vaccinoToAdd);
		    	pazienti.updateListaVaccinazione(id, vaccinazioni); //problema
		    	
		    	//creaiamo la segnalazione che gestirà il sistema
		    	Segnalazione segnalazione = new Segnalazione(pazienti.getPatient(id),
		    			reazione,
		    			dataReazione.getValue());
		    	segnalazioni.addSignal(segnalazione);
		    	
		    	segnalazioni.addSegnalazioneToVaccinazione(id, segnalazione, vaccinazioni);
		    	segnalazioni.addSegnalazioneToReazione(id, segnalazione, reazione);
		    	segnalazioni.addSignalToPatient(id, segnalazione);
		    	//salvo su file
		    	pazienti.pazientiSerialize();
		    	segnalazioni.senalazioniSerialize();
				FXMLLoader loader = new FXMLLoader(getClass().getResource("View/MainSceneMedico.fxml"));
				root = loader.load();

				stage = (Stage)((Node)event.getSource()).getScene().getWindow();
				scene = new Scene(root);
				stage.setScene(scene);
				stage.show();
				stage.setMinWidth(480);
				stage.setMaxWidth(480);
				stage.setMinHeight(200);
				stage.setMaxHeight(200);
			}else if(checkSoloReazione()) {
				
				//aggiungo la reazione
		    	Reazione reazione = new Reazione(id, 
		    			TipoReazione.values()[combSceltaReazione.getSelectionModel().getSelectedIndex()], 
		    			dataReazione.getValue(), 
		    			RiskLevel.valueOf(combGravitaReazione.getValue()),
		    			descrizioneReazField.getText());
		    	
		    	if(pazienti.getAllVaccianzioni(id).size() == 0 && vaccinazioni.size() == 0)
		    		throw new IllegalArgumentException("Il paziente deve avere almeno una vaccinazione");
		    	//aggiungo vaccinazione al paziente
		    	pazienti.updateListaVaccinazione(id, vaccinazioni);
		    	pazienti.updateReazione(id, reazione);
		    	
		    	
		    	
		    	//creaiamo la segnalazione che gestirà il sistema
		    	Segnalazione segnalazione = new Segnalazione(pazienti.getPatient(id), reazione, dataReazione.getValue());
		    	segnalazioni.addSignal(segnalazione);
		    	segnalazioni.addSegnalazioneToVaccinazione(id, segnalazione, vaccinazioni);
		    	segnalazioni.addSegnalazioneToReazione(id, segnalazione, reazione);
		    	segnalazioni.addSignalToPatient(id, segnalazione);
		    	
		    	//salvataggio dei dati
		    	pazienti.pazientiSerialize();
		    	segnalazioni.senalazioniSerialize();
		    	
				FXMLLoader loader = new FXMLLoader(getClass().getResource("View/MainSceneMedico.fxml"));
				root = loader.load();

				stage = (Stage)((Node)event.getSource()).getScene().getWindow();
				scene = new Scene(root);
				stage.setScene(scene);
				stage.show();
				stage.setMinWidth(480);
				stage.setMaxWidth(480);
				stage.setMinHeight(200);
				stage.setMaxHeight(200);
			}else {
				throw new IllegalArgumentException("Campi non completi");
			}
		} catch (IllegalArgumentException e) {
			new IllegalWindowException(e.toString());
		}
	}
	
	private boolean checkSoloReazione() {
		return dataReazione.getValue() != null && descrizioneReazField.getText() != "" &&  !combSceltaReazione.getSelectionModel().isEmpty()
				&& !combGravitaReazione.getSelectionModel().isEmpty() && dataVaccinazione.getValue() == null 
				&& combVaccino.getSelectionModel().isEmpty() &&
				combDose.getSelectionModel().isEmpty() &&  sedeField.getText() == "" && descrizioneReazField.getText().trim() != "";
	}
	
	public void reset(ActionEvent event) throws IOException {
		clearField();
	}

	public void annulla (ActionEvent event) throws IOException {
		//home
		if (checkExisting == false)
			pazienti.deletePatients(id);
		pazienti.pazientiSerialize();
		FXMLLoader loader = new FXMLLoader(getClass().getResource("View/MainSceneMedico.fxml"));
		root = loader.load();
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
		stage.setMinWidth(480);
		stage.setMaxWidth(480);
		stage.setMinHeight(200);
		stage.setMaxHeight(200);
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
