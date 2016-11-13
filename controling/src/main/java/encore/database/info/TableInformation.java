package encore.database.info;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class TableInformation {
	private String databaseName;
	private String tableName;
	private String url;
	private String user;
	private String password;
	private List<ColumnInformation> columns = new ArrayList<ColumnInformation>();

	public TableInformation(String url, String user, String password, String databaseName, String tableName) {
		this.databaseName = databaseName;
		this.tableName = tableName;
		this.url = url;
		this.user = user;
		this.password = password;

		try {
			Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
			Connection connection = DriverManager.getConnection("jdbc:mysql://" + this.url + "/" + this.databaseName
					+ "?user=" + this.user + "&password=" + this.password + "&serverTimezone=UTC");

			Statement stmt = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

			ResultSet rs = stmt.executeQuery("select TABLE_CATALOG, " + "TABLE_SCHEMA, " + "TABLE_NAME, "
					+ "COLUMN_NAME, " + "ORDINAL_POSITION," + " COLUMN_DEFAULT, " + "IS_NULLABLE, " + "DATA_TYPE, "
					+ "CHARACTER_MAXIMUM_LENGTH, " + "CHARACTER_OCTET_LENGTH, " + "NUMERIC_PRECISION,"
					+ " NUMERIC_SCALE, " + "DATETIME_PRECISION," + " CHARACTER_SET_NAME, " + "COLLATION_NAME, "
					+ "COLUMN_TYPE, " + "COLUMN_KEY, " + "EXTRA, " + "PRIVILEGES, " + "COLUMN_COMMENT, "
					+ "GENERATION_EXPRESSION " + "FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA='"
					+ this.databaseName + "' and TABLE_NAME='" + this.tableName + "'");
			while (rs.next()) {
				int i = 0;

				ColumnInformation columnInformation = new ColumnInformation();
				columnInformation.setTableSchema(rs.getString("TABLE_SCHEMA"));
				columnInformation.setTableName(rs.getString("TABLE_NAME"));
				columnInformation.setColumnName(rs.getString("COLUMN_NAME"));
				columnInformation.setOrdinalPosition(rs.getInt("ORDINAL_POSITION"));
				columnInformation.setColumnDefault(rs.getString("COLUMN_DEFAULT"));
				if (rs.getString("IS_NULLABLE").contains("NO"))
					columnInformation.setIsNullable(false);
				if (rs.getString("IS_NULLABLE").contains("YES"))
					columnInformation.setIsNullable(true);
				columnInformation.setCharacterMaximumLength(rs.getInt("CHARACTER_MAXIMUM_LENGTH"));
				columnInformation.setCharacterOctetLength(rs.getInt("CHARACTER_OCTET_LENGTH"));
				columnInformation.setNumericPrecision(rs.getInt("NUMERIC_PRECISION"));
				columnInformation.setNumericScale(rs.getInt("NUMERIC_SCALE"));
				columnInformation.setDateTimePrecision(rs.getInt("DATETIME_PRECISION"));
				columnInformation.setCharacterSetName(rs.getString("CHARACTER_SET_NAME"));
				columnInformation.setCollationName(rs.getString("COLLATION_NAME"));
				columnInformation.setColumnType(rs.getString("COLUMN_TYPE"));
				if (rs.getString("COLUMN_KEY").contains("PRI"))
					columnInformation.setColumnPK(true);
				else
					columnInformation.setColumnPK(false);
				columnInformation.setExtra(rs.getString("EXTRA"));
				columnInformation.setPrivileges(rs.getString("PRIVILEGES"));
				columnInformation.setCollumnComment(rs.getString("COLUMN_COMMENT"));
				columnInformation.setGenerationExpression(rs.getString("GENERATION_EXPRESSION"));

				columns.add(columnInformation);
				System.out.println(columnInformation.toString());

			}
			connection.close();
		} catch (Exception e) {
			System.err.println("Coœ posz³o zle w JDBC");
			System.err.println(e.toString());
		}

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

	public List<ColumnInformation> getColumns() {
		return columns;
	}

	public void setColumns(List<ColumnInformation> columns) {
		this.columns = columns;
	}

	@Override
	public String toString() {
		return "TableInformation [databaseName=" + databaseName + ", tableName=" + tableName + ", url=" + url
				+ ", user=" + user + ", password=" + password + ", columns=" + columns + "]";
	}

}
