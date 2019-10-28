package console.app;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class MainConsoleApp {
	/*
	 * @Petar & @Bogdan
	 * 
	 * C:\Users\Peca\Desktop\html css skripte
	 * */
	
	public static void main(String[] args) {
		BufferedReader reader=new BufferedReader(new InputStreamReader(System.in));
		ConsoleFunctions.write("Dobrodosli");
		
		//ucitavanje repozitorijuma
		String path=ConsoleFunctions.question("Unesite putanju do repozitorijuma:");
		
	}
	
	
	
}
