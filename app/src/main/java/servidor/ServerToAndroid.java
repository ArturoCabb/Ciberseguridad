import java.net.*;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;

import java.io.*;

public class ServerToAndroid {
    private static SSLServerSocket serverSocket = null;
    public static void main(String[] args) {
        try {
            int port = 8080;
  
            // Server Key
            int b = 3;
  
            // Client p, g, and key
            double clientP, clientG, clientA, B, Bdash;
            String Bstr;
  
            // Established the Connection
            //ServerSocket serverSocket = new ServerSocket(port);
            SSLServerSocketFactory socketFactory = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
            serverSocket = (SSLServerSocket) socketFactory.createServerSocket(port);
            System.out.println("Waiting for client on port " + serverSocket.getLocalPort() + "...");
            //Socket server = serverSocket.accept();
            SSLSocket socket = (SSLSocket) serverSocket.accept();
            System.out.println("Just connected to " + socket.getRemoteSocketAddress());
  
            // Server's Private Key
            System.out.println("From Server : Private Key = " + b);
  
            // Accepts the data from client
            BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            clientP = Integer.parseInt(entrada.readLine());
            System.out.println("From Client : P = " + clientP);
            clientG = Integer.parseInt(entrada.readLine());
            System.out.println("From Client : G = " + clientG);
            clientA = Integer.parseInt(entrada.readLine());
            System.out.println("From Client : Public Key = " + clientA);

            //DataInputStream in = new DataInputStream(server.getInputStream());
  
            //clientP = Integer.parseInt(in.readUTF()); // to accept p p es cualquier numero que quiera mientras sea primo
            //System.out.println("From Client : P = " + clientP);
  
            //clientG = Integer.parseInt(in.readUTF()); // to accept g g es un valor acordado
            //System.out.println("From Client : G = " + clientG);
  
            //clientA = Double.parseDouble(in.readUTF()); // to accept A
            //System.out.println("From Client : Public Key = " + clientA);
  
            B = ((Math.pow(clientG, b)) % clientP); // calculation of B
            Bstr = Double.toString(B);
            System.out.println(Bstr);
  
            // Sends data to client
            // Value of B
            OutputStream outToclient = socket.getOutputStream();
            DataOutputStream out = new DataOutputStream(outToclient);
  
            out.writeUTF(Bstr); // Sending B
            System.out.println("Enviando B");
  
            Bdash = ((Math.pow(clientA, b)) % clientP); // calculation of Bdash
  
            System.out.println("Secret Key to perform Symmetric Encryption = "
                               + Bdash);
            //server.close();
            out.close();
            socket.close();
        }
  
        catch (SocketTimeoutException s) {
            System.out.println("Socket timed out!");
        }
        catch (IOException e) {
        }
    }
}
