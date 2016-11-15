package encore.matching;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import encore.database.importdata.Entity;
import encore.database.importdata.EntityRepository;
import encore.database.info.ColumnInformation;
import encore.database.info.StringConnector;
import info.debatty.java.stringsimilarity.Levenshtein;

public class MatchingRepository {
	private CompanyRepository firstCompanyRepository;
	private CompanyRepository secondCompanyRepository;
	private StringConnector stringConnector;
	private int counter = 0;
	private byte[][] distanceTable;

	public int getCounter() {
		return counter;
	}

	public void setCounter(int counter) {
		this.counter = counter;
	}

	public byte[][] getDistanceTable() {
		return distanceTable;
	}

	public void setDistanceTable(byte[][] distanceTable) {
		this.distanceTable = distanceTable;
	}

	public CompanyRepository getFirstCompanyRepository() {
		return firstCompanyRepository;
	}

	public void setFirstCompanyRepository(CompanyRepository firstCompanyRepository) {
		this.firstCompanyRepository = firstCompanyRepository;
	}

	public CompanyRepository getSecondCompanyRepository() {
		return secondCompanyRepository;
	}

	public void setSecondCompanyRepository(CompanyRepository secondCompanyRepository) {
		this.secondCompanyRepository = secondCompanyRepository;
	}

	public StringConnector getStringConnector() {
		return stringConnector;
	}

	public void setStringConnector(StringConnector stringConnector) {
		this.stringConnector = stringConnector;
	}

	public MatchingRepository(String filePath1, String filePath2) throws IOException {
		super();
		this.firstCompanyRepository = new CompanyRepository(filePath1);
		this.secondCompanyRepository = new CompanyRepository(filePath2);
		this.startMatching(firstCompanyRepository, secondCompanyRepository);
		this.printMatchingResults();
	}

	public MatchingRepository(StringConnector stringConnector, EntityRepository firstEntityRepository,
			String firstfirstMatchedColumn, EntityRepository secondEntityRepository, String secondMatchedColumn) {
		this.stringConnector = stringConnector;
		this.firstCompanyRepository = new CompanyRepository(firstEntityRepository, firstfirstMatchedColumn);
		this.secondCompanyRepository = new CompanyRepository(secondEntityRepository, secondMatchedColumn);
		this.startMatching(firstCompanyRepository, secondCompanyRepository);
		this.printMatchingResults();
		// this.saveResultArray();
		this.saveMatchedResults();
	}

	public void startMatching(CompanyRepository firstCompanyRepository, CompanyRepository secondCompanyRepository) {

		// inicjalizacja tabeli wyników uzale¿niona od liczby stringów w
		// repozytoriach
		distanceTable = new byte[firstCompanyRepository.getCompanies().size()][secondCompanyRepository.getCompanies()
				.size()];
		Levenshtein levenshtein = new Levenshtein();

		for (int i = 0; i < firstCompanyRepository.getCompanies().size(); i++) {
			for (int j = 0; j < secondCompanyRepository.getCompanies().size(); j++) {
				CharSequence first = firstCompanyRepository.getCompanies().get(i).getTidyCompanyName();
				CharSequence second = secondCompanyRepository.getCompanies().get(j).getTidyCompanyName();
				// System.out.println("zamieniono wartoœci " + first + "|| " +
				// second);
				if (first != null && second != null) {
					counter++;
					try {
						double score = levenshtein.distance(
								firstCompanyRepository.getCompanies().get(i).getTidyCompanyName(),
								secondCompanyRepository.getCompanies().get(j).getTidyCompanyName());
						if (score < 120)
							distanceTable[i][j] = (byte) score;
						else
							distanceTable[i][j] = 120;
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	public void printMatchingResults() {
		for (int i = 0; i < firstCompanyRepository.getCompanies().size(); i++) {
			for (int j = 0; j < secondCompanyRepository.getCompanies().size(); j++) {
				if (this.distanceTable[i][j] == 0)
					System.out.println("[" + this.distanceTable[i][j] + "][" + i + "][" + j + "]["
							+ firstCompanyRepository.getCompanies().get(i).getTidyCompanyName() + "]["
							+ secondCompanyRepository.getCompanies().get(j).getTidyCompanyName() + "]");
				// System.out.print("[" + multi[i][j] + "]");
			}
			// System.out.println("");
		}
	}

	public void saveResultArray() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
			Connection connection = DriverManager.getConnection("jdbc:mysql://" + this.stringConnector.getUrl() + "/"
					+ this.getStringConnector().getDefaultSchema() + "?user=" + this.getStringConnector().getUser()
					+ "&password=" + this.getStringConnector().getPassword() + "&serverTimezone=UTC");

			Statement stmt = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
			String dropResiltTableSql = "DROP TABLE IF EXISTS `" + this.getStringConnector().getDefaultSchema()
					+ "`.`matching_results`;";
			String createResultTableSql = "CREATE TABLE `" + this.getStringConnector().getDefaultSchema()
					+ "`.`matching_results` (`id` INT NOT NULL AUTO_INCREMENT, `score` INT NULL,  `first_position` INT NULL,  `first_string` VARCHAR(500) NULL,  `second_position` INT NULL,  `second_string` VARCHAR(500) NULL,  PRIMARY KEY (`id`),  INDEX `index1` (`first_string` ASC),  INDEX `index2` (`second_string` ASC));";
			// usuniecie tabeli
			stmt.executeUpdate(dropResiltTableSql);
			// stworzenie nowej
			stmt.executeUpdate(createResultTableSql);

			for (int i = 0; i < firstCompanyRepository.getCompanies().size(); i++) {
				for (int j = 0; j < secondCompanyRepository.getCompanies().size(); j++) {
					if (this.distanceTable[i][j] == 0) {
						String first = firstCompanyRepository.getCompanies().get(i).getCompanyName();
						String second = secondCompanyRepository.getCompanies().get(j).getCompanyName();
						String insertSql = "INSERT INTO matching_results (score, first_position, first_string, second_position, second_string) VALUES ("
								+ (int) this.distanceTable[i][j] + "," + i + ",'" + first + "'," + j + ", '" + second
								+ "');";
						System.out.println(">>>" + insertSql + "<<<");
						stmt.executeUpdate(insertSql);
					}
				}
			}
			connection.close();

		} catch (Exception e) {
			System.err.println("M: nie uda³o siê zapisaæ wynikow matchowania MatchingRepository->saveResultArray() ");
			e.printStackTrace();
		}

	}

	public void saveMatchedResults() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
			Connection connection = DriverManager.getConnection("jdbc:mysql://" + this.stringConnector.getUrl() + "/"
					+ this.getStringConnector().getDefaultSchema() + "?user=" + this.getStringConnector().getUser()
					+ "&password=" + this.getStringConnector().getPassword() + "&serverTimezone=UTC");

			Statement stmt = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

			// firstCompanyRepository.getEntityRepository().getEntities().get(1).getEntity("id");
			// ITERACJA NAZW KOLUMN
			String dropResiltTableSql = "DROP TABLE IF EXISTS `" + this.getStringConnector().getDefaultSchema()
					+ "`.`matching_results`;";
			String sqlCreateTable = "CREATE TABLE `" + this.getStringConnector().getDefaultSchema()
					+ "`.`matching_results` (";
			for (int i = 0; i < firstCompanyRepository.getEntityRepository().getTableInformation().getColumns()
					.size(); i++) {

				sqlCreateTable += "`first_"
						+ firstCompanyRepository.getEntityRepository().getTableInformation().getColumns().get(i)
								.getColumnName()
						+ "` " + firstCompanyRepository.getEntityRepository().getTableInformation().getColumns().get(i)
								.getColumnType()
						+ ", ";

			}
			for (int i = 0; i < secondCompanyRepository.getEntityRepository().getTableInformation().getColumns()
					.size(); i++) {

				sqlCreateTable += "`second_"
						+ secondCompanyRepository.getEntityRepository().getTableInformation().getColumns().get(i)
								.getColumnName()
						+ "` " + secondCompanyRepository.getEntityRepository().getTableInformation().getColumns().get(i)
								.getColumnType()
						+ ", ";

			}
			sqlCreateTable = sqlCreateTable.substring(0, sqlCreateTable.length() - 2);
			sqlCreateTable += ");";
			stmt.executeUpdate(dropResiltTableSql);
			stmt.executeUpdate(sqlCreateTable);
			// firstCompanyRepository.getEntityRepository().getTableInformation().getColumns().get(3).getColumnType();
			System.out.println("sqlPart>>>>" + sqlCreateTable + "<<<");

			String sqlColumnsName = "INSERT INTO `" + this.getStringConnector().getDefaultSchema()
					+ "`.`matching_results` (";
			// kolumny z pierwszej tabeli
			for (int iter1 = 0; iter1 < firstCompanyRepository.getEntityRepository().getTableInformation().getColumns()
					.size(); iter1++) {
				String columnName = firstCompanyRepository.getEntityRepository().getTableInformation().getColumns()
						.get(iter1).getColumnName();
				sqlColumnsName += "first_" + columnName + ", ";
			}
			// kolumny z drugiej tabeli
			for (int iter1 = 0; iter1 < secondCompanyRepository.getEntityRepository().getTableInformation().getColumns()
					.size(); iter1++) {
				String columnName = secondCompanyRepository.getEntityRepository().getTableInformation().getColumns()
						.get(iter1).getColumnName();
				sqlColumnsName += "second_" + columnName + ", ";
			}
			sqlColumnsName = sqlColumnsName.substring(0, sqlColumnsName.length() - 2);
			sqlColumnsName += ") VALUES ";
			String sqlColumnsData = " ";
			int insertIteration = 0;

			for (int i = 0; i < firstCompanyRepository.getCompanies().size(); i++) {
				for (int j = 0; j < secondCompanyRepository.getCompanies().size(); j++) {
					if (this.distanceTable[i][j] == 0) {
						insertIteration++;
						sqlColumnsData += "(";
						// kolumny z pierwszej tabeli
						for (int iter1 = 0; iter1 < firstCompanyRepository.getEntityRepository().getTableInformation()
								.getColumns().size(); iter1++) {
							String columnName = firstCompanyRepository.getEntityRepository().getTableInformation()
									.getColumns().get(iter1).getColumnName();
							// wartoœæ konkretnej kolumny
							Object columnValue = firstCompanyRepository.getEntityRepository().getEntityList().get(i)
									.getEntity(columnName);
							sqlColumnsData += "'" + String.valueOf(columnValue) + "', ";
						}
						// kolumny z drugiej tabeli
						for (int iter1 = 0; iter1 < secondCompanyRepository.getEntityRepository().getTableInformation()
								.getColumns().size(); iter1++) {
							String columnName = secondCompanyRepository.getEntityRepository().getTableInformation()
									.getColumns().get(iter1).getColumnName();

							// wartoœæ konkretnej kolumny
							Object columnValue = secondCompanyRepository.getEntityRepository().getEntityList().get(j)
									.getEntity(columnName);
							sqlColumnsData += "'" + String.valueOf(columnValue) + "', ";
						}
						sqlColumnsData = sqlColumnsData.substring(0, sqlColumnsData.length() - 2);
						sqlColumnsData += "), ";
						//INSERT multiple row
						if (insertIteration % 100 == 0) {
							sqlColumnsData = sqlColumnsData.substring(0, sqlColumnsData.length() - 2);
							System.out.println("SQLCOLUMNDATA>>>" + sqlColumnsData);
							String sqlInsertEntity = sqlColumnsName + sqlColumnsData;
							System.out.println("INSERT>>>>" + sqlInsertEntity);
							stmt.executeUpdate(sqlInsertEntity);
							sqlColumnsData = "";
						}
					}
				}
			}
			
			//wstawienie reszty 
			sqlColumnsData = sqlColumnsData.substring(0, sqlColumnsData.length() - 2);
			System.out.println("SQLCOLUMNDATA>>>" + sqlColumnsData);
			String sqlInsertEntity = sqlColumnsName + sqlColumnsData;
			System.out.println("INSERT>>>>" + sqlInsertEntity);
			stmt.executeUpdate(sqlInsertEntity);
			sqlColumnsData = "";
			connection.close();

		} catch (Exception e) {
			System.err.println("M: nie uda³o siê zapisaæ wynikow matchowania MatchingRepository->saveResultArray() ");
			e.printStackTrace();
		}

	}

}
