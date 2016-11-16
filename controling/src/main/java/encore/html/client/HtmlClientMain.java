package encore.html.client;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.TimeUnit;
public class HtmlClientMain {

	public static void main(String[] args) {
		for(int iteracja=0;iteracja<1000;iteracja++){
			try {
				URL url = new URL("http://www.moje-ip.eu");
				  URLConnection spoof = url.openConnection();
	
	
				  spoof.setRequestProperty( "User-Agent", "Mozilla/4.0 (compatible; MSIE 5.5; Windows NT 5.0; H010818)" );
			
				  BufferedReader in = new BufferedReader(new InputStreamReader(spoof.getInputStream()));
				  String strLine = "";
				  int i=0;
	
				  while ((strLine = in.readLine()) != null){
					  i++;
	
				   if(i==266)System.out.println(iteracja+"=>"+strLine);
				  }
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				TimeUnit.SECONDS.sleep(10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	
		}
	}

}
