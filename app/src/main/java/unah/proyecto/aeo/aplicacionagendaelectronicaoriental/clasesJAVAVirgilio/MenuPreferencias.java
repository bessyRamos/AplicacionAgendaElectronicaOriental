package unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVAVirgilio;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Usuario on 09/05/2018.
 */

public class MenuPreferencias {

    //
    private SharedPreferences menu;
    private SharedPreferences.Editor editorMenu;
    Context context;

    public MenuPreferencias(Context ctx){
        this.context =ctx;
        menu = ctx.getSharedPreferences("myapp",Context.MODE_PRIVATE);
        editorMenu = menu.edit();
    }
    public void setLoginMenu(Boolean logindimMenu){
        editorMenu.putBoolean("loginInmodeMenu",logindimMenu);
        editorMenu.commit();
    }
    public boolean logindimMenu(){
        return menu.getBoolean("loginInmodeMenu",false);
    }


}
