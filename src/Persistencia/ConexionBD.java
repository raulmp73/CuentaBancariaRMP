package Persistencia;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import AaTesting.Contraseña;
import Excepciones.DAO.FalloBaseDatos;
import service.Config;

/**
 * Proporciona la conexión con la base de datos MySQL a partir de los parámetros
 * definidos en el fichero de configuración.
 *
 * @author Raul
 * @version 0.2
 */
public class ConexionBD {

    public static Config Config = new Config();

    private static final String URL =
            "jdbc:mysql://"
            + Config.get("db.ip")
            + ":"
            + Config.get("db.puerto")
            + "/"
            + Config.get("db.nombre")
            + Config.get("db.options");

    private static final String USER =
            Config.get("db.user");

    private static final String PASS =
            Config.get("db.password");
    
    public static Connection getConnection()
            throws SQLException {

        Connection con =
                DriverManager.getConnection(URL, USER, PASS);

        if (con == null || con.isClosed()) {

            throw new FalloBaseDatos(
                    "No se pudo conectar con la BD");
        }

        return con;
    }
}
