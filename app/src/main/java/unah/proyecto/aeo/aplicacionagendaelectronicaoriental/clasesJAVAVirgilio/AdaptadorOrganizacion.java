package unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVAVirgilio;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.R;

/**
 * Created by melvinrivera on 3/4/18.
 */

public class AdaptadorOrganizacion extends BaseAdapter {
    private List<EntidadOrganizacion> listaobjetos;
    private Context context;
    LayoutInflater lsinflater;

    //constructor


    public AdaptadorOrganizacion(List<EntidadOrganizacion> listaobjetos, Context context) {
        this.listaobjetos = listaobjetos;
        this.context = context;
    }

    @Override
    public int getCount() {
        return listaobjetos.size();
    }

    @Override
    public Object getItem(int position) {
        return listaobjetos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return listaobjetos.get(position).getId();
    }

    public View getView(int i, View view, ViewGroup viewGroup) {
        View v=view;

        v = View.inflate(context, R.layout.item_perfil_empresa,null);

        //TextView textid = (TextView)v.findViewById(R.id.id);
        TextView nombre = (TextView)v.findViewById(R.id.nombreOrganizacionRegistrada);
        TextView estado = (TextView)v.findViewById(R.id.estadoOrganizacionRegistrada);
        ImageView Imagen = (ImageView) v.findViewById(R.id.imagenOrganizacionRegistrada);


       // textid.setText(""+perfiles.get(i).getId());
        nombre.setText(listaobjetos.get(i).getTitulo().toString());
        estado.setText(listaobjetos.get(i).getEstado().toString());
        if (listaobjetos.get(i).getImagen().isEmpty()){
            Imagen.setImageResource(R.drawable.iconocontactowhite);

        }else {
            Glide.with(context).load(listaobjetos.get(i).getImagen()).into(Imagen);
        }





        return v;
    }
}

