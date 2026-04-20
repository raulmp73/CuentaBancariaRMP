package logica;

import java.util.ArrayList;
import modelo.Empleado;

public class GestorEmpleado {
    private ArrayList<Empleado> empleados;

    public GestorEmpleado() {
        this.empleados = new ArrayList<>();
    }

    public boolean agregarEmpleado(Empleado e) {
        return this.empleados.add(e);
    }

    public boolean validarEmpleado(Empleado e) {
        return this.empleados.contains(e);
    }

    public ArrayList<Empleado> getEmpleados() { return empleados; }
    public void setEmpleados(ArrayList<Empleado> empleados) { this.empleados = empleados; }
}
