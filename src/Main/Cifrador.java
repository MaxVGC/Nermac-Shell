/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main;

import java.security.Key;
import java.security.MessageDigest;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 *
 * @author carlo
 */
public class Cifrador {
    //private static String clave = "1234567890123456";
      private static String clave = "0000000000000000";

    public String encriptar(String text) throws Exception {
        Key key = new SecretKeySpec(clave.getBytes(), "AES");
        Cipher cifrador = Cipher.getInstance("AES");
        cifrador.init(Cipher.ENCRYPT_MODE, key);
        byte[] encriptar = cifrador.doFinal(text.getBytes());
        String codificar=Base64.getEncoder().encodeToString(encriptar);
        return codificar;
    }

    public String desencriptar(String encrypted) throws Exception {
        byte[] decodificar = Base64.getDecoder().decode(encrypted);
        Key key = new SecretKeySpec(clave.getBytes(), "AES");
        Cipher cifrador = Cipher.getInstance("AES");
        cifrador.init(Cipher.DECRYPT_MODE, key);
        String desencriptar = new String(cifrador.doFinal(decodificar));
        return desencriptar;
    }
    
    public String hash(String clear) throws Exception { 
        
        MessageDigest md = MessageDigest.getInstance("SHA-256"); 
        md.reset();
        byte[] b = md.digest(clear.getBytes()); 
        int size = b.length;
        StringBuffer h = new StringBuffer(size); 
        for (int i = 0; i < size; i++) { 
            int u = b[i]&255; // unsigned conversion 
            if (u<16) { 
                h.append("0"+Integer.toHexString(u)); 
            } else { 
                h.append(Integer.toHexString(u)); 
            } 
        } 
        return h.toString(); 
    } 

}
