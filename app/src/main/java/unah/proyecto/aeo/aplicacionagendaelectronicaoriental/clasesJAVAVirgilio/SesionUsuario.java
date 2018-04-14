package unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVAVirgilio;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Usuario on 13/04/2018.
 */

public class SesionUsuario {


        //
        private SharedPreferences preferencesUsuario;
        private SharedPreferences.Editor editorUsuario;
        Context context;

        public SesionUsuario(Context ctx){
            this.context =ctx;
            preferencesUsuario = ctx.getSharedPreferences("myapp",Context.MODE_PRIVATE);
            editorUsuario = preferencesUsuario.edit();
        }
        public void setLoginUsuario(Boolean logindimUsuario){
            editorUsuario.putBoolean("loginInmodeUsuario",logindimUsuario);
            editorUsuario.commit();
        }
        public boolean logindimUsuario(){
            return preferencesUsuario.getBoolean("loginInmodeUsuario",false);
        }



}
