package AaTesting;

public class CalculadoraDescuento {
	public double calcularTotal(double importe, boolean clientePremium) {
			if (importe < 0) {
		         throw new IllegalArgumentException("Importe inválido");
		    }
		    if (clientePremium) {
		    	if (importe >= 100) {
		    		return importe * 0.8;
		         }
		         return importe * 0.9;
		       }
		       if (importe >= 100) {
		         return importe * 0.95;
		       }
		    return importe;
		   }

}
