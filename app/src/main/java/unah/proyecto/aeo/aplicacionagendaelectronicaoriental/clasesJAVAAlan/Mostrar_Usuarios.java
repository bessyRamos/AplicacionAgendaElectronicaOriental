package unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVAAlan;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONArray;

import java.util.ArrayList;

import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.util.EntityUtils;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.R;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVAVirgilio.FormularioRegistroLogin;

/**
 * Created by alan fabricio on 14/03/2018.
 */

public class Mostrar_Usuarios extends AppCompatActivity {

    ArrayList<Fuente_mostrarUsuarios> mostrar_usuarios;
    private ListView lista;
    private int usuarioselecionado = -1;
    private  Object mActionMode;
    int id_usuario;
    String nombre_usuario;
    private int id_usuario_resibido;
Adaptador_mostrarusuarios adaptador;
    ProgressBar barra;
    int idusario;//es el id del usuario del web server.

    public  void onCreate(Bundle b){
        super.onCreate(b);

        mostrar_usuarios= new ArrayList<Fuente_mostrarUsuarios>();

        setContentView(R.layout.mostrar_usuario);
       new ArrayList<>();
        //flecha atras
        android.support.v7.app.ActionBar actionBar= getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        //fin de flecha
        lista=(ListView)findViewById(R.id.idmostrar_usuario);
        barra = findViewById(R.id.progressBarUsuarios);
        //RECIVIMOS EL USUARIO QUE SE LOGUIO EN LA AAPLICACIÓN.
        Bundle extras =getIntent().getExtras();
        if (getIntent().getExtras()!=null){
            id_usuario_resibido = getIntent().getExtras().getInt("usuario_ingreso");
        }
        //LLENAMOS LA LISTA QUEVIENE DESDE EL SERVIDOR.
        new llenarLista().execute();
       // llenarLista();
        onclick();

        FloatingActionButton floatingActionButton = (FloatingActionButton)findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Mostrar_Usuarios.this, FormularioRegistroLogin.class);
                startActivity(intent);
                finish();
            }
        });


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

    //LLENAR EL LIST VIEW DESDE EL WEB SERVER.
    private class llenarLista extends AsyncTask<String, Integer, Boolean> {
        private llenarLista(){}
        boolean resul = true;

        @Override
        protected Boolean doInBackground(String... strings) {

            try {
                JSONArray respJSON = new JSONArray(EntityUtils.toString(new DefaultHttpClient().execute(new HttpPost("https://shessag.000webhostapp.com/ConsultarTodosLosUsuarios.php")).getEntity()));
                for (int i = 0; i < respJSON.length(); i++) {
                    id_usuario = respJSON.getJSONObject(i).getInt("id_usuario");
                    nombre_usuario = respJSON.getJSONObject(i).getString("nombre_usuario");
                    mostrar_usuarios.add(new Fuente_mostrarUsuarios(id_usuario, nombre_usuario));

                }
            } catch (Exception ex) {
                Log.e("ServicioRest", "Error!", ex);
                resul = false;
            }
            return Boolean.valueOf(resul);

        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            barra.setProgress(values[0]);
        }

        protected void onPostExecute(Boolean result) {

            if (result.booleanValue()) {
                barra.setVisibility(View.INVISIBLE);
                adaptador = new Adaptador_mostrarusuarios( mostrar_usuarios,getApplicationContext());
                lista.setAdapter(adaptador);
                return;
            }
            Toast.makeText(getApplicationContext(), "Problemas de conexión", Toast.LENGTH_SHORT).show();
        }


    }


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
                    if (mostrar_usuarios.get(pos).getId()==id_usuario_resibido){
                     //   eliminarUsuario();
                        //PASAMOS EL NOMBRE DE LA CLASE QUE EJECUTA LA SENTENCIA SQL DEL WEB SERVISE
                        new eliminarUsuario().execute();
                        //Toast.makeText(Mostrar_Usuarios.this,R.string.usuario_eliminado,Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getBaseContext(), ActivityCategorias.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP));
                        finish();
                    }else{
                        new eliminarUsuario().execute();
                       // eliminarUsuario();
                        adaptador.notifyDataSetChanged();
                    }

                }else  {

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

    //ELIMINAR UN USUARIO DESDE EL WEB SERVISE.
    private class eliminarUsuario extends AsyncTask<String, Integer, Boolean> {
        private eliminarUsuario(){}
        boolean resul = true;

        @Override
        protected Boolean doInBackground(String... strings) {

            try {
                Fuente_mostrarUsuarios perf = mostrar_usuarios.get(usuarioselecionado);
                idusario=perf.getId();
                EntityUtils.toString(new DefaultHttpClient().execute(new HttpPost("https://shessag.000webhostapp.com/eliminacion_de_un_usuario.php?id_usuario="+idusario)).getEntity());

                resul = true;
            } catch (Exception ex) {
                Log.e("ServicioRest", "Error!", ex);
                resul = false;
            }
            return resul;

        }

        protected void onPostExecute(Boolean result) {

            if (resul) {
                Toast.makeText(getApplicationContext(),"Usuario Eliminado",Toast.LENGTH_SHORT).show();
                mostrar_usuarios.removeAll(mostrar_usuarios);
                new llenarLista().execute();
                adaptador.notifyDataSetChanged();
            }else {
                Toast.makeText(getApplicationContext(), "Problemas de conexión", Toast.LENGTH_SHORT).show();
            }
        }


    }


//CREACION DE EL MENU DONDE SE ENCUENTRA E BOTEN DE ELIMINAR Y EDITAR.
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

            }
            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {

        }
    };



    //LLENAR El LIST VIEW CON LA BSASE DE DATOS
/*
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

*/

    }

