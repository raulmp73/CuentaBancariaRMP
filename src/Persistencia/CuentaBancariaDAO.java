package Persistencia;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
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
     * Realiza un pago con tarjeta desde una cuenta bancaria.
     *
     * Funciona igual que una retirada (resta el importe del saldo), pero registra
     * la operación como PAGO_TARJETA y sin cuenta destino, porque el dinero sale
     * hacia un comercio, no hacia otra cuenta del banco. Saldo y operación se
     * guardan dentro de la misma transacción.
     *
     * @param idCuenta id_cuenta desde la que se paga
     * @param cantidad importe del pago (debe ser positivo)
     * @param concepto comercio o descripción del pago
     * @return true si el pago se ha guardado, false si falla
     *
     * @author Raul
     * @version 0.2
     */
    public boolean pagarConTarjeta(int idCuenta, double cantidad, String concepto) {
        // Un pago con tarjeta RESTA del saldo (delta negativo) y registra PAGO_TARJETA
        return registrarMovimiento(idCuenta, -cantidad, "PAGO_TARJETA", concepto, cantidad);
    }

    /**
     * Envía dinero de una cuenta a otra cuenta REAL del banco (transferencia o
     * Bizum).
     *
     * A diferencia de un ingreso o un retiro, aquí se tocan DOS cuentas: se resta
     * el importe del origen y se suma al destino, y se registra una única operación
     * con su id_cuenta_destino. Las tres acciones van dentro de una misma
     * transacción: si algo falla, se deshace todo (rollback), de forma que el
     * dinero nunca "desaparece" ni se duplica.
     *
     * El tipo ('TRANSFERENCIA' o 'BIZUM') se recibe por parámetro, de modo que este
     * mismo método sirve para las dos formas de pago entre cuentas.
     *
     * @param idOrigen  id_cuenta desde la que sale el dinero
     * @param idDestino id_cuenta que recibe el dinero
     * @param cantidad  importe a enviar (debe ser positivo)
     * @param tipo      tipo de operación a registrar ('TRANSFERENCIA' o 'BIZUM')
     * @param concepto  texto descriptivo de la operación
     * @return true si el envío se ha confirmado, false si se ha deshecho
     *
     * @author Raul
     * @version 0.2
     */
    public boolean transferir(int idOrigen, int idDestino, double cantidad, String tipo, String concepto) {

        String restarOrigen = "UPDATE cuenta_bancaria SET saldo = saldo - ? WHERE id_cuenta = ?";
        String sumarDestino = "UPDATE cuenta_bancaria SET saldo = saldo + ? WHERE id_cuenta = ?";

        String insertOperacion = "INSERT INTO operacion "
                + "(id_cuenta_operacion, id_cuenta_destino, tipo, concepto, fecha_hora, importe) "
                + "VALUES (?, ?, ?, ?, NOW(), ?)";

        try (Connection con = ConexionBD.getConnection()) {

            // Desactivamos el autocommit para controlar la transacción a mano
            con.setAutoCommit(false);

            try (PreparedStatement psOrigen = con.prepareStatement(restarOrigen);
                 PreparedStatement psDestino = con.prepareStatement(sumarDestino);
                 PreparedStatement psOperacion = con.prepareStatement(insertOperacion)) {

                // 1) Restamos el importe de la cuenta de origen
                psOrigen.setDouble(1, cantidad);
                psOrigen.setInt(2, idOrigen);
                psOrigen.executeUpdate();

                // 2) Sumamos el importe a la cuenta de destino
                psDestino.setDouble(1, cantidad);
                psDestino.setInt(2, idDestino);
                psDestino.executeUpdate();

                // 3) Registramos UNA operación con origen y destino
                psOperacion.setInt(1, idOrigen);
                psOperacion.setInt(2, idDestino);
                psOperacion.setString(3, tipo);
                psOperacion.setString(4, concepto);
                psOperacion.setDouble(5, cantidad);
                psOperacion.executeUpdate();

                // Si las tres consultas han ido bien, confirmamos los cambios
                con.commit();
                return true;

            } catch (Exception e) {
                // Algo ha fallado: deshacemos el envío completo (origen, destino y operación)
                con.rollback();
                System.out.println("Error en la transferencia, cambios deshechos: " + e.getMessage());
                return false;
            }

        } catch (Exception e) {
            System.out.println("Error de conexión: " + e.getMessage());
            return false;
        }
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
     * Crea una cuenta bancaria nueva para un cliente y la enlaza con él en la tabla
     * acceso, ambas inserciones dentro de una misma transacción.
     *
     * @param idCliente    id del cliente (= id_cuenta_usuario)
     * @param numeroCuenta número de cuenta generado
     * @param iban         IBAN generado
     * @param cvv          cvv generado
     * @param saldoInicial saldo con el que se abre la cuenta
     * @return id_cuenta de la cuenta creada, o -1 si falla
     *
     * @author Raul
     * @version 0.2
     */
    public int crearCuentaParaCliente(int idCliente, String numeroCuenta, String iban,
                                      String cvv, double saldoInicial) {

        String insCuenta = "INSERT INTO cuenta_bancaria (numero_cuenta, iban, saldo, estado, cvv) "
                + "VALUES (?, ?, ?, 1, ?)";
        String insAcceso = "INSERT INTO acceso (id_cliente, id_cuenta) VALUES (?, ?)";

        try (Connection con = ConexionBD.getConnection()) {

            con.setAutoCommit(false);

            try (PreparedStatement psCuenta = con.prepareStatement(insCuenta, Statement.RETURN_GENERATED_KEYS);
                 PreparedStatement psAcceso = con.prepareStatement(insAcceso)) {

                // 1) Creamos la cuenta bancaria
                psCuenta.setString(1, numeroCuenta);
                psCuenta.setString(2, iban);
                psCuenta.setDouble(3, saldoInicial);
                psCuenta.setString(4, cvv);
                psCuenta.executeUpdate();

                int idCuenta = 0;
                try (ResultSet rs = psCuenta.getGeneratedKeys()) {
                    if (rs.next()) {
                        idCuenta = rs.getInt(1);
                    }
                }

                // 2) Enlazamos la cuenta con el cliente en la tabla acceso
                psAcceso.setInt(1, idCliente);
                psAcceso.setInt(2, idCuenta);
                psAcceso.executeUpdate();

                con.commit();
                return idCuenta;

            } catch (Exception e) {
                con.rollback();
                System.out.println("Error al crear la cuenta del cliente, cambios deshechos: " + e.getMessage());
                return -1;
            }

        } catch (Exception e) {
            System.out.println("Error de conexión al crear la cuenta: " + e.getMessage());
            return -1;
        }
    }

    /**
     * Busca el id_cuenta de una cuenta bancaria a partir de su IBAN.
     *
     * Se usa en las transferencias y Bizum: el usuario teclea el IBAN de destino y
     * este método lo traduce al id_cuenta que necesita la transacción. Si ningún
     * IBAN coincide, devuelve -1 para que la capa lógica lo trate como destino
     * inexistente.
     *
     * @param iban IBAN de la cuenta destino (tal cual lo teclea el usuario)
     * @return id_cuenta de esa cuenta, o -1 si no existe ninguna con ese IBAN
     *
     * @author Raul
     * @version 0.2
     */
    public int buscarIdPorIban(String iban) {

        String sql = "SELECT id_cuenta FROM cuenta_bancaria WHERE iban = ?";

        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, iban);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id_cuenta");
                }
            }

        } catch (Exception e) {
            System.out.println("Error al buscar la cuenta por IBAN: " + e.getMessage());
        }

        return -1; // no existe ninguna cuenta con ese IBAN
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