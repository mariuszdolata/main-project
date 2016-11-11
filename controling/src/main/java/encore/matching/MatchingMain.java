package encore.matching;

import java.io.IOException;

public class MatchingMain {

	public static void main(String[] args) throws IOException {
		System.out.println("MatchingMain START!");
		String filePath1="F:\\p1.txt";
		String filePath2="F:\\p2.txt";
		long start=System.currentTimeMillis();
		MatchingRepository matchingRepository = new MatchingRepository(filePath1, filePath2);
		long stop = System.currentTimeMillis();
		
		System.out.println("czas trwania = "+(stop-start)+",  liczba wykonanych operacji="+matchingRepository.getCounter());
		


	}

}