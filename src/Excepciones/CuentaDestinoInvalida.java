package Excepciones;

/**
 * Se lanza cuando la cuenta destino de un pago (transferencia o Bizum) no es
 * válida: el IBAN no existe en el banco o coincide con la cuenta de origen.
 *
 * Es hija de {@link ExcepcionesBanco}, igual que {@link ImporteInvalido} y
 * {@link SaldoInsuficiente}, por lo que un solo catch (ExcepcionesBanco) en el
 * controlador la recoge junto con el resto.
 *
 * @author Raul
 * @version 0.2
 */
public class CuentaDestinoInvalida extends ExcepcionesBanco {

	private static final long serialVersionUID = 1L;

	public CuentaDestinoInvalida(String mensaje) {
		super(mensaje);
	}
}
