package controlador;

import Excepciones.ExcepcionesBanco;
import Util.DesgloseEfectivo;
import interfaces.IVista;
import logica.GestorEmpleado;
import modelo.Cliente;
import modelo.Cuenta;
import modelo.CuentaBancaria;
import view.vistaconsola.BancoView;

/**
 * Controlador del flujo del empleado. Permite crear clientes, añadirles cuentas,
 * gestionar las cuentas de un cliente (reutilizando el menú del cliente) y dar
 * dinero en efectivo por ventanilla.
 *
 * @author Raul
 * @version 0.2
 */
public class ControladorEmpleado {

	// Vista
	private IVista vista;

	// Lógica del empleado
	private GestorEmpleado gestorEmpleado;

	// Reutilizamos el controlador de cliente para "operar" como si fuera el cliente
	private ControladorCliente controladorCliente;

	public ControladorEmpleado() {
		this(new BancoView());
	}

	/**
	 * Constructor con la vista inyectada, para compartir un único Scanner en toda la
	 * aplicación.
	 *
	 * @param vista vista compartida
	 */
	public ControladorEmpleado(IVista vista) {
		this.vista = vista;
		this.gestorEmpleado = new GestorEmpleado();
		this.controladorCliente = new ControladorCliente(vista); // comparte la vista/Scanner
	}

	/**
	 * Muestra el menú de empleado en bucle hasta que elige salir. Cada opción se
	 * ejecuta dentro de un try/catch que muestra el mensaje de cualquier excepción
	 * de negocio (mismo patrón que el resto de la app).
	 *
	 * @param c cuenta de usuario del empleado que ha iniciado sesión
	 * @author Raul
	 * @version 0.2
	 */
	public void iniciar(Cuenta c) {

		int opcion;
		do {
			opcion = vista.menuEmpleado();

			try {
				switch (opcion) {
					case 1: crearCliente();              break;
					case 2: anadirCuenta();              break;
					case 3: gestionarCuentasDeCliente(); break;
					case 4: darEfectivo();               break;
				}
			} catch (ExcepcionesBanco e) {
				vista.mostrarMensaje(e.getMessage());
			}

		} while (opcion != 0);
	}

	/**
	 * Pide los datos de un cliente nuevo y lo crea (sin cuentas todavía).
	 */
	private void crearCliente() {
		String dni       = vista.pedirTexto("DNI: ");
		String nombre    = vista.pedirTexto("Nombre: ");
		String apellido  = vista.pedirTexto("Apellido: ");
		String telefono  = vista.pedirTexto("Teléfono: ");
		String email     = vista.pedirTexto("Email: ");
		String usuario   = vista.pedirTexto("Usuario: ");
		String password  = vista.pedirTexto("Contraseña: ");

		gestorEmpleado.crearCliente(dni, nombre, apellido, telefono, email, usuario, password);
		vista.mostrarMensaje("Cliente creado correctamente.");
	}

	/**
	 * Pide el DNI de un cliente y le crea una cuenta bancaria nueva.
	 */
	private void anadirCuenta() {
		String dni = vista.pedirTexto("DNI del cliente: ");
		double saldoInicial = vista.pedirImporte("Saldo inicial de la cuenta: ");

		gestorEmpleado.anadirCuentaACliente(dni, saldoInicial);
		vista.mostrarMensaje("Cuenta creada y asociada al cliente.");
	}

	/**
	 * Pide el DNI de un cliente y entra en su menú de cuentas (todo lo que puede
	 * hacer el propio cliente), reutilizando el ControladorCliente.
	 */
	private void gestionarCuentasDeCliente() {
		String dni = vista.pedirTexto("DNI del cliente: ");
		int idCliente = gestorEmpleado.buscarIdClientePorDni(dni); // lanza si no existe
		controladorCliente.operar(idCliente);
	}

	/**
	 * Da dinero en efectivo desde una cuenta del cliente: baja el saldo (RETIRO) y
	 * muestra el desglose en billetes y monedas a entregar.
	 */
	private void darEfectivo() {
		String dni = vista.pedirTexto("DNI del cliente: ");
		Cliente cliente = gestorEmpleado.cargarClientePorDni(dni); // lanza si no existe

		int eleccion = vista.menuCliente(cliente) - 1; // 0-based; -1 = volver
		if (eleccion < 0) {
			return; // el empleado cancela
		}

		CuentaBancaria cuenta = gestorEmpleado.buscarCuenta(eleccion);
		double cantidad = vista.pedirImporte("¿Cuánto efectivo entregar? ");

		gestorEmpleado.darEfectivo(cuenta, cantidad);

		vista.mostrarMensaje("Efectivo entregado. Nuevo saldo: " + cuenta.getSaldo() + " €");
		vista.mostrarMensaje(DesgloseEfectivo.formatear(cantidad));
	}
}
