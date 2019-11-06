package users;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

import org.json.simple.JSONObject;

import file.manipulation.Storage;
/**
 * Klasa User definise jednog korisnika prema njegovim parametrima koje nasledjuje iz specifikacije. U ovoj klasi se nalazi metoda 
 * showMenuforUser kao i geteri i seteri za polja koja definisu usera
 * 
 * @author Bogdan Stojadinovic
 * @author Petar Poznanovic
 *
 */
public class User extends user.User {
	
	
	
	public User(String username, String password) {
		super();
		super.username = username;
		super.password = password;
		
	}

	public User() {
		
	}
	/**
	 * Metoda koja prikazuje meni na konzolnoj aplikaciji za jedinstvenog korisnika u zavisnosti od privilegija koje poseduje
	 * @param path
	 * @param s
	 */
	public void showMenuforUser(String path, Storage s) {
		int cnt;
		BufferedReader reader=new BufferedReader(new InputStreamReader(System.in));
		StringBuilder sb=new StringBuilder();
		
		System.out.println("--------------------------------------------------------------------------------------------------------------------------------------------");
		sb.append("**********Trenutna lokacija:" + path+"**********\n");
		if(privileges.contains("add_user")) {
			cnt=1;
			
			sb.append("Izaberite "+cnt+" za dodavanje novog korisnika");
			
		}
		if(privileges.contains("add_directory")) {
			cnt=2;
			sb.append("\n");
			sb.append("Izaberite "+cnt+" za dodavanje novog foldera");
		}
		if(privileges.contains("add_file")) {
			cnt=3;
			sb.append("\n");
			sb.append("Izaberite "+cnt+" za dodavanje novog fajla");
		}
		if(privileges.contains("download")) {
			cnt=4;
			sb.append("\n");
			sb.append("Izaberite "+cnt+" za preuzimanje fajla");
		}
		if(privileges.contains("upload")) {
			cnt=5;
			sb.append("\n");
			sb.append("Izaberite "+cnt+ " za upload fajla");
		}
		if(privileges.contains("search_repository")) {
			cnt=6;
			sb.append("\n");
			sb.append("Izaberite "+cnt+" za pretragu repozitorijuma");
		}
		if(privileges.contains("delete_file")) {
			cnt=7;
			sb.append("\n");
			sb.append("Izaberite "+cnt+" za brisanje fajla");
		}
		if(privileges.contains("add_user")) {
			cnt=8;
			sb.append("\n");
			sb.append("Izaberite "+cnt+" za dodavanje zabranjenih ekstenzija");
		}
		if(privileges.contains("download") && s.getType().equals("local")) {
			cnt=9;
			sb.append("\n");
			sb.append("Izaberite "+cnt+" za zipovnje fajlova");
		}
		if(privileges.contains("upload") && s.getType().equals("local")) {
			cnt=10;
			sb.append("\n");
			sb.append("Izaberite "+cnt+" za anzipovanje fajlova");
		}
		
		sb.append("\n");
		sb.append("Izaberite 'ls' za prikaz sadrzine trenutnog foldera");
		sb.append("\n");
		sb.append("Izaberite 'cd' <folder name> ili <..> za kretanje po fajlu");
		//sb.append("\n"+path+" >");
		System.out.print(sb.toString());
		
	}
	

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public List<String> getPrivileges() {
		return privileges;
	}

	public void addPrevilege(String p) {
		this.privileges.add(p);
	}
	 public void setPrivileges(List<String> privileges) {
		this.privileges = privileges;
	}
	
	@Override
	public String toString() {
		String priv="";
		for(int i=0; i<privileges.size();i++) {
			priv+=privileges.get(i)+", ";
		}
		return "username: "+username+" |  password: " +password+ "    "+priv;
	}
}
