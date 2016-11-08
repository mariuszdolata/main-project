package encore.json;

public class Tic {
	private String groupLabel;
	private String custom1;
	private String custom2;
	private String custom4;
	public Tic() {
		
	}
	@Override
	public String toString() {
		return "Tic [groupLabel=" + groupLabel + ", custom1=" + custom1 + ", custom2=" + custom2 + ", custom4="
				+ custom4 + "]";
	}
	public String getGroupLabel() {
		return groupLabel;
	}
	public void setGroupLabel(String groupLabel) {
		this.groupLabel = groupLabel;
	}
	public String getCustom1() {
		return custom1;
	}
	public void setCustom1(String custom1) {
		this.custom1 = custom1;
	}
	public String getCustom2() {
		return custom2;
	}
	public void setCustom2(String custom2) {
		this.custom2 = custom2;
	}
	public String getCustom4() {
		return custom4;
	}
	public void setCustom4(String custom4) {
		this.custom4 = custom4;
	}

}
