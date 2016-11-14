package encore.matching;

import java.io.IOException;

import encore.database.importdata.EntityRepository;
import encore.database.info.StringConnector;

public class MatchingMain {

	public static void main(String[] args) throws IOException {
		System.out.println("MatchingMain START!");
		String filePath1 = "F:\\p1.txt";
		String filePath2 = "F:\\p2.txt";
		long start = System.currentTimeMillis();
		StringConnector stringConnector = new StringConnector("localhost", "java", "java", "baza_testowa");
		EntityRepository firstEntityRepository = new EntityRepository(stringConnector,"tabela1");
		EntityRepository secondEntityRepository = new EntityRepository(stringConnector,	"tabela2");
		MatchingRepository matchingRepositoryDatabase = new MatchingRepository(stringConnector, firstEntityRepository, "textTab1",
				secondEntityRepository, "textTab2");
//		MatchingRepository matchingRepository = new MatchingRepository(filePath1, filePath2);
		long stop = System.currentTimeMillis();

		System.out.println("Koniec");

	}

}