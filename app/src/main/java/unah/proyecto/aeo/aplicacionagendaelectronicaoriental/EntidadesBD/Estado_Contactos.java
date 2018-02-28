package unah.proyecto.aeo.aplicacionagendaelectronicaoriental.EntidadesBD;

/**
 * Created by melvinrivera on 22/2/18.
 */

public class Estado_Contactos {
    private int id_estado;
    private String descripcion_estado_contactos;


    public Estado_Contactos(int id_estado, String descripcion_estado_contactos) {
        this.id_estado = id_estado;
        this.descripcion_estado_contactos = descripcion_estado_contactos;
    }

    public int getId_estado() {
        return id_estado;
    }

    public void setId_estado(int id_estado) {
        this.id_estado = id_estado;
    }

    public String getDescripcion_estado_contactos() {
        return descripcion_estado_contactos;
    }

    public void setDescripcion_estado_contactos(String descripcion_estado_contactos) {
        this.descripcion_estado_contactos = descripcion_estado_contactos;
    }
}
