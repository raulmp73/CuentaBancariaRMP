package Persistencia;

import java.sql.Connection;
import java.sql.SQLException;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import service.Config;

/**
 * Proporciona conexiones a la base de datos MySQL mediante un POOL de conexiones
 * (HikariCP), en lugar de abrir una conexión física nueva en cada consulta.
 *
 * El pool se crea una sola vez (bloque static, patrón singleton) a partir de los
 * parámetros de config.properties; {@link #getConnection()} entrega una conexión
 * del pool y, al cerrarla en el try-with-resources, vuelve al pool para reutilizarse.
 *
 * Corregido en la versión 0.3: sustituido DriverManager (una conexión por consulta)
 * por HikariCP. La firma de getConnection() no cambia, así que los DAOs siguen
 * igual.
 *
 * @author Raul
 * @version 0.3
 */
public class ConexionBD {

    // Lee los parámetros desde config.properties
    private static final Config config = new Config();

    // Pool de conexiones global (una sola instancia en toda la aplicación)
    private static final HikariDataSource ds;

    static {
        // Silenciamos los logs internos de HikariCP (dejamos solo avisos/errores),
        // para no ensuciar la consola de la aplicación.
        java.util.logging.Logger.getLogger("com.zaxxer.hikari")
                .setLevel(java.util.logging.Level.WARNING);

        // URL construida desde config.properties (db.ip, db.puerto, db.nombre, db.options)
        String url = "jdbc:mysql://"
                + config.get("db.ip") + ":"
                + config.get("db.puerto") + "/"
                + config.get("db.nombre")
                + config.get("db.options");

        HikariConfig hikari = new HikariConfig();
        hikari.setJdbcUrl(url);
        hikari.setUsername(config.get("db.user"));
        hikari.setPassword(config.get("db.password"));

        hikari.setMaximumPoolSize(10);      // máximo de conexiones simultáneas
        hikari.setMinimumIdle(2);           // conexiones siempre listas
        hikari.setConnectionTimeout(30000); // espera máx. 30s por una conexión
        hikari.setPoolName("CuentaBancaria-Pool");

        ds = new HikariDataSource(hikari);
    }

    /**
     * Devuelve una conexión del pool (la reutiliza si hay alguna libre). Se usa
     * exactamente igual que antes: dentro de un try-with-resources.
     *
     * @return conexión lista para usar
     * @throws SQLException si el pool no puede entregar una conexión
     */
    public static Connection getConnection() throws SQLException {
        return ds.getConnection();
    }

    /**
     * Cierra el pool de conexiones. Conviene llamarlo al salir de la aplicación.
     */
    public static void cerrarPool() {
        if (ds != null && !ds.isClosed()) {
            ds.close();
        }
    }
}
