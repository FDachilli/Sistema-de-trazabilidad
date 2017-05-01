package ObjetosBD;

/**
 * Created by Franco on 20/4/2017.
 */

public class Entrada_lista_envio {

    public String donante;
    public String receptor;
    public String descripcion;
    public String fecha_envio;
    public String fecha_recepcion;
    public String dire_donante;
    public String dire_receptor;
    public Double longitud_donante;
    public Double latitud_donante;
    public Double latitud_receptor;
    public Double longitud_receptor;
    public int idImagen;

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Double getLongitud_donante() {
        return longitud_donante;
    }

    public void setLongitud_donante(Double longitud_donante) {
        this.longitud_donante = longitud_donante;
    }

    public Double getLatitud_donante() {
        return latitud_donante;
    }

    public void setLatitud_donante(Double latitud_donante) {
        this.latitud_donante = latitud_donante;
    }

    public Double getLatitud_receptor() {
        return latitud_receptor;
    }

    public void setLatitud_receptor(Double latitud_receptor) {
        this.latitud_receptor = latitud_receptor;
    }

    public Double getLongitud_receptor() {
        return longitud_receptor;
    }

    public void setLongitud_receptor(Double longitud_receptor) {
        this.longitud_receptor = longitud_receptor;
    }

    public int getIdImagen() {
        return idImagen;
    }

    public void setIdImagen(int idImagen) {
        this.idImagen = idImagen;
    }

    public String getDonante() {
        return donante;
    }

    public void setDonante(String donante) {
        this.donante = donante;
    }

    public String getReceptor() {
        return receptor;
    }

    public void setReceptor(String receptor) {
        this.receptor = receptor;
    }

    public String getFecha_envio() {
        return fecha_envio;
    }

    public void setFecha_envio(String fecha_envio) {
        this.fecha_envio = fecha_envio;
    }

    public String getFecha_recepcion() {
        return fecha_recepcion;
    }

    public void setFecha_recepcion(String fecha_recepcion) {
        this.fecha_recepcion = fecha_recepcion;
    }

    public String getDire_donante() {
        return dire_donante;
    }

    public void setDire_donante(String dire_donante) {
        this.dire_donante = dire_donante;
    }

    public String getDire_receptor() {
        return dire_receptor;
    }

    public void setDire_receptor(String dire_receptor) {
        this.dire_receptor = dire_receptor;
    }
}
