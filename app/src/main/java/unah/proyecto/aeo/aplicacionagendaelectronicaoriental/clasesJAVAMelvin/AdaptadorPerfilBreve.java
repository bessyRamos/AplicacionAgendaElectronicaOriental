package unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVAMelvin;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.R;

/**
 * Created by melvinrivera on 22/2/18.
 */

public class AdaptadorPerfilBreve extends RecyclerView.Adapter<ViewHolderPerfilBreve> {

        List<PerfilBreve> listaObjetos;

        public AdaptadorPerfilBreve(List<PerfilBreve> listaObjetos) {
            this.listaObjetos = listaObjetos;
        }

        @Override
        public ViewHolderPerfilBreve onCreateViewHolder(ViewGroup parent, int viewType) {
            View vista = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout_item_lista_contactos,parent,false);
            return new ViewHolderPerfilBreve(vista);
        }

        @Override
        public void onBindViewHolder(ViewHolderPerfilBreve holder, int position) {
            holder.nombrePerfilBreve.setText(listaObjetos.get(position).getNombre().toString());
            holder.imagenPerfilBreve.setImageResource(listaObjetos.get(position).getImagen());
            holder.direccionPerfilBreve.setText(listaObjetos.get(position).getDireccion().toString());
            holder.numeroTelefonoPerfilBreve.setText(listaObjetos.get(position).getNumeroTelefono().toString());
            holder.id_perfilBreve.setText(""+listaObjetos.get(position).getId());
            holder.setOnClickListener();
        }

        @Override
        public int getItemCount() {
            return listaObjetos.size();
        }
    }

