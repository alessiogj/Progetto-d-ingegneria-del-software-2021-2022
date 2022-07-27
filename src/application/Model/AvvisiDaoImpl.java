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

public class AvvisiDaoImpl implements AvvisiDao, Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static List<Avviso> avvisiNonLetti = new ArrayList<>();
	private static List<Avviso> avvisiLetti = new ArrayList<>();
	
	public AvvisiDaoImpl() {
		avvisiNonLetti = avvisiNonLettiDeserialize();
		avvisiLetti = avvisiLettiDeserialize();
	}
	
	public void avvisiSerialize() {
		avvisiLettiSerialize();
		avvisiNonLettiSerialize();
	}
	
	private void avvisiNonLettiSerialize() {
		try {
			FileOutputStream fileOutputStream = 
					new FileOutputStream("src/application/Model/Files/avvisiNonLetti.txt");
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
			objectOutputStream.writeObject(avvisiNonLetti);
		} catch (Exception e) {
			System.out.println(e);
		}
	}
	
	private List<Avviso> avvisiLettiDeserialize() {
		try {
			FileInputStream fileInputStream = 
					new FileInputStream("src/application/Model/Files/avvisiLetti.txt");
			ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
			return (List<Avviso>) objectInputStream.readObject();
			
		} catch (EOFException e) {
			return new ArrayList<>();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new ArrayList<>();
	}
	
	private void avvisiLettiSerialize() {
		try {
			FileOutputStream fileOutputStream = 
					new FileOutputStream("src/application/Model/Files/avvisiLetti.txt");
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
			objectOutputStream.writeObject(avvisiLetti);
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	private List<Avviso> avvisiNonLettiDeserialize() {
		try {
			FileInputStream fileInputStream = 
					new FileInputStream("src/application/Model/Files/avvisiNonLetti.txt");
			ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
			return (List<Avviso>) objectInputStream.readObject();
		} catch (EOFException e) {
			return new ArrayList<>();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new ArrayList<>();
	}
	
	@Override
	public List<Avviso> getAvvisiNonLetti() {
		return avvisiNonLetti;
	}

	@Override
	public List<Avviso> getAvvisiLetti() {
		return avvisiLetti;
	}

	@Override
	public void aggiungiAvvisoNonLetto(Avviso adv) {
		avvisiNonLetti.add(adv);
	}

	@Override
	public void aggiungiAvvisoLetto(Avviso adv) {
		avvisiLetti.add(adv);
	}

	public String toStringAvvisiNonLetti() {
		String res ="";
		for (Avviso avviso : avvisiNonLetti) {
			res +=avviso;
		}
		return res;
	}

	public String toStringAvvisiLetti() {
		String res ="";
		for (Avviso avviso : avvisiLetti) {
			res +=avviso;
		}
		return res;
	}

	@Override
	public void leggiAvvisi() {
		avvisiLetti.addAll(avvisiNonLetti);
		avvisiNonLetti.clear();
	}
}
