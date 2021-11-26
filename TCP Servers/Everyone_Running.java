/*
 * @Stamatakis Stylianos
 * @AM4041
 */
import java.io.*;
import java.net.*;
import java.util.*;

class Everyone_Running extends TimerTask {

    LinkedList<String> server_list = new LinkedList<String>();
    LinkedList<String> server_IPS = new LinkedList<String>();

    LinkedList<String> tmp_sockets = new LinkedList<String>();
    LinkedList<String> tmp_IPS = new LinkedList<String>();

    String my_ip, my_socket;
  
    String connection_IP,connection_socket;

    String next_socket, next_IP;

    int iam = 0;
    String message;

    public void set(String ServerSocket, String Server_IP, String cnt_socket, String cnt_IP, LinkedList<String> srv_list, LinkedList<String> srv_IPS) {

        connection_socket = cnt_socket;
        connection_IP = cnt_IP;

        my_ip = Server_IP;
        my_socket = ServerSocket;

        server_list = srv_list;
        server_IPS = srv_IPS;
    }

    @Override
    public void run() {
        try {   
            Socket write_back_socket = new Socket(connection_IP, Integer.parseInt(connection_socket));

            DataOutputStream outToServer = 
                new DataOutputStream(write_back_socket.getOutputStream());

            message = "SERVER\nALIVE\n";
            outToServer.writeBytes(message);
            outToServer.writeBytes(my_socket + "\n");
            outToServer.writeBytes(my_ip + "\n");

            write_back_socket.close();

        } catch (Exception e) {
            
            System.out.println("Everyone_Running ERROR");
            
            for (int i = 0; i < server_IPS.size(); i++) {
               
                if ( !(server_IPS.get(i).equals(connection_IP) && server_list.get(i).equals(connection_socket)) ) {                   
                    tmp_IPS.add(server_IPS.get(i));
                    tmp_sockets.add(server_list.get(i));
                }
            }
            
            for (int i = 0; i < tmp_sockets.size(); i++) {
                if (tmp_sockets.get(i).equals(my_socket) && tmp_IPS.get(i).equals(my_ip)) {
                    iam = i;
                    break;
                }
            }

            if (iam == (tmp_sockets.size() - 1)) {
                next_socket = tmp_sockets.get(0);
                next_IP = tmp_IPS.get(0);
            } else {
                iam++;
                next_socket = tmp_sockets.get(iam);
                next_IP = tmp_IPS.get(iam);
            }

            try {
 
                Socket write_back_socket = new Socket(my_ip, Integer.parseInt(my_socket));
                
                DataOutputStream outToServer = 
                    new DataOutputStream(write_back_socket.getOutputStream());

                message = "SERVER\nBAD\n";
                outToServer.writeBytes(message);
                outToServer.writeBytes(my_socket + "\n");
                
                outToServer.writeBytes(my_ip + "\n");

                outToServer.writeBytes(next_socket + "\n");
                outToServer.writeBytes(next_IP + "\n");

                for(int i = 0; i < tmp_sockets.size(); i++) {
                    outToServer.writeBytes(tmp_sockets.get(i) + "\n");
                }
    
                outToServer.writeBytes("IP\n");
    
                for(int i = 0; i < tmp_IPS.size(); i++) {
                    outToServer.writeBytes(tmp_IPS.get(i) + "\n");
                }
                outToServer.writeBytes("EOF\n");

                write_back_socket.close();
            } catch (Exception e1) {
                System.out.println("Everyone_Running ERROR2" + e1);
            }
        }
    }
}