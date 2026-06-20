package Persistencia;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import Excepciones.DAO.FalloUsuarioInvalido;
import interfaces.IPersistenciaCuentaUsuario;
import modelo.Cliente;
import modelo.Cuenta;

/**
 * Clase DAO encargada de acceder a los datos de las cuentas de usuario.
 * 
 * Esta clase se comunica con la base de datos para comprobar las credenciales
 * de inicio de sesión de los usuarios.
 *
 * Corregido en la versión 0.2: la consulta ya no compara la contraseña en el
 * SQL; solo busca por email y devuelve la cuenta. La validación de la contraseña
 * se realiza en la capa lógica mediante
 * {@link Validadores.InputValid#validarPassword(modelo.Cuenta, modelo.Cuenta)}, que lanza
 * una excepción si no coincide.
 *
 * @author Raul
 * @version 0.2
 */
public class CuentaUsuarioDAO implements IPersistenciaCuentaUsuario {

	// Comando SQL que se utilizará en las consultas
	String comando;

	// Lista de clientes (por si se usa en futuras funcionalidades)
	ArrayList<Cliente> listaCliente;

	/**
	 * Inicia sesión en la aplicación buscando una cuenta en la base de datos.
	 * 
	 * Consulta la tabla cuenta_usuario y busca un usuario por su email, devolviendo
	 * su cuenta con la contraseña almacenada. La comprobación de la contraseña se
	 * realiza después en la capa lógica.
	 *
	 * @param c cuenta con el email introducido por el usuario
	 * @return objeto Cuenta encontrado por email, o null si no existe
	 * 
	 * @author Raul
	 * @version 0.2
	 */
	@Override
	public Cuenta iniciarCuenta(Cuenta c)  {

		String comando = "SELECT id_cuenta_usuario, email, contrasena, tipo "
				+ "FROM cuenta_usuario WHERE email = ?";

		try (Connection con = ConexionBD.getConnection(); PreparedStatement ps = con.prepareStatement(comando)) {

			ps.setString(1, c.getEmail());

			try (ResultSet rs = ps.executeQuery()) {

				if (rs.next()) {
					int id = rs.getInt("id_cuenta_usuario");
					String email = rs.getString("email");
					String contrasena = rs.getString("contrasena");
					String tipo = rs.getString("tipo");

					// Devuelve la cuenta con la contraseña ALMACENADA; la validación de
					// la contraseña se hace en la capa lógica (InputValid.validarPassword).
					return new Cuenta(id, email, contrasena, tipo);
				}
				return null;
			}

		} catch (SQLException e) {
			System.out.println("error bd");
		}
		return null;
	}

	}
