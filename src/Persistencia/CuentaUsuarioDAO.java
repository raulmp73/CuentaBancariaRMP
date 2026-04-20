package Persistencia;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import modelo.Cliente;

public class CuentaUsuarioDAO {

	String comando;
	ConexionBD BD;
	ArrayList<Cliente> listaCliente;

	public void guardarClientes() throws SQLException {
        comando = "SELECT ID, NOMBRE, PRECIO FROM producto ORDER BY ID";
        listaCliente = new ArrayList<Cliente>();

        try (Connection con = BD.getConnection();
             PreparedStatement ps = con.prepareStatement(comando);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String nombre = rs.getString("nombre");
                BigDecimal precio = rs.getBigDecimal("precio");

                //listaCliente.add(new Cliente(id, nombre, precio));
            }
        }
	}
}
