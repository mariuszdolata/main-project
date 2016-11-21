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

/**
 * Klasa MatchingRepository matchuje dwa dowolne zbiory firm CompanyRepository
 * (z pliku tekstowego lub bazy danych SQL). Dla u³¹twienia matchowania
 * porównywane ci¹gi znaków s¹ czyszone z niepotrzebnych bia³ych znaków, kropek
 * czy typów spó³ek. Wielkoœæ znaków nie ma znaczenia w matchowaniu.
 * 
 * Domyœlnym algorytmem jest nieznormalizowany Levenshtein distance. Istnieje
 * mo¿liwoœæ zmiany algorytmu na znormalizowane ale koniecznie trzeba dostosowaæ
 * tablicê byte distanceTable[][] na float distanceTable[][]
 * 
 * <BR>
 * <BR>
 * Dla algorytmu niezmormalizowanego <BR>
 * <B>private int condition=0;</B>
 * 
 * <BR>
 * <BR>
 * Dla algorytmu znormalizowanego <BR>
 * <B>private int condition=1</B>
 * 
 * @author Mariusz Dolata @2016
 *
 */

public class MatchingRepository {
	/**
	 * Pierwsze zród³o danych z pliku tekstowego lub tabeli
	 * 
	 * @author Mariusz Dolata
	 *
	 */
	private CompanyRepository firstCompanyRepository;
	/**
	 * Drugie zród³o danych z pliku tekstowego lub tabeli
	 * 
	 * @author Mariusz Dolata
	 *
	 */
	private CompanyRepository secondCompanyRepository;
	/**
	 * <B>int condition;</B><BR>
	 * dla algorytmu nieznormalizowanego = <B>0</b> <BR>
	 * dla algorytmu znormalizowanego = <B>1</b>
	 * 
	 * @author Mariusz Dolata
	 *
	 */
	private int condition = 0;
	/**
	 * String connector przechowuje <BR>
	 * <B>String url</B><BR>
	 * <B>String user</B><BR>
	 * <B>String password</B><BR>
	 * <B>String defaultSchema</B><BR>
	 * 
	 * @author Mariusz Dolata
	 *
	 */
	private StringConnector stringConnector;
	/**
	 * <B>int counter</b> zlicza liczbê porównañ niepustych stringów
	 * 
	 * @author mariusz
	 *
	 */
	private int counter = 0;
	/**
	 * Dwuwymiarowa tablica przechowuje wynik matchowania.
	 * distanceTable[liczba_firm_firstCompanyRepository][liczba_firm_secondCompanyRepository].
	 * Domyœlnie ustawiony jest algorytm "Levenshtein distance"
	 * 
	 * @author mariusz
	 *
	 */
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

	/**
	 * Konstruktor dla zród³a danych podawanych w pliku
	 * 
	 * @param filePath1
	 *            - œcie¿ka do pierwszego pliku
	 * @param filePath2
	 *            - œcie¿ka do drugiego pliku
	 * @author mariusz
	 *
	 */
	public MatchingRepository(String filePath1, String filePath2) throws IOException {
		super();
		this.firstCompanyRepository = new CompanyRepository(filePath1);
		this.secondCompanyRepository = new CompanyRepository(filePath2);
		this.startMatching(firstCompanyRepository, secondCompanyRepository);
		this.printMatchingResults();
	}

	/**
	 * Konstruktor dla zród³a danych w tabeli SQL
	 * 
	 * @param stringConnector
	 *            - przechowuje dane do serwera sql url, user, password,
	 *            default_schema
	 * @param firstCompanyRepository
	 *            - pierwsze zród³o danych
	 * @param firstMatchedColumn
	 *            - nazwa kolumny do matchowania dla pierwszego zród³a
	 * @param secondCompanyRepository
	 *            - drugie zród³o danych
	 * @param secondMatchedColumn
	 *            - nazwa kolumny do matchowania dla drugiego zród³a
	 * @author mariusz
	 *
	 */

	public MatchingRepository(StringConnector stringConnector, EntityRepository firstEntityRepository,
			String firstMatchedColumn, EntityRepository secondEntityRepository, String secondMatchedColumn) {
		this.stringConnector = stringConnector;
		this.firstCompanyRepository = new CompanyRepository(firstEntityRepository, firstMatchedColumn);
		this.secondCompanyRepository = new CompanyRepository(secondEntityRepository, secondMatchedColumn);
		// this.startMatching(firstCompanyRepository, secondCompanyRepository);
		this.createTableMatchingResults();
		this.startMatchingWithSaving(firstCompanyRepository, secondCompanyRepository, 0);
		// this.printMatchingResults();
		// this.saveResultArray();
		// this.saveMatchedResults();
	}

	/**
	 * Metoda porównuj¹ca 2 stringi. <B>Levenshtein levenhstein = new
	 * Levenhstein()</b> - tutaj nastêpuje wybór algorytmu. <BR >
	 * Istnieje mo¿liwoœæ zmiany na inne (zobacz dokumentacjê info.debatty)
	 * 
	 * @param firstCompanyRepository
	 *            - pierwsze zród³o danych do porównania
	 * @param secondCompanyRepository
	 *            - drugie zród³o danych do porównania
	 */
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

	public void startMatchingWithSaving(CompanyRepository firstCompanyRepository,
			CompanyRepository secondCompanyRepository, int saveCondition) {

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
						// if (score < 120)
						// distanceTable[i][j] = (byte) score;
						// else
						// distanceTable[i][j] = 120;
						if (score <= (double) saveCondition) {
							// Zapis do bazy danych
							System.out.println("Znaleziono dobrane wyniki i=" + i + ", j=" + j);
							this.saveOnlyOneRecord(i, j);

						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	/**
	 * Funkcja wyrzucaj¹ca wynik porównania na consolê
	 * 
	 */
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

	/**
	 * Medoda zapisujaca wyniki w formie uproszczonej do tabeli
	 * <B>matching_result</b> <BR>
	 * do kolumn <b>id</b> (INT), PK, AI <b>score</b> (INT),
	 * <b>first_position</b> (INT), <b>first_string</b> (VARCHAR),
	 * <b>second_position</b> (INT), <b>second_string</b> (VARCHAR) <BR>
	 * Metoda niezalecana. Zalecana jest saveMatchedResult() zapisujaca pelne
	 * wyniki
	 */
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

	/**
	 * Metoda zapisujaca pelne wyniki porownania do bazy danych. W wynikach sa
	 * wszystke tabele z kolumny pierwszej i drugiej z prefixami <b>first_</b>
	 * oraz <b>second_</b>
	 * 
	 */
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
					if (this.distanceTable[i][j] == this.condition) {
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
						// INSERT multiple row
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

			// wstawienie reszty
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

	public void createTableMatchingResults() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
			Connection connection = DriverManager.getConnection("jdbc:mysql://" + this.stringConnector.getUrl() + "/"
					+ this.getStringConnector().getDefaultSchema() + "?user=" + this.getStringConnector().getUser()
					+ "&password=" + this.getStringConnector().getPassword() + "&serverTimezone=UTC");

			Statement stmt = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
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
			System.out.println("sqlPart>>>>" + sqlCreateTable + "<<<");
		} catch (Exception e) {
			System.err.println("M: Nie uda³o utworzyc siê tabeli matching_results");
			e.printStackTrace();
		}

	}

	public void saveOnlyOneRecord(int firstPosition, int secondPosition) {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
			Connection connection = DriverManager.getConnection("jdbc:mysql://" + this.stringConnector.getUrl() + "/"
					+ this.getStringConnector().getDefaultSchema() + "?user=" + this.getStringConnector().getUser()
					+ "&password=" + this.getStringConnector().getPassword() + "&serverTimezone=UTC");

			Statement stmt = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

			// firstCompanyRepository.getEntityRepository().getEntities().get(1).getEntity("id");
			// ITERACJA NAZW KOLUMN

			// firstCompanyRepository.getEntityRepository().getTableInformation().getColumns().get(3).getColumnType();

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

			// for (int i = 0; i < firstCompanyRepository.getCompanies().size();
			// i++) {
			// for (int j = 0; j <
			// secondCompanyRepository.getCompanies().size(); j++) {
			// if (this.distanceTable[i][j] == this.condition) {
			insertIteration++;
			sqlColumnsData += "(";
			// kolumny z pierwszej tabeli
			for (int iter1 = 0; iter1 < firstCompanyRepository.getEntityRepository().getTableInformation().getColumns()
					.size(); iter1++) {
				String columnName = firstCompanyRepository.getEntityRepository().getTableInformation().getColumns()
						.get(iter1).getColumnName();
				// wartoœæ konkretnej kolumny
				Object columnValue = firstCompanyRepository.getEntityRepository().getEntityList().get(firstPosition)
						.getEntity(columnName);
				sqlColumnsData += "'" + String.valueOf(columnValue) + "', ";
			}
			// kolumny z drugiej tabeli
			for (int iter1 = 0; iter1 < secondCompanyRepository.getEntityRepository().getTableInformation().getColumns()
					.size(); iter1++) {
				String columnName = secondCompanyRepository.getEntityRepository().getTableInformation().getColumns()
						.get(iter1).getColumnName();

				// wartoœæ konkretnej kolumny
				Object columnValue = secondCompanyRepository.getEntityRepository().getEntityList().get(secondPosition)
						.getEntity(columnName);
				sqlColumnsData += "'" + String.valueOf(columnValue) + "', ";
			}
			sqlColumnsData = sqlColumnsData.substring(0, sqlColumnsData.length() - 2);
			sqlColumnsData += "), ";
			// INSERT multiple row
			// if (insertIteration % 100 == 0) {
			// sqlColumnsData = sqlColumnsData.substring(0,
			// sqlColumnsData.length() - 2);
			// System.out.println("SQLCOLUMNDATA>>>" + sqlColumnsData);
			// String sqlInsertEntity = sqlColumnsName + sqlColumnsData;
			// System.out.println("INSERT>>>>" + sqlInsertEntity);
			// stmt.executeUpdate(sqlInsertEntity);
			// sqlColumnsData = "";
			// }
			// }
			// }
			// }

			// wstawienie reszty
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
