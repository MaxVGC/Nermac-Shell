/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main;

import java.security.Key;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 *
 * @author carlo
 */
public class Cifrador {
    private static String clave = "1234567890123456";

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
}
