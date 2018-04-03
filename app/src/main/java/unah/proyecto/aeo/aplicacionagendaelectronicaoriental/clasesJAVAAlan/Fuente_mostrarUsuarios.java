package unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVAAlan;

/**
 * Created by alan fabricio on 22/03/2018.
 */

public class Fuente_mostrarUsuarios {
    private int id;
    private String usuario;

    public Fuente_mostrarUsuarios(int id, String usuario) {
        this.id = id;
        this.usuario = usuario;
    }
    public Fuente_mostrarUsuarios(){
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }
}
