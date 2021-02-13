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
            socket = new Socket(ip, puerto);
            socket.setSoTimeout(10 * 1000);
            if (socket.isConnected()) {
                abrirFlujos();
                if (recibirCredenciales().equals(cred)) {
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
                if (st.equals("salir()")) {
                    cerrarConexion();
                    return null;
                } else {
                    st = (String) bufferDeEntrada.readUTF();
                    mostrarTexto("\n[Servidor] => " + st);
                    System.out.print("[" + username + "] => ");
                    return "\n[Servidor] => " + st;
                }
            } while (!st.equals(COMANDO_TERMINACION));
        } catch (IOException e) {
            return null;
        }
    }

    public String recibirCredenciales() throws IOException {
        String st = "";
        st = (String) bufferDeEntrada.readUTF();
        return st;
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

    public static void main(String[] argumentos) {
        Cliente cliente = new Cliente();
        Scanner escaner = new Scanner(System.in);
        mostrarTexto("Ingresa la IP: [localhost por defecto] ");
        String ip = escaner.nextLine();
        if (ip.length() <= 0) {
            ip = "localhost";
        }

        mostrarTexto("Puerto: [5050 por defecto] ");
        String puerto = escaner.nextLine();
        if (puerto.length() <= 0) {
            puerto = "5050";
        }
        cliente.ejecutarConexion(ip, Integer.parseInt(puerto));
        cliente.escribirDatos();
    }
}
