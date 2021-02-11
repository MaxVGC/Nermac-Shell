/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Frames;

import Main.Main;
import Main.Sistem;
import com.jcraft.jsch.JSchException;
import java.awt.Color;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JTextField;

/**
 *
 * @author carlo
 */
public class Home extends javax.swing.JFrame implements Runnable {

    Main main;
    static Sistem s = new Sistem();
    Thread hilo;

    /**
     * Creates new form Home
     */
    public Home(Main p) throws InterruptedException {

        initComponents();
        this.setLocationRelativeTo(null);
        this.getContentPane().setBackground(new Color(38, 34, 33));
        main = p;
        jTextField1.setHorizontalAlignment(JTextField.LEFT);
        jLabel1.setIcon(new ImageIcon("src/Img/Send.png"));
        jLabel2.setIcon(new ImageIcon("src/Img/Cpu_icon.png"));
        jLabel3.setIcon(new ImageIcon("src/Img/Ram_icon.png"));
        jLabel4.setIcon(new ImageIcon("src/Img/Hdd_icon.png"));
        cpu.setText(s.Cpu());
        ram.setText(s.Ram());
        hdd.setText(s.Disk());
    }

    public void Refresh() throws InterruptedException {

        cpu.setText(s.Cpu());
        ram.setText(s.Ram());
        hdd.setText(s.Disk());
        Thread.sleep(1000); //Tarea que consume diez segundos.

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jTextField1 = new jtextfieldround.JTextFieldRound();
        hdd = new javax.swing.JLabel();
        ram = new javax.swing.JLabel();
        cpu = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMaximumSize(new java.awt.Dimension(640, 720));
        setMinimumSize(new java.awt.Dimension(640, 720));
        setUndecorated(true);
        setPreferredSize(new java.awt.Dimension(640, 720));
        getContentPane().setLayout(null);

        jTextArea1.setEditable(false);
        jTextArea1.setBackground(new java.awt.Color(255, 255, 255));
        jTextArea1.setColumns(20);
        jTextArea1.setFont(new java.awt.Font("Century Gothic", 0, 12)); // NOI18N
        jTextArea1.setRows(5);
        jTextArea1.setCaretColor(new java.awt.Color(0, 0, 0));
        jScrollPane1.setViewportView(jTextArea1);

        getContentPane().add(jScrollPane1);
        jScrollPane1.setBounds(10, 10, 500, 640);

        jTextField1.setText("    ");
        jTextField1.setMargin(new java.awt.Insets(0, 200, 0, 0));
        getContentPane().add(jTextField1);
        jTextField1.setBounds(10, 670, 500, 30);

        hdd.setFont(new java.awt.Font("Century Gothic", 1, 24)); // NOI18N
        hdd.setForeground(new java.awt.Color(0, 0, 0));
        hdd.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        hdd.setText("0");
        getContentPane().add(hdd);
        hdd.setBounds(540, 350, 70, 30);

        ram.setFont(new java.awt.Font("Century Gothic", 1, 24)); // NOI18N
        ram.setForeground(new java.awt.Color(0, 0, 0));
        ram.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        ram.setText("0");
        getContentPane().add(ram);
        ram.setBounds(540, 210, 70, 30);

        cpu.setFont(new java.awt.Font("Century Gothic", 1, 24)); // NOI18N
        cpu.setForeground(new java.awt.Color(0, 0, 0));
        cpu.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        cpu.setText("0");
        getContentPane().add(cpu);
        cpu.setBounds(540, 70, 70, 30);

        jLabel4.setBackground(new java.awt.Color(38, 34, 33));
        jLabel4.setFont(new java.awt.Font("Century Gothic", 1, 12)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setOpaque(true);
        getContentPane().add(jLabel4);
        jLabel4.setBounds(520, 290, 110, 110);

        jLabel3.setBackground(new java.awt.Color(38, 34, 33));
        jLabel3.setFont(new java.awt.Font("Century Gothic", 1, 12)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setOpaque(true);
        getContentPane().add(jLabel3);
        jLabel3.setBounds(520, 150, 110, 110);

        jLabel2.setBackground(new java.awt.Color(38, 34, 33));
        jLabel2.setFont(new java.awt.Font("Century Gothic", 1, 12)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setOpaque(true);
        getContentPane().add(jLabel2);
        jLabel2.setBounds(520, 10, 110, 110);

        jLabel1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel1MouseClicked(evt);
            }
        });
        getContentPane().add(jLabel1);
        jLabel1.setBounds(530, 610, 90, 90);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jLabel1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel1MouseClicked
        try {
            if (!jTextField1.getText().contains("exit")) {
                String t = main.execute(jTextField1.getText());
                jTextField1.setText("    ");
                jTextArea1.setText(t);
                hilo = new Thread(this);
                hilo.start();
            } else {
                System.exit(0);
            }
            
        } catch (JSchException ex) {
            Logger.getLogger(Home.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Home.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jLabel1MouseClicked

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {

    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private static javax.swing.JLabel cpu;
    private static javax.swing.JLabel hdd;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea jTextArea1;
    private jtextfieldround.JTextFieldRound jTextField1;
    private static javax.swing.JLabel ram;
    // End of variables declaration//GEN-END:variables

    @Override
    public void run() {
        Thread ct = Thread.currentThread();
        while (ct == hilo) {
            try {
                Refresh();
            } catch (InterruptedException ex) {
                Logger.getLogger(Home.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
