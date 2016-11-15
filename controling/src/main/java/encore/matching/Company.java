package encore.matching;

/**
 * Klasa bedaca encja pojedynczej firmy. <BR>
 * zawiera tylko podstawowe dane sluzace do matchowania w tym String
 * <b>comanyName</b> oraz <b>tidyCompanyName</b> czyli wyczyszczona wersje
 * companyName. <BR>
 * do matchowania wykorzystywane jest pole <b>tidyCompanyName</b>
 * 
 * @author Mariusz Dolata
 *
 */
public class Company {
	// primary key
	private long id;
	private String companyName;
	private String tidyCompanyName = null;
	// znaleziona wartoœæ na podstawie której zosta³ wybrany rodzaj spó³ki
	private String companyTypePatern;
	// zamkniêta lista rodzajów. Uwzglêdnij inne kraje w przysz³oœci
	private PolishCompanyType companyType;
	// firma ma przypisany typ. Domyślnie false
	private boolean used;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getTidyCompanyName() {
		return tidyCompanyName;
	}

	public void setTidyCompanyName(String tidyCompanyName) {
		this.tidyCompanyName = tidyCompanyName;
	}

	public String getCompanyTypePatern() {
		return companyTypePatern;
	}

	public void setCompanyTypePatern(String companyTypePatern) {
		this.companyTypePatern = companyTypePatern;
	}

	public PolishCompanyType getCompanyType() {
		return companyType;
	}

	public void setCompanyType(PolishCompanyType companyType) {
		this.companyType = companyType;
	}

	public Company(String companyName) {
		this.companyName = companyName;
		this.used = false;
	}

	@Override
	public String toString() {
		return "Company [id=" + id + ", companyName=" + companyName + ", companyTypePatern=" + companyTypePatern
				+ ", companyType=" + companyType + "]";
	}

}