package dropbox;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import com.dropbox.core.DbxApiException;
import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.UploadErrorException;
import com.dropbox.core.v2.teamlog.FileOrFolderLogInfo;
import com.dropbox.core.v2.users.FullAccount;

import file.Folder;
import file.manipulation.Storage;

public class Connection {

	//private static final String ACCESS_TOKEN = "73QM8TaW9xAAAAAAAAAALM7btVUpGJ16bq3tldeogmcgjIS_xsHXlGS5ACc-FmUL";
	 private static final String ACCESS_TOKEN = "3Pl0YbNvFhAAAAAAAAAAGGf2O5hzjUTYTHVO9SC265yR7MyE1oDyIOIHXVelpmET";

	 static DbxRequestConfig config;
	 static DbxClientV2 client;
	 static public Storage f= new Folder();	 
	 
	 static String currentFolder= "";
		static String jsonUsers="";
		static String root;
	 
	 public Connection () {
		 
	 }
	
	
	 
	 public static void connetction() throws UploadErrorException, DbxException, IOException {
		  config = DbxRequestConfig.newBuilder("dropbox/java-tutorial").build();
	        client = new DbxClientV2(config, ACCESS_TOKEN);
	        
	        FullAccount account = null;
			try {
				account = client.users().getCurrentAccount();
			} catch (DbxApiException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (DbxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        //System.out.println(account.getName().getDisplayName());
	        
	        
	        
	 }
	 
	 public static DbxClientV2 getClient() {
		return client;
	}
	 public static DbxRequestConfig getConfig() {
		return config;
	}
	 
	public static String getToken(){
		 return ACCESS_TOKEN;
	 }
	 
}
