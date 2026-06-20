package modelo;

import java.util.ArrayList;
import java.util.Date;

/**
 * Representa una cuenta bancaria con su saldo, IBAN, estado y operaciones asociadas.
 *
 * Corregido en la versión 0.2: eliminado el campo duplicado "Saldo"; ahora solo
 * existe el campo "saldo".
 *
 * @author Raul
 * @version 0.2
 */
public class CuentaBancaria{
    private int id;
    private String iban;
    private double saldo;
    private boolean activa = true;
    private ArrayList<Operacion> operaciones;



	public CuentaBancaria(int id, double saldo, String iban, boolean activa,
			ArrayList<Operacion> operaciones) {
		this.id = id;
		this.saldo = saldo;
		this.iban = iban;
		this.activa = activa;
		this.operaciones = operaciones;
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


    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getIban() { return iban; }
    public void setIban(String iban) { this.iban = iban; }

    public double getSaldo() { return saldo; }
    public void setSaldo(double saldo) { this.saldo = saldo; }

    public void setActiva(boolean activa) { this.activa = activa; }

    public void setOperaciones(ArrayList<Operacion> operaciones) { this.operaciones = operaciones; }

    @Override
    public String toString() {
        return "CuentaBancaria [idCuenta=" + id + ", iban=" + iban + ", saldo=" + saldo + "]";
    }
}
