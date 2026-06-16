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

    public void pagarConTarjeta(CuentaBancaria c, double cantidad, String concepto) {

        // Validaciones (lanzan excepción si algo no cuadra)
        inputValid.validarImporte(cantidad);            // ImporteInvalido si <= 0
        inputValid.validarSaldoSuficiente(c, cantidad); // SaldoInsuficiente si no hay saldo

        // 1) Guardamos en la BD (saldo + operación PAGO_TARJETA, en una transacción)
        boolean guardado = cuentaBancariaDAO.pagarConTarjeta(c.getId(), cantidad, concepto);
        if (!guardado) {
            throw new ErrorEnOperacion("No se ha podido realizar el pago con tarjeta");
        }

        // 2) Reflejamos el cambio en memoria:
        c.retirar(cantidad);                                             // baja el saldo
        c.setOperaciones(cuentaBancariaDAO.cargarOperaciones(c.getId())); // recarga las operaciones desde la BD
    }

    public void transferir(CuentaBancaria origen, String ibanDestino, double cantidad, String concepto) {
        // Una transferencia es un envío de dinero entre cuentas de tipo TRANSFERENCIA
        enviarDinero(origen, ibanDestino, cantidad, "TRANSFERENCIA", concepto,
                "No se ha podido realizar la transferencia");
    }

    public void enviarBizum(CuentaBancaria origen, String ibanDestino, double cantidad, String concepto) {
        // Un Bizum es el mismo envío de dinero entre cuentas, pero de tipo BIZUM
        enviarDinero(origen, ibanDestino, cantidad, "BIZUM", concepto,
                "No se ha podido realizar el Bizum");
    }

    /**
     * Lógica común a transferencia y Bizum: validar, localizar el destino por su
     * IBAN, guardar el envío en la BD (en una transacción) y refrescar la memoria.
     * Lo único que cambia entre las dos formas de pago es el tipo y los mensajes,
     * que se reciben por parámetro.
     *
     * @param origen       cuenta del usuario desde la que sale el dinero
     * @param ibanDestino  IBAN de la cuenta que recibe el dinero
     * @param cantidad     importe a enviar
     * @param tipo         'TRANSFERENCIA' o 'BIZUM'
     * @param concepto     texto descriptivo de la operación
     * @param mensajeError mensaje a lanzar si la BD no guarda el envío
     */
    private void enviarDinero(CuentaBancaria origen, String ibanDestino, double cantidad,
                              String tipo, String concepto, String mensajeError) {

        // Validaciones (lanzan excepción si algo no cuadra)
        inputValid.validarImporte(cantidad);                 // ImporteInvalido si <= 0
        inputValid.validarSaldoSuficiente(origen, cantidad); // SaldoInsuficiente si no hay saldo

        // Localizamos la cuenta destino por su IBAN y validamos que sea correcta
        int idDestino = cuentaBancariaDAO.buscarIdPorIban(ibanDestino);
        inputValid.validarCuentaDestino(idDestino, origen.getId()); // CuentaDestinoInvalida si no existe o es la propia

        // 1) Guardamos en la BD (resta origen + suma destino + operación, en una transacción)
        boolean guardado = cuentaBancariaDAO.transferir(origen.getId(), idDestino, cantidad, tipo, concepto);
        if (!guardado) {
            throw new ErrorEnOperacion(mensajeError);
        }

        // 2) Reflejamos el cambio en memoria (solo la cuenta de origen, que es la del usuario):
        origen.retirar(cantidad);                                              // baja el saldo del origen
        origen.setOperaciones(cuentaBancariaDAO.cargarOperaciones(origen.getId())); // recarga las operaciones desde la BD
    }

    public ArrayList<CuentaBancaria> getCuenta() { return cuentas; }
    
    public CuentaBancaria buscarCuenta(int i) {
		
    	inputValid.ValidarExistenciaCuenta(i, cuentas);
    	
    	return cuentas.get(i);
	}
    public void setCuentas(ArrayList<CuentaBancaria> cuentas) { this.cuentas = cuentas; }
}
