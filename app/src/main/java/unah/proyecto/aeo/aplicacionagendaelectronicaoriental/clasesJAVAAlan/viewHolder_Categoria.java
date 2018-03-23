package unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVAAlan;


import android.animation.Animator;
import android.content.Context;
import android.content.Intent;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewAnimationUtils;
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

   /// public void onViewAttachedToWindow(RecyclerView.ViewHolder viewHolder){
      //  super.onViewAttacheedToWindow(viewHolder);
     //   animateCircularReveald(viewHolder.itemView);
  //  }


  //  public void animateCircularReveald(View view){
    //    int centerX=0;
     //   int centerY=0;
     //  int startRadius = 0;
      //  int endRadius= Math.max(view.getWidth(),view.getHeight());
      //  Animator animation = ViewAnimationUtils.createCircularReveal(view,centerX,centerY,startRadius,endRadius);
      //  view.setVisibility(View.VISIBLE);
      //  animation.start();

  // }

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
        intent.putExtra("nombre_categoria",titulo.getText().toString());
        context.startActivity(intent);
    }

}


