package controlador;

import java.sql.SQLException;

import Excepciones.ExcepcionesBanco;
import interfaces.IVista;
import logica.GestorAdministrador;
import logica.GestorCliente;
import logica.GestorCuentaBancaria;
import logica.GestorCuentaUsuario;
import logica.GestorEmpleado;
import modelo.Cliente;
import modelo.Cuenta;
import modelo.CuentaBancaria;
import view.vistaconsola.BancoView;

/**
 * Controlador del flujo del cliente: carga su información y gestiona los menús
 * de cliente y de cuenta bancaria.
 *
 * Corregido en la versión 0.2: corregida la condición de salida del menú (while
 * con -0), eliminados los gestores no utilizados y renombrados los métodos
 * (iniciar, iniciarMenu, iniciarMenuCuentaBancaria).
 *
 * @author Raul
 * @version 0.2
 */
public class ControladorCliente {

	// Vista
	private IVista vista;

	// Gestores
	private GestorCliente gestorCliente;
	private GestorCuentaBancaria gestorCuentaBancaria;

	/**
	 * Constructor del controlador de cliente.
	 * 
	 * Inicializa la vista, los DAO necesarios para acceder a la base de datos y los
	 * gestores que se encargarán de la lógica de la aplicación.
	 * 
	 * @author Raul
	 * @version 0.2
	 */
	public ControladorCliente() {
		this(new BancoView());
	}

	/**
	 * Constructor que recibe la vista ya creada (inyección), para compartir un único
	 * Scanner en toda la aplicación en lugar de que cada controlador cree el suyo.
	 *
	 * @param vista vista compartida
	 */
	public ControladorCliente(IVista vista) {
		this.vista = vista;
		this.gestorCliente = new GestorCliente();
		this.gestorCuentaBancaria = new GestorCuentaBancaria();
	}

	/**
	 * TODO: Implementar el inicio del flujo del cliente.
	 * 
	 * @author Raul
	 * @version 0.2
	 */
	public void iniciarCliente() {

	}

	/**
	 * Recibe una cuenta y carga el cliente completo asociado. Después inicia el
	 * menú principal del cliente.
	 * 
	 * @param c cuenta del usuario
	 * @author Raul
	 * @version 0.2
	 */
	public void iniciar(Cuenta c) {
		// NOTA (B3): c.getNumCuenta() contiene el id_cuenta_usuario devuelto por el login.
		// Se asume que en la BD id_cuenta_usuario coincide con id_usuario. Verificar con
		// el esquema de la BD; si no coinciden, pasar aquí el id_usuario correcto.
		operar(c.getNumCuenta());
	}

	/**
	 * Carga un cliente por su id (con sus cuentas) y lanza su menú de cuentas.
	 * Lo usa tanto el login (iniciar) como el empleado, para "ponerse en la piel"
	 * de ese cliente y operar sus cuentas con el mismo menú.
	 *
	 * @param idCliente id del cliente a gestionar
	 * @author Raul
	 * @version 0.2
	 */
	public void operar(int idCliente) {
		gestorCliente.cargarCliente(idCliente); // el GESTOR carga el cliente desde la BD
		gestorCuentaBancaria.cargarCuentas(gestorCliente.cogerCliente().getCuentasBancarias());
		iniciarMenu();
	}

	/**
	 * Muestra el menú principal del cliente. Permite seleccionar una cuenta
	 * bancaria o salir.
	 * 
	 * @author Raul
	 * @version 0.2
	 */
	public void iniciarMenu() {

		int OpcionCliente;
		do {
			OpcionCliente = vista.menuCliente(gestorCliente.cogerCliente()) - 1;// TODO: arreglar
			
			try {
				iniciarMenuCuentaBancaria(gestorCuentaBancaria.buscarCuenta(OpcionCliente));
			} catch (ExcepcionesBanco e) {	
			}
		} while (OpcionCliente != -1);
	}

	/**
	 * Muestra el menú de una cuenta bancaria concreta. Aquí se realizarán
	 * operaciones como consultar saldo, transferencias, etc.
	 * 
	 * @param cb cuenta bancaria seleccionada
	 * @author Raul
	 * @version 0.2
	 */
	public void iniciarMenuCuentaBancaria(CuentaBancaria cb) {

		int opcionCliente;
		do {

			opcionCliente = vista.menuCuentaBancaria(cb);
			switch (opcionCliente) {

			case 1:
				 vista.mostrarOperaciones(cb.getOperaciones(), gestorCliente.cogerCliente().getIdCliente());
				break;

			case 2: {
				double cantidad = vista.pedirImporte("¿Cuánto quieres ingresar? ");
				try {
					gestorCuentaBancaria.ingresar(cb, cantidad);
					vista.mostrarMensaje("Ingreso realizado. Nuevo saldo: " + cb.getSaldo() + " €");
				} catch (ExcepcionesBanco e) {
					vista.mostrarMensaje(e.getMessage());
				}
				break;
			}

			case 3: {
				double cantidad = vista.pedirImporte("¿Cuánto quieres retirar? ");
				try {
					gestorCuentaBancaria.retirar(cb, cantidad);
					vista.mostrarMensaje("Retirada realizada. Nuevo saldo: " + cb.getSaldo() + " €");
				} catch (ExcepcionesBanco e) {
					vista.mostrarMensaje(e.getMessage());
				}
				break;
			}

			case 4: {
				int formaPago = vista.menuFormasPago();
				switch (formaPago) {

				case 1: { // Transferencia: enviar dinero a otra cuenta del banco por su IBAN
					String ibanDestino = vista.pedirTexto("IBAN de destino: ");
					double cantidad = vista.pedirImporte("¿Cuánto quieres transferir? ");
					String concepto = vista.pedirTexto("Concepto: ");
					try {
						gestorCuentaBancaria.transferir(cb, ibanDestino, cantidad, concepto);
						vista.mostrarMensaje("Transferencia realizada. Nuevo saldo: " + cb.getSaldo() + " €");
					} catch (ExcepcionesBanco e) {
						vista.mostrarMensaje(e.getMessage());
					}
					break;
				}

				case 2: { // Bizum: mismo envío entre cuentas, registrado como BIZUM
					String ibanDestino = vista.pedirTexto("IBAN de destino: ");
					double cantidad = vista.pedirImporte("¿Cuánto quieres enviar por Bizum? ");
					String concepto = vista.pedirTexto("Concepto: ");
					try {
						gestorCuentaBancaria.enviarBizum(cb, ibanDestino, cantidad, concepto);
						vista.mostrarMensaje("Bizum enviado. Nuevo saldo: " + cb.getSaldo() + " €");
					} catch (ExcepcionesBanco e) {
						vista.mostrarMensaje(e.getMessage());
					}
					break;
				}

				case 3: { // Pago con tarjeta: el dinero sale a un comercio, sin cuenta destino
					double cantidad = vista.pedirImporte("¿Cuánto quieres pagar? ");
					String concepto = vista.pedirTexto("Comercio / concepto: ");
					try {
						gestorCuentaBancaria.pagarConTarjeta(cb, cantidad, concepto);
						vista.mostrarMensaje("Pago con tarjeta realizado. Nuevo saldo: " + cb.getSaldo() + " €");
					} catch (ExcepcionesBanco e) {
						vista.mostrarMensaje(e.getMessage());
					}
					break;
				}

				}
				break;
			}

			}
			// vista.mostrarOperaciones(cb.getOperaciones());

		} while (opcionCliente != 0);

	}
}