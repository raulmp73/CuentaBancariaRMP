package view.vistaconsola;

import interfaces.IVista;
import modelo.Cliente;
import modelo.*;
import modelo.CuentaBancaria;

import java.util.ArrayList;
import java.util.Scanner;

import Util.InputReader;

/**
 * Vista de consola del banco. Implementa {@link IVista} y se encarga de mostrar
 * los menús y de leer las opciones introducidas por el usuario.
 *
 * Corregido en la versión 0.2: actualizadas las llamadas a input.readInt
 * (convención de nombres camelCase).
 *
 * @author Raul
 * @version 0.2
 */
public class BancoView implements IVista {

    private Scanner sc;
    private InputReader input;
    private Mensajes msj;

    public BancoView() {
        sc = new Scanner(System.in);
        input = new InputReader();
        msj = new Mensajes();
    }

    @Override
    public Cuenta inicioSesion() {
        msj.msj("\n--- Bienvenido a la app del banco ---\n");

        msj.msjSinSalto("Email: ");
        String email = input.readString(sc);

        msj.msjSinSalto("Contraseña: ");
        String password = input.readString(sc);

        return new Cuenta(email, password);
    }

    @Override
    public int menuAdministrador() {
        msj.msj("\n======================");
        msj.msj("= Menú Administrador =");
        msj.msj("======================");
        msj.msj("1. Gestionar empleados");
        msj.msj("2. Ver estadísticas del sistema");
        msj.msj("3. Salir");
        msj.msjSinSalto("\nElige una opción: ");
        return capturarEntero();
    }

    @Override
    public int menuEmpleado() {
        msj.msj("\n===================");
        msj.msj("= Menú Empleado   =");
        msj.msj("===================");
        msj.msj("1. Agregar nuevo cliente / cuenta");
        msj.msj("2. Operar con cuentas de clientes");
        msj.msj("3. Salir");
        msj.msjSinSalto("\nElige una opción: ");
        return capturarEntero();
    }

    @Override
    public int menuCliente(Cliente c) {

        msj.msj("\n╔════════════════════════════════════════════╗");
        msj.msj("║                 APP BANCO                  ║");
        msj.msj("╠════════════════════════════════════════════╣");
        msj.msj("  Bienvenido/a: " + c.getNombre() + " " + c.getApellidos());
        msj.msj("╚════════════════════════════════════════════╝");

        msj.msj("\nTUS CUENTAS");
        msj.msj("────────────────────────────────────────────");

        if (c.getCuentasBancarias() == null || c.getCuentasBancarias().isEmpty()) {
            msj.msj("No tienes cuentas bancarias.");
        } else {
            for (int i = 0; i < c.getCuentasBancarias().size(); i++) {
                CuentaBancaria cuenta = c.getCuentasBancarias().get(i);
                msj.msj((i + 1) + ". IBAN: " + cuenta.getIban());
            }
        }

        msj.msj("0. Salir aplicación");
        msj.msj("────────────────────────────────────────────");
        msj.msjSinSalto("Elige una opción: ");

        return input.readInt(sc);
    }

    private int capturarEntero() {
        while (!sc.hasNextInt()) {
            msj.msj("Por favor, introduce un número válido.");
            sc.next();
        }

        int opcion = sc.nextInt();
        sc.nextLine();

        return opcion;
    }

    @Override
    public void mostrarMensaje(String msg) {
        msj.msj(msg);
    }

    @Override
    public double pedirImporte(String mensaje) {
        msj.msjSinSalto(mensaje);
        return input.readDouble(sc);
    }

    @Override
    public void salirAplicacion() {
        msj.msj("\nHas salido de la aplicación. ¡Hasta pronto!");
        sc.close();
    }

    @Override
    public void InicioCliente(Cliente c) {

    }

    @Override
    public int menuCuentaBancaria(CuentaBancaria cb) {

        msj.msj("\n╔════════════════════════════════════════════╗");
        msj.msj("║              CUENTA BANCARIA              ║");
        msj.msj("╠════════════════════════════════════════════╣");
        msj.msj("  IBAN:  " + cb.getIban());
        msj.msj("  Saldo: " + cb.getSaldo() + " €");
        msj.msj("╠════════════════════════════════════════════╣");
        msj.msj("║                OPERACIONES                ║");
        msj.msj("╠════════════════════════════════════════════╣");
        msj.msj("  1. Ver operaciones");
        msj.msj("  2. Ingresar dinero");
        msj.msj("  3. Retirar dinero");
        msj.msj("  4. Enviar dinero");
        msj.msj("  0. Salir");
        msj.msj("╚════════════════════════════════════════════╝");

        msj.msjSinSalto("Elige una opción: ");
        return input.readInt(sc);
    }
    @Override
    public void mostrarOperaciones(ArrayList<Operacion> operaciones, int idCuentaActual) {

        if (operaciones == null || operaciones.isEmpty()) {
            msj.msj("\nNo hay operaciones registradas.");
            return;
        }

        msj.msj("\n══════════════════════════════════════");
        msj.msj("         HISTORIAL DE OPERACIONES");
        msj.msj("══════════════════════════════════════");

        for (Operacion op : operaciones) {

            boolean esSalida = op.getIdCuentaOperacion() == idCuentaActual;
            boolean esEntrada = op.getIdCuentaDestino() != null 
                    && op.getIdCuentaDestino() == idCuentaActual;

            msj.msj("──────────────────────────────────────");

            switch (op.getTipo()) {

                case INGRESO:
                    msj.msj("Tipo: Ingreso de dinero");
                    msj.msj("Concepto: " + op.getConcepto());
                    msj.msj("Fecha: " + op.getFechaHora());
                    msj.msj("Importe: + " + op.getImporte() + " €");
                    break;

                case REINTEGRO:
                case RETIRO:
                    msj.msj("Tipo: Retirada de dinero");
                    msj.msj("Concepto: " + op.getConcepto());
                    msj.msj("Fecha: " + op.getFechaHora());
                    msj.msj("Importe: - " + op.getImporte() + " €");
                    break;

                case TRANSFERENCIA:
                    if (esSalida) {
                        msj.msj("Tipo: Transferencia enviada");
                        msj.msj("Cuenta destino: " + op.getIdCuentaDestino());
                        msj.msj("Concepto: " + op.getConcepto());
                        msj.msj("Fecha: " + op.getFechaHora());
                        msj.msj("Importe: - " + op.getImporte() + " €");
                    } else if (esEntrada) {
                        msj.msj("Tipo: Transferencia recibida");
                        msj.msj("Cuenta origen: " + op.getIdCuentaOperacion());
                        msj.msj("Concepto: " + op.getConcepto());
                        msj.msj("Fecha: " + op.getFechaHora());
                        msj.msj("Importe: + " + op.getImporte() + " €");
                    }
                    break;

                case BIZUM:
                    if (esSalida) {
                        msj.msj("Tipo: Bizum enviado");
                        msj.msj("Cuenta destino: " + op.getIdCuentaDestino());
                        msj.msj("Concepto: " + op.getConcepto());
                        msj.msj("Fecha: " + op.getFechaHora());
                        msj.msj("Importe: - " + op.getImporte() + " €");
                    } else if (esEntrada) {
                        msj.msj("Tipo: Bizum recibido");
                        msj.msj("Cuenta origen: " + op.getIdCuentaOperacion());
                        msj.msj("Concepto: " + op.getConcepto());
                        msj.msj("Fecha: " + op.getFechaHora());
                        msj.msj("Importe: + " + op.getImporte() + " €");
                    }
                    break;

                case TARJETA:
                case PAGO_TARJETA:
                    msj.msj("Tipo: Pago con tarjeta");
                    msj.msj("Concepto: " + op.getConcepto());
                    msj.msj("Fecha: " + op.getFechaHora());
                    msj.msj("Importe: - " + op.getImporte() + " €");
                    break;

                default:
                    msj.msj("Tipo: " + op.getTipo());
                    msj.msj("Concepto: " + op.getConcepto());
                    msj.msj("Fecha: " + op.getFechaHora());

                    if (esSalida) {
                        msj.msj("Importe: - " + op.getImporte() + " €");
                    } else {
                        msj.msj("Importe: + " + op.getImporte() + " €");
                    }
                    break;
            }
        }

        msj.msj("──────────────────────────────────────");
    }
}