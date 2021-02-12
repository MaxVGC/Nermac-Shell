/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Pruebas;

import Main.SesionUsuario;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.*;

public class Prueba {

    public static void main(String[] args) throws  IOException {    
      Socket socket = new Socket();
        InetAddress inetAddress = InetAddress.getLocalHost();
        int puerto = 2020;
        SocketAddress socketAddress = new InetSocketAddress(inetAddress, puerto);
        System.out.println(inetAddress.getHostAddress());
        System.out.println("d "+socketAddress.toString());
        socket.bind(socketAddress);
        int timeout = 10000;
        System.getProperty("user.name");

        socket.connect(socketAddress, timeout);
        System.out.println("Dirección Inet:" + socket.getInetAddress());
        System.out.println("Número de puerto:" + socket.getLocalPort());
        SesionUsuario n=new SesionUsuario();
        System.out.println("pass: "+n.getPassword());
        if(socket.isConnected()){
            System.out.println("connect");
        }else{
            System.out.println("disconnect");
        }
        
    }
}
