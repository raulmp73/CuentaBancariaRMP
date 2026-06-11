package service;

import java.io.FileInputStream;
import java.util.Properties;

/**
 * Carga la configuración de la aplicación desde el fichero config.properties y
 * permite consultar sus valores.
 *
 * Corregido en la versión 0.2: eliminados los System.out.println que mostraban
 * por consola datos de conexión (no imprimir credenciales).
 *
 * @author Raul
 * @version 0.2
 */
public class Config {
    
        private static Properties properties =
                new Properties();
     public Config() {
        try {
            FileInputStream fis = new FileInputStream("config.properties");
            properties.load(fis);

        } catch (Exception e) {
            System.out.println("NO SE HA PODIDO LEER EL PROPERTIES");
            e.printStackTrace();
        }
    }

    public String get(String clave) {
        return properties.getProperty(clave);
    }

}