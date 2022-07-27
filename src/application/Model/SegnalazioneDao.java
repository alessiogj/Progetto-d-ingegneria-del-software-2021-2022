package application.Model;

import java.util.List;

public interface SegnalazioneDao {
	
	public List<Segnalazione> getAllSignal();
	public void addSignal(Segnalazione segnalazione);
	public void addSegnalazioneToReazione(int id, Segnalazione segnalazione, Reazione reazione);
	public void addSegnalazioneToVaccinazione(int id, Segnalazione segnalazione, List<Vaccinazione> vaccinazioni);
	public void addSignalToPatient(int id, Segnalazione segnalazione);
	public int calcoloSegnalazioniGravi();
	public int numeroSegnalazioni();
	public int numeroSegnalazioniMensili();
	
}
