/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Pruebas;

import com.jcraft.jsch.JSchException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.nio.charset.Charset;
import java.util.Enumeration;

public class Prueba {

    public static void main(String[] args) throws Exception {
        // Aqui obtenemos la ip local de la maquina
        Enumeration e = NetworkInterface.getNetworkInterfaces();
        while (e.hasMoreElements()) {
            NetworkInterface n = (NetworkInterface) e.nextElement();
            Enumeration ee = n.getInetAddresses();
            while (ee.hasMoreElements()) {
                InetAddress i = (InetAddress) ee.nextElement();
                System.out.println(i.getHostAddress());
            }
        }
        Enumeration x = NetworkInterface.getNetworkInterfaces();
        NetworkInterface n = (NetworkInterface) x.nextElement();
        Enumeration ee = n.getInetAddresses();
        InetAddress i = (InetAddress) ee.nextElement();
        System.out.println(i.getHostAddress());
    }
}
