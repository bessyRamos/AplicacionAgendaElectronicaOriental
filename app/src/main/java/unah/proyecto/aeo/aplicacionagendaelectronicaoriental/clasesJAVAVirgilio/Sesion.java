package unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVAVirgilio;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Usuario on 09/04/2018.
 */

public class Sesion {

    //
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    Context context;

    public Sesion(Context ctx){
        this.context =ctx;
        preferences = ctx.getSharedPreferences("myapp",Context.MODE_PRIVATE);
        editor = preferences.edit();
    }
    public void setLogin(Boolean logindim){
        editor.putBoolean("loginInmode",logindim);
        editor.commit();
    }
    public boolean logindim(){
        return preferences.getBoolean("loginInmode",false);
    }
}

