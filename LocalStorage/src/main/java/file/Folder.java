package file;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import file.manipulation.Storage;
import net.lingala.zip4j.ZipFile;
import user.User;
/**
 * Klasa koja predstavlja implementaciju upravljanja folderima/fajlovima za LOKALNO SKLADISTE.
 * Ova klasa nasledjuje Abs klasu Storage iz specifikacije koja definise sve metode za upravljanje fajlovima.
 * @author Bogdan Stojadinovic
 * @author Petar Poznanovic
 *
 */
public class Folder extends Storage {
	
	
	
	public Folder() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void welcome() {
		System.out.println("DOBRODOSLI!");
	}

	@Override
	public File getJson(String path) throws IOException {
		super.type="local";
		File folder = new File(path);
		BufferedReader reader=new BufferedReader(new InputStreamReader(System.in));
		if(!(folder.exists())) {
			while(!(folder.exists())) {
				System.out.println("Zeljeni repozitorijum ne postoji. Pokusajte ponovo.");
				
				try {
					path = reader.readLine();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				folder=new File(path);
				
			}
		}
		path= folder.getAbsolutePath();
		File[] listofFiles=folder.listFiles();
		for (File file : listofFiles) {
		    if (file.getName().equals("users.json")) {
		    	return new File(file.getAbsolutePath().toString());
		    }
		}
		
		if(path.endsWith("\\")){
			folder = new File(path+"users.json");
		}else {
			folder = new File(path+"\\users.json");
		}
		try {
			folder.createNewFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		System.out.println("Repozitorijum je inicijalizovan, Vi ste novi root korisnik\n");
		String username, password;
		System.out.println("Unesite username: ");
		username=reader.readLine();
		System.out.println("Unesite password: ");
		password=reader.readLine();
		
		JSONObject storage = new JSONObject();
		JSONArray users = new JSONArray();
		JSONObject user = new JSONObject();
		user.put("username", username);
		user.put("password", password);
		JSONArray privilege= new JSONArray();
		privilege.add("add_user");
		privilege.add("add_directory");
		privilege.add("add_file");
		privilege.add("download");
		privilege.add("upload");
		privilege.add("search_repository");
		privilege.add("delete_file");
		privilege.add("add_forbidden_extension");
		user.put("privileges", privilege);
		users.add(user);
		
		JSONArray metaData= new JSONArray();
		
		
		JSONArray extensions= new JSONArray();
		
		
		storage.put("users", users);
		storage.put("ext",extensions);
		storage.put("meta", metaData);
		BufferedWriter writer=null;
		
		writer= new BufferedWriter(new FileWriter(folder.getAbsolutePath().toString()));
		writer.write(storage.toString());
		//System.out.println(storage);
		writer.close();
		return folder;
		
	}
	

 
	public Folder(String path) {
		super(path);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String addDir(String fileName, String path,String number,boolean metaBool, User u,File users) throws IOException {
		int broj=Integer.parseInt(number);
		BufferedReader reader=new BufferedReader(new InputStreamReader(System.in));
		
		if(broj==0) {
			return "Upload [ERROR]";
		}
		
		JSONObject obj = null;
		JSONArray metas = null;
		try {
			obj = (JSONObject) readJsonSimpleDemo(users.getAbsolutePath().toString());
			metas= (JSONArray) obj.get("meta");
			//System.out.println(metas);
		} catch (Exception e1) {
			return "Ucitavanje konfiguracije iz JSON fajla nije uspelo";
			
		}
		
		if(!(fileName.contains("."))) {
			JSONObject meta= new JSONObject();
			File f;
			for(int i=1;i<=broj;i++) {			
				
				if(i==1 && broj==1) {
				
					f= new File(path+"\\"+fileName);
					if(metaBool) {
						 String atr="Ime fajla: " + fileName;
						
						meta.put("name", atr);
						atr= "User who made it: "+u.getUsername();
						meta.put("creator", atr);
						 
						 SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
						 Date date = new Date(System.currentTimeMillis());
						 atr= formatter.format(date);
						 atr= "Vreme kreiranja fajla je: " + atr;
						 meta.put("date", atr);
						 atr= f.getAbsolutePath().toString();
						 meta.put("path", atr);
						 //metas.add(meta);
						 String podatak="";
						 System.out.println("Unesite naziv i sadrzaj custom metapodatka: ");
						 System.out.println("Ukoliko zelite da zavrsite sa dodavanjem custom podataka unesite u konzolu naredbu submit");
						 while(!(podatak.equals("submit"))) {
							 if(podatak.equals("submit")) {
								 
								 break;
							 }
							 System.out.println("Naziv metapodatka: ");
							 String name=reader.readLine();
							 
							 System.out.println("Sadrzaj metapodatka: ");
							 String atribut=reader.readLine();
							 meta.put(name, atribut);
							 
							 System.out.println("Ako zelite da zavrsite sa unosom metapodataka, napisite 'submit'");
							 podatak=reader.readLine();
							 
						 }
						 metas.add(meta);
					}
				}else {
					 f= new File(path+"\\"+fileName+i);
					 if(metaBool) {
						 String atr="Ime fajla: " + fileName+i;
						meta= new JSONObject();
						meta.put("name", atr);
						atr= "User who made it: "+u.getUsername();
						meta.put("creator", atr);
						 atr= "Folder roditelj: "+f.getParentFile();
						 meta.put("parent", atr);
						 SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
						 Date date = new Date(System.currentTimeMillis());
						 atr= formatter.format(date);
						 atr= "Vreme kreiranja fajla je: " + atr;
						 meta.put("date", atr);
						 atr= f.getAbsolutePath().toString();
						 meta.put("path", atr);
						 //System.out.println(i);
						// System.out.println("cao"+meta.get("name"));
						 
						 //metas.add(meta);
						 //System.out.println(metas.toString());
						 String podatak="";
						 System.out.println("Unesite naziv i sadrzaj metapodatka: ");
						 System.out.println("\n");
						 while(!(podatak.equals("submit"))) {
							 if(podatak.equals("submit")) {
								 
								 break;
							 }
							 System.out.println("Naziv metapodatka: ");
							 String name=reader.readLine();
							 
							 System.out.println("Sadrzaj metapodatka: ");
							 String atribut=reader.readLine();
							 meta.put(name, atribut);
							 
							 System.out.println("Ako zelite da zavrsite sa unosom metapodataka, napisite 'submit'");
							 podatak=reader.readLine();
							 
						 }
						 metas.add(meta);
						 obj.put("meta", metas);
						 String a="";
						 if(i==broj-1) {
								a=obj.toString();
							}
						 
					}
					
					
				}
				
				f.mkdir();
				
			}
			
			
			 BufferedWriter writer;
			try {
				writer = new BufferedWriter(new FileWriter(users));
				
				
				writer.write(obj.toString());
				System.out.println("------------------------------------------------------------");
				//System.out.println(obj.toString());
				writer.close();
			} catch (IOException e) {
				return "Pri upisivanju u fajl doslo je do greske";
			}
			
			
			
			
			return "Uploaded ------> 100%";
		}
		
		return "Upload [ERROR]";
	}

	@Override
	public String addFile(String fileName, String path,File f,boolean metaBool, User u) throws Exception {
		List<String> ext= new ArrayList<String>();
		File file= new File(path+"\\"+fileName);
		//System.out.println("----->"+f);
		//System.out.println(file.getAbsolutePath().toString());
		JSONObject obj= (JSONObject) readJsonSimpleDemo(f.getAbsolutePath().toString());
		Object arr= obj.get("ext");
		BufferedReader reader=new BufferedReader(new InputStreamReader(System.in));
		if(arr instanceof JSONArray) {
			for(Object a:((JSONArray)arr)) {
				if(a instanceof String) {
					ext.add("."+a);
				}
			}
		}
		System.out.println(ext);
		if(ext.size()==0) {
			file.createNewFile();
			
			if(metaBool) {
				JSONArray metaArr= (JSONArray) obj.get("meta");
				JSONObject meta= new JSONObject();
				 String atr="Ime fajla: " + file.getName();
					
					meta.put("name", atr);
					atr= "User who made it: "+u.getUsername();
					meta.put("creator", atr);
					 SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
					 Date date = new Date(System.currentTimeMillis());
					 atr= formatter.format(date);
					 atr= "Vreme nastanka fajla: " +atr;
					 meta.put("date", atr);
					 atr= file.getAbsolutePath().toString();
					 meta.put("path", atr);
					 String podatak="";
					 System.out.println("Unesite naziv i sadrzaj metapodatka: ");
					 System.out.println("\n");
					 while(!(podatak.equals("submit"))) {
						 if(podatak.equals("submit")) {
							 
							 break;
						 }
						 System.out.println("Naziv metapodatka: ");
						 String name=reader.readLine();
						 
						 System.out.println("Sadrzaj metapodatka: ");
						 String atribut=reader.readLine();
						 meta.put(name, atribut);
						 
						 System.out.println("Ako zelite da zavrsite sa unosom metapodataka, napisite 'submit'");
						 podatak=reader.readLine();
						 
					 }
					 metaArr.add(meta);
					 BufferedWriter writer;
						try {
							writer = new BufferedWriter(new FileWriter(f));
							writer.write(obj.toString());
							writer.close();
						} catch (IOException e) {
							return "Pri upisivanju u fajl doslo je do greske.";
						}
				
			}
			return "Uploaded ------> 100%";
		}
		boolean help=true;
		for(int i=0; i<ext.size();i++) {
			if(fileName.contains(ext.get(i))) {
				help=false;
				
			}
		}
		
		
		
		if(help) {
			file.createNewFile();
			if(metaBool) {
				JSONArray metaArr= (JSONArray) obj.get("meta");
				JSONObject meta= new JSONObject();
				 String atr="Ime fajla: " + file.getName();
					
					meta.put("name", atr);
					atr= "User who made it: "+u.getUsername();
					meta.put("creator", atr);
					 atr= "Folder roditelj: "+file.getParentFile();
					 meta.put("parent", atr);
					 SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
					 Date date = new Date(System.currentTimeMillis());
					 atr= formatter.format(date);
					 atr= "Vreme nastanka fajla: " +atr;
					 meta.put("date", atr);
					 atr= file.getAbsolutePath().toString();
					 meta.put("path", atr);
					 metaArr.add(meta);
					 BufferedWriter writer;
						try {
							writer = new BufferedWriter(new FileWriter(f));
							writer.write(obj.toString());
							writer.close();
						} catch (IOException e) {
							return "Pri upisivanju u fajl doslo je do greske.";
						}
				
			}
			return "Uploaded ------> 100%";
		}else {
			return "Fajlovi sa takvom etenzijom su zabranjeni!";
		}
		
		
		
	}
	
	@Override
	public void printMeta(String json) throws Exception {
		JSONObject obj= (JSONObject) readJsonSimpleDemo(json);
		JSONArray metas=(JSONArray) obj.get("meta");
		//JSONArray pom= new JSONArray();
		//System.out.println(metas);
		StringBuilder sb= new StringBuilder();
		for(int i=0 ; i<metas.size(); i++){
			if(metas.get(i) instanceof JSONObject) {
				String path= (String) ((JSONObject) metas.get(i)).get("path");
				if((new File(path).exists())) {
				
					
					sb.append(((JSONObject) metas.get(i)).get("name")+"   | ");
					sb.append(((JSONObject) metas.get(i)).get("creator")+"   | ");
					sb.append(((JSONObject) metas.get(i)).get("date")+"   | ");
					
					sb.append("\n");
					
				}else {
					
				}
				
				
				
				
			}
		}
		
		
		System.out.print(sb.toString());
	}
	
	@Override
	public String delete(String fileName, String path) throws IOException {
		File file = new File(path+"\\"+fileName);

		boolean exists =      file.exists();      
		boolean isDirectory = file.isDirectory();
		boolean isFile =      file.isFile();      
        
		BufferedReader reader=new BufferedReader(new InputStreamReader(System.in));
		
		if(exists) {
			if(isFile) {
				if(file.delete()) {
					return "Fajl je uspesno izbrisan";
				}else {
					return "Doslo je do greske prilikom brisanja fajla";
				}
			}
			if(isDirectory) {
				File[] listOfFiles = file.listFiles();
				if(listOfFiles.length==0) {
					if(file.delete()) return "Folder je uspesno obrisan";
				}
				String answer="";
				System.out.println("Da li ste sigurni da zelite da obrisete folder? U njemu se\n nalazi jos fajlova ili foldera. ( y , n )");
				answer=reader.readLine();
				while(!(answer.equals("y") || answer.equals("n") )) {
					System.out.println("Da li ste sigurni da zelite da obrisete folder? U njemu se\n nalazi jos fajlova ili foldera. ( y , n )");
					answer=reader.readLine();
				}
				if(answer.equals("y")) {
					FileUtils.deleteDirectory(file);
					return "Uspesno obrisan folder";
				}else {
					return "Brisanje foldera obustavljeno";
				}
				
			}
			
			
			
		}
		
        return "Ime koje ste uneli ne moze da se pronadje u direktorijumu.";
	}

	@Override
	public String download(String fileName, String path, String current) {
		File folder=new File(current);
		File destinacija=new File(path);
		File[] listofFiles=folder.listFiles();
		for(File f: listofFiles) {
			if(f.getName().equals(fileName)) {
				if(f.isDirectory()) {
					
					try {
						FileUtils.copyDirectory(f, destinacija);
					} catch (IOException e) {
						return "Doslo je do greske pri kopiranju fajlu";
					}
					//File novi= new File(path+"\\"+fileName);
					//novi.mkdir();
					return "Uspesno preuzet folder.";
				}
				if(f.isFile()) {
					try {
						FileUtils.copyFileToDirectory(f, destinacija);
						return "Uspesno preuzet fajl.";
					} catch (IOException e) {
						return "Neuspesno preuzet fajl";
					}
				}
			}
		}
		
		return "Ime koje ste uneli ne moze da se pronadje u direktorijumu.";
		
	}

	@Override
	public void searchByName1(List<File> files,String dir ,String name) {
		
		returnAllFiles(files, new File(dir));
		
		for(File f : files) {
			if(f.getName().contains(name))
	    	System.out.println(f.getName()+" -------------> " + f.getAbsolutePath().toString());
	    }
		
	}
	
	@Override
	public void searchByName2(List<File> files,String dir ,String name) {
		
		returnAllFiles(files, new File(dir));
		
		for(File f : files) {
			if(f.getName().equals(name))
	    	System.out.println(f.getName()+" -------------> " + f.getAbsolutePath().toString());
	    }
		
	}

	@Override
	public String searchByExtension1(String extension, String path) {
		File folder=new File(path);
		StringBuilder sb = new StringBuilder();
		
		File[] listOfFiles=folder.listFiles();
		
		if(extension.equals("dir")) {
			for(File f : listOfFiles) {
				if(!(f.getName().contains(".")))
		    	sb.append(f.getName()+" -------------> " + f.getAbsolutePath().toString());
				
		    }
			return sb.toString();
		}
			
		if(listOfFiles!=null) {
			
			for(File f:listOfFiles) {
			
				if(f.getName().endsWith("."+extension)) {
					//files.add(f);
					//System.out.println(f);
					//System.out.println("usao");
					
						sb.append(f.getName());
						for(int i=0; i<20-f.getName().length();i++) {
							sb.append(" ");
						}
						sb.append("--------------->   ");
						sb.append(f.getAbsolutePath().toString());
						sb.append("\n");
					
					
				}
			}
			return sb.toString();
			
			
		}
		
		return "Ne postoji fajl sa takvom ekstenzijom u trenutnom direktorijumu!";
	}

	
	@Override
	public void searchByExtension2(List<File> files, String dir, String name) {
		returnAllFiles(files, new File(dir));
		if(name.equals("dir")) {
			for(File f : files) {
				if(!(f.getName().contains(".")))
		    	System.out.println(f.getName()+" -------------> " + f.getAbsolutePath().toString());
				return;
		    }

		}
		for(File f : files) {
			if(f.getName().endsWith("."+name))
	    	System.out.println(f.getName()+" -------------> " + f.getAbsolutePath().toString());
	    }
	}
	
	private static List<File> returnAllFiles(List<File> files, File dir) {
		
		    if(dir.isFile()){
		        files.add(dir);
		    }else{
		        files.add(dir);
		        File f[] = dir.listFiles();
		        for(File dirOrFile: f){
		        	returnAllFiles(files,dirOrFile);
		        }
		    }
		
		return files;
	   
	}

	/*public static  void test(List<File> files , String dir) {
		 
		returnAllFiles(files, new File(dir));
		
		for(File f : files) {
				
		    	System.out.println(f.getName()+" -------------> " + f.getAbsolutePath().toString());
		    }
	}*/
	
	@Override
	public void addForbiddenExtensions(String e,File path) throws Exception {
		JSONObject obj= (JSONObject) readJsonSimpleDemo(path.getAbsolutePath().toString());
		JSONArray ext= (JSONArray) obj.get("ext");
		ext.add(e);
		
		BufferedWriter writer= new BufferedWriter(new FileWriter(path));
		writer.write(obj.toString());
		System.out.println(obj.toString());
		writer.close();
		
		
	}

	private static Object readJsonSimpleDemo(String filename) throws Exception {
	    FileReader reader = new FileReader(filename);
	    JSONParser jsonParser = new JSONParser();
	    return jsonParser.parse(reader);
	}

	@Override
	public void ls(String path) {
		File folder = new File(path);
		File[] listofFiles=folder.listFiles();
		int cnt=1;
		for(File f: listofFiles) {
			
			if(cnt==1) {
				int size= f.getName().length();
				String pom= "";
				for(int i=20-size; i>0;i--) {
					pom+=" ";
				}
				System.out.print(f.getName()+pom+"|"+"         ");
				
			}
			if(cnt==2) {
				System.out.println(f.getName());
				cnt=0;
			}
			cnt++;	
	
		}
		System.out.print("\n");
	}

	@Override
	public String cd(String[] command, String path, String root) {
		String pom=command[1];
		for(int i=2; i< command.length;i++) {
			pom=pom+ " "+command[i];
		}
		
		
		
		
		if(pom.equals("..")) {
			String help=path.substring(0,path.lastIndexOf(File.separator));
			
			if(root.length()==path.length()) {
				
				System.out.println("Vec se nalazite u root-u repozitorijumu");
				return root;
			}else {
				System.out.println("Trenutna lokacija: " + help);
				return help;
				
			}
		}else {
			File f= new File(path);
			File[] listOfFiles = f.listFiles();
			for(int i=0; i<listOfFiles.length;i++) {
				if(listOfFiles[i].getName().equals(pom)) {
					System.out.println("Trenutna lokacija: " + path+"\\"+pom);
					return path+"\\"+pom;
				}
			}
		}
		System.out.println("Nevalidan unos. Lokacija je ostala nepromenjena!");
		System.out.println("Trenutna lokacija: " +path);
		return path;
	}

	@Override
	public String upload(String fileName, String upload_path, String current) {
		
		File folder=new File(current);
		File destinacija=new File(upload_path);
		File[] listofFiles=folder.listFiles();
		for(File f: listofFiles) {
			if(f.getName().equals(fileName)) {
				if(f.isDirectory()) {
					
					try {
						FileUtils.copyDirectory(f, destinacija);
					} catch (IOException e) {
						
						e.printStackTrace();
						System.out.println("Upload nije uspeo.");
					}
					
					return "Uspesno uploadovan folder.";
				}
				if(f.isFile()) {					
					try {
						FileUtils.copyFileToDirectory(f, destinacija);;
						return "Uspesno uploadovan fajl.";
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
		
		
		return "Upload nije bio uspesan.";
	}


	@Override
	public void singUpAnotherUser(File f) {
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
			System.out.println("(Privilegije: add_user, add_directory, add_file, download, upload, search_repository, delete_file)");
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
			
			
			String path=f.getAbsolutePath().toString();
			JSONObject obj = null;
			try {
				obj = (JSONObject) readJsonSimpleDemo(f.getAbsolutePath().toString());
			} catch (Exception e) {
				System.out.println("Doslo je do greske prilikom citanja konfiguracionog fajla korisnika");
			}
			JSONArray users= (JSONArray) obj.get("users");
		
			users.add(user);
			
			
			
			BufferedWriter writer=null;
			
			writer= new BufferedWriter(new FileWriter(f.getAbsolutePath().toString()));
			writer.write(obj.toString());
			System.out.println(path);
			writer.close();
			
			
			
			
			
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

	@Override
	public void addForbiddenExtensions(String e, String path) throws Exception {
		// TODO Auto-generated method stub
		
	}
	
	
	 private void zip(String file, String name, String dest) {
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
	
}
