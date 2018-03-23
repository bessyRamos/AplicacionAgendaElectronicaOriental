package unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVAAlan;

import android.app.ActionBar;
import android.app.Dialog;
import android.content.ContentValues;
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

    ArrayList<Fuente_mostrarUsuarios> mostrar_usuarios;
    private ListView lista;
    private int usuarioselecionado = -1;
    private  Object mActionMode;
    String usuarioLogiado;
    String usuarioEnClick;
Adaptador_mostrarusuarios adaptador;

    public  void onCreate(Bundle b){
        super.onCreate(b);

        mostrar_usuarios= new ArrayList<Fuente_mostrarUsuarios>();

        setContentView(R.layout.mostrar_usuario);
        new ArrayList<>();
        //flecha atraz
        android.support.v7.app.ActionBar actionBar= getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        //fin de flecha
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
                removerusuario(usuarioselecionado);
                mode.finish();



            }else
            if(item.getItemId()==R.id.EditarItem){
                Fuente_mostrarUsuarios usu = mostrar_usuarios.get(usuarioselecionado);
                Intent in = new Intent(Mostrar_Usuarios.this,Editar_Usuarios.class);
               in.putExtra("id",usu.getId());
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

//METODO PARA ELIMINAR USUARIO  ATRAVES  DE UN CUADRO DE DIALOGO
public void removerusuario(final int pos) {
    AlertDialog.Builder builder = new AlertDialog.Builder(this);
    builder.setTitle(R.string.eliminar_usuario);
    String fmt= getResources().getString(R.string.mensaje_para_eliminar);
    builder.setMessage(String.format(fmt,mostrar_usuarios.get(pos).getUsuario()));
    builder.setPositiveButton(R.string.eliminar, new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
          //  usuarios.remove(pos);
            if(!mostrar_usuarios.get(pos).getUsuario().contains("Admin")){
                    eliminarUsuario();
                    Toast.makeText(Mostrar_Usuarios.this,R.string.usuario_eliminado,Toast.LENGTH_SHORT).show();
                    adaptador.notifyDataSetChanged();
            }else{
                Toast.makeText(getApplicationContext(),"No se puede eliminar el usuario Administrador",Toast.LENGTH_SHORT).show();
            }
        }
    });

    builder.setNegativeButton(R.string.canselar,new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {


        }
    });
    builder.create().show();

}

    //LLENAR El LIST VIEW CON LA BSASE DE DATOS

    public  void llenarLista(){
        ConexionSQLiteHelper bh = new ConexionSQLiteHelper(Mostrar_Usuarios.this,"bdaeo",null,1);

            SQLiteDatabase db = bh.getReadableDatabase();
            Fuente_mostrarUsuarios fuente_mostrarUsuarios = null;
            Cursor c = db.rawQuery("SELECT id_usuario,nombre_usuario FROM Usuarios WHERE estado_usuario=1",null);
                while (c.moveToNext()){
                fuente_mostrarUsuarios = new Fuente_mostrarUsuarios();
                fuente_mostrarUsuarios.setId(c.getInt(0));
                fuente_mostrarUsuarios.setUsuario(c.getString(1));


                //se añade los datos al array
                mostrar_usuarios.add(fuente_mostrarUsuarios);

            adaptador = new Adaptador_mostrarusuarios(mostrar_usuarios,getApplicationContext());

                    //se pasa ala lista el adaptador
            lista.setAdapter(adaptador);

            }

    }


    //Eliminacion de usuarios desde la base de datos.
    public  void eliminarUsuario(){
        ConexionSQLiteHelper bh = new ConexionSQLiteHelper(Mostrar_Usuarios.this,"bdaeo",null,1);
        if(bh!=null){
            SQLiteDatabase db = bh.getReadableDatabase();
            Fuente_mostrarUsuarios usu = mostrar_usuarios.get(usuarioselecionado);
            ContentValues contenedor_valores= new ContentValues();
           contenedor_valores.put("estado_usuario",2) ;
            long respuesta = db.update("USUARIOS",contenedor_valores,"id_usuario="+usu.getId(),null);
            if(respuesta>0 ){
                mostrar_usuarios.removeAll(mostrar_usuarios);
                llenarLista();
                }


            }else {
                Toast.makeText(Mostrar_Usuarios.this,"Fallo",Toast.LENGTH_LONG).show();
            }
        }
    }

