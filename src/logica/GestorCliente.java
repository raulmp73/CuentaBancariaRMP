package logica;

import java.util.ArrayList;

import Persistencia.ClienteDAO;
import interfaces.IPersistenciaCliente;
import modelo.Cliente;
import modelo.Operacion;

/**
 * Gestiona la lógica del cliente: lo carga desde la base de datos (a través de su
 * DAO) y lo mantiene como cliente actual de la sesión.
 *
 * Corregido en la versión 0.2: ahora es el GESTOR quien llama al DAO para cargar
 * el cliente (antes lo hacía el controlador, saltándose esta capa).
 *
 * @author Raul
 * @version 0.2
 */
public class GestorCliente {

    // Cliente actual cargado
    private Cliente C;

    // DAO para acceder a los datos del cliente
    private IPersistenciaCliente clienteDAO = new ClienteDAO();

    /**
     * Carga desde la base de datos el cliente con el id indicado y lo guarda como
     * cliente actual. Se usa normalmente tras el login.
     *
     * @param idUsuario id del usuario/cliente a cargar
     * @author Raul
     * @version 0.2
     */
    public void cargarCliente(int idUsuario) {
        C = clienteDAO.cargarCliente(idUsuario);
    }

    /**
     * Devuelve el cliente actual cargado.
     *
     * @return cliente actual
     * @author Raul
     * @version 0.2
     */
    public Cliente cogerCliente() {
        return C;
    }

    public ArrayList<Operacion> cogerHistorialOperaciones() {
        return null;
    }
}
