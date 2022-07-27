package Test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

import application.Model.AvvisiDaoImpl;
import application.Model.Credenziali;
import application.Model.DoseVaccino;
import application.Model.Paziente;
import application.Model.PazienteDaoImpl;
import application.Model.Reazione;
import application.Model.RiskLevel;
import application.Model.Segnalazione;
import application.Model.SegnalazioneDaoImpl;
import application.Model.TipoReazione;
import application.Model.Vaccinazione;
import application.Model.VaccinoTipo;

public class GeneraDati {

	public static void main(String[] args) {
		List<String> province = new ArrayList<>();
		Random rnd = new Random();
		String username="";
		String check="";
		
		try (Scanner scanner = new Scanner(System.in)) {
			System.out.print("Sicuro di sovrascrivere i dati (si/no)? ");
			check = scanner.nextLine();
			System.out.print("Inserisci l'username del medico (M123/M222): ");
			username = scanner.nextLine();
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		if (check.equals("si")) {
			try (Scanner scanner = new Scanner(System.in)) {
				System.out.print("Inserisci l'username del medico (M123/M222): ");
				username = scanner.nextLine();
			} catch (Exception e) {
				// TODO: handle exception
			}
			
			try(BufferedWriter sovrascriviDati = new BufferedWriter(new FileWriter("src/application/Model/FIles/pazienti.txt"))){
				sovrascriviDati.write("");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try(BufferedWriter sovrascriviDati = new BufferedWriter(new FileWriter("src/application/Model/FIles/segnalazioni.txt"))){
				sovrascriviDati.write("");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try(BufferedWriter sovrascriviDati = new BufferedWriter(new FileWriter("src/application/Model/FIles/avvisiLetti.txt"))){
				sovrascriviDati.write("");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try(BufferedWriter sovrascriviDati = new BufferedWriter(new FileWriter("src/application/Model/FIles/avvisiNonLetti.txt"))){
				sovrascriviDati.write("");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			try(BufferedReader csvReader = new BufferedReader(new FileReader("src/application/Model/Files/province-sigle.csv"))) {
				String r;
				while ((r = csvReader.readLine()) != null) {
				    province.add(r);
				}
			}catch (Exception e) {
				System.out.println(e);
			}
			PazienteDaoImpl pazienti = new PazienteDaoImpl();
			SegnalazioneDaoImpl segnalazioni = new SegnalazioneDaoImpl();
			AvvisiDaoImpl avvisi = new AvvisiDaoImpl();
			pazienti.setUsr(new Credenziali(username, ""));
			for (int i = 0; i < 101; i++) {
				long minDay = LocalDate.of(1970, 1, 1).toEpochDay();
			    long maxDay = LocalDate.of(2000, 12, 31).toEpochDay();
			    long randomBirth = ThreadLocalRandom.current().nextLong(minDay, maxDay);
			    
			    String job = givenUsingJava8_whenGeneratingRandomAlphabeticString_thenCorrect();
				Paziente tmp = new Paziente(i, LocalDate.ofEpochDay(randomBirth),job, province.get(rnd.nextInt(15)));
				pazienti.addPatient(tmp);
				pazienti.pazientiSerialize();
				minDay = LocalDate.of(2022, 6, 10).toEpochDay();
			    maxDay = LocalDate.of(2022, 6, 22).toEpochDay();
			    long randomVaccinazione = ThreadLocalRandom.current().nextLong(minDay, maxDay);
			    String descrizione = givenUsingJava8_whenGeneratingRandomAlphabeticString_thenCorrect();
			    //prima vaccinazione
				Vaccinazione vaccino = new Vaccinazione(i, 
						LocalDate.ofEpochDay(randomVaccinazione),
						VaccinoTipo.values()[rnd.nextInt(4)],
						DoseVaccino.IV,
						descrizione);
				List<Vaccinazione> vaccinazioni = new ArrayList<>();
				vaccinazioni.add(vaccino);
				//seconda vaccinazione
				minDay = LocalDate.of(2022, 6, 1).toEpochDay();
			    maxDay = LocalDate.of(2022, 6, 9).toEpochDay();
			    randomVaccinazione = ThreadLocalRandom.current().nextLong(minDay, maxDay);
				vaccino = new Vaccinazione(i, 
						LocalDate.ofEpochDay(randomVaccinazione),
						VaccinoTipo.values()[rnd.nextInt(4)],
						DoseVaccino.III,
						descrizione);
				vaccinazioni.add(vaccino);
				//terza vaccinazione
				minDay = LocalDate.of(2022, 5, 20).toEpochDay();
			    maxDay = LocalDate.of(2022, 5, 30).toEpochDay();
			    randomVaccinazione = ThreadLocalRandom.current().nextLong(minDay, maxDay);
				vaccino = new Vaccinazione(i, 
						LocalDate.ofEpochDay(randomVaccinazione),
						VaccinoTipo.values()[rnd.nextInt(4)],
						DoseVaccino.II,
						descrizione);
				vaccinazioni.add(vaccino);
				//quarta vaccinazione
				minDay = LocalDate.of(2022, 5, 10).toEpochDay();
			    maxDay = LocalDate.of(2022, 5, 19).toEpochDay();
			    randomVaccinazione = ThreadLocalRandom.current().nextLong(minDay, maxDay);
				vaccino = new Vaccinazione(i, 
						LocalDate.ofEpochDay(randomVaccinazione),
						VaccinoTipo.values()[rnd.nextInt(4)],
						DoseVaccino.I,
						descrizione);
				vaccinazioni.add(vaccino);
				pazienti.updateListaVaccinazione(i, vaccinazioni);
				pazienti.pazientiSerialize();
				minDay = LocalDate.of(2022, 6, 22).toEpochDay();
				maxDay = LocalDate.of(2022, 7, 22).toEpochDay();
				long randomReazione = ThreadLocalRandom.current().nextLong(minDay, maxDay);
				Reazione reazione = new Reazione(i, 
						TipoReazione.values()[rnd.nextInt(6 - 2) + 2 ],
						LocalDate.ofEpochDay(randomReazione),
						RiskLevel.values()[rnd.nextInt(4)], descrizione);
				pazienti.updateReazione(i, reazione);
				Segnalazione segn = new Segnalazione(tmp, reazione, LocalDate.ofEpochDay(randomReazione));
				segnalazioni.addSignal(segn);
				segnalazioni.addSegnalazioneToReazione(i, segn, reazione);
				segnalazioni.addSegnalazioneToVaccinazione(i, segn, vaccinazioni);
				segnalazioni.addSignalToPatient(i, segn);
				System.out.println("Aggiunto il paziente: " + i);
				segnalazioni.senalazioniSerialize();
				pazienti.pazientiSerialize();
				avvisi.avvisiSerialize();
			}
			System.out.println("Ho aggiunto tutti i pazienti");
		}else {
			System.out.println("Non modifico i pazienti");
		}
	}

	public static String givenUsingJava8_whenGeneratingRandomAlphabeticString_thenCorrect() {
	    int leftLimit = 97; // letter 'a'
	    int rightLimit = 122; // letter 'z'
	    int targetStringLength = 10;
	    Random random = new Random();

	    return random.ints(leftLimit, rightLimit + 1)
	      .limit(targetStringLength)
	      .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
	      .toString();
	}
}
