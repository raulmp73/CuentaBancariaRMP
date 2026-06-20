package Excepciones;

/**
 * Se lanza cuando los datos para crear un cliente no son válidos (campos
 * obligatorios vacíos, etc.).
 *
 * @author Raul
 * @version 0.2
 */
public class DatosClienteInvalidos extends ExcepcionesBanco {

	private static final long serialVersionUID = 1L;

	public DatosClienteInvalidos(String mensaje) {
		super(mensaje);
	}
}
