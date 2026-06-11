package modelo;

import java.util.Date;

public class Empleado extends Usuario {
    private int idEmpleado;
    private double sueldo;


    public Empleado(String dni, String nombre, String apellidos, String telefono, String email, Date fechaRegistro,
			int idEmpleado, double sueldo) {
		super(dni, nombre, apellidos, telefono, email, fechaRegistro);
		this.idEmpleado = idEmpleado;
		this.sueldo = sueldo;
	}

	public void crearCliente() {
    }

    public void modificarCliente() {
    }

    public int getIdEmpleado() { return idEmpleado; }
    public void setIdEmpleado(int idEmpleado) { this.idEmpleado = idEmpleado; }

    public double getSueldo() { return sueldo; }
    public void setSueldo(double sueldo) { this.sueldo = sueldo; }
}
