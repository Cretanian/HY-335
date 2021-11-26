/**
 * Code is taken from Computer Networking: A Top-Down Approach Featuring 
 * the Internet, second edition, copyright 1996-2002 J.F Kurose and K.W. Ross, 
 * All Rights Reserved.
 *
 * @Stamatakis Stylianos
 * @AM4041
 **/

import java.io.*;
import java.net.*;
import java.util.*;

class WebServer{

    public static void main(String argv[]) throws Exception  {

	String requestMessageLine;
	String fileName;

	ServerSocket listenSocket = new ServerSocket(4041);

	while(true){
		Socket connectionSocket = listenSocket.accept();

		BufferedReader inFromClient =
        	    new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
		DataOutputStream outToClient =
        	    new DataOutputStream(connectionSocket.getOutputStream());

		requestMessageLine = inFromClient.readLine();

		StringTokenizer tokenizedLine =
        	    new StringTokenizer(requestMessageLine);

		String token;
		token = tokenizedLine.nextToken();

		if (token.equals("GET") || token.equals("PUT")){

		    	fileName = tokenizedLine.nextToken();

		    	if (fileName.startsWith("/") == true )
				fileName  = fileName.substring(1);

			File file = new File(fileName);

			if(token.equals("GET")){

				int numOfBytes = (int) file.length();

                        	FileInputStream inFile  = new FileInputStream (fileName);

                        	byte[] fileInBytes = new byte[numOfBytes];
                        	inFile.read(fileInBytes);

				outToClient.writeBytes("HTTP/1.1 200 Document Follows\r\n");
				if (fileName.endsWith(".jpg"))
					outToClient.writeBytes("Content-Type: image/jpeg\r\n");
				if (fileName.endsWith(".gif"))
					outToClient.writeBytes("Content-Type: image/gif\r\n");

				outToClient.writeBytes("Content-Length: " + numOfBytes + "\r\n");
				outToClient.writeBytes("File is:");
				outToClient.write(fileInBytes, 0, numOfBytes);

				}else if(token.equals("PUT") && file.createNewFile()){

					while( !(requestMessageLine = inFromClient.readLine()).endsWith("</p>") );

					FileWriter fileWriter = new FileWriter(file);
					fileWriter.write(requestMessageLine);
    					fileWriter.close();

					outToClient.writeBytes("HTTP/1.1 201 Created\r\n");
					outToClient.writeBytes("Content-Location: /" + fileName + "\r\n");

			}

		}else{
			outToClient.writeBytes("Bad Request Message\r\n");
		}

		connectionSocket.close();
	}
    }
}
