package unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVAAlan;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

/**
 * Created by alan fabricio on 21/02/2018.
 */


public class Fuente_Categoria {
    String Titulo;

    Bitmap imagen;
    int cantidad;
    int id;
    int estado;
    String  dato;


    public Fuente_Categoria(String titulo,  Bitmap imagen, int cantidad,int id,int estado, String dato) {
        Titulo = titulo;
        this.imagen = imagen;
        this.cantidad = cantidad;
        this.id= id;
        this.estado = estado;
        this.dato = dato;
    }
    public Fuente_Categoria(){

    }

    public String getDato() {
        return dato;
    }

    public void setDato(String dato) {
        this.dato = dato;
        try {
            byte[] byteCode = Base64.decode(dato, Base64.DEFAULT);
            this.imagen = BitmapFactory.decodeByteArray(byteCode, 0, byteCode.length);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public Bitmap getImagen() {
        return imagen;
    }

    public void setImagen(Bitmap imagen) {
        this.imagen = imagen;
    }

    public String getTitulo() {
        return Titulo;
    }

    public void setTitulo(String titulo) {
        Titulo = titulo;
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

