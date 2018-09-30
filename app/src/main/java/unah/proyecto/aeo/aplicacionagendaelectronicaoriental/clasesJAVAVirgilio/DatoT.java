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
    public void setLoginDatotkn(String logindimDatotkn){
        editor.putString("loginDatos",logindimDatotkn);
        editor.commit();
    }
    public void setLoginDator(int logindimDatoid){
        editor.putInt("loginDatosR",logindimDatoid);
        editor.commit();
    }
    public void setLoginDatoid(int logindimDator){
        editor.putInt("loginDatosId",logindimDator);
        editor.commit();
    }
    public void setLoginDatostd(int logindimDatostd){
        editor.putInt("loginDatosStd",logindimDatostd);
        editor.commit();
    }

    public boolean logindimDato(){
        return preferences.getBoolean("loginInmode",false);
    }

}
