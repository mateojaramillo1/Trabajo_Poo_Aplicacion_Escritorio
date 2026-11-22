/**
 * DialogoEstudiante.java
 */
package edu.iudigital.ea3.ui;

import edu.iudigital.ea3.modelo.Estudiante;

import javax.swing.*;
import java.awt.*;

/**
 * Dialogo  para capturar los datos de un nuevo estudiante antes de insertarlo.
 */
public class DialogoEstudiante extends JDialog {

    private final JTextField campoNombre = new JTextField();
    private final JTextField campoPrograma = new JTextField();
    private final JTextField campoCorreo = new JTextField();

    private Estudiante resultado;

    public DialogoEstudiante(Frame padre) {
        super(padre, "Insertar estudiante", true);

        JPanel formulario = new JPanel(new GridLayout(3, 2, 10, 10));
        formulario.setBorder(BorderFactory.createEmptyBorder(16, 16, 10, 16));
        formulario.add(new JLabel("Nombre", SwingConstants.RIGHT));
        formulario.add(campoNombre);
        formulario.add(new JLabel("Programa", SwingConstants.RIGHT));
        formulario.add(campoPrograma);
        formulario.add(new JLabel("Correo", SwingConstants.RIGHT));
        formulario.add(campoCorreo);

        JButton botonGuardar = new JButton("Guardar");
        JButton botonCancelar = new JButton("Cancelar");
        botonGuardar.addActionListener(e -> validarYCerrar());
        botonCancelar.addActionListener(e -> dispose());

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        botones.add(botonCancelar);
        botones.add(botonGuardar);

        setLayout(new BorderLayout());
        add(formulario, BorderLayout.CENTER);
        add(botones, BorderLayout.SOUTH);
        getRootPane().setDefaultButton(botonGuardar);
        setSize(440, 210);
        setLocationRelativeTo(padre);
    }

    public Estudiante getResultado() {
        return resultado;
    }

    private void validarYCerrar() {
        String nombre = campoNombre.getText().trim();
        String programa = campoPrograma.getText().trim();
        String correo = campoCorreo.getText().trim();

        if (nombre.isEmpty() || programa.isEmpty() || correo.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Todos los campos (nombre, programa y correo) son obligatorios.",
                    "Datos incompletos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!correo.contains("@") || correo.contains(" ")) {
            JOptionPane.showMessageDialog(this,
                    "Ingrese un correo válido (debe contener @ y no tener espacios).",
                    "Formato inválido", JOptionPane.WARNING_MESSAGE);
            return;
        }

        resultado = Estudiante.nuevo(nombre, programa, correo);
        dispose();
    }
}
