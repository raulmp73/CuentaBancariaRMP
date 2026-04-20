package modelo;

public class Administrador extends Usuario {
    private int nivelAcceso;

    public Administrador() {
        super();
    }

    public void crearEmpleado() {
    }

    public void eliminarEmpleado() {
    }
    
    public void crearCliente() {
	}
    public void eliminarCliente() {
		
	}

    public void gestionarSistema() {
    }

    public int getNivelAcceso() { return nivelAcceso; }
    public void setNivelAcceso(int nivelAcceso) { this.nivelAcceso = nivelAcceso; }
}
