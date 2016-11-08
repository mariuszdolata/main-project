package encore.matching;

import java.io.IOException;

public class MatchingMain {

	public static void main(String[] args) throws IOException {
		System.out.println("MatchingMain START!");
		String filePath1="F:\\firmy1.txt";
		CompanyRepository companyRepository=new CompanyRepository(filePath1);
		companyRepository.printAllCompanies();

	}

}
