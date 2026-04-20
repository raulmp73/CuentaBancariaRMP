package modelo;

import java.util.ArrayList;

public class Cuenta {
	
	private String titular;
	private int NumCuenta;
    private String password;
    private String username;
    private boolean estado;
    private ArrayList<CuentaBancaria> cuentasBancarias;
	
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public boolean isEstado() {
		return estado;
	}

	public void setEstado(boolean estado) {
		this.estado = estado;
	}

	public ArrayList<CuentaBancaria> getCuentasBancarias() {
		return cuentasBancarias;
	}

	public void setCuentasBancarias(ArrayList<CuentaBancaria> cuentasBancarias) {
		this.cuentasBancarias = cuentasBancarias;
	}

	public Cuenta() {
		NumCuenta=0;
		
	}

	public Cuenta(String titular, double saldo, double interes, int numCuenta) {
		super();
		NumCuenta = numCuenta;
	}
	/**
	 * 
	 */
	//TODO: mover esto a transferencia 
	public String Ingresar(Cuenta C, double cantidad) {
		C.setSaldo(cantidad+C.getSaldo());
		return "El nuevo saldo es de: "+C.getSaldo();
	}
	public String Reintegro(Cuenta C, double cantidad) {
		if (cantidad>C.getSaldo()) {
			C.setSaldo(cantidad+C.getSaldo());
			return "El nuevo saldo es de: "+C.getSaldo();
		} else {
			return "Error Cantidad insuficiente ";
		}
	}
	

	/**
	 * @return the titular
	 */
	public String getTitular() {
		return Titular;
	}

	/**
	 * @param titular the titular to set
	 */
	public void setTitular(String titular) {
		Titular = titular;
	}

	/**
	 * @return the saldo
	 */
	public double getSaldo() {
		return Saldo;
	}

	/**
	 * @param saldo the saldo to set
	 */
	public void setSaldo(double saldo) {
		Saldo = saldo;
	}

	/**
	 * @return the interes
	 */
	public double getInteres() {
		return Interes;
	}

	/**
	 * @param interes the interes to set
	 */
	public void setInteres(double interes) {
		Interes = interes;
	}

	/**
	 * @return the numCuenta
	 */
	public int getNumCuenta() {
		return NumCuenta;
	}

	/**
	 * @param numCuenta the numCuenta to set
	 */
	public void setNumCuenta(int numCuenta) {
		NumCuenta = numCuenta;
	}


	
	
}
