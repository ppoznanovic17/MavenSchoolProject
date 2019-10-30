package file.manipulation;

import java.io.File;
import java.io.IOException;
import java.util.List;

public abstract class Storage implements FileOperations,FileSearch {
	
	public String path;
	public List<String> forbiddenExtensions;
	
	
	public Storage() {
		// TODO Auto-generated constructor stub
	}
	
	public Storage(String path) {
		this.path=path;
	}
	
	abstract public File getJson(String path) throws IOException;
	
	public List<String> getForbiddenExtensions() {
		return forbiddenExtensions;
	}
	public String getPath() {
		return path;
	}
	public void setForbiddenExtensions(List<String> forbiddenExtensions) {
		this.forbiddenExtensions = forbiddenExtensions;
	}
	public void setPath(String path) {
		this.path = path;
	}
	
	
}
