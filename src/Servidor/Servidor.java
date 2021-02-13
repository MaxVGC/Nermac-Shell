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
import java.net.*;
import java.io.*;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Servidor {

    String text;
    String UserClient;
    private Socket socket;
    private ServerSocket serverSocket;
    private DataInputStream bufferDeEntrada = null;
    private DataOutputStream bufferDeSalida = null;
    Scanner escaner = new Scanner(System.in);
    final String COMANDO_TERMINACION = "salir()";

    public Boolean isConnected(String a, String user, String pass) throws IOException {
        try {
            int puerto = Integer.parseInt(a);
            serverSocket = new ServerSocket(puerto);
            serverSocket.setSoTimeout(60 * 1000);
            socket = serverSocket.accept();
            if (socket.isConnected()) {
                flujos();
                enviar(user + ":" + pass);
                if (recibirConfirmacion().equals("ready")) {
                    cerrarConexion(0);
                    socket.close();
                    serverSocket.close();
                    return true;
                } else {
                    cerrarConexion(0);
                    socket.close();
                    serverSocket.close();
                    return false;
                }
            } else {
                serverSocket.close();
                socket.close();
                return false;
            }
        } catch (Exception e) {
            socket.close();
            serverSocket.close();
            return false;
        }
    }

    public void IniciarServidor(int puerto) throws IOException {
        serverSocket = new ServerSocket(puerto);
        mostrarTexto("Esperando conexión entrante en el puerto " + String.valueOf(puerto) + "...");
        socket = serverSocket.accept();
        mostrarTexto("Conexión establecida con: " + socket.getInetAddress().getHostName() + "\n\n\n");
    }

    public void flujos() {
        try {
            bufferDeEntrada = new DataInputStream(socket.getInputStream());
            bufferDeSalida = new DataOutputStream(socket.getOutputStream());
            bufferDeSalida.flush();
        } catch (IOException e) {
            mostrarTexto("Error en la apertura de flujos");
        }
    }

    public String recibirConfirmacion() throws IOException {
        String st = "";
        st = (String) bufferDeEntrada.readUTF();
        return st;
    }

    public String recibirDatos() {
        String st = "";
        try {
            do {
                st = (String) bufferDeEntrada.readUTF();
                if (st.contains("///nick///")) {
                    String d = st.substring(11, st.length());
                    UserClient = d;
                    System.out.println("\nConexión establecida con: " + UserClient + "\n");
                    System.out.print("\n[Servidor] => ");
                    return ("Conexión establecida con: " + UserClient + "\n");
                } else if (st.equals("salir()")) {
                    cerrarConexion();
                    return null;
                } else {
                    mostrarTexto("\n[" + UserClient + "] => " + st);
                    System.out.print("\n[Servidor] => ");
                    return ("[" + UserClient + "] => " + st + "\n");
                }
            } while (!st.equals(COMANDO_TERMINACION));
        } catch (IOException e) {
            return null;
        }

    }

    public void enviar(String s) {
        try {
            bufferDeSalida.writeUTF(s);
            bufferDeSalida.flush();
        } catch (IOException e) {
            mostrarTexto("Error en enviar(): " + e.getMessage());
        }
    }

    public static void mostrarTexto(String s) {
        System.out.print(s);
    }

    public void escribirDatos() {
        while (true) {
            System.out.print("[Servidor] => ");
            enviar(escaner.nextLine());
        }
    }

    public void cerrarConexion(int a) {
        try {
            bufferDeEntrada.close();
            bufferDeSalida.close();
            socket.close();
        } catch (IOException e) {
            mostrarTexto("Excepción en cerrarConexion(): " + e.getMessage());
        } finally {
            mostrarTexto("Conversación finalizada....");
        }
    }

    public void cerrarConexion() {
        try {
            bufferDeEntrada.close();
            bufferDeSalida.close();
            socket.close();
        } catch (IOException e) {
            mostrarTexto("Excepción en cerrarConexion(): " + e.getMessage());
        } finally {
            mostrarTexto("Conversación finalizada....");
            System.exit(0);
        }
    }

    public void ejecutarConexion(int puerto) {
        Thread hilo = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        IniciarServidor(puerto);
                        flujos();
                        recibirDatos();
                    } catch (IOException ex) {
                        Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
                    } finally {
                        cerrarConexion();
                    }
                }
            }
        });
        hilo.start();
    }

    public static void main(String[] args) throws IOException {
        Servidor s = new Servidor();
        Scanner sc = new Scanner(System.in);

        mostrarTexto("Ingresa el puerto [5050 por defecto]: ");
        String puerto = sc.nextLine();
        if (puerto.length() <= 0) {
            puerto = "5050";
        }
        s.ejecutarConexion(Integer.parseInt(puerto));
        s.escribirDatos();
    }
}
