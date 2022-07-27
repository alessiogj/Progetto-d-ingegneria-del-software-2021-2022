package application.Model;



import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class PazienteDaoImpl implements Serializable, PazienteDao{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static List<Paziente> listaPazientiTotali = new ArrayList<>();
	private static String usr;
	
	public PazienteDaoImpl() {
		listaPazientiTotali = pazientiDeserialize();
	}
	
	public void setUsr(Credenziali credenziali) {
		usr = credenziali.getUsername();
	}
	
	public String getUsr() {
		return usr;
	}
	
	public void pazientiSerialize() {
		try {
			FileOutputStream fileOutputStream = 
					new FileOutputStream("src/application/Model/Files/pazienti.txt");
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
			objectOutputStream.writeObject(listaPazientiTotali);
		} catch (Exception e) {
			System.out.println(e);
		}
	}
	
	@Override
	public List<Paziente> getAllPatients() {
		return listaPazientiTotali;
	}

	@Override
	public void updateListaVaccinazione(int id, List<Vaccinazione> vaccinazioni) {
		for (Paziente paziente : listaPazientiTotali) {
			if( paziente.getId() == id)
				paziente.addVaccinazioni(vaccinazioni);
		}
	}

	@Override
	public void deletePatients(int id) {
		Paziente tmp = null;
		for (Paziente paziente : listaPazientiTotali) {
			if ( paziente.getId() == id) {
				tmp = paziente;
			}
		}
		if(tmp != null)
			listaPazientiTotali.remove(tmp);
	}

	@Override
	public Paziente getPatient(int id) {
		for (Paziente paziente : listaPazientiTotali) {
			if (paziente.getId() == id)
				return paziente;
		}
		throw new IllegalArgumentException("Paziente non esistente");
		
	}

	@Override
	public List<Vaccinazione> getAllVaccianzioni(int id) {
		for (Paziente paziente : listaPazientiTotali) {
			if( paziente.getId() == id)
				return paziente.getVaccinazioni();
		}
		return new ArrayList<>();
	}

	@Override
	public List<Reazione> getAllReazioni(int id) {
		for (Paziente paziente : listaPazientiTotali) {
			if( paziente.getId() == id)
				return paziente.getReazioni();
		}
		return new ArrayList<>();
	}

	@Override
	public List<FattoreDiRischio> getAllFattoriDiRischio(int id) {
		for (Paziente paziente : listaPazientiTotali) {
			if( paziente.getId() == id)
				return paziente.getFattoriDiRischio();
		}
		return new ArrayList<>();
	}

	@Override
	public void updateReazione(int id, Reazione reazione) {
		for (Paziente paziente : listaPazientiTotali) {
			if( paziente.getId() == id)
				paziente.addReazione(reazione);
		}
	}

	@Override
	public void updateListaRischi(int id, List<FattoreDiRischio> fattoriDiRischio) {
		for (Paziente paziente : listaPazientiTotali) {
			if( paziente.getId() == id) {
				paziente.addFattoriDiRischio(fattoriDiRischio);
			}
		}
	}

	@Override
	public void addPatient(Paziente paziente) {
		listaPazientiTotali.add(paziente);
	}

	@Override
	public boolean containsPatient(int id) {
		for (Paziente x : listaPazientiTotali) {
			if(x.getId() == id)
				return true;
		}
		return false;
	}
	
	public String toString() {
		String res ="";
		for (Paziente x : listaPazientiTotali) {
			if(this.getUsr().equals(x.getIdMedico()))
				res += x.toString() + "\n\n";
		}
		return res;
	}
	
	public String toStringIdPazienti() {
		String res ="";
		for (Paziente x : listaPazientiTotali) {
			if(this.getUsr().equals(x.getIdMedico()))
				res += x.getId() + "\n\n";
		}
		return res;
	}
	
	public String toStringPaziente(int paziente) {
		String res="";
		for (Paziente x : listaPazientiTotali) {
			if(this.getUsr().equals(x.getIdMedico()) && x.getId() == paziente)
				res = x.toString() + "\n\n";
		}
		return res;
	}
	
	private List<Paziente> pazientiDeserialize() {
		try {
			FileInputStream fileInputStream = 
					new FileInputStream("src/application/Model/Files/pazienti.txt");
			ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
			//List<Paziente> pazienti = new ArrayList<>();
			return (List<Paziente>) objectInputStream.readObject();
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
