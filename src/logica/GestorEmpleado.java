package logica;

import java.util.Random;

import Excepciones.ClienteNoEncontrado;
import Excepciones.ErrorEnOperacion;
import Excepciones.ImporteInvalido;
import Persistencia.ClienteDAO;
import Persistencia.CuentaBancariaDAO;
import Util.ContraseñaHash;
import Validadores.InputValid;
import modelo.Cliente;
import modelo.Cuenta;
import modelo.CuentaBancaria;

/**
 * Gestiona la lógica de empleado: crear clientes, añadirles cuentas bancarias,
 * localizarlos por DNI y dar dinero en efectivo. Para ver/operar las cuentas de
 * un cliente reutiliza {@link GestorCliente} y {@link GestorCuentaBancaria}.
 *
 * @author Raul
 * @version 0.2
 */
public class GestorEmpleado {

    private ClienteDAO clienteDAO = new ClienteDAO();
    private CuentaBancariaDAO cuentaBancariaDAO = new CuentaBancariaDAO();
    private InputValid inputValid = new InputValid();

    // Reutilizamos los gestores del cliente para cargar y operar sus cuentas
    private GestorCliente gestorCliente = new GestorCliente();
    private GestorCuentaBancaria gestorCuentaBancaria = new GestorCuentaBancaria();

    private Random random = new Random();

    /**
     * Crea un cliente nuevo (sin cuentas todavía). Valida los datos y la cuenta de
     * acceso, cifra la contraseña con BCrypt y delega la inserción en el DAO.
     *
     * @throws Excepciones.DatosClienteInvalidos  si faltan datos obligatorios
     * @throws Excepciones.EmailyContraseñaInvalidos si el email o la contraseña no valen
     * @throws ErrorEnOperacion si la BD no pudo crear el cliente (DNI/email/usuario repetidos)
     */
    public void crearCliente(String dni, String nombre, String apellido, String telefono,
                             String email, String usuario, String password) {

        // Validaciones de negocio (lanzan excepción si algo no cuadra)
        inputValid.validarDatosCliente(dni, nombre, apellido); // campos obligatorios
        inputValid.validarDni(dni);                            // 8 números + letra de control
        inputValid.validarCuenta(new Cuenta(email, password)); // email con @ + contraseña válida

        // Ciframos la contraseña antes de guardarla (BCrypt)
        String hash = ContraseñaHash.hashPassword(password);

        int idCliente = clienteDAO.crearCliente(dni, nombre, apellido, telefono, email, usuario, hash);
        if (idCliente == -1) {
            throw new ErrorEnOperacion("No se ha podido crear el cliente (¿DNI, email o usuario ya existen?)");
        }
    }

    /**
     * Añade una cuenta bancaria nueva a un cliente identificado por su DNI. Genera
     * el número de cuenta, el IBAN y el cvv, y delega la inserción en el DAO.
     *
     * @param dni          DNI del cliente
     * @param saldoInicial saldo con el que se abre la cuenta (>= 0)
     * @throws ClienteNoEncontrado si no hay cliente con ese DNI
     * @throws ImporteInvalido     si el saldo inicial es negativo
     * @throws ErrorEnOperacion    si la BD no pudo crear la cuenta
     */
    public void anadirCuentaACliente(String dni, double saldoInicial) {

        int idCliente = buscarIdClientePorDni(dni); // lanza ClienteNoEncontrado si no existe

        if (saldoInicial < 0) {
            throw new ImporteInvalido("El saldo inicial no puede ser negativo");
        }

        String numeroCuenta = generarNumeroCuenta();
        String iban = generarIban(numeroCuenta);
        String cvv = generarCvv();

        int idCuenta = cuentaBancariaDAO.crearCuentaParaCliente(idCliente, numeroCuenta, iban, cvv, saldoInicial);
        if (idCuenta == -1) {
            throw new ErrorEnOperacion("No se ha podido crear la cuenta para el cliente");
        }
    }

    /**
     * Busca el id de un cliente por su DNI.
     *
     * @param dni DNI a buscar
     * @return id del cliente
     * @throws ClienteNoEncontrado si no existe ningún cliente con ese DNI
     */
    public int buscarIdClientePorDni(String dni) {
        int id = clienteDAO.buscarIdPorDni(dni);
        if (id == -1) {
            throw new ClienteNoEncontrado("No existe ningún cliente con el DNI " + dni);
        }
        return id;
    }

    /**
     * Carga un cliente (por DNI) con sus cuentas, dejándolas listas para operar.
     *
     * @param dni DNI del cliente
     * @return el cliente con sus cuentas cargadas
     * @throws ClienteNoEncontrado si no existe ningún cliente con ese DNI
     */
    public Cliente cargarClientePorDni(String dni) {
        int idCliente = buscarIdClientePorDni(dni);
        gestorCliente.cargarCliente(idCliente);
        gestorCuentaBancaria.cargarCuentas(gestorCliente.cogerCliente().getCuentasBancarias());
        return gestorCliente.cogerCliente();
    }

    /**
     * Devuelve la cuenta en la posición indicada del cliente cargado.
     */
    public CuentaBancaria buscarCuenta(int indice) {
        return gestorCuentaBancaria.buscarCuenta(indice);
    }

    /**
     * Entrega dinero en efectivo desde una cuenta (retirada de ventanilla).
     */
    public void darEfectivo(CuentaBancaria cuenta, double cantidad) {
        gestorCuentaBancaria.darEfectivo(cuenta, cantidad);
    }

    // --- Generadores de datos para una cuenta nueva ---

    private String generarNumeroCuenta() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 12; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }

    private String generarIban(String numeroCuenta) {
        // IBAN español simplificado: ES + 2 dígitos de control + 12 (nº cuenta) + 8 = 24 caracteres
        int control = 10 + random.nextInt(90);
        return "ES" + control + numeroCuenta + "00000000";
    }

    private String generarCvv() {
        return String.format("%03d", random.nextInt(1000));
    }
}
