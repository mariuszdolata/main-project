package encore.matching;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import encore.database.importdata.Entity;
import encore.database.importdata.EntityRepository;

public class CompanyRepository {
	private String filePath;
	private String matchedColumn;
	private List<Company> companies = new ArrayList<Company>();
	private List<CompanyTypeRegExp> companyTypePattern = new ArrayList<CompanyTypeRegExp>();

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
		this.addRegExpPattern();
		this.findCompanyType();
	}

	public CompanyRepository(EntityRepository entityRepository, String matchedColumn) {
		this.matchedColumn = matchedColumn;
		this.readCompaniesFromDatabase(entityRepository, matchedColumn);
		this.addRegExpPattern();
		this.findCompanyType();
	}

	public void readCompaniesFromTextFile(String filePath) throws IOException {
		try {
			BufferedReader br = new BufferedReader(new FileReader(filePath));
			String currentLine = null;
			while ((currentLine = br.readLine()) != null) {
				companies.add(new Company(currentLine));
			}
		} catch (IOException e) {
			System.err
					.println("M: Unable to read file from readCompaniesFromTextFile method in CompanyRepository class");
			e.printStackTrace();
		}
	}

	public void readCompaniesFromDatabase(EntityRepository entityRepository, String matchedColumn) {
		for (Entity entity : entityRepository.getEntityList()) {
			companies.add(new Company((String) entity.getEntity(matchedColumn)));
		}
	}

	public void printAllCompanies() {
		for (Company company : companies) {
			System.out.println(company.toString());
		}
	}

	public void addRegExpPattern() {
		String spolkaPattern = "(\\W)+([s]{1}[.]?[ ]?[p]?[.]?[ ]?|[s]{1}[p]{1}[.]?[ ]?|spó³ka )";
		String letter = "(?![\\p{L}])";

		String spzoo = "[ ]*([z][ ]?[o][.]?[ ]?[o][.]?|z ograniczon¹ odpowiedzialnoœci¹)";
		String spj = "[ ]*([j][.]?[ ]?|jawna)";
		String spp = "[ ]*([p]{2}[.]?[ ]?|partnerska)";
		String spk = "[ ]*([k]{1}[.]?[ ]?|komandytowa)";
		String spa = "([a]{1}[.]?[ ]?|akcyjna)";

		this.companyTypePattern.add(new CompanyTypeRegExp("spzoo", letter + spolkaPattern + spzoo + letter));
		this.companyTypePattern.add(new CompanyTypeRegExp("spj", letter + spolkaPattern + spj + letter));
		this.companyTypePattern.add(new CompanyTypeRegExp("spp", letter + spolkaPattern + spp + letter));
		this.companyTypePattern.add(new CompanyTypeRegExp("spk", letter + spolkaPattern + spk + letter));
		this.companyTypePattern.add(new CompanyTypeRegExp("spa", letter + spolkaPattern + spa + letter));

	}

	public void findCompanyType() {

		int i = 1;
		for (Company company : companies) {
			String companyName = company.getCompanyName();
			// String patternString = proba;
			// String patternString ="[s]{1}[.]?[ ]?[a]{1}[.]?[ ]?";
			for (CompanyTypeRegExp reg : companyTypePattern) {
				Pattern pattern = Pattern.compile(reg.getRegexp(), Pattern.CASE_INSENSITIVE);
				Matcher matcher = pattern.matcher(companyName);
				// boolean matcherFound=false;
				while (matcher.find()) { // zmiana while na IF (jeœli znaleziono
											// rodzaj spó³ki
					// System.out.println(Integer.toString(i++) + "=>>>>>" +
					// matcher.group() + "<<<<<"+reg.getCompanyType()+"=>" +
					// companyName);
					company.setTidyCompanyName(
							(String) companyName.trim().toUpperCase().subSequence(0, matcher.start()));
					// System.out.println("companyName="+company.getTidyCompanyName()+"<");
					// matcherFound=true;
				}
				if (company.getTidyCompanyName() == null) { // jeœli nie
															// znaleziono spó³ki
					company.setTidyCompanyName((String) companyName.toUpperCase());
				}
				// trim dla wszystkich nazw firm - elminuje spacjê na koñcu
				// wiele spacji=> jedna spacja
				// kropka na spacje
				company.setTidyCompanyName(
						company.getTidyCompanyName().trim().replaceAll("( )+", " ").replaceAll("[.]", " "));
			}
		}

	}

}