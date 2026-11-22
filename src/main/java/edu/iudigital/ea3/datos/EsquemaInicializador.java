/**
 * EsquemaInicializador.java
 */
package edu.iudigital.ea3.datos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * creamos la tabla estudiantes si no existe y carga registros iniciales cuando esta vacia.
 */
public class EsquemaInicializador {

    private static final String CREAR_TABLA = """
            CREATE TABLE IF NOT EXISTS estudiantes (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nombre TEXT NOT NULL,
                programa TEXT NOT NULL,
                correo TEXT NOT NULL
            )
            """;

    private static final String CONTAR = "SELECT COUNT(*) FROM estudiantes";

    private static final String INSERTAR =
            "INSERT INTO estudiantes (nombre, programa, correo) VALUES (?, ?, ?)";

    private static final String[][] SEMILLA = {
            {"Irene Pimienta", "Tecnologia en Desarrollo de Software", "irene.pimienta@iudigital.edu.co"},
            {"Mateo Jaramillo", "Tecnologia en Desarrollo de Software", "mateo.jaramillo@iudigital.edu.co"},
            {"Tomas Jaramillo", "Ingenieria de Software y Datos", "tomas.jaramillo@iudigital.edu.co"},
            {"Adiela Suarez", "Ingenieria de Software y Datos", "adiela.suarez@iudigital.edu.co"},
            {"Fredy Jaramillo", "Tecnologia en Desarrollo de Software", "fredy.jaramillo@iudigital.edu.co"}
    };

    private final ProbadorConexion probador;

    public EsquemaInicializador(ProbadorConexion probador) {
        this.probador = probador;
    }

    public String preparar(ConfiguracionConexion configuracion) throws ErrorDeDatosException {
        try (Connection conexion = probador.abrir(configuracion);
             Statement sentencia = conexion.createStatement()) {

            sentencia.execute(CREAR_TABLA);

            int existentes = contarFilas(conexion);
            if (existentes > 0) {
                return "La tabla estudiantes ya existía con " + existentes + " registros.";
            }

            insertarSemilla(conexion);
            return "Se creó la tabla estudiantes y se cargaron " + SEMILLA.length + " registros de ejemplo.";

        } catch (SQLException e) {
            throw new ErrorDeDatosException("No fue posible preparar el esquema. " + e.getMessage(), e);
        }
    }

    private int contarFilas(Connection conexion) throws SQLException {
        try (PreparedStatement sentencia = conexion.prepareStatement(CONTAR);
             ResultSet resultado = sentencia.executeQuery()) {
            return resultado.next() ? resultado.getInt(1) : 0;
        }
    }

    private void insertarSemilla(Connection conexion) throws SQLException {
        try (PreparedStatement sentencia = conexion.prepareStatement(INSERTAR)) {
            for (String[] fila : SEMILLA) {
                sentencia.setString(1, fila[0]);
                sentencia.setString(2, fila[1]);
                sentencia.setString(3, fila[2]);
                sentencia.addBatch();
            }
            sentencia.executeBatch();
        }
    }
}
