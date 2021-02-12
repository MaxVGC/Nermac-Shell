/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main;

import Cliente.F_sCliente;
import Servidor.F_sServidor;

/**
 *
 * @author carlo
 */
public class Iniciar {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        F_sServidor f=new F_sServidor();
        F_sCliente c=new F_sCliente();
        c.setVisible(true);
        f.setVisible(true);
    }
    
}
