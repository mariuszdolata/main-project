package encore.database.importdata;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import encore.database.info.ColumnInformation;
import encore.database.info.TableInformation;

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
	private String sqlSelectQuery;
	// wygenerowany pojedynczy rekord maj¹cy strukturê wczytywanej tabeli.
	// zmieniaj¹c tylko Object w HashMap bêd¹ dodawane kolejne obiekty
	private Entity entityTemplate;
	private List<Entity> entities;

	public EntityRepository(String url, String user, String password, String databaseName, String tableName) {
		this.databaseName = databaseName;
		this.tableName = tableName;
		this.url = url;
		this.user = user;
		this.password = password;
		this.tableInformation = new TableInformation(this.url, this.user, this.password, this.databaseName,
				this.tableName);
		//utworzenie zapytania obejmuj¹cego wszystkie kolumny (dodaj inne typy)
		this.setSqlSelectQuery();
		//wczytanie danych do List<Entity> entityList
		this.loadData();

	}

	@SuppressWarnings("deprecation")
	public void createEntityTemplate() {
		this.entityTemplate = new Entity();
		for (ColumnInformation ci : this.tableInformation.getColumns()) {
			String columnName = ci.getColumnName();
			String columnType = null;
			// wykrycie obiektu
			if (ci.getColumnType().contains("int"))
				columnType = "int";
			else if (ci.getColumnType().contains("varchar"))
				columnType = "String";
			else if (ci.getColumnType().contains("text"))
				columnType = "String";
			else if (ci.getColumnType().contains("timestamp"))
				columnType = "timestamp";
			Object dataType = null;
			switch (columnType) {
			case "int":
				dataType = (int) 0;
				break;
			case "String":
				dataType = (String) "text";
				break;
			case "timestamp":
				dataType = new Timestamp(116, 11, 14, 10, 25, 30, 20);
				break;
			}
			// DODANIE PRZYK£ADOWYCH DANYCH NA PODSTAWIE ISTNIEJ¥CYCH KOLUMN W
			// TABELI
			this.entityTemplate.putEntity(columnName, dataType);
			System.out.println("Wstawianie wartoœci:" + dataType);
		}
		System.out.println("Koniec wstawiania przyk³adowych danych");
		// this.entityTemplate.setEntity(name, obj);
	}

	public void setSqlSelectQuery() {
		this.sqlSelectQuery = "SELECT ";
		for (ColumnInformation columnInformation : this.tableInformation.getColumns()) {
			String columnName = columnInformation.getColumnName();
			this.sqlSelectQuery += columnName;
			this.sqlSelectQuery += ", ";
		}
		// usuniêcie ostatniego przecinka
		this.sqlSelectQuery = this.sqlSelectQuery.substring(0, this.sqlSelectQuery.length() - 2);
		this.sqlSelectQuery += " FROM " + this.databaseName + "." + this.tableName;
		System.out.println("sql=" + this.sqlSelectQuery);

	}

	public void loadData() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
			Connection connection = DriverManager.getConnection("jdbc:mysql://" + this.url + "/" + this.databaseName
					+ "?user=" + this.user + "&password=" + this.password + "&serverTimezone=UTC");

			Statement stmt = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
			ResultSet rs = stmt.executeQuery(this.sqlSelectQuery);
			//wczytanie rekordów z tabeli
			while(rs.next()){
				Entity entity= new Entity();
				for (ColumnInformation columnInformation : this.tableInformation.getColumns()) {
					String columnName = columnInformation.getColumnName();
					String columnType = null;
					// wykrycie obiektu
					if (columnInformation.getColumnType().contains("int"))
						columnType = "int";
					else if (columnInformation.getColumnType().contains("varchar"))
						columnType = "String";
					else if (columnInformation.getColumnType().contains("text"))
						columnType = "String";
					else if (columnInformation.getColumnType().contains("timestamp"))
						columnType = "timestamp";
					//konkretne dane
					Object dataType = null;
					switch (columnType) {
					case "int":
						dataType = rs.getInt(columnName);
						break;
					case "String":
						dataType = rs.getString(columnName);
						break;
					case "timestamp":
						dataType = rs.getTimestamp(columnName);
						break;
					}
					//wstawienie pojedynczej kolumny do obiektu Entiry
					entity.putEntity(columnName, dataType);
					// DODANIE PRZYK£ADOWYCH DANYCH NA PODSTAWIE ISTNIEJ¥CYCH KOLUMN W
					// TABELI
					entity.putEntity(columnName, dataType);
					System.out.println("Wstawianie wartoœci:" + dataType);
				}
				//Dodanie pojedynczego rekordu do listy
				this.getEntityList().add(entity);
			}
			
			connection.close();

		} catch (Exception e) {
			System.err.println("M: nie uda³o siê pobraæ danych z tabeli " + this.tableName);
			e.printStackTrace();
		}

	}

	public String getSqlSelectQuery() {
		return sqlSelectQuery;
	}

	// Generuje zapytanie do SQL pobieraj¹ce wszystkie dane z tabeli
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

	public Entity getEntityTemplate() {
		return entityTemplate;
	}

	public void setEntityTemplate(Entity entityTemplate) {
		this.entityTemplate = entityTemplate;
	}

	public List<Entity> getEntities() {
		return entities;
	}

	public void setEntities(List<Entity> entities) {
		this.entities = entities;
	}

	public TableInformation getTableInformation() {
		return tableInformation;
	}

	@Override
	public String toString() {
		return "EntityRepository [entityList=" + entityList + ", databaseName=" + databaseName + ", tableName="
				+ tableName + ", url=" + url + ", user=" + user + ", password=" + password + ", tableInformation="
				+ tableInformation + "]";
	}

	// WYWAL!!!!!!!!!!!!!
	// public void getTableInformation() {
	// EntityManagerFactory entityManagerFactory =
	// Persistence.createEntityManagerFactory("encore");
	// EntityManager entityManager = entityManagerFactory.createEntityManager();
	// entityManager.getTransaction().begin();
	// String sql = "SELECT id, tabela1str, tabela1data FROM Tabela1 WHERE
	// id<3";
	// Query query = entityManager.createQuery(sql);
	// List<Tabela1> tabList = query.getResultList();
	// entityManager.getTransaction().commit();
	// entityManager.close();
	// entityManagerFactory.close();
	//
	// }

}
