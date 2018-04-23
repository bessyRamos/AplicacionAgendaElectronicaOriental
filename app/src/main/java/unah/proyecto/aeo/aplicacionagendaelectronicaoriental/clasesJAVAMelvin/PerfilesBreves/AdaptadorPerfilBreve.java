package unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVAMelvin.PerfilesBreves;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;


import de.hdodenhof.circleimageview.CircleImageView;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.R;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVASheyli.PerfilDeLaOrganizacion;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.provider.PerfilesContract;

/**
 * Created by melvinrivera on 22/2/18.
 */

public class AdaptadorPerfilBreve extends RecyclerView.Adapter<AdaptadorPerfilBreve.ViewHolder> {

    Cursor dataCursor;
    Context context;


    public  class ViewHolder extends RecyclerView.ViewHolder{
        public    CircleImageView imagenPerfilBreve;
        public    TextView nombrePerfilBreve;
        public    TextView numeroTelefonoPerfilBreve;
        public    TextView direccionPerfilBreve;
        public    TextView id_perfilBreve;

        public ViewHolder(View itemView ){
            super(itemView);
            imagenPerfilBreve =  itemView.findViewById(R.id.imagen_organizacion);
            nombrePerfilBreve = (TextView) itemView.findViewById(R.id.nombre_organizacion);
            numeroTelefonoPerfilBreve = (TextView) itemView.findViewById(R.id.numero_telefono_organizacion);
            direccionPerfilBreve = (TextView) itemView.findViewById(R.id.direccion_organizacion);
            id_perfilBreve = (TextView) itemView.findViewById(R.id.id_Deorganizacion);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if(pos != RecyclerView.NO_POSITION){
                        Intent intent = new Intent(context,PerfilDeLaOrganizacion.class);
                        intent.putExtra("id_organizacion", getItem(pos).id);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    }
                }
            });
        }
    }



    public AdaptadorPerfilBreve(Activity mContext, Cursor cursor) {
        dataCursor = cursor;
        context = mContext;
    }

    @Override
    public AdaptadorPerfilBreve.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.card_layout_item_lista_contactos,parent,false);
        return new ViewHolder(vista);
    }

    @Override
    public void onBindViewHolder(AdaptadorPerfilBreve.ViewHolder holder, int position) {

        dataCursor.moveToPosition(position);

        holder.nombrePerfilBreve.setText(dataCursor.getString(dataCursor.getColumnIndex(PerfilesContract.ContactosEntry.COLUMN_NOMBRE)));
        holder.direccionPerfilBreve.setText(dataCursor.getString(dataCursor.getColumnIndex(PerfilesContract.ContactosEntry.COLUMN_NOMBRE_REGION)));
        if(dataCursor.getString(dataCursor.getColumnIndex(PerfilesContract.ContactosEntry.COLUMN_NUMERO_TELEFONO)).isEmpty()){
            holder.numeroTelefonoPerfilBreve.setText(dataCursor.getString(dataCursor.getColumnIndex(PerfilesContract.ContactosEntry.COLUMN_NUMERO_CELULAR)));
        }else{
            holder.numeroTelefonoPerfilBreve.setText(dataCursor.getString(dataCursor.getColumnIndex(PerfilesContract.ContactosEntry.COLUMN_NUMERO_TELEFONO)));
        }

        holder.id_perfilBreve.setText(""+dataCursor.getInt(dataCursor.getColumnIndex(PerfilesContract.ContactosEntry.COLUMN_PERFILID)));

    if (!dataCursor.getString(dataCursor.getColumnIndex(PerfilesContract.ContactosEntry.COLUMN_IMAGEN_PATH)).isEmpty()){
        Glide.with(context).
                load(dataCursor.getString(dataCursor.getColumnIndex(PerfilesContract.ContactosEntry.COLUMN_IMAGEN_PATH))).
                into(holder.imagenPerfilBreve);
    }else {
        Glide.with(context).
                load(R.drawable.iconocontactowhite).
                into(holder.imagenPerfilBreve);
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

    public PerfilBreve getItem(int position){
        if(position < 0 || position >= getItemCount()){
            throw new IllegalArgumentException("Posicion del Item fuera del rango de adaptador");
        }else if(dataCursor.moveToPosition(position)){
            return new PerfilBreve(dataCursor);
        }
        return  null;
    }
}

