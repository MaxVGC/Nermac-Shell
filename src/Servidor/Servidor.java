/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servidor;

/**
 *
 * @author carlo
 */
import Main.Cifrador;
import java.net.*;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Servidor {

    String Cliente;
    Socket Socket;
    ServerSocket SSocket;
    DataInputStream DatosIn = null;
    DataOutputStream DatosOut = null;
    String Salir = "salir()";

    public Boolean isConnected(String a, String user, String pass) throws IOException, Exception {
        int puerto = Integer.parseInt(a);
        SSocket = new ServerSocket(puerto);
        SSocket.setSoTimeout(60 * 1000);
        Socket = SSocket.accept();
        if (Socket.isConnected()) {
            AbrirDatos();
            Cifrador n = new Cifrador();
            EnviarDatos(n.hash(user) + ":" + n.hash(pass));
            if (RecibirConfirmacion().equals("ready")) {
                Servidor.this.CerrarDatos();
                Socket.close();
                SSocket.close();
                return true;
            } else {
                Servidor.this.CerrarDatos();
                Socket.close();
                SSocket.close();
                return false;
            }
        } else {
            SSocket.close();
            Socket.close();
            return false;
        }
    }

    public void IniciarServidor(int puerto) throws IOException {
        SSocket = new ServerSocket(puerto);
        Socket = SSocket.accept();
        Consola("Conexión establecida con: " + Socket.getInetAddress().getHostName() + "\n\n\n");
    }

    public void AbrirDatos() throws IOException {
        DatosIn = new DataInputStream(Socket.getInputStream());
        DatosOut = new DataOutputStream(Socket.getOutputStream());
        DatosOut.flush();
    }

    public String RecibirConfirmacion() throws IOException {
        String in = DatosIn.readUTF();
        System.out.println(in);
        return in;
    }

    public String Cmd(String command) throws IOException {
        try {
            String aux = "    ";
            Process p = Runtime.getRuntime().exec("cmd.exe /c " + command);
            InputStream s = p.getInputStream();
            BufferedReader in = new BufferedReader(new InputStreamReader(s, "UTF-8"));
            if (in.readLine() == null) {
                EnviarDatos("No se pudo ejecutar el comando '" + command + "'");
                return "No se pudo ejecutar el comando '" + command + "'";
            } else {
                String temp;
                while ((temp = in.readLine()) != null) {
                    aux = aux + "\n    " + temp;
                }
                EnviarDatos(aux);
                return aux;
            }
        } catch (IOException ex) {
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
            EnviarDatos("No se pudo ejecutar el comando '" + command + "'");
            return "No se pudo ejecutar el comando '" + command + "'";
        }
    }

    public String RecibirDatos() {
        String st = "";
        try {
            do {
                st = (String) DatosIn.readUTF();
                if (st.contains("///nick///")) {
                    String d = st.substring(11, st.length());
                    Cliente = d;
                    System.out.println("\nConexión establecida con: " + Cliente + "\n");
                    System.out.print("\n[Servidor] => ");
                    return ("Conexión establecida con: " + Cliente + "\n");
                } else if (st.substring(0, 3).equals("cmd")) {
                    return Cmd(st.substring(4, st.length())) + "\n";
                } else if (st.equals("salir()")) {
                    CerrarDatos();
                    return null;
                } else {
                    Consola("\n[" + Cliente + "] => " + st);
                    System.out.print("\n[Servidor] => ");
                    return ("[" + Cliente + "] => " + st + "\n");
                }
            } while (!st.equals(Salir));
        } catch (IOException e) {
            return null;
        }
    }

    public void EnviarDatos(String s) throws IOException {
        DatosOut.writeUTF(s);
        DatosOut.flush();
    }

    public static void Consola(String s) {
        System.out.print(s);
    }

    public void CerrarDatos() throws IOException {
        DatosIn.close();
        DatosOut.close();
        Socket.close();
    }

    public void Iniciar(int puerto) {
        Thread hilo = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        IniciarServidor(puerto);
                        AbrirDatos();
                        RecibirDatos();
                    } catch (IOException ex) {
                        Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
                    } finally {
                        try {
                            CerrarDatos();
                        } catch (IOException ex) {
                            System.exit(0);
                        }
                    }
                }
            }
        });
        hilo.start();
    }

}
