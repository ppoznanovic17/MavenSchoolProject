package console.app;

import java.awt.List;

public interface UserManipulation {

	void welcome();
	
	void singUpAnotherUser();
	
	void logIn(String user, String password);	
	
	String question(String question);
	
	
}
