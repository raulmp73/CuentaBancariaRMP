package modelo;

public class Empleado extends Usuario {
    private int idEmpleado;
    private double sueldo;

    public Empleado() {
        super();
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
