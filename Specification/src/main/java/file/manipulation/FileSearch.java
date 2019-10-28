package file.manipulation;

public interface FileSearch {

	void searchByName(String fileName);
	
	void searchByExtension(String extension);
	
	void searchByMetaData(int metaOrNo);
	
	void ls(String Path);
	
	void cd();
	
}
