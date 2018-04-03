package unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVAMelvin;

import android.graphics.Bitmap;

import java.io.Serializable;

/**
 * Created by melvinrivera on 21/2/18.
 */

public class PerfilBreve implements Serializable{
    private String nombre;
    private Bitmap imagen;
    private String numeroTelefono;
    private String direccion;
    private int id;
    int estado;

    public PerfilBreve(String nombre, Bitmap imagen, String numeroTelefono, String direccion,int id, int estado) {
        this.nombre = nombre;
        this.imagen = imagen;
        this.numeroTelefono = numeroTelefono;
        this.direccion = direccion;
        this.id = id;
        this.estado = estado;
    }

    public PerfilBreve(){

    }

    public Bitmap getImagen() {
        return imagen;
    }

    public void setImagen(Bitmap imagen) {
        this.imagen = imagen;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNumeroTelefono() {
        return numeroTelefono;
    }

    public void setNumeroTelefono(String numeroTelefono) {
        this.numeroTelefono = numeroTelefono;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }
}
