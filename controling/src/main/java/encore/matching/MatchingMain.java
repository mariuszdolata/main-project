package encore.matching;

import java.io.IOException;
import java.math.BigDecimal;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import encore.database.importdata.EntityRepository;
import encore.database.info.TableInformation;

public class MatchingMain {

	public static void main(String[] args) throws IOException {
		System.out.println("MatchingMain START!");
		String filePath1="F:\\p1.txt";
		String filePath2="F:\\p2.txt";
		long start=System.currentTimeMillis();
		EntityRepository firstEntityRepository = new EntityRepository("localhost", "java", "java", "baza_testowa", "tabela1");
		EntityRepository secondEntityRepository = new EntityRepository("localhost", "java", "java", "baza_testowa", "tabela2");
		MatchingRepository matchingRepositoryDatabase = new MatchingRepository(firstEntityRepository, "textTab1", secondEntityRepository, "textTab2");
		MatchingRepository matchingRepository = new MatchingRepository(filePath1, filePath2);
		long stop = System.currentTimeMillis();
		
//		System.out.println("czas trwania = "+(stop-start)+",  liczba wykonanych operacji="+matchingRepository.getCounter());
		
		
//		firstEntityRepository.createEntityTemplate();

//		
//		TableInformation schemaInformation1 = new TableInformation("localhost", "java", "java", "baza_testowa", "tabela1");
//		TableInformation schemaInformation2 = new TableInformation("localhost", "java", "java", "baza_testowa", "tabela2");
//		
		
		System.out.println("Koniec");

	}

}