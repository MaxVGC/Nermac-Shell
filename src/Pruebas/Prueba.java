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
import java.nio.charset.Charset;

public class Prueba {

    public static void main(String[] args) throws IOException, JSchException {
        
        String g="[Rec]dsada";

        System.out.println(g.substring(5,g.length()));
    }
}
