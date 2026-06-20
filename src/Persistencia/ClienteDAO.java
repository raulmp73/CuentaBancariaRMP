package Persistencia;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import interfaces.IPersistenciaCliente;
import modelo.Cliente;
import modelo.CuentaBancaria;

/**
 * Clase DAO encargada de gestionar el acceso a datos del cliente.
 * 
 * Se encarga de cargar un cliente completo desde la base de datos,
 * incluyendo sus datos personales y sus cuentas bancarias asociadas.
 * 
 * @author Raul
 * @version 0.2
 */
public class ClienteDAO implements IPersistenciaCliente {

    String comando;
    ConexionBD BD;
    ArrayList<Cliente> listaCliente;

    // DAO para cargar las cuentas bancarias asociadas al cliente
    CuentaBancariaDAO cuentaBancariaDAO = new CuentaBancariaDAO();

    /**
     * Carga un cliente completo a partir de su ID.
     * 
     * Este método obtiene los datos del cliente desde la base de datos,
     * junto con los IDs de sus cuentas bancarias. Posteriormente,
     * utiliza otro DAO para cargar las cuentas completas y asignarlas
     * al cliente.
     * 
     * @param idUsuario ID del usuario a buscar
     * @return objeto Cliente con todos sus datos y cuentas, o null si no existe
     * 
     * @author Raul
     * @version 0.2
     */
    @Override
    public Cliente cargarCliente(int idUsuario) {

        // LEFT JOIN en acceso: un cliente recién creado puede no tener cuentas todavía;
        // aun así debe cargarse (con la lista de cuentas vacía).
        String comando = "SELECT u.dni, u.nombre, u.apellido, c.email, u.telefono, u.fecha_registro, a.id_cuenta "
                + "FROM usuario u JOIN cuenta_usuario c ON u.id_usuario = c.id_usuario "
                + "LEFT JOIN acceso a ON c.id_usuario = a.id_cliente WHERE u.id_usuario = ?;";

        Cliente cliente = null;

        // Lista donde guardamos los IDs de las cuentas del cliente
        ArrayList<Integer> listaIds = new ArrayList<>();

        ArrayList<CuentaBancaria> cuentasBancarias;

        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement(comando)) {

            ps.setInt(1, idUsuario);

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {

                    // Guardamos el id de cada cuenta bancaria asociada (puede ser NULL
                    // si el cliente todavía no tiene ninguna cuenta -> lo saltamos)
                    int idCuenta = rs.getInt("id_cuenta");
                    if (!rs.wasNull()) {
                        listaIds.add(idCuenta);
                    }

                    // Solo creamos el cliente una vez
                    if (cliente == null) {
                        cliente = new Cliente(
                                rs.getString("dni"),
                                rs.getString("nombre"),
                                rs.getString("apellido"),
                                rs.getString("telefono"),
                                rs.getString("email"),
                                rs.getDate("fecha_registro"),
                                idUsuario,
                                new ArrayList<CuentaBancaria>()
                        );
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            // TODO: Mejorar manejo de errores (log, mensaje, etc.)
        }

        // Si el cliente existe, cargamos sus cuentas bancarias
        if (cliente != null) {

            int[] cuentas = new int[listaIds.size()];

            for (int i = 0; i < listaIds.size(); i++) {
                cuentas[i] = listaIds.get(i);
            }

            cuentasBancarias = cuentaBancariaDAO.cargarCuentasBancarias(cuentas);

            cliente.setCuentasBancarias(cuentasBancarias);
        }

        return cliente;
    }

    /**
     * Crea un cliente nuevo (sin cuentas bancarias todavía) insertando en cadena
     * en tres tablas dentro de una misma transacción: usuario, cuenta_usuario
     * (tipo 'cliente') y cliente. Si algo falla, no se inserta nada (rollback).
     *
     * Los ids se insertan en cadena para que id_usuario, id_cuenta_usuario e
     * id_cliente coincidan (de lo que depende el resto de consultas del proyecto).
     *
     * @param dni        DNI del cliente (único)
     * @param nombre     nombre
     * @param apellido   apellido
     * @param telefono   teléfono
     * @param email      email (único)
     * @param usuario    nombre de usuario (único)
     * @param contrasena contraseña ya preparada para guardar (hash)
     * @return id del cliente creado (= id_cuenta_usuario), o -1 si falla
     *
     * @author Raul
     * @version 0.2
     */
    public int crearCliente(String dni, String nombre, String apellido, String telefono,
                            String email, String usuario, String contrasena) {

        String insUsuario = "INSERT INTO usuario (dni, nombre, apellido, telefono, fecha_registro) "
                + "VALUES (?, ?, ?, ?, CURDATE())";
        String insCuentaUsuario = "INSERT INTO cuenta_usuario (id_usuario, usuario, email, contrasena, estado, tipo) "
                + "VALUES (?, ?, ?, ?, 1, 'cliente')";
        String insCliente = "INSERT INTO cliente (id_cliente, id_cuenta_usuario) VALUES (?, ?)";

        try (Connection con = ConexionBD.getConnection()) {

            con.setAutoCommit(false);

            try (PreparedStatement psU = con.prepareStatement(insUsuario, Statement.RETURN_GENERATED_KEYS);
                 PreparedStatement psCU = con.prepareStatement(insCuentaUsuario, Statement.RETURN_GENERATED_KEYS);
                 PreparedStatement psC = con.prepareStatement(insCliente)) {

                // 1) usuario
                psU.setString(1, dni);
                psU.setString(2, nombre);
                psU.setString(3, apellido);
                psU.setString(4, telefono);
                psU.executeUpdate();
                int idUsuario = idGenerado(psU);

                // 2) cuenta_usuario (tipo cliente)
                psCU.setInt(1, idUsuario);
                psCU.setString(2, usuario);
                psCU.setString(3, email);
                psCU.setString(4, contrasena);
                psCU.executeUpdate();
                int idCuentaUsuario = idGenerado(psCU);

                // 3) cliente (id_cliente = id_cuenta_usuario)
                psC.setInt(1, idCuentaUsuario);
                psC.setInt(2, idCuentaUsuario);
                psC.executeUpdate();

                con.commit();
                return idCuentaUsuario;

            } catch (Exception e) {
                con.rollback();
                System.out.println("Error al crear cliente, cambios deshechos: " + e.getMessage());
                return -1;
            }

        } catch (Exception e) {
            System.out.println("Error de conexión al crear cliente: " + e.getMessage());
            return -1;
        }
    }

    /**
     * Busca el id de un cliente a partir de su DNI. Solo devuelve usuarios cuyo
     * tipo sea 'cliente'.
     *
     * @param dni DNI a buscar
     * @return id del cliente (= id_usuario = id_cuenta_usuario), o -1 si no existe
     *
     * @author Raul
     * @version 0.2
     */
    public int buscarIdPorDni(String dni) {

        String sql = "SELECT u.id_usuario FROM usuario u "
                + "JOIN cuenta_usuario cu ON u.id_usuario = cu.id_usuario "
                + "WHERE u.dni = ? AND cu.tipo = 'cliente'";

        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, dni);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id_usuario");
                }
            }

        } catch (Exception e) {
            System.out.println("Error al buscar cliente por DNI: " + e.getMessage());
        }

        return -1; // no existe ningún cliente con ese DNI
    }

    /**
     * Devuelve la clave autogenerada por la BD tras un INSERT.
     */
    private int idGenerado(PreparedStatement ps) throws SQLException {
        try (ResultSet rs = ps.getGeneratedKeys()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return -1;
    }
}