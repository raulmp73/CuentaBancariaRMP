package modelo;

import java.util.ArrayList;

public class Cliente extends Usuario {
    private int idCliente;
    private ArrayList<CuentaBancaria> cuentasBancarias;

    public ArrayList<CuentaBancaria> getCuentasBancarias() {
		return cuentasBancarias;
	}

	public void setCuentasBancarias(ArrayList<CuentaBancaria> cuentasBancarias) {
		this.cuentasBancarias = cuentasBancarias;
	}

	public Cliente() {
        super();
    }

    public void verCuentas() {
    }

    public void verOperaciones() {
    }

    public int getIdCliente() { return idCliente; }
    public void setIdCliente(int idCliente) { this.idCliente = idCliente; }
}
