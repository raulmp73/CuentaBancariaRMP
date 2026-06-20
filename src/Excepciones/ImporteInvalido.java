package Excepciones;

/**
 * Se lanza cuando el importe de una operación no es válido (por ejemplo, cero o
 * negativo).
 *
 * @author Raul
 * @version 0.2
 */
public class ImporteInvalido extends ExcepcionesBanco {

	private static final long serialVersionUID = 1L;

	public ImporteInvalido(String mensaje) {
		super(mensaje);
	}
}
