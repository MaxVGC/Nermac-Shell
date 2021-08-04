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
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Cliente {

    static String username;
    private Socket socket;
    private DataInputStream bufferDeEntrada = null;
    private DataOutputStream bufferDeSalida = null;
    Scanner teclado = new Scanner(System.in);
    final String COMANDO_TERMINACION = "salir()";

    public Cliente() {
        this.username = System.getProperty("user.name");
    }

    public Boolean isConnected(String ip, int puerto, String cred) throws IOException, InterruptedException {
        try {
            System.out.println(ip+puerto);
            socket = new Socket(ip, puerto);
            socket.setSoTimeout(10 * 1000);
            if (socket.isConnected()) {
                abrirFlujos();
                if (recibirCredenciales().equals(cred)) {
                    System.out.println("entre");
                    enviar("ready");
                    cerrarConexion(0);
                    return true;
                } else {
                    System.out.println("no paso");
                    cerrarConexion(0);
                    return false;
                }
            } else {
                socket.close();
                return false;
            }
        } catch (Exception e) {
            Thread.sleep(10 * 1000);
            return false;
        }

    }

    public void IniciarCliente(String ip, int puerto) throws IOException {

        socket = new Socket(ip, puerto);
        mostrarTexto("Conectado a :" + socket.getInetAddress().getHostName());
        System.out.print("[" + username + "] => ");

    }

    public static void mostrarTexto(String s) {
        System.out.println(s);
    }

    public void abrirFlujos() {
        try {
            bufferDeEntrada = new DataInputStream(socket.getInputStream());
            bufferDeSalida = new DataOutputStream(socket.getOutputStream());
            bufferDeSalida.flush();
        } catch (IOException e) {
            mostrarTexto("Error en la apertura de flujos");
        }
    }

    public void enviar(String s) {
        try {
            System.out.println("antes "+s.length());
            if(s.length() <=3 || s.isEmpty()){
                s=s+"      ";
            }
            bufferDeSalida.writeUTF(s);
            bufferDeSalida.flush();
        } catch (IOException e) {
            mostrarTexto("IOException on enviar");
        }
    }

    public void cerrarConexion() {
        try {
            bufferDeEntrada.close();
            bufferDeSalida.close();
            socket.close();
            mostrarTexto("Conexión terminada");
        } catch (IOException e) {
            mostrarTexto("IOException on cerrarConexion()");
        } finally {
            System.exit(0);
        }
    }

    public void cerrarConexion(int a) {
        try {
            bufferDeEntrada.close();
            bufferDeSalida.close();
            socket.close();
            mostrarTexto("Conexión terminada");
        } catch (IOException e) {
            mostrarTexto("IOException on cerrarConexion()");
        } finally {
        }
    }

    public void ejecutarConexion(String ip, int puerto) {
        Thread hilo = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    IniciarCliente(ip, puerto);
                    abrirFlujos();
                    enviar("///nick///:" + username);
                    recibirDatos();
                } catch (IOException ex) {
                    Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
                } finally {
                    cerrarConexion();
                }
            }
        });
        hilo.start();

    }

    public String recibirDatos() {
        String st = "";
        try {
            do {
                st = (String) bufferDeEntrada.readUTF();
                if (st != "") {
                    if (st.equals("salir()")) {
                        cerrarConexion();
                        return null;
                    } else if (st.contains("[Rec]")) {
                        return st;
                    } else {
                        mostrarTexto("\n[Servidor] => " + st);
                        System.out.print("[" + username + "] => ");
                        return "\n[Servidor] => " + st;
                    }
                } else {
                    return "";
                }
            } while (!st.equals(COMANDO_TERMINACION));
        } catch (IOException e) {
            return null;
        }
    }

    public String recibirCredenciales() throws IOException, Exception {
        Cifrador n=new Cifrador();
        String st = "";
        st = (String) bufferDeEntrada.readUTF();
        System.out.println("st "+st);
        String[] aux=st.split(":");
        System.out.println(n.desencriptar(aux[0])+n.desencriptar(aux[1]));
        return n.desencriptar(aux[0])+":"+n.desencriptar(aux[1]);
    }

    public void escribirDatos() {
        String entrada = "";
        while (true) {
            System.out.print("[" + username + "] => ");
            entrada = teclado.nextLine();
            if (entrada.length() > 0) {
                enviar(entrada);
            }
        }
    }

}
