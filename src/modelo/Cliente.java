package modelo;

import java.util.ArrayList;
import java.util.Date;

public class Cliente extends Usuario {
    private int idCliente;
    private ArrayList<CuentaBancaria> cuentasBancarias;

    
    /**
     * SELECT 
    a.id_usuario,
    u.nombre,
    u.apellido,
    u.telefono,
    u.
    a.id_cuenta
FROM usuario u
JOIN cuenta_usuario a ON u.id_usuario = a.id_cliente
WHERE u.id_usuario = 1;
     * @param dni
     * @param nombre
     * @param apellidos
     * @param telefono
     * @param email
     * @param fechaRegistro
     * @param idCliente
     * @param cuentasBancarias
     */
    
    public Cliente(String dni, String nombre, String apellidos, String telefono, String email, Date fechaRegistro,
			int idCliente, ArrayList<CuentaBancaria> cuentasBancarias) {
		super(dni, nombre, apellidos, telefono, email, fechaRegistro);
		this.idCliente = idCliente;
		this.cuentasBancarias = cuentasBancarias;
	}
    
    @Override
    public String toString() {
        return  "┌──────────────────────── CLIENTE ────────────────────────┐\n" +
                "│ ID: " + idCliente + "\n" +
                "│ DNI: " + dni + "\n" +
                "│ Nombre: " + nombre + " " + apellidos + "\n" +
                "│ Teléfono: " + telefono + "\n" +
                "│ Email: " + email + "\n" +
                "│ Fecha registro: " + getFechaNacimiento() + "\n" +
                "│───────────────────── CUENTAS ───────────────────────────│\n" +
                "│ "  + "\n" +
                "└─────────────────────────────────────────────────────────┘";
    }

	public ArrayList<CuentaBancaria> getCuentasBancarias() {
		return cuentasBancarias;
	}

	public void setCuentasBancarias(ArrayList<CuentaBancaria> cuentasBancarias) {
		this.cuentasBancarias = cuentasBancarias;
	}



    public void verCuentas() {
    }

    public void verOperaciones() {
    }

    public int getIdCliente() { return idCliente; }
    public void setIdCliente(int idCliente) { this.idCliente = idCliente; }
}
