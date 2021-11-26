/*
 * Simple example TCPServer
 *
 * @Stamatakis Stylianos
 * @AM4041
 */
import java.io.*;
import java.net.*;
import java.lang.*;
import java.util.ArrayList;
import java.util.Random;

public class TCPServer
{

   public static void main(String argv[]) throws Exception
   {
      String clientSentence;
      String capitalizedSentence;
      int port = 6789;

      ServerSocket welcomeSocket = new ServerSocket(port);

      while (true)
      {
      	System.out.println("Server ready on "+port);

        Socket connectionSocket = welcomeSocket.accept();

        BufferedReader inFromClient =
            new BufferedReader(
               new InputStreamReader(connectionSocket.getInputStream()));

        DataOutputStream outToClient =
            new DataOutputStream(connectionSocket.getOutputStream());

        clientSentence = inFromClient.readLine();

	int i = 0;
	Character my_char = new Character(clientSentence.charAt(i));
        Character tmp_char = new Character(' ');
	String my_str = new String("");
	ArrayList<String> Tokens = new ArrayList<String>();

	while( !(my_char == '$') ){
		if( my_char == '\\'  && (clientSentence.charAt((i+1)) == 'n' )){

			if( !(my_str.equals(""))){
				Tokens.add(my_str);
				my_str = "";
			}
			i = i + 2;
			my_char = clientSentence.charAt(i);
		}else{
			my_str = my_str + my_char;
	                my_char = clientSentence.charAt(++i);
		}
	}

        Tokens.add( (my_str+'$') );

	Random rand = new Random();
	if( (Tokens.size() - 1) > 0){
		i = rand.nextInt(Tokens.size() - 1) ;
	}else{
		 i = 0;
	}

        capitalizedSentence = Tokens.get(i) + '\n';

        outToClient.writeBytes(capitalizedSentence);

	connectionSocket.close();

      }
   }
}
