package unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVAAlan;

import android.content.Context;
import android.content.Intent;

import android.support.v7.widget.RecyclerView;
import android.view.View;
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
    TextView Numero;
    Context context;
    int position;


    viewHolder_Categoria(View v){
        super(v);
        context = v.getContext();

        imagen = (ImageView) itemView.findViewById(R.id.foto);
        titulo = (TextView) itemView.findViewById(R.id.titulo);
        Numero = (TextView) itemView.findViewById(R.id.cantidad);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.foto:

                Intent intent = new Intent(context,ListaDeContactos.class);
                intent.putExtra("nombre_categoria",titulo.getText().toString());
                context.startActivity(intent);

        }

    }

    void setOnClickListener() {
        imagen.setOnClickListener(this);

    }

}


