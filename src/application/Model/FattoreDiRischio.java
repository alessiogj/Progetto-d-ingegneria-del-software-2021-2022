package application.Model;

import java.io.Serializable;

public class FattoreDiRischio implements Serializable{

	private static final long serialVersionUID = 1L;
	private final String descrizione;
	private final RiskLevel level;
	private final Rischi name;
	
	public FattoreDiRischio(Rischi name, String descrizione, RiskLevel level) {
		this.name = name;
		this.descrizione = descrizione;
		this.level = level;
	}

	private int compareTo(FattoreDiRischio o) {
		return this.level.ordinal() - o.level.ordinal();
	}
	
	//due fattori di rischio sono uguali per una persona se hanno lo stesso nome
	@Override
	public boolean equals(Object obj) {
		return obj instanceof FattoreDiRischio && this.name.equals(((FattoreDiRischio) obj).name);
	}
	
	public Rischi getRischio() {
		return name;
	}
	
	public String toString() {
		return name.toString().replace('_', ' ') + ", " + level + ", " + descrizione;
	}
}
