package unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVAAlan;


import android.content.Context;
import android.content.Intent;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.R;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVAMelvin.ListaDeContactos;


/**
 /**
 * Created by alan fabricio on 21/02/2018.
 */

public class viewHolder_Categoria extends RecyclerView.ViewHolder implements View.OnClickListener {
    ImageView imagen;
    TextView titulo;
    TextView cantidad;
    Context context;
    TextView id_categoria;
    int position;


    viewHolder_Categoria(View v){
        super(v);
        context = v.getContext();

        imagen = (ImageView) v.findViewById(R.id.foto);
        titulo = (TextView) v.findViewById(R.id.titulo);
        cantidad = (TextView) v.findViewById(R.id.cantidad);
        id_categoria = (TextView) v.findViewById(R.id.id_categorias);


    }

    @Override
    public void onClick(View v) {

        intenPasarAPerfilBreve();

    }

    void setOnClickListener() {
        imagen.setOnClickListener(this);
        titulo.setOnClickListener(this);
        cantidad.setOnClickListener(this);



    }

    public void intenPasarAPerfilBreve(){
        Intent intent = new Intent(context,ListaDeContactos.class);
        intent.putExtra("id_categoria",id_categoria.getText().toString());
        context.startActivity(intent);
    }

}


