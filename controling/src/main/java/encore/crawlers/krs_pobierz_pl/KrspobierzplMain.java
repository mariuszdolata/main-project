package encore.crawlers.krs_pobierz_pl;


import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;



public class KrspobierzplMain {

	public static void main(String[] args) {
System.out.println("krs-pobierz.pl START");
		
		
		try {
			Document doc = Jsoup.connect("http://krs-pobierz.pl/wojewodztwo/dolnoslaskie/1").userAgent("Mozilla").data("name", "jsoup").get();
			Elements rows=doc.select("a[href]");
			Elements links=doc.select("h3[style=\"margin: 10px 0 5px 0\"]");

	
			for(int i=0;i<links.size();i++){
				System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>");
				String companyName=links.get(i).text();
				String companyUrl=links.get(i).attr("a");
				System.out.println("companyName="+companyName+", companyUrl="+companyUrl+", ");
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		System.out.println("krs-pobierz.pl KONIEC");

	}

}
