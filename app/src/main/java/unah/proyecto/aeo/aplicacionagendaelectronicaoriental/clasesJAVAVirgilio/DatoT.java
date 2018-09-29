package unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVAVirgilio;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Usuario on 28/09/2018.
 */

public class DatoT {
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    Context context;

    public DatoT(Context ctx){
        this.context =ctx;
        preferences = ctx.getSharedPreferences("DatoT",Context.MODE_PRIVATE);
        editor = preferences.edit();
    }
    public void setLoginDato(String logindimDato){
        editor.putString("loginDatos",logindimDato);
        editor.commit();
    }
    public void setLoginDator(String logindimDato){
        editor.putString("loginDatosR",logindimDato);
        editor.commit();
    }
    public void setLoginDatoid(String logindimDato){
        editor.putString("loginDatosId",logindimDato);
        editor.commit();
    }
    public void setLoginDatostd(String logindimDato){
        editor.putString("loginDatosStd",logindimDato);
        editor.commit();
    }

    public boolean logindimDato(){
        return preferences.getBoolean("loginInmode",false);
    }

}
