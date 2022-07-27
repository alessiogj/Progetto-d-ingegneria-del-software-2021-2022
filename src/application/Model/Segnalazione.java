package application.Model;



import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.time.temporal.ChronoUnit;

public class Segnalazione implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static int UID = 1; 
	private final Paziente paziente;
	private final int id;
	private final Reazione reazioneAvversa;
	private final LocalDate dataReazioneAvversa;
	private final LocalDate dataSegnalazione;
	private List<Vaccinazione> vaccinazioniNeiDueMesi = new ArrayList<>();
	
	public Segnalazione(Paziente paziente, Reazione reazioneAvversa, LocalDate dataReazioneAvversa) {
		
		if( new SegnalazioneDaoImpl().getAllSignal().size() == 0) {
			id = 1;
		}else {
			id = new SegnalazioneDaoImpl().getAllSignal().get(new SegnalazioneDaoImpl().getAllSignal().size() - 1).id + 1;
			UID = id + 1;
		}
		this.paziente = paziente;
		this.reazioneAvversa = reazioneAvversa;
		this.dataReazioneAvversa = dataReazioneAvversa;
		this.dataSegnalazione = LocalDate.now();
		calcoloMesi();
	}
	
	public Paziente getPatient() {
		return paziente;
	}
	
	public Reazione getReazione() {
		return reazioneAvversa;
	}
	
	public List<Vaccinazione> getVaccinazioniDueMesi(){
		return vaccinazioniNeiDueMesi;
	}

	public LocalDate getDataReazione() {
		return dataReazioneAvversa;
	}
	
	public int calcoloNumeroGravita(){
		return reazioneAvversa.getRiskLevel().ordinal();
	}

	private void calcoloMesi() {
		for (Vaccinazione x : paziente.getVaccinazioni()) {
			if (ChronoUnit.DAYS.between(x.getDataVaccinazione(), reazioneAvversa.getDataReazione()) < 60 &&
					ChronoUnit.DAYS.between(x.getDataVaccinazione(), reazioneAvversa.getDataReazione()) > 0) {
				vaccinazioniNeiDueMesi.add(x);
			}
		}
	}
	
	public String toString() {
		String res = "Segnalazione numero: " + id + ", del paziente " + paziente.getId() + 
				"\nresidete a:" + paziente.getProvincia() + 
				"\nha avuto la seguente reazione: " + reazioneAvversa 
				+ "\nnella data: " + dataReazioneAvversa + ","
				+ "\nsegnalata il: " + dataSegnalazione + "\n";
		if (vaccinazioniNeiDueMesi.size()!=0)
			res += "LE VACCINAZIONI NEI MESI PRECEDENTI SONO STATE:\n";
		for (Vaccinazione vaccinazione : vaccinazioniNeiDueMesi) {
			res+= vaccinazione + "\n";
		}
		return res;
	}
	
}
