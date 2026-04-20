package controlador;

import interfaces.IVista;
import logica.GestorCuentaUsuario;
import modelo.Administrador;
import modelo.Cliente;
import modelo.Empleado;
import modelo.Usuario;
import view.vistaconsola.BancoView;

public class Controlador {
    
    // Ahora el Controlador no depende de vistas fijas, invoca interfaces
    private IVista vista ;
    private GestorCuentaUsuario gestorUsuarios;
    
    // Hacemos que se inyecte la vista desde fuera para mayor abstraccion
    public Controlador() {
        this.vista = new BancoView();
        gestorUsuarios = new GestorCuentaUsuario();
    }


    public void iniciarAplicacion() {
        Usuario usuarioActivo = null;
        
        while (usuarioActivo == null) {
            String email = vista.pedirEmail();
            String password = vista.pedirPassword();

            // Si le dio a salir sin rellenar datos, paramos las validaciones en seco y cerramos
            if(email.isEmpty() && password.isEmpty()) {
                vista.cerrar();
                return;
            }

            usuarioActivo = gestorUsuarios.login(email, password);
            
            if (usuarioActivo == null) {
                vista.mostrarMensaje("Credenciales incorrectas. Intenta de nuevo.");
            }
        }
        
        vista.mostrarMensaje("\n¡Bienvenido al panel, " + usuarioActivo.getNombre() + "!");

        if (usuarioActivo instanceof Administrador) {
            iniciarAdministrador();
        } else if (usuarioActivo instanceof Empleado) {
            iniciarEmpleado();
        } else if (usuarioActivo instanceof Cliente) {
            iniciarCliente();
        }
        
        vista.cerrar();
    }
    
    private void iniciarAdministrador() {
        int opcion;
        do {
            opcion = vista.menuAdministrador();
            switch (opcion) {
                case 1:
                    vista.mostrarMensaje("Has seleccionado: Gestionar empleados (En construcción)");
                    break;
                case 2:
                    vista.mostrarMensaje("Has seleccionado: Ver estadísticas del sistema (En construcción)");
                    break;
                case 3:
                    vista.mostrarMensaje("Saliendo de la cuenta Administrador. ¡Hasta pronto!");
                    break;
                default:
                    vista.mostrarMensaje("Opción inválida. Inténtalo de nuevo.");
            }
        } while (opcion != 3);
    }

    private void iniciarEmpleado() {
        int opcion;
        do {
            opcion = vista.menuEmpleado();
            switch (opcion) {
                case 1:
                    vista.mostrarMensaje("Has seleccionado: Agregar nuevo cliente / cuenta (En construcción)");
                    break;
                case 2:
                    vista.mostrarMensaje("Has seleccionado: Operar con cuentas de cliente (En construcción)");
                    break;
                case 3:
                    vista.mostrarMensaje("Saliendo de la cuenta Empleado. ¡Hasta pronto!");
                    break;
                default:
                    vista.mostrarMensaje("Opción inválida. Inténtalo de nuevo.");
            }
        } while (opcion != 3);
    }

    private void iniciarCliente() {
        int opcion;
        do {
            opcion = vista.menuCliente();
            switch (opcion) {
                case 1:
                    vista.mostrarMensaje("Has seleccionado: Ver mis cuentas bancarias (En construcción)");
                    break;
                case 2:
                    vista.mostrarMensaje("Has seleccionado: Realizar transferencia (En construcción)");
                    break;
                case 3:
                    vista.mostrarMensaje("Saliendo de la cuenta Cliente. ¡Hasta pronto!");
                    break;
                default:
                    vista.mostrarMensaje("Opción inválida. Inténtalo de nuevo.");
            }
        } while (opcion != 3);
    }
}
