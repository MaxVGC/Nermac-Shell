/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Cliente;

/**
 *
 * @author carlo
 */
import Main.Cifrador;
import java.net.*;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Cliente {

    static String Usuario;
    Socket Socket;
    DataInputStream DatosIn = null;
    DataOutputStream DatosOut = null;
    String Salir = "salir()";

    public Cliente() {
        this.Usuario = System.getProperty("user.name");
    }

    public Boolean isConnected(String ip, int puerto, String cred) throws IOException, InterruptedException {
        try {
            Cifrador n=new Cifrador();
            String[] aux=cred.split(":");
            System.out.println(ip + puerto);
            Socket = new Socket(ip, puerto);
            Socket.setSoTimeout(10 * 1000);
            if (Socket.isConnected()) {
                AbrirDatos();
                if (Credenciales().equals(n.hash(aux[0])+":"+n.hash(aux[1]))) {
                    System.out.println("entre");
                    EnviarDatos("ready");
                    CerrarDatos(0);
                    return true;
                } else {
                    System.out.println("no paso");
                    CerrarDatos(0);
                    return false;
                }
            } else {
                Socket.close();
                return false;
            }
        } catch (Exception e) {
            Thread.sleep(10 * 1000);
            return false;
        }

    }

    public void IniciarCliente(String ip, int puerto) throws IOException {
        Socket = new Socket(ip, puerto);
        Consola("Conectado a :" + Socket.getInetAddress().getHostName());
    }

    public static void Consola(String s) {
        System.out.println(s);
    }

    public void AbrirDatos() throws IOException {
        DatosIn = new DataInputStream(Socket.getInputStream());
        DatosOut = new DataOutputStream(Socket.getOutputStream());
        DatosOut.flush();
    }

    public void EnviarDatos(String s) throws IOException {
        System.out.println("antes " + s.length());
        if (s.length() <= 3 || s.isEmpty()) {
            s = s + "      ";
        }
        DatosOut.writeUTF(s);
        DatosOut.flush();
    }

    public void CerrarDatos() throws IOException {
        DatosIn.close();
        DatosOut.close();
        Socket.close();
    }

    public void CerrarDatos(int a) throws IOException {
        DatosIn.close();
        DatosOut.close();
        Socket.close();
    }

    public void ejecutarConexion(String ip, int puerto) {
        Thread hilo = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    IniciarCliente(ip, puerto);
                    AbrirDatos();
                    EnviarDatos("///nick///:" + Usuario);
                    RecibirDatos();
                } catch (IOException ex) {
                    Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
                } finally {
                    try {
                        Cliente.this.CerrarDatos();
                    } catch (IOException ex) {
                        System.exit(0);
                    }
                }
            }
        });
        hilo.start();

    }

    public String RecibirDatos() {
        String in = "";
        try {
            do {
                in = (String) DatosIn.readUTF();
                if (in != "") {
                    if (in.equals("salir()")) {
                        CerrarDatos();
                        return null;
                    } else if (in.contains("[Rec]")) {
                        return in;
                    } else {
                        Consola("\n[Servidor] => " + in);
                        System.out.print("[" + Usuario + "] => ");
                        return "\n[Servidor] => " + in;
                    }
                } else {
                    return "";
                }
            } while (!in.equals(Salir));
        } catch (IOException e) {
            return null;
        }
    }

    public String Credenciales() throws IOException, Exception {
        String in = DatosIn.readUTF();
        String[] aux = in.split(":");
        return aux[0] + ":" + aux[1];
    }

}
