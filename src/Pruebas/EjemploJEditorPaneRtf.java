/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Pruebas;

/**
 *
 * @author carlo
 */
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.WindowConstants;

/**
 * Ejemplo de JEditorPane con RTF
 *
 * @author Chuidiang
 *
 */
public class EjemploJEditorPaneRtf {

    /**
     * Crea un nuevo objeto EjemploJEditorPaneRtf.
     */
    public EjemploJEditorPaneRtf() {
        try {
            // Preparación de la ventana
            JFrame v = new JFrame("JEditorPane con RTF");
            JEditorPane editor = new JEditorPane();
            JScrollPane scroll = new JScrollPane(editor);
            v.getContentPane().add(scroll);
            v.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

            // Se le dice al editor que va a usar RTF
            editor.setContentType("text/rtf");
            String a="\\cf1 ssss\\cf0";
            // Un texto en RTF
            editor.setText(
                    "{\\rtf1" + "{\\colortbl ;\\red255\\green0\\blue0;} Esto\\par es "+a+"una \\b prueba\\b0  de \\i cursiva\\i0\\par y \\cf1 todo\\cf0 \\par }");

            // Se visualiza
            v.pack();
            v.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param args DOCUMENT ME!
     */
    public static void main(String[] args) {
        new EjemploJEditorPaneRtf();
    }
}
