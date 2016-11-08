package encore.matching;

/* Klasa powinna zosta� encj� i zapisywa� rezyltaty do bazy danych tak aby �atwo mo�na by�o ��czy� informacj� z wielu portali.
Koniecznie dostosuj model do wielu kraj�w
Koniecznie uwzgl�dnij matchowanie za pomoc� wielu kryteri�w
Zbadaj poprawno�� nadawania PK - KRYTYCZNE dla dzia�ania mechanizmu matchowania
*/
public class Company {
	//primary key
	private long id;
	private String companyName;
	//znaleziona warto�� na podstawie kt�rej zosta� wybrany rodzaj sp�ki
	private String companyTypePatern;
	//zamkni�ta lista rodzaj�w. Uwzgl�dnij inne kraje w przysz�o�ci
	private PolishCompanyType companyType;
	
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
	}
	@Override
	public String toString() {
		return "Company [id=" + id + ", companyName=" + companyName + ", companyTypePatern=" + companyTypePatern
				+ ", companyType=" + companyType + "]";
	}
	
	
	
	
	
	
	
	

}
