package encore.database.info;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
//import com.mysql.cj.api.jdbc.Statement;

public class SchemaInformation {
	private String dbName;
	private String tableName;
	private List<ColumnInformation> columns = new ArrayList<ColumnInformation>();
	public SchemaInformation() {

		try {
			Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
			Connection connection = DriverManager
					.getConnection("jdbc:mysql://localhost/baza_testowa?user=root&password=gurnik&serverTimezone=UTC");

			Statement stmt = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE); 
			
			ResultSet rs = stmt.executeQuery(
					"select COLUMN_NAME, DATA_TYPE, COLUMN_KEY FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA='baza_testowa' and TABLE_NAME='Tabela1'");
			while (rs.next()) {
				int i = 0;
				String columnName = rs.getString("COLUMN_NAME");
				String columnType = rs.getString("DATA_TYPE");
				String columnPK = rs.getString("COLUMN_KEY");

				System.out
						.println("nazwa kolumny=" + columnName + ", typ danych=" + columnType + ", klucz=" + columnPK);
				
			}
			connection.close();
		} catch (Exception e) {
			System.err.println("Coœ posz³o zle w JDBC");
			System.err.println(e.toString());
		}

	}

}
