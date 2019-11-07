package console.app;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;

import file.Folder;
import file.manipulation.Storage;
import sun.security.util.Resources;
import users.User;


/**
 * Klasa MainConsoleApp predstavlja konzolnu aplikaciju koja sluzi kao tester za primenu implementacije lokalnog ili udaljenog skladista
 * u zavisnosti od odabira skladista. Ova klasa u konzoli simulira command prompt i vrsi interakciju sa korisnikom preko komandi koje
 * korisnik zadaje preko konzole.
 * 
 * @author Bogdan Stojadinovic 
 * @author Petar Poznanovic
 * 
 * 
 */

public class MainConsoleApp {
	/*
	 * @Petar & @Bogdan
	 * 
	 * C:\Users\Peca\Desktop\html css skripte
	 * 
	 * C:\Users\Bogi\Desktop\lokalno
	 * */
	
	/* za koriscenje ovih boja u konzoli skini ANSI escape u marketplace-u*/
	public static final String BLUE_BOLD = "\033[1;34m";
	public static final String ANSI_RESET = "\033[0m";
	public static final String GREEN_BOLD = "\033[1;32m";
	/**
	 * U zavisnosti od odabira vrste skladista, korisnik menja samo instancu klase Storage preko polja "folder" koje moze biti instanca
	 * new Folder() -> za lokalno skladiste, i new Folder() -> za udaljeno skladiste, u zavisnosti od trenutno ubacenog dependency-ja u pom.xml
	 * fajlu ovog projekta.
	 */
	static Storage folder= new Folder();
	static User user= new User();
	static String currentFolder= "";
	static String jsonUsers="";
	static String root;
	
	public static void main(String[] args) throws Exception {
		BufferedReader reader=new BufferedReader(new InputStreamReader(System.in));
		ConsoleFunctions.write("Dobrodosli");
		//ucitavanje json fajla (univerzalno)
		 currentFolder=ConsoleFunctions.question("Unesite putanju do repozitorijuma: (ako koristite udaljeno skladiste samo pritisnite enter)");
		 folder.setPath(currentFolder);
		File file= folder.getJson(currentFolder);
		jsonUsers= file.getAbsolutePath().toString();
		String absolutePath = file.getAbsolutePath();
		/*root=absolutePath.
	    substring(0,absolutePath.lastIndexOf(File.separator));*/
		root = currentFolder;
		String currentHelp="";
		if(root.equals(""))  currentHelp="root/";
		//System.out.println(root+"/");
		
		System.out.println("Kako biste pristupili repozitorijumu molimo Vas da se ulogujete\n");
        
		System.out.println("Username: ");
		String username=reader.readLine();
		System.out.println("Password: ");
        String password=reader.readLine();
        user=ConsoleFunctions.logIn(file, username, password);
      
       List<File> files= new ArrayList<File>();
        
        
        String answer= "";
        
		while(!(answer.equals("end"))) {
			
			System.out.println("");
			System.out.println(  "Ukucajte 'cmd' za prikaz komandi" );
			System.out.println("");
			
			System.out.println(currentHelp+currentFolder + " >" );
			answer=reader.readLine();
			List<String> privileges= user.getPrivileges();
			
			if(answer.equals("1") && privileges.contains("add_user")) {
				try {
					folder.singUpAnotherUser(file);
				} catch (Exception e) {
					
					e.printStackTrace();
				}
				continue;
			}else
			if(answer.equals("2") && privileges.contains("add_directory")) {
				String name;
				name=ConsoleFunctions.question("Unesite ime koje zelite da dodelite folderu:");
				String brojFoldera;
				brojFoldera=ConsoleFunctions.question("Unesite broj koliko foldera zelite da napravite:");
				boolean meta;
				String metaString="";
				while(!(metaString.equals("y") || metaString.equals("n"))) {
					metaString=ConsoleFunctions.question("Da li zlite da cuvate i meta podatke kreiranog/nih foldera(y,n)?");
				}
				if(metaString.equals("y"))meta=true;
				else meta = false;
				//String path = "help";
				System.out.println(folder.addDir(name, currentFolder, brojFoldera, meta, user, jsonUsers));
				continue;
				
				//System.out.println("2");
				
			}else
			if(answer.equals("3") && privileges.contains("add_file")) {
				String name;
				name=ConsoleFunctions.question("Unesite ime koje zelite da dodelite fajlu:");
				boolean meta;
				String metaString="";
				while(!(metaString.equals("y") || metaString.equals("n"))) {
					metaString=ConsoleFunctions.question("Da li zlite da cuvate i meta podatke kreiranog fajla(y,n)?");
				}
				if(metaString.equals("y"))meta=true;
				else meta = false;
				 
				System.out.println(folder.addFile(name, currentFolder, jsonUsers, meta, user));
				continue;
			}else
			if(answer.equals("4") && privileges.contains("download")) {
				String name;
				name=ConsoleFunctions.question("Unesite ime fajl koji zelite da preuzmete:");
				String putanja;
				putanja=ConsoleFunctions.question("Unesite putanju na kojoj zelite da se fajl preuzme: ");
				System.out.println(folder.download(name, putanja,currentFolder));
				continue;
			}
			else if(answer.equals("5") && privileges.contains("upload")) {
				String name;
				name=ConsoleFunctions.question("Unesite ime fajla koji zelite da uploadujete: ");
				String putanja;
				putanja=ConsoleFunctions.question("Unesite putanju sa koje zelite da fajl bude uploadovan: ");
				System.out.println(folder.upload(name, putanja, currentFolder));
			}else
			if(answer.equals("6") && privileges.contains("search_repository")) {
				//System.out.println("5");
				String opcija=ConsoleFunctions.question("Unesite opciju pretrage: "+"\n"+
				"1 za pretragu fajla po imenu u trenutnom ali i u svim pod direktorijumima (ime fajla sadrzi unesenu rec )"+"\n"+
				"2 za pretragu fajla po imenu u trenutnom ali i u svim pod direktorijumima (ime fajla isto kao unesena rec)"+"\n"+
				"3 za pretragu fajla po ekstenziji u trenutnom direktorijumu (ekstenzije pisati bez '.' npr. exe,txt itd.)"+"\n"+
				"4 za pretragu fajla po ekstenziji u trenutnom ali i u svim pod direktorijumima  (ekstenzije pisati bez '.' npr. exe,txt itd.)");
				if(opcija.equals("1")) {
					String name=ConsoleFunctions.question("Unesite ime fajla za pretragu:");
					List<File> list= new ArrayList<File>();
					folder.searchByName1(list, currentFolder, name);
				}else if(opcija.equals("2")) {
					String name=ConsoleFunctions.question("Unesite ime fajla za pretragu:");
					List<File> list= new ArrayList<File>();
					folder.searchByName2(list, currentFolder, name);
				}
				else if(opcija.equals("3")) {
					String extension=ConsoleFunctions.question("Unesite ime ekstenzije za pretragu (ukoliko zelite da izaberete foldere tj. direktorijume ukucajte 'dir'):");
					
					System.out.println(folder.searchByExtension1(extension, currentFolder));
				}
				else if(opcija.equals("4")) {
					String name=ConsoleFunctions.question("Unesite ime ekstenzije za pretragu (ukoliko zelite da izaberete foldere tj. direktorijume ukucajte 'dir'):");
					List<File> list= new ArrayList<File>();
					folder.searchByExtension2(list, currentFolder, name);
				}else {
					System.out.println("Opcija ne postoji.");
				}
				continue;
			}else
			if(answer.equals("7") && privileges.contains("delete_file")) {
				String name;
				name=ConsoleFunctions.question("Unesite ime fajla koji zelite da bude obrisan:");
				System.out.println(folder.delete(name, currentFolder));
				
				continue;
			}else if(answer.equals("8") && privileges.contains("add_user")) {
				String name;
				name=ConsoleFunctions.question("Unesite extenziju koju zelite da zabranite (unos ide bez '.' [.exe : neispravno] [exe : ispravno] :");
				folder.addForbiddenExtensions(name,jsonUsers);
				continue;
			}else if(answer.equals("9") && privileges.contains("download")) {
				String name;
				name=ConsoleFunctions.question("Unesite folder ili fajl koji zelite da zipujete :");
				String destination;
				System.out.println("Ukoliko zelite da se zipovani folder nalazi u trenutnom folderu unesite opciju: '.' ");
				destination=ConsoleFunctions.question("Ukoliko zelite da se zipovan drugom fajlu, unesite putanju do tog fajla");
				if(destination.equals(".")) {
					ConsoleFunctions.zip(currentFolder, name, currentFolder);
				}else {
					ConsoleFunctions.zip(currentFolder, name, destination);
				}
				continue;
			}else if(answer.equals("10") && privileges.contains("upload") ) {
				String name;
				name=ConsoleFunctions.question("Unesite folder ili fajl koji zelite da anzipujete :");
				String destination;
				System.out.println("Ukoliko zelite da se anzipovanje izvrsi u trenutnom folderu unesite opciju: '.' ");
				destination=ConsoleFunctions.question("Ukoliko zelite da se anzipovanje izvrsi u drugom fajlu, unesite putanju do tog fajla");
				if(destination.equals(".")) {
					ConsoleFunctions.unzipAndAdd(currentFolder+"\\"+name, currentFolder);
				}else {
					ConsoleFunctions.unzipAndAdd(currentFolder+"\\"+name, destination);
				}
				
				continue;
			}else if(answer.equals("ls")) {
				folder.ls(currentFolder);
				continue;
			}else if(answer.startsWith("cd")) {
				String[] tokens = answer.split(" ");
				if(tokens.length==1) {
					System.out.println("Nepotpuna komanda");
				}else if(tokens.length>1) {
					currentFolder=folder.cd(tokens, currentFolder, root);
					System.out.println(currentFolder);
				}
				
				continue;
			}else if(answer.equals("meta")) {
				
				folder.printMeta(jsonUsers);
			}else if(answer.equals("end")){
				break;
			}else if(answer.equals("cmd")){
				user.showMenuforUser(currentFolder,folder);
				
			}else {
				System.out.println("Ta opcija ne postoji. Pokusajte ponovo.");
				System.out.println("");
			}
		}
		System.out.println("Uspesno ste se izlogovali");
	}
	
	
	
}
