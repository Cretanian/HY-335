/*
 * Simple example TCP Client
 *
 * @Stamatakis Stylianos
 * @AM4041
 */
import java.io.*; 
import java.net.*; 
public class STCPClient { 

	 public static void main(String argv[]) throws Exception 
	 { 
		  String sentence,tmp_str; 
		  String modifiedSentence; 

		  BufferedReader inFromUser = 
			 new BufferedReader(new InputStreamReader(System.in)); 

		  Socket clientSocket = new Socket("147.52.19.9", 4041); 

		  DataOutputStream outToServer = 
			 new DataOutputStream(clientSocket.getOutputStream()); 

		  BufferedReader inFromServer = 
			 new BufferedReader(new
			 InputStreamReader(clientSocket.getInputStream())); 

		 sentence = inFromUser.readLine();
		 sentence = sentence + '\n';
		 String[] str = sentence.split(" ",0);

		 if(str[0].equals("PUT")){

			System.out.print("Content-type: ");
			sentence = sentence  + "Content-type: ";
			sentence = sentence + inFromUser.readLine() + '\n';

			System.out.print("Content-length: ");
			sentence = sentence + "Content-length: ";
			sentence = sentence + inFromUser.readLine() + '\n';
			System.out.print("\n");
			sentence = sentence + "\r\n";
			sentence = sentence + inFromUser.readLine() + '\n';
		}

		outToServer.writeBytes(sentence); 

		System.out.println("FROM SERVER:");
		while( (modifiedSentence  = inFromServer.readLine()) != null){ 
			System.out.println(modifiedSentence);
		}

		  clientSocket.close();
	 }
}
