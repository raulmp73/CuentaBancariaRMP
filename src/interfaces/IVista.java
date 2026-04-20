package interfaces;

public interface IVista {
    String pedirEmail();
    String pedirPassword();
    int menuAdministrador();
    int menuEmpleado();
    int menuCliente();
    void mostrarMensaje(String msg);
    void cerrar();
}
