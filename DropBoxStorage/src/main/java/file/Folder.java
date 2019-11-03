package file;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.ListFolderContinueErrorException;
import com.dropbox.core.v2.files.ListFolderErrorException;
import com.dropbox.core.v2.files.ListFolderResult;
import com.dropbox.core.v2.files.Metadata;
import com.dropbox.core.v2.files.UploadErrorException;

import dropbox.Connection;
import file.manipulation.Storage;
import user.User;

public class Folder extends Storage {
	
	
	Connection c;
	
	@Override
	public File getJson(String path) throws IOException {
		
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String download(String fileName, String download_path, String filePath) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addForbiddenExtensions(String e, String path) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void printMeta(String path) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String addDir(String fileName, String path, String number, boolean meta, User u, String users)
			throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String addFile(String fileName, String path, String f, boolean metaBool, User u) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String upload(String fileName, String upload_path, String filePath) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void ls(String Path) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String cd(String[] command, String path, String root) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void searchByName1(List<File> files, String dir, String name) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void searchByName2(List<File> files, String dir, String name) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String searchByExtension1(String extension, String path) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void searchByExtension2(List<File> files, String dir, String name) {
		// TODO Auto-generated method stub
		
	}

	

}
