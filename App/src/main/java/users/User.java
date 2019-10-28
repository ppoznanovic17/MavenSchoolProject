package users;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

import org.json.simple.JSONObject;

public class User {
	
	private String username;
	private String password;
	private List<String> privileges;
	
	public User(String username, String password) {
		super();
		this.username = username;
		this.password = password;
		
	}

	public User() {
		
	}
	
	public void showMenuforUser(String path) {
		int cnt;
		BufferedReader reader=new BufferedReader(new InputStreamReader(System.in));
		StringBuilder sb=new StringBuilder();
		//String korisnik=main.get("Username").toString();
		//System.out.println(korisnik);
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
		if(privileges.contains("upload_file")) {
			cnt=3;
			sb.append("\n");
			sb.append("Izaberite "+cnt+" za dodavanje novog fajla");
		}
		if(privileges.contains("download_file")) {
			cnt=4;
			sb.append("\n");
			sb.append("Izaberite "+cnt+" za preuzimanje fajla");
		}
		if(privileges.contains("search_repository")) {
			cnt=5;
			sb.append("\n");
			sb.append("Izaberite "+cnt+" za pretragu repozitorijuma");
		}
		if(privileges.contains("delete_file")) {
			cnt=6;
			sb.append("\n");
			sb.append("Izaberite "+cnt+" za brisanje fajla");
		}
		if(privileges.contains("add_user")) {
			cnt=7;
			sb.append("\n");
			sb.append("Izaberite "+cnt+" za dodavanje zabranjenih ekstenzija");
		}
		
		sb.append("\n");
		sb.append("Izaberite 'ls' za prikaz sadrzine trenutnog foldera");
		sb.append("\n");
		sb.append("Izaberite 'cd' <folder name> ili <..> za kretanje po fajlu");
		sb.append("\n"+path+" >");
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
