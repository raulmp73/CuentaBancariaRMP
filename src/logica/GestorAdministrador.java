package logica;

import java.util.ArrayList;
import java.util.List;

import modelo.Administrador;
import modelo.Usuario;

/**
 * Gestiona la lógica relacionada con los administradores del sistema.
 *
 * @author Raul
 * @version 0.2
 */
public class GestorAdministrador {
    private ArrayList<Administrador> administradores;

    public GestorAdministrador() {
        this.administradores = new ArrayList<>();
    }

    public boolean agregarAdministrador(Administrador a) {
        return this.administradores.add(a);
    }

    public boolean validarAdministrador(Administrador a) {
        return this.administradores.contains(a);
    }

    public void gestionarSistema() {
    }

    public List<Usuario> verUsuarios() {
        return new ArrayList<>();
    }

    public ArrayList<Administrador> getAdministradores() { return administradores; }
    public void setAdministradores(ArrayList<Administrador> administradores) { this.administradores = administradores; }
    
}
