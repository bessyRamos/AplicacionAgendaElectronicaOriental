package unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVAAlan;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.ConexionSQLiteHelper;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.EntidadesBD.Usuarios;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.R;

/**
 * Created by alan fabricio on 14/03/2018.
 */

public class Mostrar_Usuarios extends AppCompatActivity {
    private ArrayList<Usuarios> usuarios = new ArrayList<>();
    private ListView lista;
    private int usuarioselecionado = -1;
    private  Object mActionMode;
    String usuarioLogiado;
    String usuarioEnClick;

    public  void onCreate(Bundle b){
        super.onCreate(b);

        setContentView(R.layout.mostrar_usuario);
        lista=(ListView)findViewById(R.id.idmostrar_usuario);
        Bundle extras =getIntent().getExtras();
        if(extras!=null){
            usuarioLogiado=extras.getString("Usuario_logiado");
        }
        llenarLista();

        onclick();


    }
    public void onclick(){
        lista.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        lista.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                usuarioselecionado = position;
                mActionMode =Mostrar_Usuarios.this.startActionMode(amc);
                view.setSelected(true);
                return true;

            }
        });
    }
    private ActionMode.Callback amc = new ActionMode.Callback() {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            getMenuInflater().inflate(R.menu.opciones,menu);

            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            if(item.getItemId()== R.id.EliminarItem){

                AlertDialog.Builder builder = new AlertDialog.Builder(Mostrar_Usuarios.this);
                builder.setTitle(R.string.Eliminar_usuario);
                builder.setMessage(R.string.mensaje);
                builder.setPositiveButton(R.string.eliminar, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(lista.getSelectedItem()=="Admin"){

                            Toast.makeText(Mostrar_Usuarios.this,"No se puede eliminar el usuario Administrador",Toast.LENGTH_SHORT).show();

                        }else{
                            eliminarUsuario();
                            Toast.makeText(Mostrar_Usuarios.this,R.string.usuario_eliminado,Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                builder.setNegativeButton(R.string.canselar, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(Mostrar_Usuarios.this,R.string.usuario_no_eliminado,Toast.LENGTH_SHORT).show();

                    }
                });
                Dialog dialog = builder.create();
                dialog.show();


                mode.finish();
            }else
            if(item.getItemId()==R.id.EditarItem){
                Usuarios usu = usuarios.get(usuarioselecionado);
                Intent in = new Intent(Mostrar_Usuarios.this,Editar_Usuarios.class);
               in.putExtra("id",usu.getId_usuario());
                startActivity(in);
                mode.finish();
                 finish();

            }
            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {

        }
    };


    //LLENAR El LIST VIEW CON LA BSASE DE DATOS

    public  void llenarLista(){
        ConexionSQLiteHelper bh = new ConexionSQLiteHelper(Mostrar_Usuarios.this,"bdaeo",null,1);
        if(bh!=null){
            SQLiteDatabase db = bh.getReadableDatabase();
            Cursor c = db.rawQuery("SELECT * FROM Usuarios",null);

            if (c.moveToFirst()){
                do{

                    usuarios.add(new Usuarios(c.getInt(0),c.getString(1), c.getString(2), c.getString(3),c.getInt(4),c.getInt(5)));
                }while (c.moveToNext());
            }
        }
        String[] arreglo = new String[usuarios.size()];
        for(int i =0; i<arreglo.length;i++){
            arreglo[i] = usuarios.get(i).getNombre_usuario();

        }
        ArrayAdapter<String> adaptador = new ArrayAdapter<String>(Mostrar_Usuarios.this,android.R.layout.simple_list_item_1,arreglo);

        lista.setAdapter(adaptador);
        adaptador.notifyDataSetChanged();


    }

    public  void eliminarUsuario(){
        ConexionSQLiteHelper bh = new ConexionSQLiteHelper(Mostrar_Usuarios.this,"bdaeo",null,1);
        if(bh!=null){
            SQLiteDatabase db = bh.getReadableDatabase();
            Usuarios usu = usuarios.get(usuarioselecionado);
            long respuesta = db.delete("USUARIOS","id_usuario="+usu.getId_usuario(),null);
            if(respuesta>0 ){
                usuarios.removeAll(usuarios);
                llenarLista();
                }


            }else {
                Toast.makeText(Mostrar_Usuarios.this,"Fallo",Toast.LENGTH_LONG).show();
            }
        }
    }