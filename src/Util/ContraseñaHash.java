package Util;

import org.mindrot.jbcrypt.BCrypt;

/**
 * Utilidad para el manejo de contraseñas: generación de hash y comprobación.
 *
 * Corregido en la versión 0.2: añadido el método {@link #comprobarPassword(String, String)}
 * para validar la contraseña dentro del programa. Como las contraseñas de la base
 * de datos todavía están en texto plano, las compara directamente; si en el futuro
 * se almacenan hasheadas con BCrypt, las verifica con BCrypt automáticamente.
 *
 * @author Raul
 * @version 0.2
 */
public class ContraseñaHash {

	/**
	 * Genera el hash BCrypt de una contraseña.
	 *
	 * @param password contraseña en texto plano
	 * @return hash BCrypt de la contraseña
	 */
	public static String hashPassword(String password) {
		return BCrypt.hashpw(password, BCrypt.gensalt());
	}

	/**
	 * Comprueba una contraseña contra un hash BCrypt.
	 *
	 * @param password contraseña en texto plano
	 * @param hash     hash BCrypt almacenado
	 * @return true si la contraseña coincide con el hash
	 */
	public static boolean checkPassword(String password, String hash) {
		return BCrypt.checkpw(password, hash);
	}

	/**
	 * Comprueba si una contraseña en texto plano coincide con la almacenada en la
	 * base de datos.
	 *
	 * Si el valor almacenado es un hash BCrypt lo verifica con BCrypt; si está en
	 * texto plano (estado actual de la BD) compara los valores directamente.
	 *
	 * @param passwordPlano contraseña introducida por el usuario
	 * @param almacenada    contraseña tal y como está guardada en la BD
	 * @return true si coinciden, false en caso contrario
	 */
	public static boolean comprobarPassword(String passwordPlano, String almacenada) {
		if (passwordPlano == null || almacenada == null) {
			return false;
		}
		if (almacenada.startsWith("$2a$") || almacenada.startsWith("$2b$") || almacenada.startsWith("$2y$")) {
			return BCrypt.checkpw(passwordPlano, almacenada);
		}
		return passwordPlano.equals(almacenada);
	}
}
