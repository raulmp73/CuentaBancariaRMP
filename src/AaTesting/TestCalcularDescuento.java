package AaTesting;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class TestCalcularDescuento {

	
	
	@Test
	void testCalculadoraDescuento() {
		
		// throwexception
	CalculadoraDescuento cd = new CalculadoraDescuento();
	double resultado = cd.calcularTotal(0, false);
		
	assertThrows(IllegalArgumentException.class, ()-> cd.calcularTotal(-10, false));
	
	//Cliente premiun - >= 100
	resultado = cd.calcularTotal(33.33, true);
	assertEquals(29.99, resultado, 0.01, "test- 1");
	
	}


}
