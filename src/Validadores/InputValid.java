package Validadores;

import java.util.ArrayList;

import Excepciones.CuentaDestinoInvalida;
import Excepciones.DatosClienteInvalidos;
import Excepciones.DniInvalido;
import Excepciones.EmailyContraseñaInvalidos;
import Excepciones.ExisteCuenta;
import Excepciones.ImporteInvalido;
import Excepciones.SaldoInsuficiente;
import Util.ContraseñaHash;
import modelo.Cuenta;
import modelo.CuentaBancaria;

/**
 * Validaciones de negocio de la aplicación. Comprueba los datos de entrada y, si
 * no cumplen las reglas, lanza una excepción de la jerarquía
 * {@link Excepciones.ExcepcionesBanco} (en lugar de devolver un valor de control).
 *
 * Vive en el paquete <b>Validadores</b> (reglas de negocio), separada de las
 * utilidades técnicas del paquete <b>Util</b> (lectura de teclado en
 * {@link Util.InputReader}, hash de contraseñas en {@link Util.ContraseñaHash}).
 *
 * Corregido en la versión 0.2: método ValidarContraseña renombrado a
 * validarContraseña (convención de nombres camelCase).
 *
 * @author Raul
 * @version 0.2
 */
public class InputValid {

	/**
	 * Valida que una cuenta tenga un email y una contraseña correctos.
	 *
	 * Si el email no contiene @ o la contraseña no cumple los requisitos, lanza una
	 * excepción personalizada.
	 *
	 * @param c cuenta que se quiere validar
	 * @throws EmailyContraseñaInvalidos si el email o la contraseña son inválidos
	 *
	 * @author Raul
	 * @version 0.2
	 */
	public void validarCuenta(Cuenta c) throws EmailyContraseñaInvalidos {

		if (c.getEmail() == null || !c.getEmail().contains("@")) {
			throw new EmailyContraseñaInvalidos("Email inválido");
		}

		if (!validarContraseña(c.getPassword())) {
			throw new EmailyContraseñaInvalidos("Contraseña inválida");
		}
	}

	/**
	 * Valida que una contraseña sea correcta.
	 *
	 * La contraseña debe tener al menos 4 caracteres y contener como mínimo 2
	 * números.
	 *
	 * @param contraseña contraseña que se quiere validar
	 * @return true si la contraseña es válida, false si no lo es
	 *
	 * @author Raul
	 * @version 0.2
	 */
	public boolean validarContraseña(String contraseña) {

		if (contraseña == null || contraseña.length() < 3) {
			// TODO: Poner la longitud mínima en una variable de entorno
			return false;
		}

		int contadorNumeros = 0;

		for (int i = 0; i < contraseña.length(); i++) {
			if (Character.isDigit(contraseña.charAt(i))) {
				contadorNumeros++;
			}
		}

		return contadorNumeros >= 2;
	}

	/**
	 * Comprueba que la contraseña introducida coincide con la almacenada en la
	 * base de datos. Sigue el mismo patrón que {@link #validarCuenta(Cuenta)}: si
	 * las credenciales no son válidas lanza una excepción en lugar de devolver un
	 * valor de control.
	 *
	 * @param introducida cuenta con la contraseña escrita por el usuario
	 * @param almacenada  cuenta recuperada de la BD (o null si el email no existe)
	 * @throws EmailyContraseñaInvalidos si el email no existe o la contraseña no coincide
	 *
	 * @author Raul
	 * @version 0.2
	 */
	public void validarPassword(Cuenta introducida, Cuenta almacenada) throws EmailyContraseñaInvalidos {

		if (almacenada == null
				|| !ContraseñaHash.comprobarPassword(introducida.getPassword(), almacenada.getPassword())) {
			throw new EmailyContraseñaInvalidos("Email o contraseña incorrectos");
		}
	}

	/**
	 * Valida que el importe de una operación sea correcto (mayor que 0).
	 *
	 * @param cantidad importe a validar
	 * @throws ImporteInvalido si el importe es cero o negativo
	 *
	 * @author Raul
	 * @version 0.2
	 */
	public void validarImporte(double cantidad) {
		if (cantidad <= 0) {
			throw new ImporteInvalido("El importe debe ser mayor que 0");
		}
	}

	/**
	 * Valida que una cuenta tenga saldo suficiente para sacar una cantidad
	 * (retiro o transferencia).
	 *
	 * @param cb       cuenta de la que se quiere sacar dinero
	 * @param cantidad importe que se quiere retirar
	 * @throws SaldoInsuficiente si la cantidad supera el saldo disponible
	 *
	 * @author Raul
	 * @version 0.2
	 */
	public void validarSaldoSuficiente(CuentaBancaria cb, double cantidad) {
		if (cantidad > cb.getSaldo()) {
			throw new SaldoInsuficiente("Saldo insuficiente. Tu saldo es de " + cb.getSaldo() + " €");
		}
	}

	/**
	 * Valida que exista una cuenta bancaria en la posición indicada de la lista.
	 *
	 * @param i  índice de la cuenta dentro de la lista
	 * @param cb lista de cuentas bancarias del cliente
	 * @throws ExisteCuenta si no hay cuenta en esa posición
	 *
	 * @author Raul
	 * @version 0.2
	 */
	public void ValidarExistenciaCuenta(int i, ArrayList<CuentaBancaria> cb) {

		if (cb.get(i) == null) {
			throw new ExisteCuenta("No existe ninguna cuenta con ese ID");
		}
	}

	/**
	 * Valida que la cuenta destino de un pago (transferencia o Bizum) sea válida.
	 *
	 * El destino se identifica por su IBAN; el DAO lo busca y devuelve el id_cuenta
	 * correspondiente, o -1 si ese IBAN no existe en el banco. Aquí se comprueba que
	 * el destino exista y que no sea la propia cuenta de origen.
	 *
	 * @param idDestino id_cuenta de la cuenta destino (-1 si el IBAN no existe)
	 * @param idOrigen  id_cuenta de la cuenta desde la que se envía el dinero
	 * @throws CuentaDestinoInvalida si el IBAN no existe o coincide con el origen
	 *
	 * @author Raul
	 * @version 0.2
	 */
	public void validarCuentaDestino(int idDestino, int idOrigen) {

		if (idDestino <= 0) {
			throw new CuentaDestinoInvalida("No existe ninguna cuenta con ese IBAN");
		}

		if (idDestino == idOrigen) {
			throw new CuentaDestinoInvalida("No puedes enviarte dinero a tu propia cuenta");
		}
	}

	/**
	 * Valida los datos obligatorios para crear un cliente nuevo. El email y la
	 * contraseña se validan aparte con {@link #validarCuenta(Cuenta)}.
	 *
	 * @param dni      DNI del cliente
	 * @param nombre   nombre del cliente
	 * @param apellido apellido del cliente
	 * @throws DatosClienteInvalidos si algún campo obligatorio está vacío
	 *
	 * @author Raul
	 * @version 0.2
	 */
	public void validarDatosCliente(String dni, String nombre, String apellido) {
		if (dni == null || dni.isBlank()
				|| nombre == null || nombre.isBlank()
				|| apellido == null || apellido.isBlank()) {
			throw new DatosClienteInvalidos("El DNI, el nombre y el apellido son obligatorios");
		}
	}

	// Letras del DNI en el orden del algoritmo oficial (índice = número % 23)
	private static final String LETRAS_DNI = "TRWAGMYFPDXBNJZSQVHLCKE";

	/**
	 * Valida un DNI español: debe tener 8 números seguidos de 1 letra, y la letra
	 * debe coincidir con la que calcula el algoritmo oficial (número % 23).
	 *
	 * @param dni DNI a validar (se ignoran mayúsculas/minúsculas y espacios)
	 * @throws DniInvalido si el formato o la letra de control no son correctos
	 *
	 * @author Raul
	 * @version 0.2
	 */
	public void validarDni(String dni) {

		if (dni == null) {
			throw new DniInvalido("El DNI es obligatorio");
		}

		String d = dni.trim().toUpperCase();

		// Formato: 8 dígitos + 1 letra
		if (!d.matches("\\d{8}[A-Z]")) {
			throw new DniInvalido("El DNI debe tener 8 números y una letra (ej. 12345678Z)");
		}

		// La letra debe ser la que corresponde al número
		int numero = Integer.parseInt(d.substring(0, 8));
		char letraCorrecta = LETRAS_DNI.charAt(numero % 23);

		if (d.charAt(8) != letraCorrecta) {
			throw new DniInvalido("La letra del DNI no es correcta (debería ser " + letraCorrecta + ")");
		}
	}
}
