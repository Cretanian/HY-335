
/*
 * @Stamatakis Stylianos
 * @AM4041
 */
import java.io.*;
import java.net.*;

class Client_Connection implements Runnable {

    String request;
    String modifiedSentence;
  
    String IP;
    int port;

    public Client_Connection(String rqst,String cnt_IP,String cnt_socket) {
        request = rqst;
        port =  Integer.parseInt(cnt_socket);
        IP = cnt_IP;
    }

    @Override
    public void run() {
        try {    
            Socket clientSocket = new Socket(IP, port);

            DataOutputStream outToServer = 
                new DataOutputStream(clientSocket.getOutputStream());

            BufferedReader inFromServer = 
                new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        
            outToServer.writeBytes(request); 

            System.out.println("FROM SERVER:");
            modifiedSentence  = inFromServer.readLine();
	    	while( !(modifiedSentence.equals("EOF")) ){ 
                System.out.println(modifiedSentence);
                modifiedSentence  = inFromServer.readLine();
		    }
            
            clientSocket.close();

            } catch (IOException e) {
                System.out.println("Error in Client_Connection\n" + e);
        }
    }
}