package unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVAMelvin;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.R;

/**
 * Created by melvinrivera on 3/4/18.
 */

public class AdaptadorMostrarPerfiles extends BaseAdapter {
    private List<Fuente_mostrarPerfiles> perfiles;
    private Context context;

    //constructor

    public AdaptadorMostrarPerfiles(List<Fuente_mostrarPerfiles> perfiles, Context context) {
        this.perfiles = perfiles;
        this.context = context;
    }


    public int getCount() {
        return perfiles.size() ;
    }


    public Object getItem(int i) {
        return perfiles.get(i);
    }



    public long getItemId(int i) {
        perfiles.get(i).getId();
        return i;
    }

    public View getView(int i, View view, ViewGroup viewGroup) {
        View v=view;
        v = View.inflate(context, R.layout.activity_item_listview_mostrar_perfiles,null);

        TextView textid = (TextView)v.findViewById(R.id.id_mostrarperfiles);
        TextView textnombre = (TextView)v.findViewById(R.id.mostrarperfiles);

        textid.setText(""+perfiles.get(i).getId());
        textnombre.setText(perfiles.get(i).getPerfil());

        return v;
    }
}
