package file.manipulation;

import java.io.File;
import java.io.IOException;

public interface FileOperations {

	String uploadDir(String fileName, String path,String number);
	
	String delete (String fileName, String path) throws IOException;
	
	String download (String fileName, String download_path,String filePath);
	
	String uploadFile(String filename, String path,String file) throws IOException, Exception;

	void addForbiddenExtensions(String e, String path) throws Exception;

	
	
}
