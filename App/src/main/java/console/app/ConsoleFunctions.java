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

public class ConsoleFunctions {
	//Arrays.asList("add_user","add_directory","upload_file","download_file","search_repository","delete_file");
	public static BufferedReader reader=new BufferedReader(new InputStreamReader(System.in));
	
	public static void write(String message) {
		System.out.println(message);
	}
	
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
				 		//System.out.println("LOOOG IN");
				 		return u;
			    	}
			    	
			       
			    }
			}
			
		
		
		
		User u= new User(username, password);
		u.setPrivileges(privileges);
		
		return u;
		
	}
	
	public static void singUpAnotherUser(File f) throws Exception {
		BufferedReader reader=new BufferedReader(new InputStreamReader(System.in));
		JSONObject user=new JSONObject();
		System.out.println("Unesite podatke za novog korisnika.");
		System.out.println("Username: ");
		try {
			String username=reader.readLine();
			System.out.println("Password: ");
			String password=reader.readLine();
			
			user.put("username", username);
			user.put("password", password);
			
			JSONArray privileges=new JSONArray();
			System.out.println("Unesite privilegije koje ce korisnik imati: ");
			System.out.println("");
			System.out.println("(Privilegije: add_user, add_directory, upload_file, download_file, search_repository, delete_file)");
			System.out.println("");
			System.out.println("Ako zelite da zavrsite sa dodavanjem korisnika napisite 'submit'. ");
			String privilegija = "";
			while(!(privilegija.equals("submit"))) {
				privilegija=reader.readLine();
				if(!(privilegija.equals("add_user") || privilegija.equals("add_directory") || privilegija.equals("add_file") || privilegija.equals("upload")
						  || privilegija.equals("delete_file") || privilegija.equals("search_repository") || privilegija.equals("download") || privilegija.equals("submit"))) {
							System.out.println("Niste uneli tacno ime privilegije. Pokusajte ponovo");
						}
						else if(privileges.contains(privilegija)) {
							System.out.println("Vec ste uneli tu privilegiju.");
						}else if(privilegija.equals("submit")){
							
							break;
						}else {
							privileges.add(privilegija);
							System.out.println("Privilegija dodata.");
							System.out.println("(Privilegije: add_user, add_directory, add_file, download, upload, search_repository, delete_file)");
							System.out.println("");
							System.out.println("Ako zelite da zavrsite sa dodavanjem korisnika napisite 'submit'. ");
						}
						
			}
			
			user.put("privileges", privileges);
			
			//ConsoleApp.getUsers().add(main);
			//File folder=new File(ConsoleApp.getFolder().getAbsolutePath()+'\\'+"users.json");
			String path=f.getAbsolutePath().toString();
			JSONObject obj= (JSONObject) readJsonSimpleDemo(f.getAbsolutePath().toString());
			JSONArray users= (JSONArray) obj.get("users");
		
			users.add(user);
			
			
			BufferedWriter writer=null;
			
			writer= new BufferedWriter(new FileWriter(f.getAbsolutePath().toString()));
			writer.write(obj.toString());
			System.out.println(path);
			writer.close();
			//showMenuforUser((JSONObject) ConsoleApp.getUsers().get(0));
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	
	}
	
	public JSONArray getUsersFromJson(String path) {
		return null;
		
	}
	
	public static Object readJsonSimpleDemo(String filename) throws Exception {
	    FileReader reader = new FileReader(filename);
	    JSONParser jsonParser = new JSONParser();
	    return jsonParser.parse(reader);
	}
	
	
	
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
