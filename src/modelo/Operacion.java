package modelo;

import java.util.Date;

public class Operacion {
    private int idOperacion;
    private TipoOperacion tipo;
    private double importe;
    private Date fecha;

    public Operacion() {
    }

    public void ejecutar() {
    }

    public boolean validar() {
        return true;
    }

    public int getIdOperacion() { return idOperacion; }
    public void setIdOperacion(int idOperacion) { this.idOperacion = idOperacion; }

    public TipoOperacion getTipo() { return tipo; }
    public void setTipo(TipoOperacion tipo) { this.tipo = tipo; }

    public double getImporte() { return importe; }
    public void setImporte(double importe) { this.importe = importe; }

    public Date getFecha() { return fecha; }
    public void setFecha(Date fecha) { this.fecha = fecha; }

    @Override
    public String toString() {
        return "Operacion [idOperacion=" + idOperacion + ", tipo=" + tipo + ", importe=" + importe + ", fecha=" + fecha + "]";
    }
}
