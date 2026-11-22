/**
 * ExportadorCsv.java
 */
package edu.iudigital.ea3.util;

import edu.iudigital.ea3.modelo.Estudiante;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * aqui exportamos la lista de estudiantes a un archivo CSV codificado en UTF-8.
 */
public class ExportadorCsv {

    private static final String CABECERA = "id,nombre,programa,correo";

    public int exportar(List<Estudiante> estudiantes, Path destino) throws IOException {
        try (BufferedWriter escritor = Files.newBufferedWriter(destino, StandardCharsets.UTF_8)) {
            escritor.write(CABECERA);
            escritor.newLine();
            for (Estudiante estudiante : estudiantes) {
                escritor.write(linea(estudiante));
                escritor.newLine();
            }
        }
        return estudiantes.size();
    }

    private String linea(Estudiante estudiante) {
        return estudiante.id()
                + "," + proteger(estudiante.nombre())
                + "," + proteger(estudiante.programa())
                + "," + proteger(estudiante.correo());
    }

    private String proteger(String valor) {
        if (valor == null) {
            return "";
        }
        if (valor.contains(",") || valor.contains("\"") || valor.contains("\n") || valor.contains("\r")) {
            return "\"" + valor.replace("\"", "\"\"") + "\"";
        }
        return valor;
    }
}
