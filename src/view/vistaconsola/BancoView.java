package view.vistaconsola;

import interfaces.IVista;
import java.util.Scanner;

public class BancoView implements IVista {
    private Scanner sc;

    public BancoView() {
        sc = new Scanner(System.in);
    }

    @Override
    public String pedirEmail() {
        System.out.println("\n--- Inicio de Sesión ---");
        System.out.print("Email: ");
        return sc.nextLine();
    }

    @Override
    public String pedirPassword() {
        System.out.print("Contraseña: ");
        return sc.nextLine();
    }

    @Override
    public int menuAdministrador() {
        System.out.println("\n=====================");
        System.out.println("= Menú Administrador=");
        System.out.println("=====================");
        System.out.println("1. Gestionar empleados");
        System.out.println("2. Ver estadísticas del sistema");
        System.out.println("3. Salir");
        System.out.print("Elije una opción: ");
        return capturarEntero();
    }

    @Override
    public int menuEmpleado() {
        System.out.println("\n=====================");
        System.out.println("=   Menú Empleado   =");
        System.out.println("=====================");
        System.out.println("1. Agregar nuevo cliente / cuenta");
        System.out.println("2. Operar con cuentas de clientes");
        System.out.println("3. Salir");
        System.out.print("Elije una opción: ");
        return capturarEntero();
    }

    @Override
    public int menuCliente() {
        System.out.println("\n=====================");
        System.out.println("=    Menú Cliente   =");
        System.out.println("=====================");
        System.out.println("1. Ver mis cuentas bancarias");
        System.out.println("2. Realizar transferencia");
        System.out.println("3. Salir");
        System.out.print("Elije una opción: ");
        return capturarEntero();
    }

    private int capturarEntero() {
        while (!sc.hasNextInt()) {
            System.out.println("Por favor, introduce un número válido.");
            sc.next();
        }
        int opcion = sc.nextInt();
        sc.nextLine(); // limpiar el buffer
        return opcion;
    }

    @Override
    public void mostrarMensaje(String msg) {
        System.out.println(msg);
    }

    @Override
    public void cerrar() {
        if (sc != null) {
            sc.close();
        }
    }
}
