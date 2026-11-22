/**
 * Main.java
 */
package edu.iudigital.ea3;

import edu.iudigital.ea3.ui.VentanaPrincipal;

import javax.swing.*;

/**
 * este es el punto de entrada de la aplicacion.
 */
public class Main {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.out.println("Se usa la apariencia por defecto de JSwing");
        }
        SwingUtilities.invokeLater(() -> new VentanaPrincipal().setVisible(true));
    }
}
