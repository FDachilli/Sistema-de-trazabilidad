package ObjetosBD;

import java.util.Date;

/**
 * Created by Franco on 15/4/2017.
 */

public class Envio {
    int id_envio;
    int receptor;
    Date fecha_recepcion;
    double longitud_donante;
    double latitud_donante;
    double longitud_receptor;
    double latitud_receptor;
    String descripcion;
    String nombre_donante;

    public String getNombre_donante() {
        return nombre_donante;
    }

    public void setNombre_donante(String nombre_donante) {
        this.nombre_donante = nombre_donante;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public double getLongitud_donante() {
        return longitud_donante;
    }

    public void setLongitud_donante(double longitud_donante) {
        this.longitud_donante = longitud_donante;
    }

    public double getLatitud_donante() {
        return latitud_donante;
    }

    public void setLatitud_donante(double latitud_donante) {
        this.latitud_donante = latitud_donante;
    }

    public double getLongitud_receptor() {
        return longitud_receptor;
    }

    public void setLongitud_receptor(double longitud_receptor) {
        this.longitud_receptor = longitud_receptor;
    }

    public double getLatitud_receptor() {
        return latitud_receptor;
    }

    public void setLatitud_receptor(double latitud_receptor) {
        this.latitud_receptor = latitud_receptor;
    }

    public int getReceptor() {
        return receptor;
    }

    public void setReceptor(int receptor) {
        this.receptor = receptor;
    }

    public Date getFecha_recepcion() {
        return fecha_recepcion;
    }

    public void setFecha_recepcion(Date fecha_recepcion) {
        this.fecha_recepcion = fecha_recepcion;
    }

    public int getId_envio() {
        return id_envio;
    }

    public void setId_envio(int id_envio) {
        this.id_envio = id_envio;
    }
}
