package encore.database.importdata;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

/*Klasa wczytuje informacje nt danej tabeli oraz pobiera w³aœciwe informacje
 * 
 * 
 */

public class EntityRepository {
	private List<Entity> entityList = new ArrayList<Entity>();
	private String tableName;

	public EntityRepository(String tableName) {
		this.tableName = tableName;
		//pobranie informacji o tabeli - DODAJ MOZLIWOSC ZMIANY NAZWY TABELI
		this.getTableInformation();
	}

	public List<Entity> getEntityList() {
		return entityList;
	}

	public void setEntityList(List<Entity> entityList) {
		this.entityList = entityList;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	
	public void getTableInformation(){
		EntityManagerFactory entityManagerFactory=Persistence.createEntityManagerFactory("encore");
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		entityManager.getTransaction().begin();
		String sql="SELECT id, tabela1str, tabela1data FROM Tabela1 WHERE id<3";
		Query query = entityManager.createQuery(sql);
		List<Tabela1> tabList = query.getResultList();
		entityManager.getTransaction().commit();
		entityManager.close();
		entityManagerFactory.close();
		
	}

}
