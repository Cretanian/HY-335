/*
 * @Stamatakis Stylianos
 * @AM4041
 */
import java.io.*;
import java.net.*;
import java.util.*;

class Handler extends Thread {

    String requestMessageLine;
    String modifiedSentence;

    String fileName, files_data = "";
    int where_iam;

    String flag_socket, flag_IP;

    String next_IP,next_socket;
    String tmp_next_IP,tmp_next_socket;

    ServerSocket my_listen_Socket;
    String my_ip,my_socket;

    LinkedList<String> server_list = new LinkedList<String>();
    LinkedList<String> server_IPS = new LinkedList<String>();
    
    HashMap <String,String> SERVER_DATA = new HashMap <String,String>();

    String token;
    Timer timer;
    Everyone_Running task;

    String tmp;
    Socket wainting;

    LinkedList<String> tmp_sockets = new LinkedList<String>();
    LinkedList<String> tmp_IPS = new LinkedList<String>();

    public Handler(String ServerSocket,String Server_IP,LinkedList<String> srv_list, LinkedList<String> srv_IPS,ServerSocket listenSocket){
        server_list = srv_list;
        server_IPS = srv_IPS;

        tmp_next_IP = Server_IP;
        tmp_next_socket = ServerSocket;

        next_IP = Server_IP; 
        next_socket = ServerSocket;

        my_ip = Server_IP;
        my_socket = ServerSocket;
        my_listen_Socket = listenSocket;
    }

    @Override
    public void run(){
        try{
            timer = new Timer();
            task = new Everyone_Running();
            task.set(my_socket, my_ip, next_socket, next_IP, server_list, server_IPS);
            timer.schedule(task, 1000, 1000);

            while(true){            
                Socket connectionSocket = my_listen_Socket.accept();
                
                BufferedReader INPUT =
                    new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));

                DataOutputStream OUTPUT =
                    new DataOutputStream(connectionSocket.getOutputStream());
                   
                requestMessageLine = INPUT.readLine();
                
                StringTokenizer tokenizedLine =
                    new StringTokenizer(requestMessageLine);

                token = tokenizedLine.nextToken();

                if(token.equals("SERVER")){
                    requestMessageLine = INPUT.readLine();
                   
                    if(requestMessageLine.equals("NEW")){

                        tmp_next_IP = next_IP;
                        tmp_next_socket = next_socket;

                        requestMessageLine = INPUT.readLine();

                        next_socket = requestMessageLine;

                        requestMessageLine = INPUT.readLine();

                        next_IP = requestMessageLine;

                        where_iam = 0;
                        while( !(server_list.get(where_iam).equals(my_socket) && server_IPS.get(where_iam).equals(my_ip)) ){
                            where_iam++;
                        }

                        if(where_iam == 0 && (server_list.size() == 1) ){
                            server_list.add(next_socket);
                            server_IPS.add(next_IP);
                        }else{
                            server_list.add( where_iam + 1, next_socket);
                            server_IPS.add( where_iam + 1, next_IP);
                        }

                        connectionSocket.close();
System.out.println(server_list);   
System.out.println(server_IPS);                     
                        Thread UPDATE_LIST = new Thread( new Update_lists(next_socket, next_IP, my_socket, my_ip, tmp_next_socket, tmp_next_IP, server_list, server_IPS) );
                        UPDATE_LIST.run();

                        tmp_next_socket = next_socket;
                        tmp_next_IP = next_IP;
                        task.set(my_socket, my_ip, next_socket, next_IP, server_list, server_IPS);

                    }else if(requestMessageLine.equals("UPDATE")){

                        flag_socket = INPUT.readLine();
                        flag_IP = INPUT.readLine();

                        if( !( my_socket.equals(flag_socket) && my_ip.equals(flag_IP)) ){

                            tmp_next_socket = INPUT.readLine();
                            tmp_next_IP = INPUT.readLine();
                          

                            if (server_list.isEmpty() && server_list.isEmpty()){
                                next_socket = tmp_next_socket;
                                next_IP = tmp_next_IP;
                            }

                            server_list.clear();
                            server_IPS.clear();

                            requestMessageLine = INPUT.readLine();

                            while( !(requestMessageLine.equals("IP")) ){
                                server_list.add(requestMessageLine);
                                requestMessageLine = INPUT.readLine();
                            }

                            requestMessageLine = INPUT.readLine();

                            while( !(requestMessageLine.equals("EOF")) ){
                                server_IPS.add(requestMessageLine);
                                requestMessageLine = INPUT.readLine();
                            }

                            connectionSocket.close();
 
                           Thread UPDATE_LIST = new Thread( new Update_lists(next_socket, next_IP, flag_socket, flag_IP, tmp_next_socket, tmp_next_IP, server_list, server_IPS));
                           UPDATE_LIST.run();

                           tmp_next_socket = next_socket;
                           tmp_next_IP = next_IP;
                           task.set(my_socket, my_ip, next_socket, next_IP, server_list, server_IPS);

                        }else{    
                            connectionSocket.close();
                        }

                    }else if(requestMessageLine.equals("ALIVE")){
                     
                        flag_socket = INPUT.readLine();
                        flag_IP = INPUT.readLine();
                       
                        if( !( my_socket.equals(flag_socket) && my_ip.equals(flag_IP)) ){
                             
                            try{
                                Socket write_back_socket = new Socket(next_IP, Integer.parseInt(next_socket));
                                
                                DataOutputStream outToServer = 
                                    new DataOutputStream(write_back_socket.getOutputStream());
                                    
                                outToServer.writeBytes("SERVER\nALIVE\n");
                                outToServer.writeBytes(flag_socket + "\n");
                                outToServer.writeBytes(flag_IP + "\n");

                                write_back_socket.close();
                            }catch(Exception e){
           
                
                                for (int i = 0; i < server_IPS.size(); i++) {
               
                                    if ( !(server_IPS.get(i).equals(next_IP) && server_list.get(i).equals(next_socket)) ) {                   
                                        tmp_IPS.add(server_IPS.get(i));
                                        tmp_sockets.add(server_list.get(i));
                                    }
                                }

                                int iam = 0;
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
                                    String message;
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
                                }catch(Exception e1){
                                    System.out.println("da fuck");
                                }
                            }

                        }else{ 
                            connectionSocket.close();    
                        }
                    }else if(requestMessageLine.equals("BAD")){
System.out.println("Someone died :'(");
                                         
                        flag_socket = INPUT.readLine();
                        flag_IP = INPUT.readLine();
                        tmp_next_socket = INPUT.readLine();
                        tmp_next_IP = INPUT.readLine();
                        next_socket = tmp_next_socket;
                        next_IP = tmp_next_IP;

                        server_list.clear();
                        server_IPS.clear();

                        requestMessageLine = INPUT.readLine();

                        while( !(requestMessageLine.equals("IP")) ){
                            server_list.add(requestMessageLine);
                            requestMessageLine = INPUT.readLine();
                        }

                        requestMessageLine = INPUT.readLine();

                        while( !(requestMessageLine.equals("EOF")) ){
                            server_IPS.add(requestMessageLine);
                            requestMessageLine = INPUT.readLine();
                        }

                        connectionSocket.close();

                        Thread UPDATE_LIST = new Thread( new Update_lists(next_socket, next_IP, flag_socket, flag_IP, tmp_next_socket, tmp_next_IP, server_list, server_IPS));
                        UPDATE_LIST.run();

                        task.set(my_socket, my_ip, next_socket, next_IP, server_list, server_IPS);
                    
                    }else if(requestMessageLine.equals("PUT")){
                        flag_socket = INPUT.readLine();
                        flag_IP = INPUT.readLine(); 

                        if( !( my_socket.equals(flag_socket) && my_ip.equals(flag_IP)) ){                          
                            fileName = INPUT.readLine();
                            modifiedSentence = INPUT.readLine();
    
                            files_data = "";
                            while( !(modifiedSentence.equals("EOF")) ){
                                files_data = files_data + modifiedSentence + "\n";
                                modifiedSentence = INPUT.readLine();
                            }

                            SERVER_DATA.put(fileName,files_data);

                            connectionSocket.close();
System.out.println(SERVER_DATA);
                            Thread send_hash = new Thread( new Update_hashmap(next_socket, next_IP,  flag_socket, flag_IP ,fileName, files_data) );
                            send_hash.run();

                        }else{
                            connectionSocket.close();
                            OUTPUT =
                                new DataOutputStream(wainting.getOutputStream());

                            OUTPUT.writeBytes("PUT command executed successfully\n");
                            OUTPUT.writeBytes("EOF\n");
                            wainting.close();
                        }
                    
                    }else if(requestMessageLine.equals("GET")){
                        
                        flag_socket = INPUT.readLine();
                        flag_IP = INPUT.readLine();

                        fileName = INPUT.readLine();
                        files_data = INPUT.readLine();
                        
                        if( flag_socket.equals(my_socket) && flag_IP.equals(my_ip) ){
                            
                            if( files_data.equals("EOF")){
                                connectionSocket.close();
                               
                                OUTPUT =
                                    new DataOutputStream(wainting.getOutputStream());

                                OUTPUT.writeBytes("Sorry this file doesn't exist\n");
                            }else{
                                tmp = "";
                                while( !(files_data.equals("EOF")) ){
                                    tmp = tmp + files_data + "\n"; 
                                    files_data  = INPUT.readLine();
                                }
                                connectionSocket.close();

                                SERVER_DATA.put(fileName, tmp);
                               
                                OUTPUT =
                                    new DataOutputStream(wainting.getOutputStream());

                                OUTPUT.writeBytes(tmp);
                            }
                            OUTPUT.writeBytes("EOF\n");
                            wainting.close();

                        }else{
                              
                            if( SERVER_DATA.containsKey(fileName) ){
                                tmp = SERVER_DATA.get(fileName);
                            }else if(files_data.equals("EOF")){
                                tmp = files_data + "\n";
                            }else{
                                tmp = "";
                                while( !(files_data.equals("EOF")) ){
                                    tmp = tmp + files_data + "\n"; 
                                    files_data  = INPUT.readLine();
                                }
                                SERVER_DATA.put(fileName, tmp);
                            }

                            connectionSocket.close();
                            
                            Thread send_hash = new Thread( new Get_request(next_socket, next_IP, flag_socket, flag_IP, fileName, tmp) );
                            send_hash.run();
                        }
                    }
                }else if(token.equals("PUT")){

                    token = tokenizedLine.nextToken();
                    fileName = token;

                    if (fileName.startsWith("/") == true ){
                        fileName  = fileName.substring(1);
                    }

                    modifiedSentence  = INPUT.readLine();
                    modifiedSentence  = INPUT.readLine();
                    modifiedSentence  = INPUT.readLine();

                    modifiedSentence  = INPUT.readLine();

                    files_data = "";
                    while( !(modifiedSentence.equals("EOF")) ){ 
                        files_data = files_data + modifiedSentence + "\n";
                        modifiedSentence  = INPUT.readLine();
                    }

                    SERVER_DATA.put(fileName,files_data);

System.out.println(SERVER_DATA);
                    Thread send_hash = new Thread( new Update_hashmap(next_socket, next_IP, my_socket, my_ip, fileName, files_data) );
                    send_hash.run();
                    wainting = connectionSocket;
                    

                }else if(token.equals("GET")){
                    
                    token = tokenizedLine.nextToken();
                    fileName = token;
                    if( SERVER_DATA.containsKey(fileName) ){
                        files_data = SERVER_DATA.get(fileName);
                        OUTPUT.writeBytes(files_data);
                        OUTPUT.writeBytes("EOF\n");
                        connectionSocket.close();
                    }else{
                        files_data = "EOF\n";
                        Thread GET_GET = new Thread(new Get_request(next_socket, next_IP, my_socket, my_ip, fileName, files_data) );
                        GET_GET.run();
                        wainting = connectionSocket;
                    }

                }
            }
        }catch(Exception e){
            System.out.println("Handler Error\n" + e);
        }
    }
}
