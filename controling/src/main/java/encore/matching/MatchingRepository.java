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

		// inicjalizacja tabeli wyników uzale¿niona od liczby stringów w
		// repozytoriach
		distanceTable = new byte[firstCompanyRepository.getCompanies().size()][secondCompanyRepository.getCompanies()
				.size()];
		System.out.println("Stworzono tablicê 2d");
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
					+ "`.`matching_results` (" + "`id` INT NOT NULL AUTO_INCREMENT," + " `score` INT NULL,"
					+ " `first_position` INT NULL," + " `second_position` INT NULL," + "PRIMARY KEY (`id`));";
			
			String create="CREATE TABLE `"+this.getStringConnector().getDefaultSchema()+"`.`matching_results` (`id` INT NOT NULL AUTO_INCREMENT, `score` INT NULL,  `first_position` INT NULL,  `first_string` VARCHAR(500) NULL,  `second_position` INT NULL,  `second_string` VARCHAR(500) NULL,  PRIMARY KEY (`id`),  INDEX `index1` (`first_string` ASC),  INDEX `index2` (`second_string` ASC));";
			// usuniecie tabeli
			stmt.executeUpdate(dropResiltTableSql);
			// stworzenie nowej
			stmt.executeUpdate(create);

			for (int i = 0; i < firstCompanyRepository.getCompanies().size(); i++) {
				for (int j = 0; j < secondCompanyRepository.getCompanies().size(); j++) {
					if (this.distanceTable[i][j] == 0) {
						String first=firstCompanyRepository.getCompanies().get(i).getCompanyName();
						String second=secondCompanyRepository.getCompanies().get(j).getCompanyName();
						String insertSql = "INSERT INTO matching_results (score, first_position, first_string, second_position, second_string) VALUES ("
								+ (int) this.distanceTable[i][j] + "," + i + ",'"+first+"',"+j+", '"+second+"');";
						System.out.println(">>>"+insertSql+"<<<");
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
}
