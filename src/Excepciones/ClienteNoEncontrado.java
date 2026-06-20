package Excepciones;

/**
 * Se lanza cuando no existe ningún cliente con el dato de búsqueda indicado
 * (por ejemplo, un DNI que no está en la base de datos).
 *
 * @author Raul
 * @version 0.2
 */
public class ClienteNoEncontrado extends ExcepcionesBanco {

	private static final long serialVersionUID = 1L;

	public ClienteNoEncontrado(String mensaje) {
		super(mensaje);
	}
}
