package file;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.DownloadZipBuilder;
import com.dropbox.core.v2.files.DownloadZipResult;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.ListFolderContinueErrorException;
import com.dropbox.core.v2.files.ListFolderErrorException;
import com.dropbox.core.v2.files.ListFolderResult;
import com.dropbox.core.v2.files.Metadata;
import com.dropbox.core.v2.files.UploadErrorException;

import dropbox.Connection;
import file.manipulation.Storage;
import net.lingala.zip4j.ZipFile;
import user.User;

public class Folder extends Storage {
	
	
	Connection c;
	
	@Override
	public File getJson(String path) throws IOException {
		this.type="drop";
		c= new Connection();
		
		try {
			Connection.connetction();
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		DbxRequestConfig  config = c.getConfig();
		DbxClientV2  client= c.getClient();
		
			if(existUsers(path, c)) {
	    	
				System.out.println("Postoji");
				String localPath = "help";
				OutputStream outputStream = new FileOutputStream(localPath+"\\users.json");
				FileMetadata metadata;
				try {
					metadata = client.files()
						 .downloadBuilder(path+"/users.json")
				        .download(outputStream);
				} catch (DbxException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	    	
				File users = new File(localPath+"/users.json");
				return users;
				
			}else{
		    	System.out.println("Ne postoji");
		    	
		    	
		    	BufferedReader reader=new BufferedReader(new InputStreamReader(System.in));
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
				File usersJson= new File("help/users.json");
				usersJson.createNewFile();
				writer= new BufferedWriter(new FileWriter(usersJson));
				writer.write(storage.toString());
				System.out.println(storage);
				writer.close();
				
				
				
		    	try (InputStream in = new FileInputStream(usersJson)) {
		    	    try {
						FileMetadata metadata = client.files().uploadBuilder(path+"/users.json")
						    .uploadAndFinish(in);
					} catch (UploadErrorException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (DbxException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		    	}
				
				return usersJson;
		    	
		    	
		    }
		    
		    
					
		  
		   
	}	
	
	private boolean existUsers(String path, Connection c) {
		ListFolderResult result = null;
		
		DbxRequestConfig  config = c.getConfig();
		DbxClientV2  client= c.getClient();
		
		try {
			 result = client.files().listFolder(path);
			 
		} catch (ListFolderErrorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DbxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		boolean exist=false;
		while (true) {
		    for (Metadata metadata : result.getEntries()) {
		    	if(metadata.getPathLower().contains("users.json")) {
		    		return true;
		    	}
		        System.out.println(metadata.getPathLower());
		    }

		    if (!result.getHasMore()) {
		        break;
		    }
		}
		
		return false;
	}
	@Override
	public String delete(String fileName, String path) throws IOException {
		DbxClientV2 client = c.getClient();
		try {
			client.files().delete("/"+fileName);
			return "Uspesno obrisan fajl.";
		} catch (DbxException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			return "ERROR fajl sa takvim imenom ne postoji.";
		}
		
	}

	@Override
	public String download(String fileName, String download_path, String filePath) {
		DbxClientV2 client = c.getClient();
		System.out.println(fileName);
		if(fileName.equals("")) {
			fileName="pomoc";
		}
		try {
			if(fileName.contains(".")) {
				
			OutputStream out = new FileOutputStream(download_path+"\\"+fileName);
			FileMetadata metadata = client.files().downloadBuilder(filePath+"/"+fileName).download(out);
			return "Uspesno preuzet fajl.";
			}else {
				OutputStream out = new FileOutputStream(download_path+"\\"+fileName+".zip");
				DownloadZipResult metadata = client.files().downloadZip("/"+fileName).download(out);
				File f= new File(download_path+"\\"+fileName+".zip");
				unzipAndAdd(f.getAbsolutePath().toString(), download_path);
				
				return "Uspesno preuzet folder.";
			}
			
			} catch (DbxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			//return "ERROR -> Takav fajl ne postoji u repozitorijumu.";
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			//return "ERROR -> Takav fajl ne postoji u repozitorijumu.";
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			//return "ERROR -> Takav fajl ne postoji u repozitorijumu.";
		}
		return "ERROR";
	}

	@Override
	public void addForbiddenExtensions(String e, String path) throws Exception {
		JSONObject obj= (JSONObject) readJsonSimpleDemo(path);
		JSONArray ext= (JSONArray) obj.get("ext");
		ext.add(e);
		
		BufferedWriter writer= new BufferedWriter(new FileWriter(path));
		writer.write(obj.toString());
		System.out.println(obj.toString());
		writer.close();
	}

	@Override
	public void printMeta(String path) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String addDir(String fileName, String pathCurr,String number,boolean metaBool, User u,String users) throws IOException {
		int broj=Integer.parseInt(number);
		String path = "help";
		BufferedReader reader=new BufferedReader(new InputStreamReader(System.in));
		if(broj==0) {
			return "Upload [ERROR]";
		}
		
		JSONObject obj = null;
		JSONArray metas = null;
		DbxClientV2 client = c.getClient();
		try {
			obj = (JSONObject) readJsonSimpleDemo(users);
			metas= (JSONArray) obj.get("meta");
			//System.out.println(metas);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
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
						 atr= "Folder roditelj: "+f.getParentFile();
						 meta.put("parent", atr);
						 SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
						 Date date = new Date(System.currentTimeMillis());
						 atr= formatter.format(date);
						 atr= "Vreme kreiranja fajla je: " + atr;
						 meta.put("date", atr);
						 atr= f.getAbsolutePath().toString();
						 meta.put("path", atr);
						 //metas.add(meta);
						 /*String podatak="";
						 System.out.println("Unesite naziv i sadrzaj metapodatka: ");
						 System.out.println("\n");
						 System.out.println("Naziv metapodatka: ");
						 String name=reader.readLine();
						 
						 System.out.println("Sadrzaj metapodatka: ");
						 String atribut=reader.readLine();
						 meta.put(name, atribut);
						 System.out.println("Ako zelite da zavrsite sa unosom metapodataka, napisite 'submit'");
						 //podatak=reader.readLine();
						 while(!(podatak.equals("submit"))) {
							 podatak = reader.readLine();
							 if(podatak.equals("submit")) {
								 System.out.println("ukucan submit");
								 break;
							 }else {
								 System.out.println("Naziv metapodatka: ");
								  name=reader.readLine();
								 
								 System.out.println("Sadrzaj metapodatka: ");
								  atribut=reader.readLine();
								 meta.put(name, atribut);
								 
								 System.out.println("Ako zelite da zavrsite sa unosom metapodataka, napisite 'submit'");
								 metas.add(meta);
							 }
							 
						 }*/
						 
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
						 /*String podatak="";
						 System.out.println("Unesite naziv i sadrzaj metapodatka: ");
						 System.out.println("\n");
						 System.out.println("Naziv metapodatka: ");
						 String name=reader.readLine();
						 
						 System.out.println("Sadrzaj metapodatka: ");
						 String atribut=reader.readLine();
						 meta.put(name, atribut);
						 System.out.println("Ako zelite da zavrsite sa unosom metapodataka, napisite 'submit'");
						 while(!(podatak.equals("submit"))) {
							 
							 podatak = reader.readLine();
							 if(podatak.equals("submit")) {
								 System.out.println("ukucan submit");
								 break;
							 }else {
								 System.out.println("Naziv metapodatka: ");
								  name=reader.readLine();
								 
								 System.out.println("Sadrzaj metapodatka: ");
								  atribut=reader.readLine();
								 meta.put(name, atribut);
								 
								 System.out.println("Ako zelite da zavrsite sa unosom metapodataka, napisite 'submit'");
								 metas.add(meta);
							 }
							 
						 }*/
						 metas.add(meta);
						 obj.put("meta", metas);
						 String a="";
						 if(i==broj-1) {
								a=obj.toString();
							}
						 
					}
					
					
				}
				
				f.mkdir();
				try {
					client.files().createFolder(pathCurr+"/"+f.getName());
					
				} catch (DbxException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				f.delete();
			}
			
			
			 BufferedWriter writer;
			try {
				writer = new BufferedWriter(new FileWriter(users));
				
				
				writer.write(obj.toString());
				System.out.println("------------------------------------------------------------");
				//System.out.println(obj.toString());
				writer.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			
			return "Uploaded ------> 100%";
		}
		
		return "Upload [ERROR]";
	}
	

	@Override
	public String addFile(String fileName, String path, String f, boolean metaBool, User u) throws Exception {
		List<String> ext= new ArrayList<String>();
		path = "help";
		File file= new File(path+"\\"+fileName);
		//System.out.println("----->"+f);
		//System.out.println(file.getAbsolutePath().toString());
		JSONObject obj= (JSONObject) readJsonSimpleDemo(f);
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
					 atr= "Folder roditelj: "+file.getParentFile();
					 meta.put("parent", atr);
					 SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
					 Date date = new Date(System.currentTimeMillis());
					 atr= formatter.format(date);
					 atr= "Vreme nastanka fajla: " +atr;
					 meta.put("date", atr);
					 atr= file.getAbsolutePath().toString();
					 meta.put("path", atr);
					 /*String podatak="";
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
						 
					 }*/
					 metaArr.add(meta);
					 BufferedWriter writer;
						try {
							writer = new BufferedWriter(new FileWriter(f));
							writer.write(obj.toString());
							writer.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
				
			}
			
			
			
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
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
				
			}
			DbxClientV2 client = c.getClient();
			
			try (InputStream in = new FileInputStream(file)) {
	    	    try {
					FileMetadata metadata = client.files().uploadBuilder("/"+fileName)
					    .uploadAndFinish(in);
					file.delete();
				} catch (UploadErrorException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (DbxException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	    	}
			
			file.delete();
			return "Uploaded ------> 100%";
		}else {
			return "Fajlovi sa takvom etenzijom su zabranjeni!";
		}
	}

	@Override
	public String upload(String fileName, String upload_path, String filePath) {
		DbxClientV2 client = c.getClient();
		File folder= new File(upload_path+"/"+fileName);
		if(folder.isDirectory()) {
			zip(upload_path, fileName, upload_path);
			try (InputStream in = new FileInputStream(folder.getAbsolutePath().toString()+".zip")) {
	    	    try {
					FileMetadata metadata = client.files().uploadBuilder("/"+fileName+".zip")
					    .uploadAndFinish(in);
					
					return "Uspeo upload";
					//file.delete();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return e.toString();
				} 
			}catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				return e.toString();
			}
			
			
		}
		try (InputStream in = new FileInputStream(upload_path+"/"+fileName)) {
    	    try {
				FileMetadata metadata = client.files().uploadBuilder("/"+fileName)
				    .uploadAndFinish(in);
				
				return "Uspeo upload";
				//file.delete();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return e.toString();
			} 
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return e.toString();
		}
		
	}

	@Override
	public void ls(String path) {
		DbxClientV2 client = c.getClient();
		ListFolderResult result = null;
		try {
			 result = client.files().listFolder(path);
			 
		} catch (ListFolderErrorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DbxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int cnt=1;
		while (true) {
		    for (Metadata metadata : result.getEntries()) {
		    	
		    	if(cnt==1) {
					int size= metadata.getName().length();
					String pom= "";
					for(int i=20-size; i>0;i--) {
						pom+=" ";
					}
					System.out.print(metadata.getName()+pom+"|"+"         ");
					
				}
				if(cnt==2) {
					System.out.println(metadata.getName());
					cnt=0;
				}
				cnt++;	
		
			}
		    	
		    if (!result.getHasMore()) {
		        break;
		    }
		       
		    }
		
	}

	@Override
	public String cd(String[] command, String path, String root) {
		String pom=command[1];
		for(int i=2; i< command.length;i++) {
			pom=pom+ " "+command[i];
		}
		//System.out.println("root:" +root);
		//System.out.println("curr:" + path);
		//System.out.println(pom);
		if(pom.equals("..") ) {
			
			//System.out.println("help: " +help);
			if(root.equals("")) {
				System.out.println("Vec se nalazite u root-u repozitorijumu");
				return root;
			}else {
				String help=path.substring(0,path.lastIndexOf(File.separator));
				System.out.println("Trenutna lokacija: " + help);
				return help;
				
			}
		}else {
			ListFolderResult result = null;
			DbxClientV2 client = c.getClient();
			try {
				result = client.files().listFolder(path);
				for (Metadata metadata : result.getEntries()) {
			    	if(metadata.getPathLower().contains(pom)) {
			    		System.out.println("Fajl nadjen -->" +metadata.getPathLower());
			    		return metadata.getPathLower();
			    	}
			    }
			} catch (DbxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		}
		System.out.println("Nevalidan unos. Lokacija je ostala nepromenjena!");
		System.out.println("Trenutna lokacija: " +path);
		return path;
	}
	
	@Override
	public void searchByName1(List<File> files, String dir, String name) {
		if(dir.equals("")) dir= "/petar3";
		
		String name1= dir.substring(dir.lastIndexOf("/")+1);
		if(name1.length()<2) name1="/";
		/*System.out.println(name1+"  name1");
		System.out.println(dir+ "dir ");
		System.out.println(f.getAbsolutePath().toString()+"    dirpath");*/
		download(name1,dir, "/help");
		System.out.println(dir);
		/*ListFolderResult result = null;
		DbxClientV2 client = c.getClient();
		try {
			result = client.files().listFolder("");
			for (Metadata metadata : result.getEntries()) {
		    	if(metadata.getPathLower().contains(name)) {
		    		System.out.println("Fajl nadjen -->" +metadata.getPathLower());
		    		return;
		    	}
		    }
		} catch (DbxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Fajl ne postoji u repozitorijumu.");
		return;*/
	}

	@Override
	public void searchByName2(List<File> files, String dir, String name) {
		DbxClientV2 client  = c.getClient();
		try {
			client.files().getMetadata("/"+name);
			System.out.println("Fajl postoji.");
			return;
		} catch (DbxException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			System.out.println("Fajl ne postoji.");
			return;
		}
		
	}

	@Override
	public String searchByExtension1(String extension, String path) {
		ListFolderResult result = null;
		DbxClientV2 client = c.getClient();
		try {
			result = client.files().listFolder("");
			for (Metadata metadata : result.getEntries()) {
		    	if(metadata.getPathLower().endsWith("."+extension)) {
		    		return "Fajl nadjen -->" + metadata.getPathLower();
		    	}
		    }
		} catch (DbxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//System.out.println("Fajl ne postoji u repozitorijumu.");
		return "Fajl ne postoji u repozitorijumu.";
	}

	@Override
	public void searchByExtension2(List<File> files, String dir, String name) {
		// TODO Auto-generated method stub
		
	}

	private static Object readJsonSimpleDemo(String filename) throws Exception {
	    FileReader reader = new FileReader(filename);
	    JSONParser jsonParser = new JSONParser();
	    return jsonParser.parse(reader);
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
			JSONObject obj = null;
			try {
				obj = (JSONObject) readJsonSimpleDemo(f.getAbsolutePath().toString());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			JSONArray users= (JSONArray) obj.get("users");
		
			users.add(user);
			obj.put("users", users);
			
			
			BufferedWriter writer=null;
			
			writer= new BufferedWriter(new FileWriter(f.getAbsolutePath().toString()));
			writer.write(obj.toString());
			System.out.println(path);
			writer.close();
			
			DbxClientV2 client = c.getClient();
			try {
				client.files().delete("/users.json");
				
			} catch (DbxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				
			}
			
			try (InputStream in = new FileInputStream(f)) {
	    	    try {
					FileMetadata metadata = client.files().uploadBuilder("/users.json")
					    .uploadAndFinish(in);
				} catch (UploadErrorException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (DbxException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	    	}
			} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
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
		System.out.println(new File(source));
	    try {
		         
		    	ZipFile file = new ZipFile(source);
				file.extractAll(destination);
				System.out.println("Anzipovanje uspelo!");
		      
		    } catch (Exception e) {
		    	System.out.println("Anzipovanje nije uspelo");
		    	e.printStackTrace();
		    	return;
		    }
	}

}
