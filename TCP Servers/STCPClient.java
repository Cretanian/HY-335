/*
 * @Stamatakis Stylianos
 * @AM4041
 */
import java.io.*;
import java.util.*;

public class STCPClient { 

	 public static void main(String argv[]) throws Exception 
	 { 
		String sentence;
		String tmp_input = "";
		 
		String inputString;
		String port;
		Scanner scanner;

		BufferedReader inFromUser = 
			new BufferedReader(new InputStreamReader(System.in)); 

		System.out.println("Enter IP address:");
		scanner = new Scanner(System. in);
		inputString = scanner.nextLine();
		
		System.out.println("Enter port:");
		scanner = new Scanner(System. in);
		port = scanner.nextLine();
		
		System.out.println("Enter your request:");

		sentence = inFromUser.readLine();
		sentence = sentence + "\n";
		String[] str = sentence.split(" ",0);

		if(str[0].equals("PUT")){

			System.out.print("Content-type: ");
			sentence = sentence  + "Content-type: ";
			sentence = sentence + inFromUser.readLine() + "\n";

			System.out.print("Content-length: ");
			sentence = sentence + "Content-length: ";
			sentence = sentence + inFromUser.readLine() + "\n";
			System.out.print("\n");
			sentence = sentence + "\r\n";

			while( !(tmp_input.equals("EOF")) ){
				tmp_input = inFromUser.readLine();
				sentence = sentence + tmp_input + "\n";
			}
		}

		Thread request = new Thread( new Client_Connection(sentence,inputString,port) );
		request.run();

	 }
}
