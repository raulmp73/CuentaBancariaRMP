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
