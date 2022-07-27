package application.Model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Reazione implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private TipoReazione reazione;
	private LocalDate dataSegnalazione;
	private LocalDate dataReazione;
	private RiskLevel gravita;
	private String descrizione;
	private int id;
	private List<Segnalazione> segnalazioni = new ArrayList<>();
	
	public Reazione(int id, TipoReazione reazione,LocalDate dataReazione,RiskLevel gravita, String descrizione) {
		this.reazione = reazione;
		this.id = id;
		this.dataSegnalazione = LocalDate.now();
		if (dataReazione.isAfter(dataSegnalazione))
			throw new IllegalArgumentException("La data della segnalazione è precedente alla reazione");
		this.dataReazione = dataReazione;
		
		this.gravita = gravita;
		this.descrizione = descrizione;
	}
	
	public void addSegnalazione(Segnalazione segnalazione) {
		segnalazioni.add(segnalazione);
	}
	
	//due reazioni sono uguali se hanno la stessa reazione
	@Override
	public boolean equals(Object obj) {
		return obj instanceof Reazione && this.reazione.ordinal() == ((Reazione) obj).reazione.ordinal();
	}
	
	public TipoReazione getReazione() {
		return reazione;
	}
	
	public LocalDate getDataReazione() {
		return dataReazione;
	}
	
	public RiskLevel getRiskLevel() {
		return gravita;
	}
	
	public String toString() {
		String res= reazione.toString().replace('_', ' ') + ", " + dataSegnalazione + ", " + dataReazione + ", " + gravita + ", " + descrizione;
				 //"Le segnalazioni relative a tale reazione sono:\n";
		return res;	
	}
}
