/**
 * ProbadorConexion.java
 */
package edu.iudigital.ea3.datos;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * esta es la que encapsula la carga del driver, la apertura segura y la validacion de conexiones JDBC.
 */
public class ProbadorConexion {

    public ResultadoPrueba probar(ConfiguracionConexion configuracion) {
        if (configuracion == null || !configuracion.esValida()) {
            return ResultadoPrueba.fallo("Fallo de conexión",
                    "La URL de conexión no puede quedar vacía. Ejemplo: jdbc:sqlite:estudiantes.db");
        }

        String driver = configuracion.nombreDelDriver();
        if (!driver.isEmpty()) {
            try {
                Class.forName(driver);
            } catch (ClassNotFoundException e) {
                return ResultadoPrueba.fallo("Fallo del driver",
                        "No se encontró la clase del driver " + driver + " en el classpath del proyecto."
                                + System.lineSeparator()
                                + "Motor: " + configuracion.motor()
                                + System.lineSeparator()
                                + "Error técnico: " + e.getMessage());
            }
        }

        long inicio = System.currentTimeMillis();
        try (Connection conexion = abrir(configuracion)) {
            long duracion = System.currentTimeMillis() - inicio;
            DatabaseMetaData metadatos = conexion.getMetaData();
            String usuarioMostrado = (configuracion.usuario() == null || configuracion.usuario().isBlank())
                    ? "sin usuario"
                    : configuracion.usuario();
            String detalle = "Motor: " + metadatos.getDatabaseProductName()
                    + " " + metadatos.getDatabaseProductVersion()
                    + System.lineSeparator()
                    + "Driver: " + metadatos.getDriverName()
                    + " " + metadatos.getDriverVersion()
                    + System.lineSeparator()
                    + "URL: " + configuracion.url()
                    + System.lineSeparator()
                    + "Usuario: " + usuarioMostrado
                    + System.lineSeparator()
                    + "Tiempo de respuesta: " + duracion + " ms";
            return ResultadoPrueba.exito(detalle);
        } catch (SQLException e) {
            return ResultadoPrueba.fallo("Fallo de conexión",
                    "No fue posible conectar con la base de datos."
                            + System.lineSeparator()
                            + "Error técnico: " + e.getMessage()
                            + System.lineSeparator()
                            + "Código de error: " + e.getErrorCode()
                            + System.lineSeparator()
                            + "Estado SQL: " + e.getSQLState());
        }
    }

    public Connection abrir(ConfiguracionConexion configuracion) throws SQLException {
        if (configuracion.usuario() == null || configuracion.usuario().isBlank()) {
            return DriverManager.getConnection(configuracion.url());
        }
        return DriverManager.getConnection(configuracion.url(), configuracion.usuario(), configuracion.contrasena());
    }
}
