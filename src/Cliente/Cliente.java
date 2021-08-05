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
            System.out.println(ip + puerto);
            Socket = new Socket(ip, puerto);
            Socket.setSoTimeout(10 * 1000);
            if (Socket.isConnected()) {
                AbrirDatos();
                if (Credenciales().equals(cred)) {
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
        String st = "";
        try {
            do {
                st = (String) DatosIn.readUTF();
                if (st != "") {
                    if (st.equals("salir()")) {
                        CerrarDatos();
                        return null;
                    } else if (st.contains("[Rec]")) {
                        return st;
                    } else {
                        Consola("\n[Servidor] => " + st);
                        System.out.print("[" + Usuario + "] => ");
                        return "\n[Servidor] => " + st;
                    }
                } else {
                    return "";
                }
            } while (!st.equals(Salir));
        } catch (IOException e) {
            return null;
        }
    }

    public String Credenciales() throws IOException, Exception {
        Cifrador n = new Cifrador();
        String st = "";
        st = (String) DatosIn.readUTF();
        System.out.println("st " + st);
        String[] aux = st.split(":");
        System.out.println(n.desencriptar(aux[0]) + n.desencriptar(aux[1]));
        return n.desencriptar(aux[0]) + ":" + n.desencriptar(aux[1]);
    }

}
