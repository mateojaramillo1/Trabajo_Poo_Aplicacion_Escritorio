/**
 * ConfiguracionConexion.java
 */
package edu.iudigital.ea3.datos;

/**
 * Parametros de conexion JDBC suministrados por el usuario.
 */
public record ConfiguracionConexion(String url, String usuario, String contrasena) {

    public boolean esValida() {
        return url != null && !url.isBlank();
    }

    public String nombreDelDriver() {
        if (url == null) {
            return "";
        }
        if (url.startsWith("jdbc:sqlite:")) {
            return "org.sqlite.JDBC";
        }
        if (url.startsWith("jdbc:mysql:")) {
            return "com.mysql.cj.jdbc.Driver";
        }
        if (url.startsWith("jdbc:postgresql:")) {
            return "org.postgresql.Driver";
        }
        if (url.startsWith("jdbc:h2:")) {
            return "org.h2.Driver";
        }
        return "";
    }

    public String motor() {
        String driver = nombreDelDriver();
        return switch (driver) {
            case "org.sqlite.JDBC" -> "SQLite";
            case "com.mysql.cj.jdbc.Driver" -> "MySQL";
            case "org.postgresql.Driver" -> "PostgreSQL";
            case "org.h2.Driver" -> "H2";
            default -> "desconocido";
        };
    }
}
