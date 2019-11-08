package file.manipulation;

import java.io.File;
import java.io.IOException;
import java.util.List;
/**
 * Abs klasa koja predstavlja specifikaciju za upravljanje folderima i fajlovima u skladistu.
 * Ova klasa implementira oba interfejsa koji sadrze metode za upravljanje fajlovima (FileOperations i FileSearch)
 * i nju nasledjuju klase koje prestavljaju implementaciju upravljanja fajlovima/folderima za odredjeno skladiste.
 * (implementacija za oba skladista nasledjuje ovu klasu).
 * @author Petar Poznanovic
 * @author Bogdan Stojadinovic
 *
 */
public abstract class Storage implements FileOperations,FileSearch {
	
	public String path;
	public List<String> forbiddenExtensions;
	public String type;
	
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
	
	public String getType() {
		return type;
	}
	
	abstract public void welcome();
	
}
