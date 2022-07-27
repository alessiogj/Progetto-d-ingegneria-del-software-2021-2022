package application.Model;

import java.io.FileReader;

import com.google.gson.Gson;

public class Credenziali {
	private final String username;
	private final String password;
	
	public Credenziali(String username, String password){
		this.username=username;
		this.password=password;
	}
	
	public String getUsername() {
		return username;
	}
	
	public String getPassword() {
		return password;
	}
	public String toString() {
		return "username: " + getUsername() + ", " + "password: " + getPassword();
	}
	
	//legge dal json relativo al login e ritorna un array di credenziali predefinite dal sistema
	public static Credenziali[] getCredentials() {
		try (FileReader scanner  = new FileReader ("src/application/Model/Files/login.json")){
			return new Gson().fromJson(scanner, Credenziali[].class);
		}catch (Exception e) {
			throw new IllegalAccessError();
		}
	}
}
