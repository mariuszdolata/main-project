//package encore.json;
//
//import encore.json.TicketRepository.Campaign;
//
//public class Campaign implements Comparable<Campaign>{
//	private String key;
//	private int numberOfTickets;
//	private int numberOfLeads;
//	private int numberOfUnsubscribes;
//	private int numberOfNegatives;
//	public Campaign(String key, int numberOfTickets, int numberOfLeads, int numberOfUnsubscribes,
//			int numberOfNegatives) {
//		super();
//		this.key = key;
//		this.numberOfTickets = numberOfTickets;
//		this.numberOfLeads = numberOfLeads;
//		this.numberOfUnsubscribes = numberOfUnsubscribes;
//		this.numberOfNegatives = numberOfNegatives;
//	}
//	@Override
////	public int compareTo(Campaign another){
////		if(this.getNumberOfLeads()<another.getNumberOfLeads()) return -1;
////		else if (this.getNumberOfLeads()>=another.getNumberOfLeads())return 1;
////		else return 0;
////	}
//	public String getKey() {
//		return key;
//	}
//	public void setKey(String key) {
//		this.key = key;
//	}
//	public int getNumberOfTickets() {
//		return numberOfTickets;
//	}
//	public void setNumberOfTickets(int numberOfTickets) {
//		this.numberOfTickets = numberOfTickets;
//	}
//	public int getNumberOfLeads() {
//		return numberOfLeads;
//	}
//	public void setNumberOfLeads(int numberOfLeads) {
//		this.numberOfLeads = numberOfLeads;
//	}
//	public int getNumberOfUnsubscribes() {
//		return numberOfUnsubscribes;
//	}
//	public void setNumberOfUnsubscribes(int numberOfUnsubscribes) {
//		this.numberOfUnsubscribes = numberOfUnsubscribes;
//	}
//	public int getNumberOfNegatives() {
//		return numberOfNegatives;
//	}
//	public void setNumberOfNegatives(int numberOfNegatives) {
//		this.numberOfNegatives = numberOfNegatives;
//	}
//	@Override
//	public String toString() {
//		return "Campaign [key=" + key + ", numberOfTickets=" + numberOfTickets + ", numberOfLeads=" + numberOfLeads
//				+ ", numberOfUnsubscribes=" + numberOfUnsubscribes + ", numberOfNegatives=" + numberOfNegatives
//				+ "]";
//	}
//	
//	
//}
