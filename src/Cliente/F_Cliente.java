/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Cliente;

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
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import static Cliente.Cliente.Usuario;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.SimpleAttributeSet;

/**
 *
 * @author carlo
 */
public class F_Cliente extends javax.swing.JFrame {

    Cliente c = new Cliente();
    Font FP;
    int aux = 0;

    /**
     * Creates new form F_Cliente
     */
    //Cliente c;
    public F_Cliente(String ip, int port) throws UnknownHostException {
        initComponents();
        jScrollPane1.getViewport().setOpaque(false);
        cpu_icon.setIcon(new ImageIcon("src/Img/Cpu_icon.png"));
        ram_icon.setIcon(new ImageIcon("src/Img/Ram_icon.png"));
        hdd_icon.setIcon(new ImageIcon("src/Img/Hdd_icon.png"));
        FP = Fuentes.setFuente("/Img/ubuntu.ttf", 16f);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
        jTextPane1.setFont(FP);
        jLabel4.setFont(FP);
        jLabel4.setText("[" + System.getProperty("user.name") + "@" + InetAddress.getLocalHost().getHostAddress() + ":" + port + "]");
        jLabel1.setIcon(new ImageIcon("src/Img/consola.png"));
        jLabel2.setIcon(new ImageIcon("src/Img/Barra.gif"));
        jLabel3.setIcon(new ImageIcon("src/Img/Btn_close.png"));
        jLabel5.setIcon(new ImageIcon("src/Img/Ren_icon.png"));
        ejecutarConexion(ip, port);
    }

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
        if (a.substring(0, 5).equals("[Rec]")) {
            String rec = a.substring(5, a.length());
            String[] aux = rec.split(":");
            cpu.setText(aux[0]);
            cpubar.setValue(Integer.parseInt(aux[0].replace("%", "")));
            hdd.setText(aux[1]);
            hddbar.setValue(Integer.parseInt(aux[1].replace("%", "")));
            ram.setText(aux[2]);
            rambar.setValue(Integer.parseInt(aux[2].replace("%", "")));
        } else if (aux == 1) {
            Thread.sleep(5000);
            System.exit(0);
        } else if (a != null && aux == 0) {
            jLabel5.setIcon(new ImageIcon("src/Img/Ren_icon.png"));
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
                    c.AbrirDatos();
                    c.EnviarDatos("///nick///:" + Usuario);
                    Ordenar("Conexión establecida con el servidor");
                    while (true) {
                        Ordenar(c.RecibirDatos());
                    }
                } catch (Exception ex) {
                    System.out.println(ex);
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
        hdd = new javax.swing.JLabel();
        ram = new javax.swing.JLabel();
        cpu = new javax.swing.JLabel();
        hdd_icon = new javax.swing.JLabel();
        ram_icon = new javax.swing.JLabel();
        cpu_icon = new javax.swing.JLabel();
        hddbar = new rojerusan.componentes.RSProgressCircleAnimated();
        rambar = new rojerusan.componentes.RSProgressCircleAnimated();
        cpubar = new rojerusan.componentes.RSProgressCircleAnimated();
        jLabel5 = new javax.swing.JLabel();
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

        jTextPane1.setEditable(false);
        jTextPane1.setBackground(new java.awt.Color(55, 33, 52));
        jTextPane1.setBorder(null);
        jTextPane1.setForeground(new java.awt.Color(255, 255, 255));
        jTextPane1.setOpaque(false);
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

        hdd.setFont(new java.awt.Font("Century Gothic", 1, 24)); // NOI18N
        hdd.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        hdd.setText("0");
        getContentPane().add(hdd);
        hdd.setBounds(1180, 480, 70, 30);

        ram.setFont(new java.awt.Font("Century Gothic", 1, 24)); // NOI18N
        ram.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        ram.setText("0");
        getContentPane().add(ram);
        ram.setBounds(1180, 340, 70, 30);

        cpu.setFont(new java.awt.Font("Century Gothic", 1, 24)); // NOI18N
        cpu.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        cpu.setText("0");
        getContentPane().add(cpu);
        cpu.setBounds(1180, 200, 70, 30);

        hdd_icon.setBackground(new java.awt.Color(38, 34, 33));
        hdd_icon.setFont(new java.awt.Font("Century Gothic", 1, 12)); // NOI18N
        hdd_icon.setForeground(new java.awt.Color(255, 255, 255));
        getContentPane().add(hdd_icon);
        hdd_icon.setBounds(1160, 420, 110, 110);

        ram_icon.setBackground(new java.awt.Color(38, 34, 33));
        ram_icon.setFont(new java.awt.Font("Century Gothic", 1, 12)); // NOI18N
        ram_icon.setForeground(new java.awt.Color(255, 255, 255));
        getContentPane().add(ram_icon);
        ram_icon.setBounds(1160, 280, 110, 110);

        cpu_icon.setBackground(new java.awt.Color(38, 34, 33));
        cpu_icon.setFont(new java.awt.Font("Century Gothic", 1, 12)); // NOI18N
        cpu_icon.setForeground(new java.awt.Color(255, 255, 255));
        getContentPane().add(cpu_icon);
        cpu_icon.setBounds(1160, 140, 110, 110);

        hddbar.setForeground(new java.awt.Color(255, 255, 255));
        hddbar.setValue(50);
        hddbar.setAnimated(false);
        hddbar.setString("");
        hddbar.setStringPainted(false);
        getContentPane().add(hddbar);
        hddbar.setBounds(1160, 420, 110, 110);

        rambar.setForeground(new java.awt.Color(255, 255, 255));
        rambar.setValue(50);
        rambar.setAnimated(false);
        rambar.setString("");
        rambar.setStringPainted(false);
        getContentPane().add(rambar);
        rambar.setBounds(1160, 280, 110, 110);

        cpubar.setForeground(new java.awt.Color(255, 255, 255));
        cpubar.setValue(50);
        cpubar.setAnimated(false);
        cpubar.setString("");
        cpubar.setStringPainted(false);
        getContentPane().add(cpubar);
        cpubar.setBounds(1160, 140, 110, 110);
        getContentPane().add(jLabel5);
        jLabel5.setBounds(1210, 60, 71, 50);
        getContentPane().add(jLabel2);
        jLabel2.setBounds(5, 685, 1270, 30);
        getContentPane().add(jLabel1);
        jLabel1.setBounds(0, 0, 1280, 720);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTextField1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField1KeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            if (jTextField1.getText().equals("salir()")) {
                try {
                    c.EnviarDatos(jTextField1.getText());
                } catch (IOException ex) {
                    Logger.getLogger(F_Cliente.class.getName()).log(Level.SEVERE, null, ex);
                }
                System.exit(0);
            } else {
                jTextPane1.setText(jTextPane1.getText() + "\n[" + System.getProperty("user.name") + "] => " + jTextField1.getText());
                try {
                    c.EnviarDatos(jTextField1.getText());
                } catch (IOException ex) {
                    Logger.getLogger(F_Cliente.class.getName()).log(Level.SEVERE, null, ex);
                }
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
    private static javax.swing.JLabel cpu;
    private javax.swing.JLabel cpu_icon;
    private rojerusan.componentes.RSProgressCircleAnimated cpubar;
    private static javax.swing.JLabel hdd;
    private javax.swing.JLabel hdd_icon;
    private rojerusan.componentes.RSProgressCircleAnimated hddbar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextPane jTextPane1;
    private static javax.swing.JLabel ram;
    private javax.swing.JLabel ram_icon;
    private rojerusan.componentes.RSProgressCircleAnimated rambar;
    // End of variables declaration//GEN-END:variables
}
