package file.manipulation;

import java.io.File;
import java.io.IOException;

import user.User;

public interface FileOperations {

	
	
	String delete (String fileName, String path) throws IOException;
	
	String download (String fileName, String download_path,String filePath);
	
	

	void addForbiddenExtensions(String e, String path) throws Exception;

	
	void printMeta(String path) throws Exception;


	String addDir(String fileName, String path, String number, boolean meta, User u, String users) throws IOException;

	

	String addFile(String fileName, String path, String f, boolean metaBool, User u) throws Exception;

	String upload(String fileName, String upload_path, String filePath);
	
	
}
