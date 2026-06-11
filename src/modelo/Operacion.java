package modelo;

import java.util.Date;

/**
 * Representa una operación bancaria (ingreso, retiro, transferencia, etc.)
 * asociada a una cuenta, con su importe, concepto y fecha.
 *
 * @author Raul
 * @version 0.2
 */
public class Operacion {

    private int idOperacion;
    private int idCuentaOperacion;
    private Integer idCuentaDestino; // Integer porque puede ser NULL
    private TipoOperacion tipo;
    private String concepto;
    private Date fechaHora;
    private double importe;

    public Operacion() {
    }

    public Operacion(int idOperacion, int idCuentaOperacion, Integer idCuentaDestino,
                     TipoOperacion tipo, String concepto, Date fechaHora, double importe) {
        this.idOperacion = idOperacion;
        this.idCuentaOperacion = idCuentaOperacion;
        this.idCuentaDestino = idCuentaDestino;
        this.tipo = tipo;
        this.concepto = concepto;
        this.fechaHora = fechaHora;
        this.importe = importe;
    }

    public int getIdOperacion() {
        return idOperacion;
    }

    public int getIdCuentaOperacion() {
        return idCuentaOperacion;
    }

    public Integer getIdCuentaDestino() {
        return idCuentaDestino;
    }

    public TipoOperacion getTipo() {
        return tipo;
    }

    public String getConcepto() {
        return concepto;
    }

    public Date getFechaHora() {
        return fechaHora;
    }

    public double getImporte() {
        return importe;
    }
}