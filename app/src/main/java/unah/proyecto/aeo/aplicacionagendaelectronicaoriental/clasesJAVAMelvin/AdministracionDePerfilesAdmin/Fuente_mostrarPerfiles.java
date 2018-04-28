package unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVAMelvin.AdministracionDePerfilesAdmin;

/**
 * Created by melvinrivera on 3/4/18.
 */

public class Fuente_mostrarPerfiles {
    private int id;
    private String perfil;
    private String imagen;

    public Fuente_mostrarPerfiles(int id, String perfil, String imagen) {
        this.id = id;
        this.perfil = perfil;
        this.imagen = imagen;
    }
    public Fuente_mostrarPerfiles(){
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPerfil() {
        return perfil;
    }

    public void setPerfil(String perfil) {
        this.perfil = perfil;
    }
}
