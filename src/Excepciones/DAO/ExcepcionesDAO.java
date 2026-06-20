package Excepciones.DAO;

import java.sql.SQLException;

import Excepciones.ExcepcionesBanco;

public class ExcepcionesDAO extends SQLException {
	private static final long serialVersionUID = 1L; 

	public ExcepcionesDAO(String mensaje) {
		super(mensaje);
	}
}

