package file.manipulation;

import java.io.File;
import java.util.List;

public interface FileSearch {

	
	 public void addForbiddenExtensions(String e,String path) throws Exception;

	void ls(String Path);
	
	public String cd(String[] command, String path, String root);


	void searchByName1(List<File> files, String dir, String name);

	void searchByName2(List<File> files, String dir, String name);

	public String searchByExtension1(String extension, String path);
	
	void searchByExtension2(List<File> files, String dir, String name);
	
}
