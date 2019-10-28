package file;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import file.manipulation.Storage;

public class Folder extends Storage {
	
	public static final String BLUE = "\033[0;34m";    // BLUE
	
	public Folder() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public File getJson(String path) throws IOException {
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
		privilege.add("upload_file");
		privilege.add("download_file");
		privilege.add("search_repository");
		privilege.add("delete_file");
		privilege.add("add_forbidden_extension");
		user.put("privileges", privilege);
		users.add(user);
		
		JSONArray extensions= new JSONArray();
		
		
		storage.put("users", users);
		storage.put("ext",extensions);
		
		BufferedWriter writer=null;
		
		writer= new BufferedWriter(new FileWriter(folder.getAbsolutePath().toString()));
		writer.write(storage.toString());
		System.out.println(storage);
		writer.close();
		return folder;
		
	}
	
	
 
	public Folder(String path) {
		super(path);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String uploadDir(String fileName, String path) {
		if(!(fileName.contains("."))) {
			File f= new File(path+"\\"+fileName);
			
			f.mkdir();
			return "Uploaded ------> 100%";
		}
		
		return "Upload [ERROR]";
	}

	@Override
	public String uploadFile(String fileName, String path) throws Exception {
		List<String> ext= new ArrayList<String>();
		File file= new File(path+"\\"+fileName);
		System.out.println(file.getAbsolutePath().toString());
		JSONObject obj= (JSONObject) readJsonSimpleDemo(path+"\\users.json");
		Object arr= obj.get("ext");
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
			return "Uploaded ------> 100%";
		}else {
			return "Fajlovi sa takvom etenzijom su zabranjeni!";
		}
		
		
	}
	
	@Override
	public String delete(String fileName, String path) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String download(String fileName, String path) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void searchByName(String fileName) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void searchByExtension(String extension) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void searchByMetaData(int metaOrNo) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addForbiddenExtensions(String e,String path) throws Exception {
		JSONObject obj= (JSONObject) readJsonSimpleDemo(path);
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
	public void cd() {
		// TODO Auto-generated method stub
		
	}
	
}
