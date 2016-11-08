package encore.matching;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CompanyRepository {
	private String filePath;
	private List<Company> companies=new ArrayList<Company>();
	
	
	
	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public List<Company> getCompanies() {
		return companies;
	}

	public void setCompanies(List<Company> companies) {
		this.companies = companies;
	}
	

	public CompanyRepository(String filePath) throws IOException {
		this.filePath = filePath;
		this.readCompaniesFromTextFile(this.filePath);
	}

	public void readCompaniesFromTextFile(String filePath) throws IOException{
		try{
			BufferedReader br=new BufferedReader(new FileReader(filePath));
			String currentLine=null;
			while((currentLine=br.readLine())!=null){
				companies.add(new Company(currentLine));
			}
		}catch(IOException e){
			System.err.println("M: Unable to read file from readCompaniesFromTextFile method in CompanyRepository class");
			e.printStackTrace();
		}
	}
	
	public void printAllCompanies(){
		for(Company company:companies){
			System.out.println(company.toString());
		}
	}

}
