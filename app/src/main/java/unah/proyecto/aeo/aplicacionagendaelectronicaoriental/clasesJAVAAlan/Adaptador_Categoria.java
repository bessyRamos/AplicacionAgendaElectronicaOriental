package unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVAAlan;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.R;

/**
 * Created by alan fabricio on 22/02/2018.
 */

public class Adaptador_Categoria extends RecyclerView.Adapter  <viewHolder_Categoria>{
    List<Fuente_Categoria> ListaObjetos;

    public Adaptador_Categoria(List<Fuente_Categoria> listaObjetos) {
        ListaObjetos = listaObjetos;
    }

    @Override
    public viewHolder_Categoria onCreateViewHolder(ViewGroup parent, int viewType) {

        View vista = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item_categorias,parent,false);
        return new viewHolder_Categoria(vista);
    }

    @Override
    public void onBindViewHolder(viewHolder_Categoria holder, int position) {
        holder.titulo.setText(ListaObjetos.get(position).getTitulo().toString());
        holder.imagen.setImageResource(ListaObjetos.get(position).getImagen());
        holder.cantidad.setText(" " + ListaObjetos.get(position).getCantidad());
        holder.id_categoria.setText(""+ListaObjetos.get(position).getId());
        holder.setOnClickListener();
    }

    @Override
    public int getItemCount() {
        return ListaObjetos.size();
    }
}
