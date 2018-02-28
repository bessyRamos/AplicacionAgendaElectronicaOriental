package unah.proyecto.aeo.aplicacionagendaelectronicaoriental.EntidadesBD;

/**
 * Created by melvinrivera on 22/2/18.
 */

public class Roles {
    private int id_rol;
    private int descripcion_rol;

    public Roles(int id_rol, int descripcion_rol) {
        this.id_rol = id_rol;
        this.descripcion_rol = descripcion_rol;
    }

    public int getId_rol() {
        return id_rol;
    }

    public void setId_rol(int id_rol) {
        this.id_rol = id_rol;
    }

    public int getDescripcion_rol() {
        return descripcion_rol;
    }

    public void setDescripcion_rol(int descripcion_rol) {
        this.descripcion_rol = descripcion_rol;
    }
}
