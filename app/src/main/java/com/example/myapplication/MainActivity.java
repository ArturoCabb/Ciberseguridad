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
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class MainActivity extends AppCompatActivity {

    private TextView tvA, tvB, tvP, tvG, tvCS, tvPA;
    private Button btnInit;
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
        btnInit = (Button) findViewById(R.id.btIn);

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
        long x = Diffie_Helman_Client(G, a, P);
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

            // Cerrar el socket
            socket.close();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Log.println(Log.ASSERT, "OK", "Iniciando ultimo calculo");
        long Adash = Diffie_helman_Complementario(B, a, P);
        tvCS.setText("Código secreto: " + Long.toString(Adash));
    }

    private long Diffie_helman_Complementario(long B, long a, long P) {
        return calculatePower(B, a, P);
    }

    private long Diffie_Helman_Client(long G, Long a, Long P) {
        // Generar llave del cliente @param x
        return calculatePower(G, a, P);
    }

    // Create calculatePower() method
    private long calculatePower(long x, long y, long P) {
        return (y == 1)? x : ((long)Math.pow(x, y)) % P;
    }
}