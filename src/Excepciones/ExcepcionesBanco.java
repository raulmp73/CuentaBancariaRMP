package Excepciones;

public class ExcepcionesBanco extends RuntimeException{
	
	private static final long serialVersionUID = 1L; 

	public ExcepcionesBanco(String mensaje) {
		super(mensaje);
	}
}
