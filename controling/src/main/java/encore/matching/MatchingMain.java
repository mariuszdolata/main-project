package encore.matching;

import java.io.IOException;
import java.math.BigDecimal;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import encore.importdata.database.EntityRepository;

public class MatchingMain {

	public static void main(String[] args) throws IOException {
		System.out.println("MatchingMain START!");
		String filePath1="F:\\p1.txt";
		String filePath2="F:\\p2.txt";
		long start=System.currentTimeMillis();
//		MatchingRepository matchingRepository = new MatchingRepository(filePath1, filePath2);
		long stop = System.currentTimeMillis();
		
//		System.out.println("czas trwania = "+(stop-start)+",  liczba wykonanych operacji="+matchingRepository.getCounter());
		
		
//		EntityRepository er = new EntityRepository("bbb");
		EntityManagerFactory entityManagerFactory=Persistence.createEntityManagerFactory("encoreDB");
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		
		entityManager.clear();
		entityManagerFactory.close();
		
		System.out.println("M: The end");

	}

}