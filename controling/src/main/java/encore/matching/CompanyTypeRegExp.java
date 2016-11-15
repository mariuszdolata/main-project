package encore.matching;

public class CompanyTypeRegExp {
	private String regexp;
	private String companyType;

	public CompanyTypeRegExp(String companyType, String regexp) {
		this.companyType = companyType;
		this.regexp = regexp;
	}

	@Override
	public String toString() {
		return "CompanyTypeRegExp [regexp=" + regexp + ", companyType=" + companyType + "]";
	}

	public String getRegexp() {
		return regexp;
	}

	public void setRegexp(String regexp) {
		this.regexp = regexp;
	}

	public String getCompanyType() {
		return companyType;
	}

	public void setCompanyType(String companyType) {
		this.companyType = companyType;
	}

}
