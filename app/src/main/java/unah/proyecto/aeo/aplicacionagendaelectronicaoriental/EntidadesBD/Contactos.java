package unah.proyecto.aeo.aplicacionagendaelectronicaoriental.EntidadesBD;

/**
 * Created by melvinrivera on 22/2/18.
 */

public class Contactos {
    private int id_contacto;
    private String nombre_organizacion;
    private String numero_fijo;
    private String numero_movil;
    private String direccion;
    private int imagen;
    private String e_mail;
    private String descripcion_organizacion;
    private int latitud;
    private int longitud;

    public Contactos(int id_contacto, String nombre_organizacion, String numero_fijo, String numero_movil, String direccion, int imagen, String e_mail, String descripcion_organizacion, int latitud, int longitud) {
        this.id_contacto = id_contacto;
        this.nombre_organizacion = nombre_organizacion;
        this.numero_fijo = numero_fijo;
        this.numero_movil = numero_movil;
        this.direccion = direccion;
        this.imagen = imagen;
        this.e_mail = e_mail;
        this.descripcion_organizacion = descripcion_organizacion;
        this.latitud = latitud;
        this.longitud = longitud;
    }

    public int getId_contacto() {
        return id_contacto;
    }

    public void setId_contacto(int id_contacto) {
        this.id_contacto = id_contacto;
    }

    public String getNombre_organizacion() {
        return nombre_organizacion;
    }

    public void setNombre_organizacion(String nombre_organizacion) {
        this.nombre_organizacion = nombre_organizacion;
    }

    public String getNumero_fijo() {
        return numero_fijo;
    }

    public void setNumero_fijo(String numero_fijo) {
        this.numero_fijo = numero_fijo;
    }

    public String getNumero_movil() {
        return numero_movil;
    }

    public void setNumero_movil(String numero_movil) {
        this.numero_movil = numero_movil;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public int getImagen() {
        return imagen;
    }

    public void setImagen(int imagen) {
        this.imagen = imagen;
    }

    public String getE_mail() {
        return e_mail;
    }

    public void setE_mail(String e_mail) {
        this.e_mail = e_mail;
    }

    public String getDescripcion_organizacion() {
        return descripcion_organizacion;
    }

    public void setDescripcion_organizacion(String descripcion_organizacion) {
        this.descripcion_organizacion = descripcion_organizacion;
    }

    public int getLatitud() {
        return latitud;
    }

    public void setLatitud(int latitud) {
        this.latitud = latitud;
    }

    public int getLongitud() {
        return longitud;
    }

    public void setLongitud(int longitud) {
        this.longitud = longitud;
    }
}
