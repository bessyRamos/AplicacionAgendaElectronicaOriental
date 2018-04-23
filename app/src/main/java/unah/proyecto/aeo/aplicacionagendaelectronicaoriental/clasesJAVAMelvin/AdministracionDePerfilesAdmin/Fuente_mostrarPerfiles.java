package unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVAMelvin.AdministracionDePerfilesAdmin;

/**
 * Created by melvinrivera on 3/4/18.
 */

public class Fuente_mostrarPerfiles {
    private int id;
    private String perfil;

    public Fuente_mostrarPerfiles(int id, String perfil) {
        this.id = id;
        this.perfil = perfil;
    }
    public Fuente_mostrarPerfiles(){
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
