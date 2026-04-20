package Util;

import java.util.Scanner;

public class InputReader {
	
	static Scanner sc = new Scanner(System.in);
	
	public int ReadInt() {
		while (!sc.hasNextInt()) {
			System.out.println("Intentalo de nuevo: ");
			sc.next();
		} 
		return sc.nextInt();
	}
	public double ReadDouble() {
		while (!sc.hasNextDouble()) {
			System.out.println("Intentalo de nuevo: ");
			sc.next();
		} 
		return sc.nextDouble();
	}
	/**
	 * 
	 * @param input
	 * @return
	 */
	public static String readString() {
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
