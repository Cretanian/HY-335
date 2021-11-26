/*
 * @Stamatakis Stylianos
 * @AM4041
 */
import java.io.*;
import java.net.*;

class Get_request implements Runnable {

    String message;

    String flag_socket,flag_ip;
    String filename, filedata;

    String connection_IP;
    int connection_socket;

    public Get_request(String cnt_socket, String cnt_IP,String starter_socket, String starter_ip, String flname, String fldata) {
       connection_socket = Integer.parseInt(cnt_socket);
       connection_IP = cnt_IP;

       flag_socket = starter_socket;
       flag_ip = starter_ip;

       filename = flname;
       filedata = fldata;
    }

    @Override
    public void run() {
        try {
            
            Socket clientSocket = new Socket(connection_IP, connection_socket);
            

            DataOutputStream outToServer = 
                new DataOutputStream(clientSocket.getOutputStream());

            message = "SERVER\nGET\n";

            outToServer.writeBytes(message);

            outToServer.writeBytes(flag_socket + "\n"); 
            outToServer.writeBytes(flag_ip + "\n");

            outToServer.writeBytes(filename + "\n");
            outToServer.writeBytes(filedata);
            outToServer.writeBytes("EOF\n");
            
            clientSocket.close();

            } catch (Exception e) {
                System.out.println("Get_request ERROR\n" + e);
        }
    }
}