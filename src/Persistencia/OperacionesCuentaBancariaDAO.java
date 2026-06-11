package Persistencia;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import modelo.Operacion;
import modelo.TipoOperacion;

/**
 * Clase DAO encargada de gestionar las operaciones de las cuentas bancarias.
 * 
 * Permite cargar todas las operaciones asociadas a una cuenta bancaria
 * desde la base de datos.
 * 
 * Corregido en la versión 0.2: unificado el acceso a la conexión usando
 * ConexionBD.getConnection() (estático), eliminando el campo "ConexionBD BD".
 *
 * @author Raul
 * @version 0.2
 */
public class OperacionesCuentaBancariaDAO {

    String comando;

    /**
     * Carga todas las operaciones asociadas a una cuenta bancaria.
     * 
     * Consulta la tabla "operacion" y construye una lista de objetos Operacion
     * con los datos obtenidos.
     * 
     * @param idCuenta ID de la cuenta bancaria
     * @return lista de operaciones de la cuenta
     * 
     * @author Raul
     * @version 0.2
     */
    public ArrayList<Operacion> cargarOperaciones(int idCuenta) {

        // Lista donde se almacenarán las operaciones
        ArrayList<Operacion> operaciones = new ArrayList<>();

        String sql = "SELECT id_operacion, id_cuenta_operacion, id_cuenta_destino, tipo, concepto, fecha_hora, importe FROM operacion WHERE id_cuenta_operacion = ? or id_cuenta_destino = ? ORDER BY fecha_hora";

        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            // Sustituimos el parámetro ? por el ID de la cuenta
            ps.setInt(1, idCuenta);
            ps.setInt(2, idCuenta);

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {

                    // Puede ser null si no hay cuenta destino (ej: ingreso)
                    Integer idCuentaDestino = rs.getObject("id_cuenta_destino") == null
                            ? null
                            : rs.getInt("id_cuenta_destino");

                    // Creamos la operación con los datos de la BD
                    Operacion operacion = new Operacion(
                            rs.getInt("id_operacion"),
                            rs.getInt("id_cuenta_operacion"),
                            idCuentaDestino,
                            TipoOperacion.valueOf(rs.getString("tipo")),
                            rs.getString("concepto"),
                            rs.getTimestamp("fecha_hora"),
                            rs.getDouble("importe")
                    );

                    operaciones.add(operacion);
                }

            } catch (Exception e) {
                System.out.println(e);
            }

        } catch (Exception e) {
            System.out.println(e);
        }

        return operaciones;
    }
}