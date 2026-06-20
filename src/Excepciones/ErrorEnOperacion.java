package Excepciones;

/**
 * Se lanza cuando una operación bancaria (ingreso, retiro...) no se ha podido
 * guardar en la base de datos.
 *
 * @author Raul
 * @version 0.2
 */
public class ErrorEnOperacion extends ExcepcionesBanco {

	private static final long serialVersionUID = 1L;

	public ErrorEnOperacion(String mensaje) {
		super(mensaje);
	}
}
