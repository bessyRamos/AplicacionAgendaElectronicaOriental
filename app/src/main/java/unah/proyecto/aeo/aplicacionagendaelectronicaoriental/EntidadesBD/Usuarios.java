package unah.proyecto.aeo.aplicacionagendaelectronicaoriental.EntidadesBD;

/**
 * Created by melvinrivera on 22/2/18.
 */

public class Usuarios {
    private int id_usuario;
    private String nombre_usuario;
    private String nombre_propio;
    private String contrasena;
    private int estado_usuario;
    private int rol;

    public Usuarios(int id_usuario, String nombre_usuario, String nombre_propio, String contrasena, int estado_usuario, int rol) {

        setId_usuario(id_usuario);
        setNombre_usuario(nombre_usuario);
        setNombre_propio(nombre_propio);
        setContrasena(contrasena);
        setEstado_usuario(estado_usuario);
        setRol(rol);
    }

    public int getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(int id_usuario) {
        this.id_usuario = id_usuario;
    }

    public String getNombre_usuario() {
        return nombre_usuario;
    }

    public void setNombre_usuario(String nombre_usuario) {
        this.nombre_usuario = nombre_usuario;
    }

    public String getNombre_propio() {
        return nombre_propio;
    }

    public void setNombre_propio(String nombre_propio) {
        this.nombre_propio = nombre_propio;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public int getRol() {
        return rol;
    }

    public void setRol(int rol) {
        this.rol = rol;
    }

    public int getEstado_usuario() {
        return estado_usuario;
    }

    public void setEstado_usuario(int estado_usuario) {
        this.estado_usuario = estado_usuario;
    }
}
