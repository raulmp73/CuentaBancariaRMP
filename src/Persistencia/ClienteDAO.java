package Persistencia;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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

        String comando = "SELECT u.dni, u.nombre, u.apellido, c.email, u.telefono, u.fecha_registro, a.id_cuenta "
                + "FROM usuario u JOIN cuenta_usuario c ON u.id_usuario = c.id_usuario "
                + "JOIN acceso a ON c.id_usuario = a.id_cliente WHERE u.id_usuario = ?;";

        Cliente cliente = null;

        // Lista donde guardamos los IDs de las cuentas del cliente
        ArrayList<Integer> listaIds = new ArrayList<>();

        ArrayList<CuentaBancaria> cuentasBancarias;

        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement(comando)) {

            ps.setInt(1, idUsuario);

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {

                    // Guardamos el id de cada cuenta bancaria asociada
                    listaIds.add(rs.getInt("id_cuenta"));

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
}