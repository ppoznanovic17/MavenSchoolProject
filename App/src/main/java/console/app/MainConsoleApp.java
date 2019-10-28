package console.app;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import org.json.simple.JSONArray;

import file.Folder;
import file.manipulation.Storage;
import users.User;

public class MainConsoleApp {
	/*
	 * @Petar & @Bogdan
	 * 
	 * C:\Users\Peca\Desktop\html css skripte
	 * */
	
	
	
	static Storage folder= new Folder();
	static User user= new User();
	static String currentFolder= "";
	static String jsonUsers="";
	static String root;
	public static void main(String[] args) throws Exception {
		BufferedReader reader=new BufferedReader(new InputStreamReader(System.in));
		ConsoleFunctions.write("Dobrodosli");
		//ucitavanje json fajla (univerzalno)
		 currentFolder=ConsoleFunctions.question("Unesite putanju do repozitorijuma:");
		File file= folder.getJson(currentFolder);
		jsonUsers= file.getAbsolutePath().toString();
		String absolutePath = file.getAbsolutePath();
		root=absolutePath.
	    substring(0,absolutePath.lastIndexOf(File.separator));
		currentFolder= root;
		
		System.out.println(file.getAbsolutePath().toString());
		//log in
		System.out.println("Kako biste pristupili repozitorijumu molimo Vas da se ulogujete\n");
        try {
			System.out.println("Username: ");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        String username=reader.readLine();
        try {
			System.out.println("Password: ");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        String password=reader.readLine();
        user=ConsoleFunctions.logIn(file, username, password);
        System.out.println(user.getPrivileges());
        System.out.println(user.toString());

        String answer= "";
		while(!(answer.equals("end"))) {
			user.showMenuforUser(currentFolder);
			answer=reader.readLine();
			List<String> privileges= user.getPrivileges();
			//System.out.println(privilegije);
			if(answer.equals("1") && privileges.contains("add_user")) {
				try {
					ConsoleFunctions.singUpAnotherUser(file);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				continue;
			}else
			if(answer.equals("2") && privileges.contains("add_directory")) {
				String name;
				name=ConsoleFunctions.question("Unesite ime koje zelite da dodelite folderu:");
				System.out.println(folder.uploadDir(name, currentFolder));
				continue;
				
				//System.out.println("2");
			}else
			if(answer.equals("3") && privileges.contains("upload_file")) {
				String name;
				name=ConsoleFunctions.question("Unesite ime koje zelite da dodelite fajlu:");
				System.out.println(folder.uploadFile(name, currentFolder ));
				continue;
			}else
			if(answer.equals("4") && privileges.contains("download_file")) {
				String name;
				name=ConsoleFunctions.question("Unesite ime koje zelite da dodelite fajlu:");
				continue;
			}else
			if(answer.equals("5") && privileges.contains("search_repository")) {
				System.out.println("5");
				continue;
			}else
			if(answer.equals("6") && privileges.contains("delete_file")) {
				String name;
				name=ConsoleFunctions.question("Unesite ime koje zelite da dodelite fajlu:");
				System.out.println(folder.delete(name, currentFolder));
				
				continue;
			}if(answer.equals("7") && privileges.contains("add_user")) {
				String name;
				name=ConsoleFunctions.question("Unesite extenziju koju zelite da zabranite (unos ide bez '.' [.exe : neispravno] [exe : ispravno] :");
				folder.addForbiddenExtensions(name,jsonUsers);
				continue;
			}if(answer.equals("ls")) {
				folder.ls(currentFolder);
				continue;
			}if(answer.startsWith("cd")) {
				String[] tokens = answer.split(" ");
				if(tokens.length==1) {
					System.out.println("Nepotpuna komanda");
				}else if(tokens.length>1) {
					currentFolder=folder.cd(tokens, currentFolder, root);
					System.out.println(currentFolder);
				}
				
				continue;
			}else if(answer.equals("end")){
				break;
			}else {
				System.out.println("Ta opcija ne postoji. Pokusajte ponovo.");
				System.out.println("");
			}
		}
		System.out.println("Uspesno ste se izlogovali");
	}
	
	
	
}
