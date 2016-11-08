package encore.json;

public class Stats {
	private int lead;
	private int unsubscribe;
	private int negative;
	private int total;
	public Stats() {
		super();
		// TODO Auto-generated constructor stub
	}
	public int getLead() {
		return lead;
	}
	public void incrementLead() {
		lead++;
	}
	public void setLead(int lead) {
		this.lead = lead;
	}
	public int getUnsubscribe() {
		return unsubscribe;
	}
	public void incrementUnsubscribe(){
		this.unsubscribe++;
	}
	public void setUnsubscribe(int unsubscribe) {
		this.unsubscribe = unsubscribe;
	}
	public int getNegative() {
		return negative;
	}
	public void incrementNegative(){
		this.negative++;
	}
	public void setNegative(int negative) {
		this.negative = negative;
	}
	public int getTotal() {
		return total;
	}
	public void incrementTotal(){
		this.total++;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	public Stats(int lead, int unsubscribe, int negative, int total) {
		super();
		this.lead = lead;
		this.unsubscribe = unsubscribe;
		this.negative = negative;
		this.total = total;
	}
	@Override
	public String toString() {
		return "Stats [lead=" + lead + ", unsubscribe=" + unsubscribe + ", negative=" + negative + ", total=" + total
				+ "]";
	}
}
