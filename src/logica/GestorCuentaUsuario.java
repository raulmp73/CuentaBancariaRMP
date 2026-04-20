package logica;

import java.util.ArrayList;
import modelo.Usuario;

public class GestorCuentaUsuario {
    private ArrayList<Usuario> usuarios;

    public GestorCuentaUsuario() {
        this.usuarios = new ArrayList<>();
    }

    public boolean agregarUsuario(Usuario u) {
        return this.usuarios.add(u);
    }

    public Usuario buscarUsuario(String email) {
        for (Usuario u : usuarios) {
            if (u.getEmail() != null && u.getEmail().equals(email)) {
                return u;
            }
        }
        return null;
    }

    public boolean validarUsuario(Usuario u) {
        return this.usuarios.contains(u);
    }

    public Usuario login(String email, String password) {
    	return null;
    }

    public ArrayList<Usuario> getUsuarios() { return usuarios; }
    public void setUsuarios(ArrayList<Usuario> usuarios) { this.usuarios = usuarios; }
}
