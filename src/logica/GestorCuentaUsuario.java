package logica;

import java.sql.SQLException;
import java.util.ArrayList;

import Persistencia.CuentaUsuarioDAO;
import Validadores.InputValid;
import interfaces.IPersistenciaCuentaUsuario;
import interfaces.IVista;
import modelo.Cuenta;
import modelo.Usuario;

/**
 * Gestiona el inicio de sesión de las cuentas de usuario, validando los datos
 * antes de delegar la consulta en la capa de persistencia.
 *
 * Corregido en la versión 0.2: eliminada la doble llamada SQL en iniciarCuenta.
 *
 * @author Raul
 * @version 0.2
 */
public class GestorCuentaUsuario {
	private Cuenta CuentaUsuario;
	private InputValid inputValid = new InputValid();

	public GestorCuentaUsuario() {

	}

	public Cuenta iniciarCuenta(Cuenta c, IPersistenciaCuentaUsuario BD) {
		inputValid.validarCuenta(c);
		CuentaUsuario = BD.iniciarCuenta(c);
		inputValid.validarPassword(c, CuentaUsuario);
		return CuentaUsuario;
	}

	// public Usuario buscarUsuario(String email) {}

	// public boolean validarUsuario(Usuario u) {return this.usuarios.contains(u);}

	// public Usuario login(String email, String password) {return null;}

	// public ArrayList<Usuario> getUsuarios() { return usuarios; }
	// public void setUsuarios(ArrayList<Usuario> usuarios) { this.usuarios =
	// usuarios; }
}
