package encore.database.importdata;

import java.sql.Date;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Entity;

@Entity
public class Tabela1 {
	@Id
	@GeneratedValue
	private int id;
	private String tabela1str;
	private Date tabela1data;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTabela1str() {
		return tabela1str;
	}
	public void setTabela1str(String tabela1str) {
		this.tabela1str = tabela1str;
	}
	public Date getTabela1data() {
		return tabela1data;
	}
	public void setTabela1data(Date tabela1data) {
		this.tabela1data = tabela1data;
	}
	public Tabela1(int id, String tabela1str, Date tabela1data) {
		super();
		this.id = id;
		this.tabela1str = tabela1str;
		this.tabela1data = tabela1data;
	}
	public Tabela1() {
		super();
	}
	
	

}
