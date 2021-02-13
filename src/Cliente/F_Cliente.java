/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Cliente;

import static Cliente.Cliente.username;
import Frames.Alerta;
import Main.Fuentes;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

/**
 *
 * @author carlo
 */
public class F_Cliente extends javax.swing.JFrame {

    Cliente c = new Cliente();
    Font FP;

    /**
     * Creates new form F_Cliente
     */
    //Cliente c;
    public F_Cliente(String ip, int port) throws UnknownHostException {
        initComponents();
        FP = Fuentes.setFuente("/Img/ubuntu.ttf", 16f);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
        jTextPane1.setFont(FP);
        jLabel4.setFont(FP);
        jLabel4.setText("[" + System.getProperty("user.name") + "@" + InetAddress.getLocalHost().getHostAddress() + ":" + port + "]");
        jLabel1.setIcon(new ImageIcon("src/Img/consola.png"));
        jLabel2.setIcon(new ImageIcon("src/Img/Barra.gif"));
        jLabel3.setIcon(new ImageIcon("src/Img/Btn_close.png"));
        ejecutarConexion(ip, port);
        c.escribirDatos();

    }
    int aux = 0;

    private int findLastNonWordChar(String text, int index) {
        while (--index >= 0) {
            if (String.valueOf(text.charAt(index)).matches("\\W")) {
                break;
            }
        }
        return index;
    }

    private int findFirstNonWordChar(String text, int index) {
        while (index < text.length()) {
            if (String.valueOf(text.charAt(index)).matches("\\W")) {
                break;
            }
            index++;
        }
        return index;
    }

    public void Ordenar(String a) throws InterruptedException {
        if (a != null && aux == 0) {
            String g = jTextPane1.getText();
            final StyleContext cont = StyleContext.getDefaultStyleContext();
            final AttributeSet attrRed = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground, Color.RED);
            final AttributeSet attrBlack = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground, Color.WHITE);
            final AttributeSet attrBlue = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground, Color.BLUE);
            DefaultStyledDocument doc = new DefaultStyledDocument() {
                public void insertString(int offset, String str, AttributeSet a) throws BadLocationException {
                    super.insertString(offset, str, a);

                    String text = getText(0, getLength());
                    int before = findLastNonWordChar(text, offset);
                    if (before < 0) {
                        before = 0;
                    }
                    int after = findFirstNonWordChar(text, offset + str.length());
                    int wordL = before;
                    int wordR = before;
                    while (wordR <= after) {
                        if (wordR == after || String.valueOf(text.charAt(wordR)).matches("\\W")) {
                            if (text.substring(wordL, wordR).matches("(\\W)*(Servidor)")) {
                                System.out.println("count " + wordL + wordR);
                                setCharacterAttributes(wordL + 1, wordR - wordL, attrRed, false);
                            } else if (text.substring(wordL, wordR).matches("(\\W)*(" + System.getProperty("user.name") + ")")) {
                                setCharacterAttributes(wordL + 1, wordR - wordL, attrBlue, false);
                            } else {
                                setCharacterAttributes(wordL, wordR - wordL, attrBlack, false);
                            }
                            wordL = wordR;
                        }
                        wordR++;
                    }
                }

                public void remove(int offs, int len) throws BadLocationException {
                    super.remove(offs, len);

                    String text = getText(0, getLength());
                    int before = findLastNonWordChar(text, offs);
                    if (before < 0) {
                        before = 0;
                    }
                    int after = findFirstNonWordChar(text, offs);

                    if (text.substring(before, after).matches("(\\W)*(Servidor|ahh)")) {
                        setCharacterAttributes(before, after - before, attrRed, false);
                    } else if (text.substring(before, after).matches("(\\W)*(" + System.getProperty("user.name") + "|ahh)")) {
                        setCharacterAttributes(before, after - before, attrBlue, false);
                    } else {
                        setCharacterAttributes(before, after - before, attrBlack, false);
                    }
                }
            };
            jTextPane1.setDocument(doc);
            jTextPane1.setText(g + a);
        } else if (aux == 1) {
            Thread.sleep(5000);
            System.exit(0);
        } else {
            aux = 1;
            Alerta f = new Alerta(3);
            f.setVisible(true);
            this.dispose();
        }
    }

    public void ejecutarConexion(String ip, int puerto) {
        Thread hilo = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    c.IniciarCliente(ip, puerto);
                    c.abrirFlujos();
                    c.enviar("///nick///:" + username);
                    Ordenar("Conexión establecida con el servidor");
                    while (true) {
                        Ordenar(c.recibirDatos());
                    }
                } catch (IOException ex) {
                    Logger.getLogger(F_Cliente.class.getName()).log(Level.SEVERE, null, ex);
                } catch (InterruptedException ex) {
                    Logger.getLogger(F_Cliente.class.getName()).log(Level.SEVERE, null, ex);
                } finally {
                    //cerrarConexion();
                }
            }
        });
        hilo.start();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel4 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextPane1 = new javax.swing.JTextPane();
        jTextField1 = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMaximumSize(new java.awt.Dimension(1280, 720));
        setMinimumSize(new java.awt.Dimension(1280, 720));
        setUndecorated(true);
        getContentPane().setLayout(null);

        jLabel4.setForeground(new java.awt.Color(0, 204, 0));
        jLabel4.setText("SERVIDOR");
        getContentPane().add(jLabel4);
        jLabel4.setBounds(5, 2, 290, 30);

        jLabel3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel3MouseClicked(evt);
            }
        });
        getContentPane().add(jLabel3);
        jLabel3.setBounds(1250, 4, 26, 26);

        jScrollPane1.setBackground(new java.awt.Color(55, 33, 52));
        jScrollPane1.setBorder(null);
        jScrollPane1.setForeground(new java.awt.Color(255, 255, 255));
        jScrollPane1.setOpaque(false);

        jTextPane1.setBackground(new java.awt.Color(55, 33, 52));
        jTextPane1.setBorder(null);
        jTextPane1.setForeground(new java.awt.Color(255, 255, 255));
        jScrollPane1.setViewportView(jTextPane1);

        getContentPane().add(jScrollPane1);
        jScrollPane1.setBounds(5, 40, 1270, 630);

        jTextField1.setFont(new java.awt.Font("Century Gothic", 1, 14)); // NOI18N
        jTextField1.setForeground(new java.awt.Color(255, 255, 255));
        jTextField1.setBorder(null);
        jTextField1.setCaretColor(new java.awt.Color(255, 255, 255));
        jTextField1.setOpaque(false);
        jTextField1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextField1KeyPressed(evt);
            }
        });
        getContentPane().add(jTextField1);
        jTextField1.setBounds(45, 685, 1230, 30);
        getContentPane().add(jLabel2);
        jLabel2.setBounds(5, 685, 1270, 30);
        getContentPane().add(jLabel1);
        jLabel1.setBounds(0, 0, 1280, 720);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTextField1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField1KeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            if (jTextField1.getText().equals("salir()")) {
                c.enviar(jTextField1.getText());
                System.exit(0);
            } else {
                jTextPane1.setText(jTextPane1.getText() + "\n[" + System.getProperty("user.name") + "] => " + jTextField1.getText());
                c.enviar(jTextField1.getText());
                jTextField1.setText("");
                jLabel2.setIcon(new ImageIcon("src/Img/Barra.gif"));
            }
        } else {
            jLabel2.setIcon(new ImageIcon("src/Img/Barra.png"));
        }    }//GEN-LAST:event_jTextField1KeyPressed

    private void jLabel3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel3MouseClicked
        System.exit(0);
    }//GEN-LAST:event_jLabel3MouseClicked

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) throws UnknownHostException {
        F_Cliente f = new F_Cliente("localhost", 2020);
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextPane jTextPane1;
    // End of variables declaration//GEN-END:variables
}
