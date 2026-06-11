package interfaces;

import java.sql.SQLException;

import modelo.Cliente;

/**
 * Define el acceso a datos del cliente en la capa de persistencia.
 *
 * @author Raul
 * @version 0.2
 */
public interface IPersistenciaCliente {

	public Cliente cargarCliente(int idUsuario) ;
}
