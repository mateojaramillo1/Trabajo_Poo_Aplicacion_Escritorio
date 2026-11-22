/**
 * ModeloTablaEstudiantes.java
 */
package edu.iudigital.ea3.ui;

import edu.iudigital.ea3.modelo.Estudiante;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

/**
 * Modelo de tabla Swing para mostrar los registros de estudiantes.
 */
public class ModeloTablaEstudiantes extends AbstractTableModel {

    private static final String[] COLUMNAS = {"ID", "Nombre", "Programa", "Correo"};

    private final List<Estudiante> estudiantes = new ArrayList<>();

    public void reemplazar(List<Estudiante> nuevos) {
        estudiantes.clear();
        estudiantes.addAll(nuevos);
        fireTableDataChanged();
    }

    public Estudiante obtener(int fila) {
        return estudiantes.get(fila);
    }

    public List<Estudiante> obtenerTodos() {
        return List.copyOf(estudiantes);
    }

    @Override
    public int getRowCount() {
        return estudiantes.size();
    }

    @Override
    public int getColumnCount() {
        return COLUMNAS.length;
    }

    @Override
    public String getColumnName(int columna) {
        return COLUMNAS[columna];
    }

    @Override
    public Class<?> getColumnClass(int columna) {
        return columna == 0 ? Integer.class : String.class;
    }

    @Override
    public Object getValueAt(int fila, int columna) {
        Estudiante estudiante = estudiantes.get(fila);
        return switch (columna) {
            case 0 -> estudiante.id();
            case 1 -> estudiante.nombre();
            case 2 -> estudiante.programa();
            case 3 -> estudiante.correo();
            default -> "";
        };
    }
}
