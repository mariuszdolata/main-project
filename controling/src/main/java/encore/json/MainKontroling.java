package encore.json;
import encore.json.*;
import com.google.gson.Gson;

public class MainKontroling {

	public static void main(String[] args) {
		System.out.println("MainJson class");
		TicketRepository ticketRespository=new TicketRepository();
		ticketRespository.readFromFile();
		ticketRespository.showAllStats();
		ticketRespository.printAllGroups();
		ticketRespository.createStats();
		ticketRespository.showAllGroupedStats();
//		ticketRespository.sortByTickets();

	}

}
