/**
 * Estudiante.java
 */
package edu.iudigital.ea3.modelo;

/**
 * aqui representamos un registro de estudiante en la base de datos.
 */
public record Estudiante(int id, String nombre, String programa, String correo) {

    public static Estudiante nuevo(String nombre, String programa, String correo) {
        return new Estudiante(0, nombre, programa, correo);
    }

    public boolean esNuevo() {
        return id == 0;
    }
}
