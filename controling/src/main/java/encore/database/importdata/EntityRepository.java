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
import encore.database.info.StringConnector;
import encore.database.info.TableInformation;

/**
 * Klasa wczytujaca dane z tabeli do <b> entityList</b>
 * 
 * @author Mariusz Dolata
 *
 */
public class EntityRepository {
	/**
	 * Lista wczytanych firm
	 */
	private List<Entity> entityList = new ArrayList<Entity>();
	private String databaseName;
	private String tableName;
	private String url;
	private String user;
	private String password;
	/**
	 * Przechowuje informacje o tabeli (nazwy oraz typy wszytkich kolumn)
	 */
	private TableInformation tableInformation;
	private String sqlSelectQuery;
	// wygenerowany pojedynczy rekord maj�cy struktur� wczytywanej tabeli.
	// zmieniaj�c tylko Object w HashMap b�d� dodawane kolejne obiekty
	private Entity entityTemplate;

	/**
	 * @param stringConnector
	 *            - informacje do polaczenia z serwerem mysql (url, user,
	 *            password, default_Schema)
	 * @param tableName
	 *            - nazwa tabeli do wczytania
	 */
	public EntityRepository(StringConnector stringConnector, String tableName) {
		this.databaseName = stringConnector.getDefaultSchema();
		this.url = stringConnector.getUrl();
		this.user = stringConnector.getUser();
		this.password = stringConnector.getPassword();
		this.tableName = tableName;
		this.tableInformation = new TableInformation(this.url, this.user, this.password, this.databaseName,
				this.tableName);
		// utworzenie zapytania obejmuj�cego wszystkie kolumny (dodaj inne typy)
		this.setSqlSelectQuery();
		// wczytanie danych do List<Entity> entityList
		this.loadData();

	}

	/**
	 * Metoda tworzaca zapytanie SELECT uwzgledniajace wszystkie kolumny w
	 * tabeli
	 */
	public void setSqlSelectQuery() {
		this.sqlSelectQuery = "SELECT ";
		for (ColumnInformation columnInformation : this.tableInformation.getColumns()) {
			String columnName = columnInformation.getColumnName();
			this.sqlSelectQuery += columnName;
			this.sqlSelectQuery += ", ";
		}
		// usuni�cie ostatniego przecinka
		this.sqlSelectQuery = this.sqlSelectQuery.substring(0, this.sqlSelectQuery.length() - 2);
		this.sqlSelectQuery += " FROM " + this.databaseName + "." + this.tableName;
		System.out.println("SELECT QUERY sql=" + this.sqlSelectQuery);

	}

	/**
	 * Metoda wczytujaca dane do entityList
	 */
	public void loadData() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
			Connection connection = DriverManager.getConnection("jdbc:mysql://" + this.url + "/" + this.databaseName
					+ "?user=" + this.user + "&password=" + this.password + "&serverTimezone=UTC");

			Statement stmt = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
			ResultSet rs = stmt.executeQuery(this.sqlSelectQuery);
			// wczytanie rekord�w z tabeli
			while (rs.next()) {
				Entity entity = new Entity();
				for (ColumnInformation columnInformation : this.tableInformation.getColumns()) {
					String columnName = columnInformation.getColumnName();
					String columnType = null;
					// wykrycie obiektu
					if (columnInformation.getColumnType().contains("int"))
						columnType = "int";
					else if (columnInformation.getColumnType().contains("char"))
						columnType = "String";
					else if (columnInformation.getColumnType().contains("text"))
						columnType = "String";
					else if (columnInformation.getColumnType().contains("timestamp"))
						columnType = "timestamp";
					// konkretne dane
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
					// wstawienie pojedynczej kolumny do obiektu Entiry
					entity.putEntity(columnName, dataType);
					// DODANIE PRZYK�ADOWYCH DANYCH NA PODSTAWIE ISTNIEJ�CYCH
					// KOLUMN W
					// TABELI
					// entity.putEntity(columnName, dataType);
					// System.out.println("Wstawianie warto�ci:" + dataType);
				}
				// Dodanie pojedynczego rekordu do listy
				this.getEntityList().add(entity);
			}

			connection.close();

		} catch (Exception e) {
			System.err.println("M: nie uda�o si� pobra� danych z tabeli " + this.tableName);
			e.printStackTrace();
		}

	}

	public String getSqlSelectQuery() {
		return sqlSelectQuery;
	}

	// Generuje zapytanie do SQL pobieraj�ce wszystkie dane z tabeli
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

	public TableInformation getTableInformation() {
		return tableInformation;
	}

	@Override
	public String toString() {
		return "EntityRepository [entityList=" + entityList + ", databaseName=" + databaseName + ", tableName="
				+ tableName + ", url=" + url + ", user=" + user + ", password=" + password + ", tableInformation="
				+ tableInformation + "]";
	}

}
