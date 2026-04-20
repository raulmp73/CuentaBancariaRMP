package modelo;

import java.util.ArrayList;

public class CuentaBancaria extends Usuario {
	private String Titular;
	private double Saldo, Interes;
    private String iban;
    private double saldo;
    private boolean activa = true;
    private ArrayList<Operacion> operaciones;

    public CuentaBancaria() {
        this.operaciones = new ArrayList<>();
    }


    public String getTitular() {
		return Titular;
	}

	public void setTitular(String titular) {
		Titular = titular;
	}

	public double getInteres() {
		return Interes;
	}

	public void setInteres(double interes) {
		Interes = interes;
	}

	public void ingresar(double cantidad) {
        this.saldo += cantidad;
    }

    public void retirar(double cantidad) {
        this.saldo -= cantidad;
    }

    public void transferir(CuentaBancaria destino, double cantidad) {
    }

    public ArrayList<Operacion> getOperaciones() {
        return this.operaciones;
    }

    public boolean isActiva() {
        return this.activa;
    }

    public void activar() {
        this.activa = true;
    }

    public void desactivar() {
        this.activa = false;
    }


    public String getIban() { return iban; }
    public void setIban(String iban) { this.iban = iban; }

    public double getSaldo() { return Saldo; }
    public void setSaldo(double Saldo) { this.Saldo = Saldo; }

    public void setActiva(boolean activa) { this.activa = activa; }

    public void setOperaciones(ArrayList<Operacion> operaciones) { this.operaciones = operaciones; }

    @Override
    public String toString() {
        return "CuentaBancaria [idCuenta="  + ", iban=" + iban + ", saldo=" + saldo + "]";
    }
}
