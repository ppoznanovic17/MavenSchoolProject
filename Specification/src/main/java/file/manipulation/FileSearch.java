package file.manipulation;

public interface FileSearch {

	void searchByName(String fileName);
	
	String searchByExtension(String extension,String path);
	
	void searchByMetaData(int metaOrNo);
	
	void ls(String Path);
	
	

	String cd(String[] command, String path, String root);
	
}
