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
		this.saveResultArray();
	}

	public void startMatching(CompanyRepository firstCompanyRepository, CompanyRepository secondCompanyRepository) {

		// inicjalizacja tabeli wynik�w uzale�niona od liczby string�w w
		// repozytoriach
		distanceTable = new byte[firstCompanyRepository.getCompanies().size()][secondCompanyRepository.getCompanies()
				.size()];
		System.out.println("Stworzono tablic� 2d");
		Levenshtein levenshtein = new Levenshtein();

		for (int i = 0; i < firstCompanyRepository.getCompanies().size(); i++) {
			for (int j = 0; j < secondCompanyRepository.getCompanies().size(); j++) {
				CharSequence first = firstCompanyRepository.getCompanies().get(i).getTidyCompanyName();
				CharSequence second = secondCompanyRepository.getCompanies().get(j).getTidyCompanyName();
				// System.out.println("zamieniono warto�ci " + first + "|| " +
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
			System.err.println("M: nie uda�o si� zapisa� wynikow matchowania MatchingRepository->saveResultArray() ");
			e.printStackTrace();
		}

	}

	public void saveMatchedResults(){
		try {
			Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
			Connection connection = DriverManager.getConnection("jdbc:mysql://" + this.stringConnector.getUrl() + "/"
					+ this.getStringConnector().getDefaultSchema() + "?user=" + this.getStringConnector().getUser()
					+ "&password=" + this.getStringConnector().getPassword() + "&serverTimezone=UTC");

			Statement stmt = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
			
//			firstCompanyRepository.getEntityRepository().getEntities().get(1).getEntity("id");
			//ITERACJA NAZW KOLUMN
			String dropResiltTableSql = "DROP TABLE IF EXISTS `" + this.getStringConnector().getDefaultSchema()
					+ "`.`matching_results`;";
			String sqlPart="CREATE TABLE `"+this.getStringConnector().getDefaultSchema()+"`.`matching_results` (";
			for(int i=0;i<firstCompanyRepository.getEntityRepository().getTableInformation().getColumns().size();i++){

				sqlPart+="`first_"+firstCompanyRepository.getEntityRepository().getTableInformation().getColumns().get(i).getColumnName()+"` "+firstCompanyRepository.getEntityRepository().getTableInformation().getColumns().get(i).getColumnType()+", ";
				
			}
			for(int i=0;i<secondCompanyRepository.getEntityRepository().getTableInformation().getColumns().size();i++){

				sqlPart+="`second_"+secondCompanyRepository.getEntityRepository().getTableInformation().getColumns().get(i).getColumnName()+"` "+secondCompanyRepository.getEntityRepository().getTableInformation().getColumns().get(i).getColumnType()+", ";
				
			}
			sqlPart=sqlPart.substring(0, sqlPart.length()-2);
			sqlPart+=");";
			stmt.executeUpdate(dropResiltTableSql);
			stmt.executeUpdate(sqlPart);
//			firstCompanyRepository.getEntityRepository().getTableInformation().getColumns().get(3).getColumnType();
			System.out.println("sqlPart>>>>"+sqlPart+"<<<");

			for (int i = 0; i < firstCompanyRepository.getCompanies().size(); i++) {
				for (int j = 0; j < secondCompanyRepository.getCompanies().size(); j++) {
					if (this.distanceTable[i][j] == 0) {
						String sqlColumnsName="INSERT INTO `"+this.getStringConnector().getDefaultSchema()+"`.`matching_results` (";
						String sqlColumnsData=" VALUES (";
						for(int iter1=0;iter1<firstCompanyRepository.getEntityRepository().getTableInformation().getColumns().size();iter1++){
							sqlColumnsName+=firstCompanyRepository.getEntityRepository().getTableInformation().getColumns().get(iter1).getColumnName()+", ";
							String columnType=firstCompanyRepository.getEntityRepository().getTableInformation().getColumns().get(iter1).getColumnType();
							
							//WSTAW warto�ci z repozytorium do string�w
//							if(columnType.contains("int")) 
//							else if( columnType.contains("varchar"))
//							else sqlColumnsData+="\" \"";
//							}
						}
//						String insertSql = "INSERT INTO matching_results (score, first_position, first_string, second_position, second_string) VALUES ("
//								+ (int) this.distanceTable[i][j] + "," + i + ",'" + first + "'," + j + ", '" + second
//								+ "');";
//						System.out.println(">>>" + insertSql + "<<<");
//						stmt.executeUpdate(insertSql);
					}
				}
			}
			
			connection.close();

		} catch (Exception e) {
			System.err.println("M: nie uda�o si� zapisa� wynikow matchowania MatchingRepository->saveResultArray() ");
			e.printStackTrace();
		}
		
	}

}
