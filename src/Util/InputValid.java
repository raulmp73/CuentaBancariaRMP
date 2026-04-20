package Util;


public class InputValid {
	
	InputReader read = new InputReader();
	
	public int ValidarPositivoInt() {
		int opcion=-1;
		while (opcion<0) {
			opcion=read.ReadInt();
			if (opcion<=0) {
				System.out.println("Has escrito un numero negativo");
			}
		}
		return opcion;
	}
	public double ValidarPositivoDouble() {
		double opcion=-1;
		while (opcion<0) {
			opcion=read.ReadDouble();
			if (opcion<=0) {
				System.out.println("Has escrito un numero negativo");
			}
		}
		return opcion;
	}
}
