package encore.matching;

import java.io.IOException;

import encore.database.importdata.EntityRepository;
import info.debatty.java.stringsimilarity.Levenshtein;

public class MatchingRepository {
	private CompanyRepository firstCompanyRepository;
	private CompanyRepository secondCompanyRepository;
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

	public MatchingRepository(String filePath1, String filePath2) throws IOException {
		super();
		this.firstCompanyRepository = new CompanyRepository(filePath1);
		this.secondCompanyRepository = new CompanyRepository(filePath2);
		this.startMatching(firstCompanyRepository, secondCompanyRepository);
		this.printMatchingResults();
	}
	public MatchingRepository(EntityRepository firstEntityRepository, String firstfirstMatchedColumn, EntityRepository secondEntityRepository, String secondMatchedColumn){
		this.firstCompanyRepository=new CompanyRepository(firstEntityRepository, firstfirstMatchedColumn);
		this.secondCompanyRepository=new CompanyRepository(secondEntityRepository, secondMatchedColumn);
		this.startMatching(firstCompanyRepository, secondCompanyRepository);
		this.printMatchingResults();
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
					// System.out.println("Wejscie w if");
					counter++;
					try {
						// distanceTable[i][j] = lew.levenshteinDistance(first,
						// second);
						double score = levenshtein.distance(
								firstCompanyRepository.getCompanies().get(i).getTidyCompanyName(),
								secondCompanyRepository.getCompanies().get(j).getTidyCompanyName());
						if (score < 120)
							distanceTable[i][j] = (byte) score;
						else
							distanceTable[i][j] = 120;
						// System.out.println("Wejscie w try");
					} catch (Exception e) {
						// System.err.println("M: ubable to compute
						// levensteinDistance");
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

}
