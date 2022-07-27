package application.Model;

import java.util.List;


public interface PazienteDao {
	
		public List<Paziente> getAllPatients();
		public Paziente getPatient(int id);
		public List<Vaccinazione> getAllVaccianzioni(int id);
		public List<Reazione> getAllReazioni(int id);
		public List<FattoreDiRischio> getAllFattoriDiRischio(int id);
		public void addPatient(Paziente paziente);
		public void updateListaVaccinazione(int id, List<Vaccinazione> vaccini);
		public void updateReazione(int id, Reazione reazione);
		public void updateListaRischi(int id, List<FattoreDiRischio> fattoriDiRischio);
		public boolean containsPatient(int id);
		public void deletePatients(int id);
}
