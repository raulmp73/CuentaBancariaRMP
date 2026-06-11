package Persistencia;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import modelo.Cliente;
import modelo.CuentaBancaria;
import modelo.Operacion;

/**
 * Clase DAO encargada de acceder a los datos de las cuentas bancarias.
 * 
 * Se encarga de cargar las cuentas bancarias desde la base de datos
 * a partir de sus IDs, incluyendo también sus operaciones asociadas.
 * 
 * Corregido en la versión 0.2: eliminado el campo "ConexionBD BD" que provocaba
 * un NullPointerException; ahora se usa ConexionBD.getConnection() (estático).
 *
 * @author Raul
 * @version 0.2
 */
public class CuentaBancariaDAO {

    String comando;
    ArrayList<Cliente> listaCliente;

    // DAO para cargar las operaciones de cada cuenta
    OperacionesCuentaBancariaDAO operacionDAO = new OperacionesCuentaBancariaDAO();

    /**
     * Carga una lista de cuentas bancarias a partir de sus IDs.
     * 
     * Para cada ID recibido, consulta la base de datos y construye
     * un objeto CuentaBancaria con su saldo, IBAN, estado y operaciones.
     * 
     * @param IDSCuentaBancaria array de IDs de cuentas bancarias
     * @return lista de cuentas bancarias completas
     * 
     * @author Raul
     * @version 0.2
     */
    public ArrayList<CuentaBancaria> cargarCuentasBancarias(int[] IDSCuentaBancaria) {

        // Lista donde se guardarán las cuentas cargadas
        ArrayList<CuentaBancaria> lista = new ArrayList<>();

        CuentaBancaria cuenta;

        String sql = "SELECT numero_cuenta, saldo, iban, estado FROM cuenta_bancaria WHERE id_cuenta = ?";

        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            // Recorremos todos los IDs recibidos
            for (int id : IDSCuentaBancaria) {

                // Sustituimos el parámetro ? por el ID actual
                ps.setInt(1, id);

                try (ResultSet rs = ps.executeQuery()) {

                    while (rs.next()) {

                        // Creamos la cuenta bancaria con los datos obtenidos
                        cuenta = new CuentaBancaria(
                                id, // id_cuenta (necesario para ingresos/retiros)
                                rs.getDouble("saldo"),
                                rs.getString("iban"),
                                rs.getInt("estado") == 1, // true si estado = 1
                                operacionDAO.cargarOperaciones(id) // cargamos operaciones
                        );

                        lista.add(cuenta);
                    }

                } catch (Exception e) {
                    // TODO: Manejar error al procesar resultados
                }
            }

        } catch (Exception e) {
            // TODO: Manejar error de conexión o consulta
        }

        return lista;
    }

    /**
     * Realiza un ingreso de dinero en una cuenta bancaria.
     *
     * Actualiza el saldo de la cuenta y registra la operación de tipo INGRESO,
     * ambas dentro de una misma transacción: si algo falla, no se aplica ninguna
     * de las dos (rollback), de forma que el saldo y las operaciones nunca quedan
     * descuadrados.
     *
     * @param idCuenta id_cuenta de la cuenta donde se ingresa
     * @param cantidad importe a ingresar (debe ser positivo)
     * @param concepto texto descriptivo de la operación
     * @return true si el ingreso se ha guardado, false si falla
     *
     * @author Raul
     * @version 0.2
     */
    public boolean ingresar(int idCuenta, double cantidad, String concepto) {
        // Un ingreso SUMA al saldo (delta positivo) y registra una operación INGRESO
        return registrarMovimiento(idCuenta, cantidad, "INGRESO", concepto, cantidad);
    }

    /**
     * Realiza una retirada de dinero de una cuenta bancaria.
     *
     * Resta el importe del saldo y registra la operación de tipo RETIRO, ambas
     * dentro de una misma transacción. NO comprueba el saldo disponible: esa
     * validación se hace en la capa lógica ({@link logica.GestorCuentaBancaria}).
     *
     * @param idCuenta id_cuenta de la cuenta de la que se retira
     * @param cantidad importe a retirar (debe ser positivo)
     * @param concepto texto descriptivo de la operación
     * @return true si la retirada se ha guardado, false si falla
     *
     * @author Raul
     * @version 0.2
     */
    public boolean retirar(int idCuenta, double cantidad, String concepto) {
        // Una retirada RESTA del saldo (delta negativo) y registra una operación RETIRO
        return registrarMovimiento(idCuenta, -cantidad, "RETIRO", concepto, cantidad);
    }

    /**
     * Recarga desde la BD la lista de operaciones de una cuenta.
     *
     * Se usa tras un ingreso o retiro para que el array en memoria refleje
     * exactamente lo que hay en la base de datos (mismo id, misma fecha_hora y
     * mismo orden), en lugar de construir la operación a mano.
     *
     * @param idCuenta id_cuenta de la cuenta
     * @return lista de operaciones actualizada desde la BD
     */
    public ArrayList<Operacion> cargarOperaciones(int idCuenta) {
        return operacionDAO.cargarOperaciones(idCuenta);
    }

    /**
     * Aplica un movimiento de saldo y registra su operación asociada, ambas cosas
     * dentro de una misma transacción: si algo falla, no se aplica ninguna de las
     * dos (rollback), de forma que el saldo y las operaciones nunca quedan
     * descuadrados.
     *
     * @param idCuenta   id_cuenta afectada
     * @param deltaSaldo cantidad a sumar al saldo (positiva en ingresos, negativa en retiradas)
     * @param tipo       tipo de operación a registrar ('INGRESO', 'RETIRO', ...)
     * @param concepto   texto descriptivo de la operación
     * @param importe    importe de la operación (siempre positivo)
     * @return true si el movimiento se ha confirmado, false si se ha deshecho
     */
    private boolean registrarMovimiento(int idCuenta, double deltaSaldo, String tipo,
                                        String concepto, double importe) {

        String updateSaldo = "UPDATE cuenta_bancaria SET saldo = saldo + ? WHERE id_cuenta = ?";

        String insertOperacion = "INSERT INTO operacion "
                + "(id_cuenta_operacion, id_cuenta_destino, tipo, concepto, fecha_hora, importe) "
                + "VALUES (?, NULL, ?, ?, NOW(), ?)";

        try (Connection con = ConexionBD.getConnection()) {

            // Desactivamos el autocommit para controlar la transacción a mano
            con.setAutoCommit(false);

            try (PreparedStatement psSaldo = con.prepareStatement(updateSaldo);
                 PreparedStatement psOperacion = con.prepareStatement(insertOperacion)) {

                // 1) Aplicamos el cambio de saldo (positivo si ingreso, negativo si retiro)
                psSaldo.setDouble(1, deltaSaldo);
                psSaldo.setInt(2, idCuenta);
                psSaldo.executeUpdate();

                // 2) Registramos la operación
                psOperacion.setInt(1, idCuenta);
                psOperacion.setString(2, tipo);
                psOperacion.setString(3, concepto);
                psOperacion.setDouble(4, importe);
                psOperacion.executeUpdate();

                // Si las dos consultas han ido bien, confirmamos los cambios
                con.commit();
                return true;

            } catch (Exception e) {
                // Algo ha fallado: deshacemos el movimiento completo
                con.rollback();
                System.out.println("Error en el movimiento, cambios deshechos: " + e.getMessage());
                return false;
            }

        } catch (Exception e) {
            System.out.println("Error de conexión: " + e.getMessage());
            return false;
        }
    }
}