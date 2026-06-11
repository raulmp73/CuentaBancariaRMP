package Excepciones;

/**
 * Se lanza cuando se intenta retirar o transferir más dinero del saldo
 * disponible en la cuenta.
 *
 * @author Raul
 * @version 0.2
 */
public class SaldoInsuficiente extends ExcepcionesBanco {

	private static final long serialVersionUID = 1L;

	public SaldoInsuficiente(String mensaje) {
		super(mensaje);
	}
}
