package application.Model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


public class Vaccinazione implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final LocalDate dataVaccino;
	private final VaccinoTipo tipovaccino;
	private final DoseVaccino numDose;
	private final String sede;
	private final int idPaziente;
	private List<Segnalazione> segnalazioni = new ArrayList<>();
	
	public Vaccinazione(int idPaziente, LocalDate dataVaccino, VaccinoTipo tipoVaccino, DoseVaccino numDose, String sede) {
		this.dataVaccino = dataVaccino;
		this.tipovaccino = tipoVaccino;
		this.numDose = numDose;
		this.sede = sede;
		this.idPaziente=idPaziente;
	}
	
	public void addSegnalazioni(List<Segnalazione> listaSegnalazioni) {
		segnalazioni.addAll(listaSegnalazioni);
	}
	
	public void addSegnalazione(Segnalazione segnalazione) {
		segnalazioni.add(segnalazione);
	}
	
	/**
	 * Due vaccini sono uguali se sono stati fatti lo stesso giorno, la stessa data, hanno stessa tipologia e nella stessa sede
	 */
	@Override
	public boolean equals(Object obj) {
		return obj instanceof Vaccinazione && dataVaccino.equals(((Vaccinazione)obj).dataVaccino) && 
				tipovaccino.ordinal() == ((Vaccinazione)obj).tipovaccino.ordinal()
				&& sede.equals(((Vaccinazione)obj).sede);
	}
	
	@Override
	public String toString() {
		String res = "Vaccinazione del " + dataVaccino.toString() + " fatta a: " + sede +
				" Dose n° " + numDose + ", " + tipovaccino;
		return res;
	}
	
	public String getDate() {
		return dataVaccino.toString();
	}
	
	public LocalDate getDataVaccinazione() {
		return dataVaccino;
	}
	
	public VaccinoTipo getVaccinazione() {
		return tipovaccino;
	}
	
	public DoseVaccino getDose() {
		return numDose;
	}
	
	public String sede() {
		return sede;
	}
}
