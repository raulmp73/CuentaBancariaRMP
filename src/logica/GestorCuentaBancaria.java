package logica;

import java.util.ArrayList;
import modelo.CuentaBancaria;

public class GestorCuentaBancaria {
    private ArrayList<CuentaBancaria> cuentas;

    public GestorCuentaBancaria() {
        this.cuentas = new ArrayList<>();
    }

    public boolean crearCuenta(CuentaBancaria c) {
        return this.cuentas.add(c);
    }

    public boolean ingresar(CuentaBancaria c, double cantidad) {
        if (cuentas.contains(c)) {
            c.ingresar(cantidad);
            return true;
        }
        return false;
    }

    public boolean retirar(CuentaBancaria c, double cantidad) {
        if (cuentas.contains(c)) {
            c.retirar(cantidad);
            return true;
        }
        return false;
    }

    public boolean transferir(CuentaBancaria origen, CuentaBancaria destino, double cantidad) {
        if (cuentas.contains(origen) && origen.getSaldo() >= cantidad) {
            origen.retirar(cantidad);
            destino.ingresar(cantidad);
            return true;
        }
        return false;
    }

    public ArrayList<CuentaBancaria> getCuentas() { return cuentas; }
    public void setCuentas(ArrayList<CuentaBancaria> cuentas) { this.cuentas = cuentas; }
}
