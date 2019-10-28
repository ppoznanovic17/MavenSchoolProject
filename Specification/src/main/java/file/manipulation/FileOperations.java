package file.manipulation;

public interface FileOperations {

	String upload(String fileName, String path);
	
	String delete (String fileName, String path);
	
	String download (String fileName, String path);
	
	
	
}
