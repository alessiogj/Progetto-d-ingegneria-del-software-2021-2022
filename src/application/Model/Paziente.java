package application.Model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Paziente implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private static List<Integer> listaAllID = new ArrayList<>(); //lista di identificativi univoci
	private final String idMedico; //identificativo del medico che ha a carico i pazienti
	private final int id; //identificiativo del paziente
	private final LocalDate dataNascita; //data di nascita del paziente
	private final String professione; //professione del paziente
	private final String provinvcia; // provincia del paziente
	private List<Vaccinazione> listaVaccinazioni = new ArrayList<>(); //lista di tutte le vaccinazioni del paziente
	private List<FattoreDiRischio> fattoriDiRischio = new ArrayList<>();
	private List<Reazione> reazioniPaziente = new ArrayList<>(); 
	private List<Segnalazione> listaSegnalazioni = new ArrayList<>();
	private static PazienteDaoImpl pazienti = new PazienteDaoImpl();
	private int numeroVaccinazioni=0;
	private int numeroReazioniAvverse=0;
	
	public Paziente(int id, LocalDate dataNascita, String professione, String provincia) {
		//l'identificativo del paziente deve essere univoco tra tutti i pazienti
		if(pazienti.containsPatient(id))
			throw new IllegalArgumentException("Paziente esistente");
		listaAllID.add(id);
		this.id = id;
		//controlliamo la data di nascita che non sia dopo la data odierna
		if (dataNascita.isAfter(LocalDate.now()))
			throw new IllegalArgumentException("La data di nascita non può essere successiva alla data odierna");
		this.dataNascita = dataNascita;
		this.professione = professione;
		this.provinvcia = provincia;
		this.idMedico = new PazienteDaoImpl().getUsr();		
	}
	
	public String getIdMedico() {
		return idMedico;
	}
	
	public LocalDate birthDay() {
		return dataNascita;
	}
	
	public int getNumVaccinazioni() {
		return numeroVaccinazioni;
	}
	
	public String getProvincia() {
		return provinvcia;
	}
	
	public void addFattoriDiRischio(List<FattoreDiRischio> fattori) {
		this.fattoriDiRischio.addAll(fattori);
	}

	public void addFattoreDiRischio(FattoreDiRischio fattore) {
		this.fattoriDiRischio.add(fattore);
	}
	
	public void addSegnalazione(Segnalazione segnalazione) {
		this.listaSegnalazioni.add(segnalazione);
	}
	
	public List<Segnalazione> getSegnalazioni(){
		return listaSegnalazioni;
	}
	
	public void addReazioni(List<Reazione> reazioni) {
		this.reazioniPaziente.addAll(reazioni);
		numeroReazioniAvverse += reazioni.size();
	}
	
	public void addReazione(Reazione reazione) {
		for (Reazione x : reazioniPaziente) {
			if(x.equals(reazione) && x.getDataReazione().equals(reazione.getDataReazione()))
				throw new IllegalArgumentException("La reazione aggiunta è presente per tale paziente");
		}
		this.reazioniPaziente.add(reazione);
		numeroReazioniAvverse++;	
	}
	
	public void addVaccinazioni(List<Vaccinazione> vaccini) {
		this.listaVaccinazioni.addAll(vaccini);
		numeroVaccinazioni += vaccini.size();
	}
	
	public List<Vaccinazione> getVaccinazioni() {
		return listaVaccinazioni;
	}
	
	public List<Reazione> getReazioni(){
		return reazioniPaziente;
	}
	
	public List<FattoreDiRischio> getFattoriDiRischio(){
		return fattoriDiRischio;
	}

	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		return obj instanceof Paziente && this.id == ((Paziente)obj).id;
	}

	public String toString() {
		String res = "";
		res+= "Medico: " + idMedico;
		res+="\nID: " + id;
		res+="\nData di nascita: " + dataNascita;
		res+="\nProfessione: " + professione;
		res+="\nProvincia: " + provinvcia;
		if(fattoriDiRischio.size()!=0)
			res+="\nI FATTORI DI RISCHIO SONO:\n";
		for (FattoreDiRischio x : fattoriDiRischio) {
			res += x.toString() + "\n";
		}
		
		res+="\nLE VACCINAZIONI SONO: " + numeroVaccinazioni + "\n";
		for (Vaccinazione x : listaVaccinazioni) {
			res += x.toString() + "\n";
		}
		
		res+="\nLE REAZIONI SONO: " + numeroReazioniAvverse+ "\n";
		for (Reazione x : reazioniPaziente) {
			res += x.toString() + "\n";
		}
		return res;
	}
	
	public int getId() {
		return id;
	}

}
