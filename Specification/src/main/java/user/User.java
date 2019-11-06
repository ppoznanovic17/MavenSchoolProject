package user;

import java.util.ArrayList;
import java.util.List;
/**
 * Abs klasa koja definise specifikaciju za jedinstvenog korisnika konzolne aplikacije. Ovu klasu nasledjuje klasa User u konzolnoj aplikaciji.
 * 
 * @author Petar Poznanovic
 * @author Bogdan Stojadinovic
 *
 */
public abstract class User {

	public String username;
	public String password;
	public List<String> privileges;
	
	public List<String> getPrivileges() {
		return privileges;
	}
	public void setPrivileges(List<String> privileges) {
		this.privileges = privileges;
	}
	
	public String getPassword() {
		return password;
	}
	public String getUsername() {
		return username;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	
}
