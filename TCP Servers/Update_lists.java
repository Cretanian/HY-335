/*
 * @Stamatakis Stylianos
 * @AM4041
 */
import java.io.*;
import java.net.*;
import java.util.*;

class Update_lists implements Runnable {

    LinkedList<String> server_list = new LinkedList<String>();
    LinkedList<String> server_IPS = new LinkedList<String>();

    String flag_socket,flag_IP;
    String message;
    String connection_IP;
    int connection_socket;

    String next_socket, next_IP;

    public Update_lists(String cnt_socket, String cnt_IP, String starter_socket, String starter_ip, String nxt_socket, String nxt_IP, LinkedList<String> srv_list, LinkedList<String> srv_IPS) {
        connection_socket = Integer.parseInt(cnt_socket);
        connection_IP = cnt_IP;

        flag_socket = starter_socket;
        flag_IP = starter_ip;

        next_socket = nxt_socket;
        next_IP = nxt_IP;

        server_list = srv_list;
        server_IPS = srv_IPS;
    }

    @Override
    public void run() {
        
        try {
            Socket write_back_socket = new Socket(connection_IP, connection_socket);
           
            DataOutputStream outToServer = 
                new DataOutputStream(write_back_socket.getOutputStream());

            message = "SERVER\nUPDATE\n";

            outToServer.writeBytes(message);
            outToServer.writeBytes(flag_socket + "\n");
            outToServer.writeBytes(flag_IP + "\n");
            
            outToServer.writeBytes(next_socket + "\n");
            outToServer.writeBytes(next_IP + "\n");

            for(int i = 0; i < server_list.size(); i++) {
                outToServer.writeBytes(server_list.get(i) + "\n");
            }

            outToServer.writeBytes("IP\n");

            for(int i = 0; i < server_IPS.size(); i++) {
                outToServer.writeBytes(server_IPS.get(i) + "\n");
            }
            outToServer.writeBytes("EOF\n");
            
            write_back_socket.close();

        } catch (Exception e) {
            System.out.println("Update_lists ERROR\n" + e);
        }
    }
}
