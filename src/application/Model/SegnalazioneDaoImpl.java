package application.Model;



import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;



public class SegnalazioneDaoImpl implements SegnalazioneDao, Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static List<Segnalazione> segnalazioni = new ArrayList<>();
	private static PazienteDaoImpl pazienti = new PazienteDaoImpl();
	private static AvvisiDaoImpl avvisi = new AvvisiDaoImpl();
	
	public SegnalazioneDaoImpl() {
		segnalazioni = segnalazioniDeserialize();
	}

	public void senalazioniSerialize() {
		try {
			FileOutputStream fileOutputStream = 
					new FileOutputStream("src/application/Model/Files/segnalazioni.txt");
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
			objectOutputStream.writeObject(segnalazioni);
		} catch (Exception e) {
			System.out.println(e);
		}
	}
	
	@Override
	public List<Segnalazione> getAllSignal() {
		return segnalazioni;
	}

	@Override
	public int calcoloSegnalazioniGravi() {
		int contGravita=0;
		for (Segnalazione x : segnalazioni) {
			if (x.calcoloNumeroGravita() >= 2 && ChronoUnit.DAYS.between(x.getDataReazione(), LocalDate.now()) <= 7) {
				contGravita++;
			}
		}
		return contGravita;
	}



	@Override
	public void addSignal(Segnalazione segnalazione) {
		segnalazioni.add(segnalazione);	
		//ogni volta che aggiungo segnalazioni verifico se devono essere registrati avvisi per il farmacologo
		if(this.numeroSegnalazioniMensili() % 5 == 0 
				&& this.numeroSegnalazioniMensili() != 0 
				&& segnalazione.getReazione().getRiskLevel().ordinal() >= 2)
			avvisi.aggiungiAvvisoNonLetto(new Avviso("Segnalazioni gravi segnalate"));
		if(this.numeroSegnalazioni() % 50 == 0 
				&& this.numeroSegnalazioni() != 0)
			avvisi.aggiungiAvvisoNonLetto(new Avviso("Sono presenti 50 segnalazioni"));
		avvisi.avvisiSerialize();
	}

	@Override
	public void addSegnalazioneToReazione(int id, Segnalazione segnalazione, Reazione reazione) {
		//se le reazioni di un paziente corrispondono alla reazione appena aggiunta allora l'inserisco
		for(Reazione x: pazienti.getAllReazioni(id)) {
			if(x.equals(reazione)) {
				x.addSegnalazione(segnalazione);
			}
		}	
	}

	@Override
	public void addSegnalazioneToVaccinazione(int id, Segnalazione segnalazione, List<Vaccinazione> vaccinazioni) {
		for (Vaccinazione vaccinazione : vaccinazioni) {
			//se la vaccinazione di un paziente corrisponde alla vaccinazione appena inserita allora
			//aggiungo la segnalazione
			for (Vaccinazione x : pazienti.getAllVaccianzioni(id)) {
				if(x.equals(vaccinazione)) {
						x.addSegnalazione(segnalazione);
				}
			}
		}
	}
	
	public String toString() {
		String res = "";
		for (Segnalazione segnalazione : segnalazioni) {
			res += segnalazione;
		}
		return res;
	}

	@Override
	public int numeroSegnalazioni() {
		int cont=0;
		for (Segnalazione segnalazione : segnalazioni) {
			cont++;
		}
		return cont;
	}
	
	@Override
	public int numeroSegnalazioniMensili() {
		int cont=0;
		for (Segnalazione segnalazione : segnalazioni) {
			if (ChronoUnit.DAYS.between(segnalazione.getDataReazione(), LocalDate.now()) <= 30)
				cont++;
		}
		return cont;
	}

	@Override
	public void addSignalToPatient(int id, Segnalazione segnalazione) {
		for (Paziente x : pazienti.getAllPatients()) {
			if (x.getId() == id) {
				x.addSegnalazione(segnalazione);
			}
		}		
	}

	private List<Segnalazione> segnalazioniDeserialize() {
		try {
			FileInputStream fileInputStream = 
					new FileInputStream("src/application/Model/Files/segnalazioni.txt");
			ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
			return (List<Segnalazione>) objectInputStream.readObject();
		} catch (EOFException e) {
			return new ArrayList<>();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new ArrayList<>();
	}
	
}
