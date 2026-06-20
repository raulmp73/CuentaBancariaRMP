package Util;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Utilidad técnica que descompone un importe en euros en los billetes y monedas
 * necesarios para entregarlo (algoritmo voraz, de mayor a menor denominación).
 *
 * Vive en Util porque es una herramienta de cálculo genérica: no conoce reglas
 * del banco, solo reparte un importe en denominaciones de euro.
 *
 * @author Raul
 * @version 0.2
 */
public class DesgloseEfectivo {

    // Denominaciones en céntimos, de mayor a menor (billetes y monedas de euro)
    private static final int[] DENOMINACIONES_CENT =
            {50000, 20000, 10000, 5000, 2000, 1000, 500, 200, 100, 50, 20, 10, 5, 2, 1};

    private static final String[] ETIQUETAS =
            {"500 €", "200 €", "100 €", "50 €", "20 €", "10 €", "5 €",
             "2 €", "1 €", "0,50 €", "0,20 €", "0,10 €", "0,05 €", "0,02 €", "0,01 €"};

    /**
     * Calcula cuántas unidades de cada denominación hacen falta para un importe.
     * Trabaja en céntimos (enteros) para evitar errores de redondeo con double.
     *
     * @param importe importe a desglosar (en euros)
     * @return mapa ordenado etiqueta -> nº de unidades (solo las que aparecen)
     */
    public static LinkedHashMap<String, Integer> calcular(double importe) {

        long centimos = Math.round(importe * 100);
        LinkedHashMap<String, Integer> desglose = new LinkedHashMap<>();

        for (int i = 0; i < DENOMINACIONES_CENT.length; i++) {
            int unidades = (int) (centimos / DENOMINACIONES_CENT[i]);
            if (unidades > 0) {
                desglose.put(ETIQUETAS[i], unidades);
                centimos -= (long) unidades * DENOMINACIONES_CENT[i];
            }
        }
        return desglose;
    }

    /**
     * Devuelve el desglose ya formateado en texto, listo para mostrar.
     *
     * @param importe importe a desglosar (en euros)
     * @return texto multilínea con los billetes/monedas a entregar
     */
    public static String formatear(double importe) {

        LinkedHashMap<String, Integer> desglose = calcular(importe);

        StringBuilder sb = new StringBuilder();
        sb.append("Efectivo a entregar (").append(importe).append(" €):\n");

        if (desglose.isEmpty()) {
            sb.append("  (nada que entregar)");
            return sb.toString();
        }

        for (Map.Entry<String, Integer> e : desglose.entrySet()) {
            sb.append("  ").append(e.getValue()).append(" x ").append(e.getKey()).append("\n");
        }
        return sb.toString().stripTrailing();
    }
}
