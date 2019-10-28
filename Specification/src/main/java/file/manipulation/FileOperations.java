package file.manipulation;

import java.io.IOException;

public interface FileOperations {

	String uploadDir(String fileName, String path);
	
	String delete (String fileName, String path) throws IOException;
	
	String download (String fileName, String path);
	
	String uploadFile(String filename, String path) throws IOException, Exception;

	void addForbiddenExtensions(String e, String path) throws Exception;

	
	
}
