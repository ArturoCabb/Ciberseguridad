import java.net.*;
import java.io.*;

public class ServerToAndroid {
    public static void main(String[] args) {
        try {
            int port = 8080;
  
            // Server Key
            int b = 3;
  
            // Client p, g, and key
            double clientP, clientG, clientA, B, Bdash;
            String Bstr;
  
            // Established the Connection
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("Waiting for client on port " + serverSocket.getLocalPort() + "...");
            Socket server = serverSocket.accept();
            System.out.println("Just connected to " + server.getRemoteSocketAddress());
  
            // Server's Private Key
            System.out.println("From Server : Private Key = " + b);
  
            // Accepts the data from client
            BufferedReader entrada = new BufferedReader(new InputStreamReader(server.getInputStream()));
            String mensajeServidor;
            clientP = Integer.parseInt(entrada.readLine());
            clientG = Integer.parseInt(entrada.readLine());
            clientA = Integer.parseInt(entrada.readLine());
            //DataInputStream in = new DataInputStream(server.getInputStream());
  
            //clientP = Integer.parseInt(in.readUTF()); // to accept p
            System.out.println("From Client : P = " + clientP);
  
            //clientG = Integer.parseInt(in.readUTF()); // to accept g
            System.out.println("From Client : G = " + clientG);
  
            //clientA = Double.parseDouble(in.readUTF()); // to accept A
            System.out.println("From Client : Public Key = " + clientA);
  
            B = ((Math.pow(clientG, b)) % clientP); // calculation of B
            Bstr = Double.toString(B);
            System.out.println(Bstr);
  
            // Sends data to client
            // Value of B
            OutputStream outToclient = server.getOutputStream();
            DataOutputStream out = new DataOutputStream(outToclient);
  
            out.writeUTF(Bstr); // Sending B
            System.out.println("Enviando B");
  
            Bdash = ((Math.pow(clientA, b)) % clientP); // calculation of Bdash
  
            System.out.println("Secret Key to perform Symmetric Encryption = "
                               + Bdash);
            server.close();
        }
  
        catch (SocketTimeoutException s) {
            System.out.println("Socket timed out!");
        }
        catch (IOException e) {
        }
    }
}
