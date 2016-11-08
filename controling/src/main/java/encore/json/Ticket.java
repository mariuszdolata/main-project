package encore.json;

/*import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;*/

//@Entity
public class Ticket {

	//@Id
	//@GeneratedValue
	private long id;
	private static long idClass=0;
	private String groupLabel;
	private String custom1;
	private String custom2;
	private String custom4;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
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
	@Override
	public String toString() {
		return "Ticket [id=" + id + ", groupLabel=" + groupLabel + ", custom1=" + custom1 + ", custom2=" + custom2
				+ ", custom4=" + custom4 + "]";
	}
	public Ticket() {
		this.id=this.idClass;
		this.idClass++;
	}
	
}
