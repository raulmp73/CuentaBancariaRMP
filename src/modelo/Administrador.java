package modelo;

import java.util.Date;

public class Administrador extends Usuario {
    private int nivelAcceso;

    

    public Administrador(String dni, String nombre, String apellidos, String telefono, String email, Date fechaRegistro,
			int nivelAcceso) {
		super(dni, nombre, apellidos, telefono, email, fechaRegistro);
		this.nivelAcceso = nivelAcceso;
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
