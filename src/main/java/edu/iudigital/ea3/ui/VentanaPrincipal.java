/**
 * VentanaPrincipal.java
 */
package edu.iudigital.ea3.ui;

import javax.swing.*;
import java.awt.*;

/**
 * esta es la ventana principal que integra la prueba de conexion y la consulta/insercion de estudiantes.
 */
public class VentanaPrincipal extends JFrame {

    private final JTabbedPane pestanas = new JTabbedPane();
    private final PanelEstudiantes panelEstudiantes = new PanelEstudiantes();

    public VentanaPrincipal() {
        super("Estudiantes IU Digital. Conexión a base de datos, consulta e inserción");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        PanelConexion panelConexion = new PanelConexion(configuracion -> {
            panelEstudiantes.activar(configuracion);
            pestanas.setEnabledAt(1, true);
        });

        pestanas.addTab("Conexión", panelConexion);
        pestanas.addTab("Estudiantes", panelEstudiantes);
        pestanas.setEnabledAt(1, false);

        add(pestanas, BorderLayout.CENTER);
        setSize(920, 580);
        setLocationRelativeTo(null);
    }
}
