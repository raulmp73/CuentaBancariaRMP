package modelo;

import java.util.ArrayList;

/**
 * Representa la cuenta de acceso de un usuario (credenciales y datos básicos de
 * login) junto con su titular y tipo.
 *
 * Corregido en la versión 0.2: el método setTitular se asignaba a sí mismo;
 * ahora asigna correctamente this.titular.
 *
 * @author Raul
 * @version 0.2
 */
public class Cuenta {
	
	private String titular;
	private int NumCuenta;
    private String password;
    private String username;
    private String email;
    private boolean estado;
    private Usuario usuario;
    private String tipo;
    
   
    
    

	public Cuenta(String titular, int numCuenta, String password, String username, String email, boolean estado, Usuario usuario) {
		this.titular = titular;
		NumCuenta = numCuenta;
		this.password = password;
		this.username = username;
		this.email = email;
		this.estado = estado;
		this.usuario = usuario;
	}
	/**
	 * 
	 * @param email
	 * @param contraseña
	 * @param id
	 * @param tipo
	 * 
	 * @version 0.1
	 */
	
	public Cuenta(String email, String contraseña) {
		this.email = email;
		this.password = contraseña;
	}
	
	public Cuenta(int numCuenta, String email, String password, String tipo) {
		super();
		NumCuenta = numCuenta;
		this.email = email;
		this.password = password;
		this.tipo = tipo;
	}
	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public boolean isEstado() {
		return estado;
	}

	public void setEstado(boolean estado) {
		this.estado = estado;
	}


	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Cuenta() {
		NumCuenta=0;
		
	}

	public Cuenta(String titular, double saldo, double interes, int numCuenta) {
		super();
		NumCuenta = numCuenta;
	}
	/**
	 * 
	 */
	//TODO: mover esto a transferencia 
	/**
	 * 
	 * @param C
	 * @param cantidad
	 * @return
	 * 
	 * public String Ingresar(Cuenta C, double cantidad) {
		C.setSaldo(cantidad+C.getSaldo());
		return "El nuevo saldo es de: "+C.getSaldo();
	}
	public String Reintegro(Cuenta C, double cantidad) {
		if (cantidad>C.getSaldo()) {
			C.setSaldo(cantidad+C.getSaldo());
			return "El nuevo saldo es de: "+C.getSaldo();
		} else {
			return "Error Cantidad insuficiente ";
		}
	}
	 */
	
	

	/**
	 * @return the titular
	 */
	public String getTitular() {
		return titular;
	}

	/**
	 * @param titular the titular to set
	 */
	public void setTitular(String titular) {
		this.titular = titular;
	}


	/**
	 * @return the numCuenta
	 */
	public int getNumCuenta() {
		return NumCuenta;
	}

	/**
	 * @param numCuenta the numCuenta to set
	 */
	public void setNumCuenta(int numCuenta) {
		NumCuenta = numCuenta;
	}


	
	
}
