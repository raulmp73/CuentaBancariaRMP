package AaTesting;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import Persistencia.CuentaUsuarioDAO;
import modelo.Cuenta;

/**
 * Pruebas del DAO de cuenta de usuario.
 *
 * Corregido en la versión 0.2: el test ya compila (antes usaba una firma
 * inexistente de iniciarCuenta y un assert inválido) y se marca como @Disabled
 * porque requiere una conexión real a la base de datos.
 *
 * @author Raul
 * @version 0.2
 */
public class TestingCuentaUsuarioDAO {

	/**
	 * Prueba el inicio de sesión contra la BD. Deshabilitado porque requiere una
	 * conexión real a la base de datos; pendiente de sustituir por un mock.
	 */
	@Disabled("Requiere conexión a BD — pendiente de mock")
	@Test
	public void testInicioSesion() {
		CuentaUsuarioDAO dao = new CuentaUsuarioDAO();
		Cuenta credenciales = new Cuenta("ana@email.com", "1234");
		Cuenta resultado = dao.iniciarCuenta(credenciales);
		assertNotNull(resultado);
	}

}
