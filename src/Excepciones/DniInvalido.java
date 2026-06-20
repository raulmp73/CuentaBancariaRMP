package Excepciones;

/**
 * Se lanza cuando un DNI no es válido: no tiene el formato 8 números + 1 letra,
 * o la letra de control no se corresponde con el número.
 *
 * @author Raul
 * @version 0.2
 */
public class DniInvalido extends ExcepcionesBanco {

	private static final long serialVersionUID = 1L;

	public DniInvalido(String mensaje) {
		super(mensaje);
	}
}
