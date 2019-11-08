package file.manipulation;

import java.io.File;
import java.io.IOException;

import user.User;
/**
 * Interfejs koji predstavlja specifikaciju koja definise metode koje se vrse nad fajlovima i folderima u odredjenom skladistu
 * (lokalnom ili udaljenom)
 * @author Bogdan Stojadinovic
 * @author Petar Poznanovic
 *
 */

public interface FileOperations {

	
	/**
	 * Metoda za brisanje fajla/foldera iz skladista
	 * @param fileName ime fajla za brisanje
	 * @param path putanja trenutnog direktorijuma u kom se korisnik nalazi
	 * @return metoda vraca string koji nam govori da li je fajl uspesno obrisan ili ne
	 * @throws IOException
	 */
	String delete (String fileName, String path) throws IOException;
	/**
	 * Metoda za preuzimanje fajla/foldera iz skladista na odredjenu putanju
	 * @param fileName ime fajla za preuzimanje
	 * @param download_path putanja na kojoj korisnik zeli da preuzme fajl
	 * @param filePath putanja trenutnog direktorijuma u kom se korisnik nalazi
	 * @return Metoda vraca poruku u obliku stringa koja obavestava korisnika o uspesnosti preuzimanja fajla
	 */
	String download (String fileName, String download_path,String filePath);
	
	
/**
 * Metoda za dodeljivanje ekstenzija koje ce biti zabranjene prilikom kreiranja fajla sa takvom ekstenzijom.
 * Ovu metoda je omogucena samo root korisniku (adminu).
 * @param e naziv ekstenzije 
 * @param path putanja gde ce se cuvati da je ekstenzija zabranjena (isti json fajl koji cuva i korisnike)
 * @throws Exception
 */
	void addForbiddenExtensions(String e, File f) throws Exception;

	/**
	 * Metoda koja prikazuje meta podatke za trenutne fajlove u skladistu
	 * @param path putanja gde ce se cuvati svi meta podaci za fajlove u skladistu (isti json fajl koji cuva i korisnike)
	 * @throws Exception
	 */
	void printMeta(String path) throws Exception;

/**
 * Metoda za dodavanje novog direktorijuma u skladiste
 * 
 * @param fileName ime direktorijuma koji se dodaje
 * @param path putanja trenutnog direktorijuma u kom se korisnik nalazi
 * @param number broj direktorijuma koliko korisnik zeli da kreira 
 * @param meta boolean koji je true ako korisnik izabere da zeli da cuva metapodatke vezane za novi direktorijum
 * @param u korisnik koji dodaje novi direktorijum
 * @param users  json fajl u koji se upisuju metapodaci za direktorijume
 * @return Metoda vraca poruku o uspesnosti kreiranja novog direktorijuma
 * @throws IOException
 */
	String addDir(String fileName, String path, String number, boolean meta, User u, File f) throws IOException;

	
/**
 * Metoda za dodavanje novog fajla u skladiste
 * @param fileName ime fajla koji se dodaje
 * @param path putanja trenutnog direktorijuma u kom se korisnik nalazi
 * @param f json users fajl u koji se upisuju meta podaci novog fajla koji se dodaje
 * @param metaBool boolean koji je true ukoliko korisnik zeli da cuva metapodatke za novi fajl koji kreira
 * @param u korisnik koji dodaje novi fajl
 * @return Metoda vraca poruku o uspesnosti kreiranja novog fajla
 * @throws Exception
 */
	String addFile(String fileName, String path, File f, boolean metaBool, User u) throws Exception;
	
	
/**
 * Metoda za uploadovanje postojeceg fajla na skladiste
 * @param fileName ime fajla koji se uploaduje
 * @param upload_path putanja na koju korisnik zeli da uploaduje fajl
 * @param filePath putanja trenutnog direktorijuma u kom se korisnik nalazi
 * @return Metoda vraca poruku o uspesnosti uploadovanja fajla na skladiste
 */
	String upload(String fileName, String upload_path, String filePath);
	
	
/**
 * Metoda za dodavanje novog korisnika u korisnicku bazu (users.json fajl).
 * Ovu metodu moze upotrebiti samo root korisnik (admin).
 * @param f fajl u koji ce se novi korisnik dodati
 */
	void singUpAnotherUser(File f);
	
	
}
