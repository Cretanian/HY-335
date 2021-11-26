
/**
 * @Stamatakis Stylianos
 * @AM4041
 **/

import java.io.*;
import java.net.*;
import java.util.*;

class WebServer{
  
    public static void main(String argv[]) throws Exception  {
        String inputString;
        String port;
        
        Scanner scanner;

        String myport,myip;

        LinkedList<String> server_PORTS = new LinkedList<String>();
        LinkedList<String> server_IPS = new LinkedList<String>();

        System.out.println("Enter your port:");
		scanner = new Scanner(System.in);
        myport = scanner.nextLine();

        System.out.println("Enter your IP address:");
        scanner = new Scanner(System.in);
        myip = scanner.nextLine();
        
        ServerSocket listenSocket = new ServerSocket( Integer.parseInt(myport) );

        System.out.println("Do you want to connect to server? Y/N");
        scanner = new Scanner(System.in);
        inputString = scanner.nextLine();

        if(inputString.equals("Y")){

            System.out.println("Enter IP address:");
            scanner = new Scanner(System.in);
            inputString = scanner.nextLine();

            System.out.println("Enter port:");
            scanner = new Scanner(System.in);
            port = scanner.nextLine();
            
            Socket Web_Socket = new Socket(inputString, Integer.parseInt(port));

            DataOutputStream outToServer = 
                new DataOutputStream(Web_Socket.getOutputStream());

            outToServer.writeBytes("SERVER\nNEW\n");
            outToServer.writeBytes(myport + "\n"); 
            outToServer.writeBytes(myip + "\n");

            Web_Socket.close();

            Thread MAIN = new Thread( new Handler(myport, myip, server_PORTS, server_IPS, listenSocket) );
            MAIN.run();
        
        }else{
            server_PORTS.add(myport);
            server_IPS.add(myip);

            Thread MAIN = new Thread( new Handler(myport, myip, server_PORTS, server_IPS, listenSocket) );
            MAIN.run();
        }
	    System.out.println("BAD STAFF");
    }
}
