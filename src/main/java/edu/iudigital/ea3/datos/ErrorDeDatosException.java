/**
 * ErrorDeDatosException.java
 */
package edu.iudigital.ea3.datos;

/**
 * Excepcion de aplicacion para errores ocurridos en la capa de acceso a datos.
 */
public class ErrorDeDatosException extends Exception {

    public ErrorDeDatosException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
