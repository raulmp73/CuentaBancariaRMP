package Util;

import java.util.Scanner;

/**
 * Utilidad para leer datos introducidos por teclado (enteros, decimales y
 * cadenas) validando el tipo.
 *
 * Corregido en la versión 0.2: métodos ReadInt y ReadDouble renombrados a
 * readInt y readDouble (convención de nombres camelCase).
 *
 * @author Raul
 * @version 0.2
 */
public class InputReader {
	
	
	public int readInt(Scanner sc) {
		while (!sc.hasNextInt()) {
			System.out.println("Intentalo de nuevo: ");
			sc.next();
		} 
		return sc.nextInt();
	}
	public double readDouble(Scanner sc) {
		while (!sc.hasNextDouble()) {
			System.out.println("Intentalo de nuevo: ");
			sc.next();
		} 
		return sc.nextDouble();
	}
	public String readString(Scanner sc) {
		String input = sc.nextLine();

		return input.trim();

	}

	/**
	 * Lee una línea de texto completa (por ejemplo un IBAN o un concepto),
	 * absorbiendo el salto de línea que dejan pendiente readInt/readDouble.
	 *
	 * Al mezclar nextInt()/nextDouble() con la lectura de líneas, el Scanner deja
	 * un "\n" sin consumir; si no se limpia, el primer nextLine() devolvería una
	 * cadena vacía. Por eso, si la primera línea viene vacía, se lee la siguiente.
	 *
	 * @param sc scanner de entrada
	 * @return el texto introducido por el usuario, sin espacios sobrantes
	 */
	public String readLinea(Scanner sc) {
		String linea = sc.nextLine(); // consume lo que quede de la línea anterior (p. ej. tras un readInt)

		if (linea.isEmpty()) {
			linea = sc.nextLine(); // solo había un salto pendiente: ahora sí leemos el texto real
		}

		return linea.trim();
	}
	/**
	 * 
	 * @param input
	 * @return
	 */
	public String readSoloLetra(Scanner sc) {
		//TODO: que me lea una linea entera
		String input = sc.nextLine();

		while (!input.chars().allMatch(Character::isLetter)) {
		    System.out.println("Solo puedes usar letras:");
		    input = sc.nextLine();
		   
		}
		return input;

	}
	//public String readString() {
		//String input = sc.next();
		//while (!input.chars().allMatch(Character::isLetter)) {
			//System.out.println("Solo puedes usar letras");
			//input = sc.next();
		//}
		//return input;
	//}
}
