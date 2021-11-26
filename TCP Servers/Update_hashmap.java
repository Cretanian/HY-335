/*
 * @Stamatakis Stylianos
 * @AM4041
 */
import java.io.*;
import java.net.*;

class Update_hashmap implements Runnable {
    String message;

    String flag_socket,flag_ip;

    String file_data,file_name;

    String connection_IP;
    int connection_socket;


    public Update_hashmap(String cnt_socket, String cnt_IP, String starter_socket, String starter_ip, String flname, String fldata) {
        connection_socket = Integer.parseInt(cnt_socket);
        connection_IP = cnt_IP;

        flag_socket = starter_socket;
        flag_ip = starter_ip;

        file_name = flname;
        file_data = fldata;
    }

    @Override
    public void run() {
        
        try {
            Socket write_back_socket = new Socket(connection_IP, connection_socket);
           
            DataOutputStream outToServer = 
                new DataOutputStream(write_back_socket.getOutputStream());

            message = "SERVER\nPUT\n";

            outToServer.writeBytes(message);
            outToServer.writeBytes(flag_socket + "\n");
            outToServer.writeBytes(flag_ip + "\n");

            outToServer.writeBytes(file_name + "\n");
            outToServer.writeBytes(file_data);
            outToServer.writeBytes("EOF\n");

            write_back_socket.close();

        } catch (Exception e) {
            System.out.println("Update_hashmap ERROR\n" + e);
        }
    }
}
