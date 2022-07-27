package application.Model;


import java.io.Serializable;
import java.time.LocalDate;

public class Avviso implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String avviso;
	private LocalDate data;
	public Avviso(String avviso) {
		this.avviso = avviso;
		this.data = LocalDate.now();
	}
	
	public LocalDate getData() {
		return data;
	}
	
	public String getAvviso() {
		return avviso;
	}
	
	public String toString() {
		return avviso + " in data " + data;
	}

}
