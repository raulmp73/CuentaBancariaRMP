package app;

import controlador.Controlador;
import interfaces.IVista;

/**
 * Punto de entrada de la aplicación. Crea el controlador e inicia la ejecución.
 *
 * Corregido en la versión 0.2: actualizada la llamada a controlador.iniciar
 * (convención de nombres camelCase).
 *
 * @author Raul
 * @version 0.2
 */
public class Main {
	/**
	 * 
	 * TODO: subir Version 0.1 (entrega 1)
	 * TODO: Archivo txt fallos try catch
	 */
    public static void main(String[] args) {
        
        Controlador controlador = new Controlador();
        controlador.iniciar();
    }
}
