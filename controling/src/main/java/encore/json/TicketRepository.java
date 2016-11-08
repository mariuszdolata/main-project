package encore.json;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

//import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

public class TicketRepository {
	private List<Ticket> tickets=new ArrayList<Ticket>();
	private Set<String> groups=new HashSet<String>();
	private Map<String, Stats> map=new TreeMap<String,Stats>();
//	private List<Campaign> campaigns=new ArrayList<Campaign>();
	private int numberOfTickets;
	private int numberOfLeads;
	private int nonNumberOfLeads;
	private int numberOfUnsubscribes;
	private int nonNumberOfUnsubscribes;
	private int numberOfNegative;
	private int nonNumberOfNegative;
	

	////PROBA SORTOWANIA
	
	
	
	//KONIEC PROBY SORTOWANIA
	public List<Ticket> getTickets() {
		return tickets;
	}

	public void setTickets(List<Ticket> tickets) {
		this.tickets = tickets;
	}

	public int getNumberOfTickets() {
		return numberOfTickets;
	}

	public void setNumberOfTickets(int numberOfTickets) {
		this.numberOfTickets = numberOfTickets;
	}

	public int getNumberOfLeads() {
		return numberOfLeads;
	}

	public void setNumberOfLeads(int numberOfLeads) {
		this.numberOfLeads = numberOfLeads;
	}

	public int getNumberOfUnsubscribes() {
		return numberOfUnsubscribes;
	}

	public void setNumberOfUnsubscribes(int numberOfUnsubscribes) {
		this.numberOfUnsubscribes = numberOfUnsubscribes;
	}

	public int getNumberOfNegative() {
		return numberOfNegative;
	}

	public void setNumberOfNegative(int numberOfNegative) {
		this.numberOfNegative = numberOfNegative;
	}

	public TicketRepository(){
	
	}
		
	public void readFromFile(){
		try {
			JsonReader jsonReader = new JsonReader(new FileReader("F:\\raport8.11.json"));
			System.out.println("Reading from json file complited.");
			jsonReader.beginObject();
			//readFromShortFile(jsonReader);
			readFromFullFile(jsonReader);

		} catch (Exception e) {
			System.err.println("Unable to find or read from json file - blad zewnetrzny"+e.toString());
		}
	}
	
	private void readFromShortFile(JsonReader jsonReader) throws IOException {
		while(jsonReader.hasNext()){
			String name=jsonReader.nextName();
			System.out.println("name before=>"+name);
			if(name.equals("results")){ //"fields" or "results"
				System.out.println("name in=>"+name);
				try{
					System.out.println("name in in=>"+name);
					readApp(jsonReader);
					System.out.println("name in out=>"+name);
				}catch(IOException e){ System.err.println("blad wewnetrzny"+e.toString());}
			}
			System.out.println("name after=>"+name);
		
		}
		
	}
	private void readFromFullFile(JsonReader jsonReader) throws IOException {
		System.out.println("Wejscie do pelnego pliku");
		while(jsonReader.hasNext()){ //odczytywanie kolejnych linii pliku
			String name=jsonReader.nextName();
			String value1, value2, value3, value4;
			System.out.println("name beforeFull: "+name);
			if(name.equals("fields")){
				System.out.println("znaleziony obiekt fields");
				jsonReader.beginObject();
				//name=jsonReader.nextName();
			//	System.out.println("Name  kolejnego obiektu1 "+name);
//				jsonReader.beginObject();
//				name=jsonReader.nextName();
//				System.out.println("Name  kolejnego obiektu2 "+name);
//				value1=jsonReader.nextString();
//				name=jsonReader.nextName();
//				value2=jsonReader.nextString();
//				System.out.println("Name  kolejnego obiektu2 "+name + "; wartosc1: "+value1+" wartosc2: "+value2);
//				jsonReader.beginObject();
//				name=jsonReader.nextName();
				
				//Wczytanie nag³ówkow
				while(jsonReader.hasNext()){
					System.out.println("iteracje");
					name=jsonReader.nextName();
					System.out.println("Name  kolejnego obiektu1 "+name);
					jsonReader.beginObject();
					value3=jsonReader.nextName();
					System.out.println("Name  kolejnego obiektu2 "+value3);
					value1=jsonReader.nextString();
					value4=jsonReader.nextName();
					value2=jsonReader.nextString();
					System.out.println("Name  kolejnego obiektu2 "+value4 + "; wartosc1: "+value1+" wartosc2: "+value2);
					jsonReader.endObject();
				}
				jsonReader.endObject();
				//Wczytanie danych
				name=jsonReader.nextName();
				System.out.println("Kolejne name= "+name);
				if(name.equals("results")){
					System.err.println("Wracamy do gry");
					jsonReader.beginArray();
					while(jsonReader.hasNext()){
						jsonReader.beginObject();
						name=jsonReader.nextName();
						Ticket ticket=new Ticket();
						if(name.contains("group__label")){
							ticket.setGroupLabel(jsonReader.nextString());	
							groups.add(ticket.getGroupLabel());
						}
						name=jsonReader.nextName();
						if(name.contains("custom_1")){
							ticket.setCustom1(jsonReader.nextString());
						}
						name=jsonReader.nextName();
						if(name.contains("custom_2")){
							ticket.setCustom2(jsonReader.nextString());
						}
						name=jsonReader.nextName();
						if(name.contains("custom_4")){
							ticket.setCustom4(jsonReader.nextString());
						}
						tickets.add(ticket);
						//System.out.println(ticket.toString());

						jsonReader.endObject();
					}
					jsonReader.endArray();
				}
				
			}
			//jsonReader.skipValue();
		/*	if(name.equals("results")){
				System.out.println("znaleziony tablice results#########################");
				jsonReader.endObject();
				jsonReader.beginArray();
				
				jsonReader.endArray();
			}*/
			/*jsonReader.skipValue();
			Gson gson=new Gson();
			Response response=gson.fromJson(jsonReader, Response.class);
			System.out.println("Zakoñczenie operacji");*/
			System.out.println("Kolejna iteracja petli while");
		}
	}

	private void readApp(JsonReader jsonReader)  throws IOException{

		System.out.println("readApp - wejscie");
		jsonReader.beginArray();
		while(jsonReader.hasNext()){
			jsonReader.beginObject();
			String name=jsonReader.nextName();
			Ticket ticket=new Ticket();
			if(name.contains("group__label")){
				ticket.setGroupLabel(jsonReader.nextString());	
				groups.add(ticket.getGroupLabel());
			}
			name=jsonReader.nextName();
			if(name.contains("custom_1")){
				ticket.setCustom1(jsonReader.nextString());
			}
			name=jsonReader.nextName();
			if(name.contains("custom_2")){
				ticket.setCustom2(jsonReader.nextString());
			}
			name=jsonReader.nextName();
			if(name.contains("custom_4")){
				ticket.setCustom4(jsonReader.nextString());
			}
			tickets.add(ticket);
			System.out.println(ticket.toString());

			jsonReader.endObject();
		}
		jsonReader.endArray();

		
	}
	public void showAllStats(){
		this.setNumberOfTickets(this.tickets.size());
		
		for(int i=0;i<tickets.size();i++){
			if(tickets.get(i).getCustom1().equals("1")) this.numberOfLeads++;	
			else this.nonNumberOfLeads++;			
			if(tickets.get(i).getCustom2().equals("1"))this.numberOfUnsubscribes++;
			else this.nonNumberOfUnsubscribes++;
			if(tickets.get(i).getCustom4().equals("1"))this.numberOfNegative++;
			else this.nonNumberOfNegative++;
		}
		if((this.nonNumberOfLeads+this.nonNumberOfLeads)==tickets.size()){
			System.out.println("Tickety z leadami + tickety bez leada == liczba ticketow");
		}
		else System.err.println("Suma ticketów z leadami oraz bez leada != liczby ticketow");
		System.out.println("\nLiczba ticketów: "+Integer.toString(this.numberOfTickets)+
				"\nLiczba leadów ogólnie: "+Integer.toString(this.numberOfLeads)+";"+Integer.toString(this.nonNumberOfLeads)+
				"\nLiczba unsubscribów ogólnie: "+Integer.toString(this.numberOfUnsubscribes)+";"+Integer.toString(this.nonNumberOfUnsubscribes)+
				"\nLiczba negatywów ogólnie: " + Integer.toString(this.numberOfNegative)+";"+ Integer.toString(this.nonNumberOfNegative));
	}
	public void printAllGroups(){
		Iterator<String> iterator=this.groups.iterator();
		while(iterator.hasNext()){
			System.out.println(iterator.next());
		}
	}
	public void createStats(){
		Iterator<String> iterator=this.groups.iterator();
		//Stats stats=new Stats(0,0,0);
		//Stworzenie mapy ze wszystkimi kampaniami
		while(iterator.hasNext()){
			String key=iterator.next();
			map.put(key, new Stats(0,0,0,0));
		}
		//
		for(int i=0;i<tickets.size();i++){
			String key=tickets.get(i).getGroupLabel();
			//System.out.println("key: "+key);
			Stats statsTmp=map.get(key);
			statsTmp.incrementTotal();
			if(tickets.get(i).getCustom1().equals("1")){
				statsTmp.incrementLead();				
			}
					
			if(tickets.get(i).getCustom2().equals("1")){
				statsTmp.incrementUnsubscribe();
			}
			
			if(tickets.get(i).getCustom4().equals("1")){
				statsTmp.incrementNegative();
			}
			
			map.put(key, statsTmp);
			//Stworzenie listy podsumowuj¹cej kampaniê
//			campaigns.add(new Campaign(key, statsTmp.getTotal(),statsTmp.getLead(), statsTmp.getUnsubscribe(), statsTmp.getNegative()));
		}
		System.out.println("Zakoñczono tworzenie pogrupowanej mapy");
	}
	public void showAllGroupedStats(){
		//POBRANIE KLUCZY
		Iterator<String> iterator=this.groups.iterator();
		while(iterator.hasNext()){
			String key=iterator.next();
			String key2=String.format("-%1$"+40+ "s", key);
			Stats statsTmp=map.get(key);
			System.out.println(key2+"::   total: "+Integer.toString(statsTmp.getTotal())+"   leads: "+Integer.toString(statsTmp.getLead())+"   unsubscribe: "+Integer.toString(statsTmp.getUnsubscribe())+"   negative: "+Integer.toString(statsTmp.getNegative()));
		}
	}
//	public void sortByTickets(){
//		Collections.sort(campaigns);
//		System.out.println("Koniec sortowania");
//		for(int i=0;i<campaigns.size();i++){
//			System.out.println("Sortowanie :"+campaigns.get(i).getKey()+" tickets: "+campaigns.get(i).getNumberOfTickets());
//		}
//	}
}
