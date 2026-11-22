/**
 * ResultadoPrueba.java
 */
package edu.iudigital.ea3.datos;

/**
 * este es el resultado estructurado de la prueba de conexion JDBC.
 */
public record ResultadoPrueba(boolean exitosa, String titulo, String detalle) {

    public static ResultadoPrueba exito(String detalle) {
        return new ResultadoPrueba(true, "Conexión exitosa", detalle);
    }

    public static ResultadoPrueba fallo(String titulo, String detalle) {
        return new ResultadoPrueba(false, titulo, detalle);
    }
}
