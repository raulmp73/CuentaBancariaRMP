package app;

import controlador.Controlador;
import interfaces.IVista;

public class Main {
    public static void main(String[] args) {
        
        // Creamos la nueva UI visual por Swing que es irrompible. 
        // ¡Si necesitas cambiar a consola, solo pon new BancoView() !
        
        Controlador controlador = new Controlador();
        controlador.iniciarAplicacion();
    }
}
