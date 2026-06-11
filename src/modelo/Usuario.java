package modelo;

import java.util.ArrayList;
import java.util.Date;

/**
 * Clase base abstracta para los usuarios del sistema (cliente, empleado y
 * administrador). Contiene los datos personales comunes a todos ellos.
 *
 * @author Raul
 * @version 0.2
 */
public abstract class Usuario {
    protected String dni;
    protected String nombre;
    protected String apellidos;
    protected String telefono;
    protected String email;
    protected Date fechaRegistro;
    
    
    
    public Usuario(String dni, String nombre, String apellidos, String telefono, String email, Date fechaRegistro) {
		super();
		this.dni = dni;
		this.nombre = nombre;
		this.apellidos = apellidos;
		this.telefono = telefono;
		this.email = email;
		this.fechaRegistro = fechaRegistro;
	}
	public String getDni() { return dni; }
    public void setDni(String dni) { this.dni = dni; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellidos() { return apellidos; }
    public void setApellidos(String apellidos) { this.apellidos = apellidos; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public Date getFechaNacimiento() { return fechaRegistro; }
    public void setFechaNacimiento(Date fechaNacimiento) { this.fechaRegistro = fechaNacimiento; }

    @Override
    public String toString() {
        return "Usuario [dni=" + dni + ", nombre=" + nombre + ", apellidos=" + apellidos + "]";
    }
}
