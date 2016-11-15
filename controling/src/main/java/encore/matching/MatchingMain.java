package encore.matching;

import java.io.IOException;

import encore.database.importdata.EntityRepository;
import encore.database.info.StringConnector;

/**
 * Klasa "main" sluzaca do matchowania firm z dwoch zrodel (plik lub tabela).
 * <BR>
 * Dla porownania firm z dwoch tabel wywolaj <b>matchingRepository</b> z
 * obiektem stringConnector oraz dwoma zrodlami danych (obiekt @See
 * EntityRepository) <BR>
 * Dla porownania firm z dwoch plikow tekstowych wywolaj
 * <b>matchingRepository</b> z parametrami String <b>filePath1</b> oraz String
 * <b>filePath2</b>
 * 
 * @author Mariusz Dolata @2016
 * @See EntityRepository
 */
public class MatchingMain {

	public static void main(String[] args) throws IOException {
		System.out.println("MatchingMain START!");
		String filePath1 = "F:\\p1.txt";
		String filePath2 = "F:\\p2.txt";
		long start = System.currentTimeMillis();
		StringConnector stringConnector = new StringConnector("localhost", "java", "java", "baza_testowa");
		EntityRepository firstEntityRepository = new EntityRepository(stringConnector, "tabela1");
		EntityRepository secondEntityRepository = new EntityRepository(stringConnector, "tabela2");
		MatchingRepository matchingRepositoryDatabase = new MatchingRepository(stringConnector, firstEntityRepository,
				"firma", secondEntityRepository, "firma");

		long stop = System.currentTimeMillis();

		System.out.println("Koniec");

	}

}