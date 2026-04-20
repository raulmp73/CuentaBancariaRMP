package logica;

import java.util.ArrayList;
import modelo.Cliente;

public class GestorCliente {
    private ArrayList<Cliente> clientes;

    public GestorCliente() {
        this.clientes = new ArrayList<>();
    }

    public boolean agregarCliente(Cliente c) {
        return this.clientes.add(c);
    }

    public Cliente buscarCliente(Cliente c) {
        int index = clientes.indexOf(c);
        if (index >= 0) {
            return clientes.get(index);
        }
        return null;
    }

    public boolean validarCliente(Cliente c) {
        return this.clientes.contains(c);
    }

    public ArrayList<Cliente> getClientes() { return clientes; }
    public void setClientes(ArrayList<Cliente> clientes) { this.clientes = clientes; }
}
