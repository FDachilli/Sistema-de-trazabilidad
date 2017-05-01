package ObjetosBD;

/**
 * Created by Franco on 27/4/2017.
 */

public class Entrada_notificacion {

    private String nombre_donante;
    private String fecha_envio;
    private String dire_donante;
    private String descripcion;

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getNombre_donante() {
        return nombre_donante;
    }

    public void setNombre_donante(String nombre_donante) {
        this.nombre_donante = nombre_donante;
    }

    public String getFecha_envio() {
        return fecha_envio;
    }

    public void setFecha_envio(String fecha_envio) {
        this.fecha_envio = fecha_envio;
    }

    public String getDire_donante() {
        return dire_donante;
    }

    public void setDire_donante(String dire_donante) {
        this.dire_donante = dire_donante;
    }
}
