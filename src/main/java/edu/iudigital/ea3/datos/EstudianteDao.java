/**
 * EstudianteDao.java
 */
package edu.iudigital.ea3.datos;

import edu.iudigital.ea3.modelo.Estudiante;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * esta es las clases para consultar e insertar registros de estudiantes en la base de datos.
 */
public class EstudianteDao {

    private static final String LISTAR =
            "SELECT id, nombre, programa, correo FROM estudiantes ORDER BY id";

    private static final String BUSCAR =
            "SELECT id, nombre, programa, correo FROM estudiantes "
                    + "WHERE LOWER(nombre) LIKE ? OR LOWER(programa) LIKE ? OR LOWER(correo) LIKE ? ORDER BY id";

    private static final String INSERTAR =
            "INSERT INTO estudiantes (nombre, programa, correo) VALUES (?, ?, ?)";

    private final ProbadorConexion probador;
    private final ConfiguracionConexion configuracion;

    public EstudianteDao(ProbadorConexion probador, ConfiguracionConexion configuracion) {
        this.probador = probador;
        this.configuracion = configuracion;
    }

    public List<Estudiante> listar() throws ErrorDeDatosException {
        try (Connection conexion = probador.abrir(configuracion);
             PreparedStatement sentencia = conexion.prepareStatement(LISTAR)) {
            return leerEstudiantes(sentencia);
        } catch (SQLException e) {
            throw new ErrorDeDatosException("No fue posible consultar los estudiantes. " + e.getMessage(), e);
        }
    }

    public List<Estudiante> buscar(String texto) throws ErrorDeDatosException {
        if (texto == null || texto.isBlank()) {
            return listar();
        }
        String patron = "%" + texto.toLowerCase() + "%";
        try (Connection conexion = probador.abrir(configuracion);
             PreparedStatement sentencia = conexion.prepareStatement(BUSCAR)) {
            sentencia.setString(1, patron);
            sentencia.setString(2, patron);
            sentencia.setString(3, patron);
            return leerEstudiantes(sentencia);
        } catch (SQLException e) {
            throw new ErrorDeDatosException("No fue posible buscar estudiantes. " + e.getMessage(), e);
        }
    }

    public Estudiante insertar(Estudiante estudiante) throws ErrorDeDatosException {
        try (Connection conexion = probador.abrir(configuracion);
             PreparedStatement sentencia = conexion.prepareStatement(INSERTAR, Statement.RETURN_GENERATED_KEYS)) {

            sentencia.setString(1, estudiante.nombre());
            sentencia.setString(2, estudiante.programa());
            sentencia.setString(3, estudiante.correo());
            sentencia.executeUpdate();

            try (ResultSet llaves = sentencia.getGeneratedKeys()) {
                int idGenerado = llaves.next() ? llaves.getInt(1) : 0;
                return new Estudiante(idGenerado, estudiante.nombre(), estudiante.programa(), estudiante.correo());
            }

        } catch (SQLException e) {
            throw new ErrorDeDatosException("No fue posible insertar el estudiante. " + e.getMessage(), e);
        }
    }

    private List<Estudiante> leerEstudiantes(PreparedStatement sentencia) throws SQLException {
        List<Estudiante> estudiantes = new ArrayList<>();
        try (ResultSet resultado = sentencia.executeQuery()) {
            while (resultado.next()) {
                estudiantes.add(construir(resultado));
            }
        }
        return estudiantes;
    }

    private Estudiante construir(ResultSet resultado) throws SQLException {
        return new Estudiante(
                resultado.getInt("id"),
                resultado.getString("nombre"),
                resultado.getString("programa"),
                resultado.getString("correo"));
    }
}
