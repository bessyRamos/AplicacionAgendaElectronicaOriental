package unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVAAlan;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.R;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVAMelvin.PerfilesBreves.ListaDeContactos;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.provider.CategoriasContract;

/**
 * Created by alan fabricio on 22/02/2018.
 */

public class Adaptador_Categoria extends RecyclerView.Adapter<Adaptador_Categoria.ViewHolder>{


    Cursor dataCursor;
    Context context;


    public  class ViewHolder extends RecyclerView.ViewHolder{
        public     ImageView imagen;
        public     TextView titulo;
        public     TextView cantidad;
        public    TextView id_categoria;

        public ViewHolder(View itemView ){
            super(itemView);
            imagen = (ImageView) itemView.findViewById(R.id.foto);
            titulo = (TextView) itemView.findViewById(R.id.titulo);
            cantidad = (TextView) itemView.findViewById(R.id.cantidad);
            id_categoria = (TextView) itemView.findViewById(R.id.id_categorias);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if(pos != RecyclerView.NO_POSITION){
                        Intent intent = new Intent(context,ListaDeContactos.class);
                        intent.putExtra("id_categoria", getItem(pos).id);
                        intent.putExtra("nombre_categoria", getItem(pos).Titulo);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    }
                }
            });
        }
    }



    public Adaptador_Categoria(Activity mContext, Cursor cursor) {
        dataCursor = cursor;
        context = mContext;
    }

    @Override
    public Adaptador_Categoria.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.card_item_categorias,parent,false);
        return new ViewHolder(vista);
    }

    @Override
    public void onBindViewHolder(Adaptador_Categoria.ViewHolder holder, int position) {

        dataCursor.moveToPosition(position);

        holder.titulo.setText(dataCursor.getString(dataCursor.getColumnIndex(CategoriasContract.CategoriasEntry.COLUMN_NOMBRE_CATEGORIA)));

        holder.cantidad.setText( dataCursor.getString(dataCursor.getColumnIndex(CategoriasContract.CategoriasEntry.COLUMN_CANTIDAD))+" Contactos");
        holder.id_categoria.setText(dataCursor.getString(dataCursor.getColumnIndex(CategoriasContract.CategoriasEntry.COLUMN_ID_CATEGORIA)));

        if(!dataCursor.getString(dataCursor.getColumnIndex(CategoriasContract.CategoriasEntry.COLUMN_IMAGEN_CATEGORIA)).isEmpty()){
            Glide.with(context).load(dataCursor.getString(dataCursor.getColumnIndex(CategoriasContract.CategoriasEntry.COLUMN_IMAGEN_CATEGORIA))).into(holder.imagen);
        }else {
            Glide.with(context).load(R.drawable.wait).into(holder.imagen);
        }

    }

    public Cursor swapCursor(Cursor cursor){
        if(dataCursor == cursor){
            return null;
        }
        Cursor oldCursor = dataCursor;
        this.dataCursor = cursor;
        if(cursor != null){
            this.notifyDataSetChanged();
        }
        return oldCursor;
    }

    @Override
    public int getItemCount() {
        return (dataCursor==null)?0:dataCursor.getCount();
    }

    public Fuente_Categoria getItem(int position){
        if(position < 0 || position >= getItemCount()){
            throw new IllegalArgumentException("Posicion del Item fuera del rango de adaptador");
        }else if(dataCursor.moveToPosition(position)){
            return new Fuente_Categoria(dataCursor);
        }
        return  null;
    }

}
