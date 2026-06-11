package logica;

import java.util.ArrayList;

import Excepciones.ErrorEnOperacion;
import Persistencia.CuentaBancariaDAO;
import Validadores.InputValid;
import modelo.CuentaBancaria;

/**
 * Gestiona la lógica de las cuentas bancarias: carga, búsqueda, ingresos,
 * retiros y transferencias.
 *
 * @author Raul
 * @version 0.2
 */
public class GestorCuentaBancaria {
    private ArrayList<CuentaBancaria> cuentas;
	private InputValid inputValid ;
	private CuentaBancariaDAO cuentaBancariaDAO;


    public GestorCuentaBancaria() {
        this.cuentas = new ArrayList<>();
        this.inputValid = new InputValid();
        this.cuentaBancariaDAO = new CuentaBancariaDAO();
    }

    public void cargarCuentas(ArrayList<CuentaBancaria> cuenta) {
        cuentas = cuenta;
    }

    public void ingresar(CuentaBancaria c, double cantidad) {

        // Validación: el importe debe ser mayor que 0 (lanza ImporteInvalido)
        inputValid.validarImporte(cantidad);

        // 1) Guardamos en la BD (saldo + operación, en una transacción)
        boolean guardado = cuentaBancariaDAO.ingresar(c.getId(), cantidad, "Ingreso de efectivo");
        if (!guardado) {
            throw new ErrorEnOperacion("No se ha podido realizar el ingreso");
        }

        // 2) Reflejamos el cambio en memoria:
        c.ingresar(cantidad);                                             // sube el saldo
        c.setOperaciones(cuentaBancariaDAO.cargarOperaciones(c.getId())); // recarga las operaciones desde la BD
    }

    public void retirar(CuentaBancaria c, double cantidad) {

        // Validaciones (lanzan excepción si algo no cuadra)
        inputValid.validarImporte(cantidad);            // ImporteInvalido si <= 0
        inputValid.validarSaldoSuficiente(c, cantidad); // SaldoInsuficiente si no hay saldo

        // 1) Guardamos en la BD (saldo + operación, en una transacción)
        boolean guardado = cuentaBancariaDAO.retirar(c.getId(), cantidad, "Retirada de efectivo");
        if (!guardado) {
            throw new ErrorEnOperacion("No se ha podido realizar la retirada");
        }

        // 2) Reflejamos el cambio en memoria:
        c.retirar(cantidad);                                             // baja el saldo
        c.setOperaciones(cuentaBancariaDAO.cargarOperaciones(c.getId())); // recarga las operaciones desde la BD
    }

    public boolean transferir(CuentaBancaria origen, CuentaBancaria destino, double cantidad) {
        if (cuentas.contains(origen) && origen.getSaldo() >= cantidad) {
            origen.retirar(cantidad);
            destino.ingresar(cantidad);
            return true;
        }
        return false;
    }

    public ArrayList<CuentaBancaria> getCuenta() { return cuentas; }
    
    public CuentaBancaria buscarCuenta(int i) {
		
    	inputValid.ValidarExistenciaCuenta(i, cuentas);
    	
    	return cuentas.get(i);
	}
    public void setCuentas(ArrayList<CuentaBancaria> cuentas) { this.cuentas = cuentas; }
}
