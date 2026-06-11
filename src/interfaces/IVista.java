package interfaces;

import java.util.ArrayList;

import Persistencia.CuentaBancariaDAO;
import modelo.*;

/**
 * Define las operaciones de la vista: mostrar menús y mensajes y recoger las
 * opciones del usuario.
 *
 * @author Raul
 * @version 0.2
 */
public interface IVista {
    
    Cuenta inicioSesion();
    int menuAdministrador();
    int menuEmpleado();
    int menuCliente(Cliente c);
    void mostrarMensaje(String msg);
    void salirAplicacion();
    void InicioCliente(Cliente c);
    int menuCuentaBancaria(CuentaBancaria cb);
    public void mostrarOperaciones(ArrayList<Operacion> operaciones, int idCuentaActual);
    double pedirImporte(String mensaje);
}
