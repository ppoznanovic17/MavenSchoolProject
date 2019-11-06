package file.manipulation;

import java.io.File;
import java.util.List;
/**
 * Interfejs koji predstavlja specifikaciju koja definise razlicite metode pretrage nad fajlovima i folderima u odredjenom skladistu
 * (lokalnom i udaljenom)
 * @author Bogdan Stojadinovic
 * @author Petar Poznanovic
 *
 */
public interface FileSearch {

	/**
	 * Metoda koja prikazuje meta podatke za trenutne fajlove u skladistu
	 * @param path putanja gde ce se cuvati svi meta podaci za fajlove u skladistu (isti json fajl koji cuva i korisnike)
	 * @throws Exception
	 */
	 public void addForbiddenExtensions(String e,String path) throws Exception;

	 
	 /**
	  * Metoda za prikaz svih fajlova i foldera u trenutnom direktorijumu.
	  * (Nalik na komandu ls u command promtu) 
	  * @param Path putanja do trenutnog direktorijuma u kom se korisnik nalazi
	  */
	void ls(String Path);
	
	
	/**
	 * Metoda za kretanje po skladistu preko postojecih direktorijuma
	 * @param command komanda koja moze oznacavati kretanje unapred ili unazad u odnosu na trenutni direktorijum
	 * @param path putanja do trenutnog direktorijuma u kom se korisnik nalazi
	 * @param root putanja do root direktorijuma u skladistu, ima ulogu u definisanju granica do koje se korisnik moze kretati u skladistu
	 * @return Metoda vraca ime trenutnog direktorijuma u kom se korisnik nalazi nakon izvrsene komande
	 */
	public String cd(String[] command, String path, String root);

	/**
	 * Metoda za pretragu odredjenog fajla po imenu u trenutnom ali i u svim pod direktorijumima skladista
	 * (ime fajla sadrzi unesenu rec za pretragu)
	 * @param files lista fajlova u kojoj se proverava da li zadati fajl postoji (po imenu)
	 * @param dir putanja do trenutnog direktorijuma u kom se korisnik nalazi
	 * @param name ime fajla koji korisnik pretrazuje
	 */
	void searchByName1(List<File> files, String dir, String name);

	
	/**
	 * Metoda za pretragu odredjenog fajla po imenu u trenutnom ali i u svim pod direktorijumima skladista
	 * (ime fajla identicno kao unesena rec za pretragu)
	 * @param files lista fajlova u kojoj se proverava da li zadati fajl postoji (po imenu)
	 * @param dir putanja do trenutnog direktorijuma u kom se korisnik nalazi
	 * @param name ime fajla koji korisnik pretrazuje (puno ime)
	 */
	void searchByName2(List<File> files, String dir, String name);

	
	/**
	 * Metoda za pretragu svih fajlova po ekstenziji u trenutnom direktorijumu skladista
	 * @param extension naziv ekstenzije koju korisnik trazi  
	 * @param path putanja do trenutnog direktorijuma u kom se korisnik nalazi
	 * @return Metoda vraca sve fajlove koji postoje u trenutnom direktorijumu sa zadatom ekstenzijom
	 */
	public String searchByExtension1(String extension, String path);
	
	
	/**
	 * Metoda za pretragu svih fajlova po ekstenziji u trenutnom ali i u svim pod direktorijumima skladista
	 * @param extension naziv ekstenzije koju korisnik trazi  
	 * @param path putanja do trenutnog direktorijuma u kom se korisnik nalazi
	 * @return Metoda vraca sve fajlove koji postoje u trenutnom i svim pod direktorijumima sa zadatom ekstenzijom
	 */
	void searchByExtension2(List<File> files, String dir, String name);
	
}
