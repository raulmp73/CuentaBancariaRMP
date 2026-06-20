package AaTesting;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class TestCalculadora {

    @Test
    void testSumar() {
        assertEquals(15, Calculadora.sumar(10, 5));
    }

    @Test
    void testRestar() {
        assertEquals(5, Calculadora.restar(10, 5));
    }

    @Test
    void testMultiplicar() {
        assertEquals(50, Calculadora.multiplicar(10, 5));
    }

    @Test
    void testDividir() {
        assertEquals(2, Calculadora.dividir(10, 5));
    }

}