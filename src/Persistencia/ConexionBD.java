package Persistencia;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBD {

    private String HOST = "192.168.18.58";
    private String PORT = "3306";
    private String DB   = "basedatos";
    private String USER = "rmp";
    private String PASS = "rmp";
    private String URL = "jdbc:mysql://100.82.96.75:3306/basedatos?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }
}
