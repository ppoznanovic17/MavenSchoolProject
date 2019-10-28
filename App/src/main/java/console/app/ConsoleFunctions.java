package console.app;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ConsoleFunctions {
	
	public static BufferedReader reader=new BufferedReader(new InputStreamReader(System.in));
	
	public static void write(String message) {
		System.out.println(message);
	}
	
	public static String question(String message) {
		System.out.println(message);
		String answer="";
		try {
			answer=reader.readLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return answer;
		
	}
	
}
