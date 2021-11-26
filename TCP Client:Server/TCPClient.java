/*
 * Simple example TCPClient
 *
 * @Stamatakis Stylianos
 * @AM4041
 */
import java.io.*;
import java.net.*;
public class TCPClient {

	 public static void main(String argv[]) throws Exception 
	 {
		String sentence;
		String modifiedSentence; 

		BufferedReader inFromUser =
			 new BufferedReader(new InputStreamReader(System.in)); 

		Socket clientSocket = new Socket("147.52.19.9", 6789); 

		DataOutputStream outToServer = 
			 new DataOutputStream(clientSocket.getOutputStream()); 

		BufferedReader inFromServer = 
			 new BufferedReader(new
			 InputStreamReader(clientSocket.getInputStream())); 

		sentence = inFromUser.readLine();  

		outToServer.writeBytes(sentence + '\n'); 

		while( (modifiedSentence = inFromServer.readLine()) != null){ ; 

			System.out.println("FROM SERVER: " + modifiedSentence);

		}
		  clientSocket.close();

	 }
}

