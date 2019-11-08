package console.app;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;

import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;


import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import net.lingala.zip4j.ZipFile;
import users.User;
/**
 * Klasa ConsoleFunctions predstavlja klasu koja sluzi kao pomocna klasa konzolnoj aplikaciji. U ovoj klasi se nalaze metode za
 * koje konzolna aplikacija treba da izvrsi nakon sto se aplikacija pokrene.
 * 
 * @author Bogdan Stojadinovic
 * @author Petar Poznanovic
 */
public class ConsoleFunctions {
	//Arrays.asList("add_user","add_directory","upload_file","download_file","search_repository","delete_file");
	public static BufferedReader reader=new BufferedReader(new InputStreamReader(System.in));
	
	public static void write(String message) {
		System.out.println(message);
	}
	/**
	 * Metoda za interakciju sa korisnikom preko konzole. Metoda question prima string koji aplikacija salje korisniku, a vraca 
	 * odgovor koji je korisnik napisao u konzolu.
	 * @param message
	 *  Obican string koji ova metoda prima iz glavne konzolne aplikacije u Main metodi.
	 * @return
	 * Ova metoda vraca string koji predstavlja odgovor koji je korisnik dao na pitanje iz parametra message.
	 */
	public static String question(String message) {
		System.out.println(message);
		String answer="";
		try {
			answer=reader.readLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return answer;
		
	}
	/**
	 * Metoda login sluzi za autentifikaciju korisnika iz users.json fajla
	 * @param f
	 * @param username
	 * @param password
	 * @return
	 * @throws Exception
	 */
	public static User logIn(File f,String username, String password) throws Exception {
		JSONObject obj= (JSONObject) readJsonSimpleDemo(f.getAbsolutePath().toString());
		JSONArray users= (JSONArray) obj.get("users");
		String user="",pass="";
		String log=username+password;
		List<String> userpass= new ArrayList<String>();
		for(Object o: users){
		    if ( o instanceof JSONObject ) {
		    	user= ((JSONObject) o).get("username").toString();
		    	pass= ((JSONObject) o).get("password").toString();
		    	userpass.add(user+pass);
		    	}
		    }
		
		while(!(userpass.contains(log))) {
			System.out.println("Pogresan username ili password!");
			System.out.println("Username: ");
			username=reader.readLine();
			System.out.println("Password: ");
			password=reader.readLine();
			log=username+password;
		}
		
		List<String> privileges=new ArrayList<String>();
		
			 obj= (JSONObject) readJsonSimpleDemo(f.getAbsolutePath().toString());
			 users= (JSONArray) obj.get("users");
			for(Object o: users){
			    if ( o instanceof JSONObject ) {
			    	 //System.out.println("usao1");
			    	 //System.out.println(username);
			    	// System.out.println(((JSONObject) o).get("username").toString());
			    	if(((JSONObject) o).get("username").toString().equals(username) && ((JSONObject) o).get("password").toString().equals(password)){
			    	 JSONArray privile= (JSONArray) ((JSONObject) o).get("privileges");
			    	// System.out.println("usao2");
			    	 for(Object pri:  privile) {
			    		 if(pri instanceof String) {
			    			 privileges.add((String) pri);
			    			 
			    		 }
			    	 }
			    	 User u= new User(username, password);
				 		u.setPrivileges(privileges);
				 		System.out.println("Uspesno ste se prijavili!");
				 		return u;
			    	}
			    	
			       
			    }
			}
			
		
		
		
		User u= new User(username, password);
		
		u.setPrivileges(privileges);
		System.out.println(u.getUsername() + "      " + privileges);
		return u;
		
	}
	
	
	
	public JSONArray getUsersFromJson(String path) {
		return null;
		
	}
	/**
	 * Metoda za citanje json fajla
	 * @param filename
	 * @return
	 * @throws Exception
	 */
	public static Object readJsonSimpleDemo(String filename) throws Exception {
	    FileReader reader = new FileReader(filename);
	    JSONParser jsonParser = new JSONParser();
	    return jsonParser.parse(reader);
	}
	
	
	/**
	 * Metoda za zipovanje fajlova
	 * @param file
	 * @param name
	 * @param dest
	 */
	public static void zip(String file, String name, String dest) {
		ZipFile zip= new ZipFile(dest+"\\"+name+".zip");	
		File folder= new File(file+"\\"+name);
		
		if(folder.isDirectory()) {
			folder= new File(file+"\\"+name);
			File[] listofFiles= folder.listFiles();
			for(File f: listofFiles) {
				f= new File(file+"\\"+name);
				try {
					zip.addFolder(f);
					System.out.println("Zipovanje uspelo!");
				} catch (Exception e) {
						System.out.println("Zipovanje nije uspelo");
						return;
				}
			
			}
		}
		if(folder.isFile()) {
			try {
				System.out.println("JESTE FAJL");
				zip.addFile(folder);
				System.out.println("Zipovanje uspelo!");
			} catch (Exception e) {
				System.out.println("Zipovanje nije uspelo");
				return;
			}
		}
	}
	/**
	 * Metoda za unzipovanje fajlova
	 * @param source
	 * @param destination
	 */
	public static void unzipAndAdd(String source, String destination) {
		//System.out.println(new File(source));
	    try {
		         
		    	ZipFile file = new ZipFile(source);
				file.extractAll(destination);
				System.out.println("Anzipovanje uspelo!");
		      
		    } catch (Exception e) {
		    	System.out.println("Anzipovanje nije uspelo");
		    	return;
		    }
	}
}
