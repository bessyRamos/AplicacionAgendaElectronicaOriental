package unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVAAlan;

/**
 * Created by alan fabricio on 21/02/2018.
 */


public class Fuente_Categoria {
    String Titulo;
    int imagen;
    int cantidad;
    int id;
    int estado;

    public Fuente_Categoria(String titulo, int imagen, int cantidad,int id,int estado) {
        Titulo = titulo;
        this.imagen = imagen;
        this.cantidad = cantidad;
        this.id= id;
        this.estado = estado;
    }
    public Fuente_Categoria(){

    }
    public String getTitulo() {
        return Titulo;
    }

    public void setTitulo(String titulo) {
        Titulo = titulo;
    }

    public int getImagen() {
        return imagen;
    }

    public void setImagen(int imagen) {
        this.imagen = imagen;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }
}

