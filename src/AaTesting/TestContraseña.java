package AaTesting;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class TestContraseña {

	@Test
	void testEsValida() {
		Contraseña c= new Contraseña();
		
		boolean resultado= c.esValida("12345678");
		assertEquals(false, resultado, "test- 1");
		
		resultado = c.esValida("1234560");
		assertEquals(false, resultado, "Test- 2");
	}

}
