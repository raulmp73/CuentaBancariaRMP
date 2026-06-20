package controlador;

import java.sql.SQLException;
import java.util.Iterator;

import Excepciones.EmailyContraseñaInvalidos;
import Excepciones.DAO.ExcepcionesDAO;
import Excepciones.DAO.FalloUsuarioInvalido;
import Persistencia.CuentaUsuarioDAO;
import interfaces.IPersistencia;
import interfaces.IPersistenciaCuentaUsuario;
import interfaces.IVista;
import logica.GestorAdministrador;
import logica.GestorCliente;
import logica.GestorCuentaBancaria;
import logica.GestorCuentaUsuario;
import logica.GestorEmpleado;
import modelo.Administrador;
import modelo.Cliente;
import modelo.Cuenta;
import modelo.CuentaBancaria;
import modelo.Empleado;
import modelo.Usuario;
import view.vistaconsola.BancoView;

/**
 * Controlador principal de la aplicación. Coordina la vista, los DAO y los
 * gestores para gestionar el inicio de sesión y derivar el flujo según el rol.
 *
 * Corregido en la versión 0.2: añadido break en el switch de roles, eliminados
 * los gestores no utilizados y renombrado el método Iniciar a iniciar.
 *
 * @author Raul
 * @version 0.2
 */
public class Controlador {

	// controladores

	private ControladorCliente CCliente;
	private ControladorEmpleado CEmpleado;
	// Vista
	private IVista vista;

	// Gestores
	private GestorCuentaUsuario gestorCuentaUsuario;

	// Persistencia
	private IPersistenciaCuentaUsuario cuentaUsuarioDAO;

	// Constructor
	public Controlador() {

		// Vista (una sola, compartida por todos los controladores -> un único Scanner)
		this.vista = new BancoView();

		// Controladores (reciben la vista compartida)
		CCliente = new ControladorCliente(vista);
		CEmpleado = new ControladorEmpleado(vista);

		// DAO
		this.cuentaUsuarioDAO = new CuentaUsuarioDAO();

		// Gestores
		this.gestorCuentaUsuario = new GestorCuentaUsuario();
	}

	/**
	 * 
	 * Inicia la app
	 * 
	 * @author Raul
	 * @version 0.1
	 */
	public void iniciar() {
		iniciarSesion();
	}

	/**
	 * Inicia el flujo principal de la aplicación.
	 * 
	 * Este método ejecuta un bucle que solicita al usuario iniciar sesión hasta que
	 * lo consigue correctamente. Mientras no se complete el inicio de sesión de
	 * forma válida, el sistema seguirá solicitándolo.
	 * 
	 * Utiliza el método iniciarSesion(), que devuelve true cuando el usuario ha
	 * iniciado sesión correctamente y false en caso contrario.
	 * 
	 * @author Raul
	 * @version 0.2
	 * @throws SQLException
	 * @throws EmailyContraseñaInvalidos
	 */
	public void iniciarSesion()  {

		
		String email;
		String contraseña;
		int intentos = 0;
		int intentoMaximo = 3; // TODO: añadir properties
		do {
			Cuenta cuenta = vista.inicioSesion();
			try {
				cuenta = gestorCuentaUsuario.iniciarCuenta(cuenta, cuentaUsuarioDAO);
				switch (cuenta.getTipo()) {
					case "cliente":       { CCliente.iniciar(cuenta);  break; }
					case "empleado":      { CEmpleado.iniciar(cuenta); break; }
					case "administrador": { vista.mostrarMensaje("Panel de administrador todavía no implementado."); break; }
				}
			} catch (EmailyContraseñaInvalidos e) {
				vista.mostrarMensaje(e.getMessage());
				intentos = intentos+1;
				vista.mostrarMensaje("Intentos: "+intentos+" de "+intentoMaximo); 
				
			} 
		} while (intentos<intentoMaximo );//TODO: hacer properties


	}
	/**
	 * //usuarioActivo = gestorUsuarios.login(email, password);
	 * 
	 * HASTA AL POLLA
	 * 
	 * //if (usuarioActivo == null) { // vista.mostrarMensaje("Credenciales
	 * incorrectas. Intenta de nuevo."); //}
	 */
	// vista.mostrarMensaje("\n¡Bienvenido al panel, " + usuarioActivo.getNombre() +
	// "!");
	/**
	 * public void pedirCredenciales() {
	 * 
	 * 
	 * if (usuarioActivo instanceof Administrador) { iniciarAdministrador(); } else
	 * if (usuarioActivo instanceof Empleado) { iniciarEmpleado(); } else if
	 * (usuarioActivo instanceof Cliente) { iniciarCliente(); } }
	 * 
	 */
}
