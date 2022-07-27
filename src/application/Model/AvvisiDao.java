package application.Model;

import java.util.List;

public interface AvvisiDao {
	public List<Avviso> getAvvisiNonLetti();
	public List<Avviso> getAvvisiLetti();
	public void aggiungiAvvisoNonLetto(Avviso adv);
	public void aggiungiAvvisoLetto(Avviso adv);
	public void leggiAvvisi();
}
