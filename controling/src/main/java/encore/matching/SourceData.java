package encore.matching;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class SourceData {
	public void SourceData(){
		EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("encore");
		EntityManager entityManager = entityManagerFactory.createEntityManager();

	
		entityManager.close();
		entityManagerFactory.close();
	}
		

}
