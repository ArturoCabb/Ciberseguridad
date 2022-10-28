package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

public class MainActivity extends AppCompatActivity {

    private TextView tvA, tvB, tvP, tvG, tvCS, tvPA;
    private static final String IP = "192.168.1.78";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        tvA = (TextView) findViewById(R.id.A);
        tvB = (TextView) findViewById(R.id.B);
        tvP = (TextView) findViewById(R.id.P);
        tvG = (TextView) findViewById(R.id.G);
        tvPA = (TextView) findViewById(R.id.PA);
        tvCS = (TextView) findViewById(R.id.CS);
    }

    public void Iniciar(View view){
        // Declaración de @param P y de @param G
        final long P = 23, G = 9;
        tvP.setText("Valor de P: " + Long.toString(P));
        tvG.setText("Valor de G: " + Long.toString(G));

        String A;
        long B = 0, a = 4;

        // Delclaración de llave del cliente @param a
        tvA.setText("Llave privada A: " + Long.toString(a));
        DiffieHelman dif = new DiffieHelman();
        long x = dif.DiffieHelmanClient(G, a, P);
        A = Long.toString(x);
        tvPA.setText("Llave publica A:" + A);
        Log.println(Log.ASSERT, "OK", "Hasta aqui vamos bien");

        try {
            // Crear socket
            Socket socket = new Socket(IP, 8080);
            Log.println(Log.ASSERT, "OK", "Si se crea la conexion socket");
            // Enviar mensaje al servidor
            PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
            Log.println(Log.ASSERT, "OK", "iniciando el envía");
            out.println(Long.toString(P));
            out.println(Long.toString(G));
            out.println(A);
            Log.println(Log.ASSERT, "OK", "Datos enviados");

            // Recibir mensaje
            Log.println(Log.ASSERT, "OK", "Iniciando la recepcion");
            BufferedReader br = new BufferedReader(new InputStreamReader((socket.getInputStream())));
            String msg = br.readLine();
            if (msg != null) {
                Double b = Double.parseDouble(msg);
                B = b.longValue();
            }
            else {
                tvB.setText("¡O no!");
            }
            Log.println(Log.ASSERT, "OK", "Datos recibidos");

            // Cerrar la transmisión
            out.close();
            br.close();
            Log.println(Log.ASSERT, "OK", "Transmisión cerrada");
            // Cerrar el socket
            socket.close();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Log.println(Log.ASSERT, "OK", "Iniciando ultimo calculo");
        long Adash = dif.DiffieHelmanClient(B, a, P);
        Log.println(Log.ASSERT, "OK", "Se calculo el Adash");
        tvCS.setText("Código secreto: " + Long.toString(Adash));
        HashSHA sha = new HashSHA();
        String myHash = sha.getMyHash(String.valueOf(Adash));
        Log.println(Log.ASSERT, "OK", "Se calculo el Hash " + myHash);
    }

    public void runServer(View view) {
        // Declaración de @param P y de @param G
        final long P = 23, G = 9;
        tvP.setText("Valor de P: " + Long.toString(P));
        tvG.setText("Valor de G: " + Long.toString(G));

        String A;
        long B = 0, a = 4;

        // Delclaración de llave del cliente @param a
        tvA.setText("Llave privada A: " + Long.toString(a));
        DiffieHelman dif = new DiffieHelman();
        long x = dif.DiffieHelmanClient(G, a, P);
        A = Long.toString(x);
        tvPA.setText("Llave publica A: " + A);
        Log.println(Log.ASSERT, "OK", "Hasta aqui vamos bien");

        try {
            SSLSocketFactory socketFactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
            SSLSocket socket = (SSLSocket) socketFactory.createSocket(IP ,8080);
            Log.println(Log.ASSERT, "OK", "Si se crea la conexion socket");
            // Enviar mensaje al servidor
            Log.println(Log.ASSERT, "OK", "iniciando el envía");
            PrintWriter out = new PrintWriter (new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())));
            //OutputStream out = socket.getOutputStream();
            Log.println(Log.ASSERT, "OK", "Buffer listo");
            out.println(Long.toString(P));
            //out.write(String.valueOf(P));
            //out.flush();
            out.println(Long.toString(G));
            //out.write(String.valueOf(G));
            //out.flush();
            out.println(A);
            //out.write(A);
            //out.flush();
            Log.println(Log.ASSERT, "OK", "Datos enviados");

            // Recibir mensaje
            Log.println(Log.ASSERT, "OK", "Iniciando la recepcion");
            //BufferedReader br = new BufferedReader(new InputStreamReader((socket.getInputStream())));
            InputStream br = socket.getInputStream();
            //String msg = br.readLine();
            String msg = String.valueOf(br.read());
            if (msg != null) {
                Double b = Double.parseDouble(msg);
                B = b.longValue();
            }
            else {
                tvB.setText("¡O no!");
            }
            Log.println(Log.ASSERT, "OK", "Datos recibidos");

            Log.println(Log.ASSERT, "OK", "Iniciando ultimo calculo");
            long Adash = dif.DiffieHelmanClient(B, a, P);
            tvCS.setText("Código secreto: " + Long.toString(Adash));
            HashSHA sha = new HashSHA();
            String myHash = sha.getMyHash(String.valueOf(Adash));
            String mensaje = "odioAgina";
            String m = cifrarMensaje(myHash, mensaje);

            // Cerrar la transmisión
            out.close();
            br.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String cifrarMensaje(String key, String clearText) {
        System.out.println ("texto claro: " + clearText);
        CifradoDes cd = new CifradoDes();
        String c = null;
        try {
            c = cd.encryptForDES(clearText, key);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return c;
    }

    public String descifrarMensaje(String key, String secureText) {
        System.out.println ("texto cifrado: " + secureText);
        CifradoDes cd = new CifradoDes();
        String d = null;
        try {
             d = cd.decryptForDES(secureText, key);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return d;
    }
}