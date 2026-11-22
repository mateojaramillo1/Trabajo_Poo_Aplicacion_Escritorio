/**
 * PanelEstudiantes.java
 */
package edu.iudigital.ea3.ui;

import edu.iudigital.ea3.datos.ConfiguracionConexion;
import edu.iudigital.ea3.datos.ErrorDeDatosException;
import edu.iudigital.ea3.datos.EstudianteDao;
import edu.iudigital.ea3.datos.ProbadorConexion;
import edu.iudigital.ea3.modelo.Estudiante;
import edu.iudigital.ea3.util.ExportadorCsv;

import javax.swing.*;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

/**
 * aqui vemos el panel para consultar, insertar estudiantes y exportar la tabla a CSV.
 */
public class PanelEstudiantes extends JPanel {

    private final ModeloTablaEstudiantes modelo = new ModeloTablaEstudiantes();
    private final JTable tabla = new JTable(modelo);
    private final JTextField campoBusqueda = new JTextField(18);
    private final JLabel etiquetaEstado = new JLabel("Sin conexión activa", SwingConstants.LEFT);
    private final JButton botonConsultar = new JButton("Consultar");
    private final JButton botonInsertar = new JButton("Insertar");
    private final JButton botonExportar = new JButton("Exportar CSV");

    private final ProbadorConexion probador = new ProbadorConexion();
    private final ExportadorCsv exportador = new ExportadorCsv();

    private EstudianteDao dao;

    public PanelEstudiantes() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(14, 14, 14, 14));
        add(construirBarraSuperior(), BorderLayout.NORTH);
        add(construirTabla(), BorderLayout.CENTER);
        add(construirBarraInferior(), BorderLayout.SOUTH);
        conectarEventos();
        habilitarAcciones(false);
    }

    public void activar(ConfiguracionConexion configuracion) {
        this.dao = new EstudianteDao(probador, configuracion);
        habilitarAcciones(true);
        consultar();
    }

    private JPanel construirBarraSuperior() {
        JPanel barra = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        barra.add(new JLabel("Buscar por nombre, programa o correo"));
        barra.add(campoBusqueda);
        barra.add(botonConsultar);
        barra.add(botonInsertar);
        barra.add(botonExportar);
        return barra;
    }

    private JScrollPane construirTabla() {
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.setRowHeight(24);
        tabla.setAutoCreateRowSorter(false);
        tabla.setRowSorter(new TableRowSorter<>(modelo));
        tabla.getTableHeader().setFont(tabla.getTableHeader().getFont().deriveFont(Font.BOLD));
        JScrollPane panel = new JScrollPane(tabla);
        panel.setBorder(BorderFactory.createTitledBorder("Tabla estudiantes"));
        return panel;
    }

    private JPanel construirBarraInferior() {
        etiquetaEstado.setFont(etiquetaEstado.getFont().deriveFont(Font.BOLD, 14f));
        JPanel barra = new JPanel(new BorderLayout());
        barra.setBorder(BorderFactory.createEmptyBorder(6, 4, 0, 4));
        barra.add(etiquetaEstado, BorderLayout.WEST);
        return barra;
    }

    private void conectarEventos() {
        botonConsultar.addActionListener(e -> consultar());
        botonInsertar.addActionListener(e -> insertar());
        botonExportar.addActionListener(e -> exportar());
        campoBusqueda.addActionListener(e -> consultar());
    }

    private void habilitarAcciones(boolean estado) {
        botonConsultar.setEnabled(estado);
        botonInsertar.setEnabled(estado);
        botonExportar.setEnabled(estado);
        campoBusqueda.setEnabled(estado);
    }

    private void consultar() {
        if (dao == null) {
            return;
        }
        try {
            List<Estudiante> estudiantes = dao.buscar(campoBusqueda.getText().trim());
            modelo.reemplazar(estudiantes);
            actualizarEstado();
        } catch (ErrorDeDatosException e) {
            informarError(e);
        }
    }

    private void insertar() {
        DialogoEstudiante dialogo = new DialogoEstudiante(ventana());
        dialogo.setVisible(true);
        Estudiante nuevo = dialogo.getResultado();
        if (nuevo == null) {
            return;
        }
        try {
            Estudiante guardado = dao.insertar(nuevo);
            consultar();
            etiquetaEstado.setText("Estudiante insertado con id " + guardado.id()
                    + ". " + resumen());
        } catch (ErrorDeDatosException e) {
            informarError(e);
        }
    }

    private void exportar() {
        JFileChooser selector = new JFileChooser();
        selector.setSelectedFile(new File("estudiantes.csv"));
        if (selector.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) {
            return;
        }
        Path destino = selector.getSelectedFile().toPath();
        try {
            int filas = exportador.exportar(modelo.obtenerTodos(), destino);
            JOptionPane.showMessageDialog(this,
                    "Se exportaron " + filas + " registros a " + destino,
                    "Exportación completa", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this,
                    "No fue posible escribir el archivo. " + e.getMessage(),
                    "Error de escritura", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void actualizarEstado() {
        etiquetaEstado.setText(resumen());
    }

    private String resumen() {
        return "Registros visibles: " + modelo.getRowCount();
    }

    private void informarError(ErrorDeDatosException e) {
        etiquetaEstado.setText("Ocurrió un error con la base de datos.");
        JOptionPane.showMessageDialog(this, e.getMessage(),
                "Error de base de datos", JOptionPane.ERROR_MESSAGE);
    }

    private Frame ventana() {
        return (Frame) SwingUtilities.getWindowAncestor(this);
    }
}
