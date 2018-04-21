package unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVAMelvin;


import android.database.Cursor;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.provider.*;

/**
 * Created by melvinrivera on 21/2/18.
 */

public class PerfilBreve {
    public String nombre;
    public String imagen;
    public String numeroTelefono;
    public String direccion;
    public int id;

    public PerfilBreve(Cursor cursor) {
        this.nombre = cursor.getString(cursor.getColumnIndex(PerfilesContract.ContactosEntry.COLUMN_NOMBRE));
        this.imagen = cursor.getString(cursor.getColumnIndex(PerfilesContract.ContactosEntry.COLUMN_IMAGEN_PATH));
        if(cursor.getString(cursor.getColumnIndex(PerfilesContract.ContactosEntry.COLUMN_NUMERO_TELEFONO)).isEmpty()){
            this.numeroTelefono = cursor.getString(cursor.getColumnIndex(PerfilesContract.ContactosEntry.COLUMN_NUMERO_CELULAR));
        }else{
            this.numeroTelefono = cursor.getString(cursor.getColumnIndex(PerfilesContract.ContactosEntry.COLUMN_NUMERO_TELEFONO));
        }

        this.direccion = cursor.getString(cursor.getColumnIndex(PerfilesContract.ContactosEntry.COLUMN_NOMBRE_REGION));
        this.id = cursor.getInt(cursor.getColumnIndex(PerfilesContract.ContactosEntry.COLUMN_PERFILID));
    }


}
