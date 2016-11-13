package encore.database.importdata;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import encore.database.info.TableInformation;
import encore.database.info.ColumnInformation;

/*Klasa wczytuje informacje nt danej tabeli oraz pobiera w³aœciwe informacje
 * 
 * 
 */

public class EntityRepository {
	private List<Entity> entityList = new ArrayList<Entity>();
	private String databaseName;
	private String tableName;
	private String url;
	private String user;
	private String password;
	private TableInformation tableInformation;
	//wygenerowany pojedynczy rekord maj¹cy strukturê wczytywanej tabeli.
	//zmieniaj¹c tylko Object w HashMap bêd¹ dodawane kolejne obiekty
	private Entity entityTemplate; 
	private List<Entity> entities;

	public EntityRepository(String url, String user, String password, String databaseName, String tableName) {
		this.databaseName=databaseName;
		this.tableName=tableName;
		this.url=url;
		this.user=user;
		this.password=password;
		this.tableInformation=new TableInformation(this.url, this.user, this.password, this.databaseName, this.tableName);

	}
	public void createEntityTemplate(){
		this.entityTemplate=new Entity();
		for(ColumnInformation ci:this.tableInformation.getColumns()){
			String columnName = ci.getColumnName();
			String columnType = null;
			//wykrycie obiektu
			if(ci.getColumnType().contains("int")) columnType="int";
			else if(ci.getColumnType().contains("varchar")) columnType="String";
			else if(ci.getColumnType().contains("text")) columnType="String";
			else if(ci.getColumnType().contains("date")) columnType="Date";
			
			switch(columnType){
			case "int":
				break;
			case "String":
				break;
			case "Date":
				break;
			}
		}
//		this.entityTemplate.setEntity(name, obj);
	}

	public List<Entity> getEntityList() {
		return entityList;
	}

	public void setEntityList(List<Entity> entityList) {
		this.entityList = entityList;
	}

	public String getDatabaseName() {
		return databaseName;
	}

	public void setDatabaseName(String databaseName) {
		this.databaseName = databaseName;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setTableInformation(TableInformation tableInformation) {
		this.tableInformation = tableInformation;
	}

	@Override
	public String toString() {
		return "EntityRepository [entityList=" + entityList + ", databaseName=" + databaseName + ", tableName="
				+ tableName + ", url=" + url + ", user=" + user + ", password=" + password + ", tableInformation="
				+ tableInformation + "]";
	}

	// WYWAL!!!!!!!!!!!!!
	public void getTableInformation() {
		EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("encore");
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		entityManager.getTransaction().begin();
		String sql = "SELECT id, tabela1str, tabela1data FROM Tabela1 WHERE id<3";
		Query query = entityManager.createQuery(sql);
		List<Tabela1> tabList = query.getResultList();
		entityManager.getTransaction().commit();
		entityManager.close();
		entityManagerFactory.close();

	}

}
