package interfaces;

import java.sql.SQLException;

import modelo.Cuenta;

/**
 * Define el acceso a datos de la cuenta de usuario en la capa de persistencia.
 *
 * @author Raul
 * @version 0.2
 */
public interface IPersistenciaCuentaUsuario {
	public Cuenta iniciarCuenta(Cuenta c);

}
