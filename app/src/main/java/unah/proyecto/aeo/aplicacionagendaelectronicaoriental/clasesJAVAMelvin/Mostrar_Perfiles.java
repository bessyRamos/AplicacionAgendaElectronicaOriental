package unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVAMelvin;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.Toolbar;

import org.json.JSONArray;

import java.util.ArrayList;

import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.util.EntityUtils;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.ConexionSQLiteHelper;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.R;

public class Mostrar_Perfiles extends AppCompatActivity {
    ArrayList<Fuente_mostrarPerfiles> mostrar_perfiles;
    private ListView lista;
    private int perfilselecionado = -1;
    int id_contacto;
    String nombre_organizacion;
    AdaptadorMostrarPerfiles adaptadorMostrarPerfiles;
    private  Object mActionMode;
    int idperf;
    ProgressBar barra;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostrar__perfiles);

        android.support.v7.app.ActionBar actionBar= getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        barra = findViewById(R.id.progressBarPerfiles);
        lista = (ListView) findViewById(R.id.listviewperfiles);
        mostrar_perfiles= new ArrayList<Fuente_mostrarPerfiles>();

        new llenarLista().execute();

       lista.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                perfilselecionado = position;
                mActionMode =Mostrar_Perfiles.this.startActionMode(amc);
                view.setSelected(true);
                return true;
            }
        });
    }

    private class llenarLista extends AsyncTask<String, Integer, Boolean> {
        private llenarLista(){}
        boolean resul = true;

        @Override
        protected Boolean doInBackground(String... strings) {

            try {
               JSONArray respJSON = new JSONArray(EntityUtils.toString(new DefaultHttpClient().execute(new HttpPost("https://shessag.000webhostapp.com/ConsultarTodosLosPerfiles.php")).getEntity()));
                for (int i = 0; i < respJSON.length(); i++) {
                    id_contacto = respJSON.getJSONObject(i).getInt("id_contacto");
                    nombre_organizacion = respJSON.getJSONObject(i).getString("nombre_organizacion");
                    mostrar_perfiles.add(new Fuente_mostrarPerfiles(id_contacto, nombre_organizacion));

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
                adaptadorMostrarPerfiles = new AdaptadorMostrarPerfiles( mostrar_perfiles,getApplicationContext());
                lista.setAdapter(adaptadorMostrarPerfiles);
                return;
            }
            Toast.makeText(getApplicationContext(), "Problemas de conexión", Toast.LENGTH_SHORT).show();
        }


    }

    public void removerperfil(final int pos) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Eliminar Perfil");
        String fmt= getResources().getString(R.string.eliminarPerfil);
        builder.setMessage(String.format(fmt,mostrar_perfiles.get(pos).getPerfil()));
        builder.setPositiveButton(R.string.eliminar, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //  usuarios.remove(pos);


                        new eliminarPerfil().execute();

            }
        });

        builder.setNegativeButton(R.string.canselar,new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {


            }
        });
        builder.create().show();

    }



    private class eliminarPerfil extends AsyncTask<String, Integer, Boolean> {
        private eliminarPerfil(){}
        boolean resul = true;

        @Override
        protected Boolean doInBackground(String... strings) {

            try {
                Fuente_mostrarPerfiles perf = mostrar_perfiles.get(perfilselecionado);
                idperf=perf.getId();
                EntityUtils.toString(new DefaultHttpClient().execute(new HttpPost("https://shessag.000webhostapp.com/eliminarPerfil.php?id_contacto="+idperf)).getEntity());
                resul = true;
            } catch (Exception ex) {
                Log.e("ServicioRest", "Error!", ex);
                resul = false;
            }
            return resul;

        }

        protected void onPostExecute(Boolean result) {

            if (resul) {
                Toast.makeText(getApplicationContext(),"Perfil Eliminado",Toast.LENGTH_SHORT).show();
                mostrar_perfiles.removeAll(mostrar_perfiles);
                new llenarLista().execute();
                adaptadorMostrarPerfiles.notifyDataSetChanged();
            }else {
                Toast.makeText(getApplicationContext(), "Problemas de conexión", Toast.LENGTH_SHORT).show();
            }
        }


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
                removerperfil(perfilselecionado);
                mode.finish();

            }/*else
            if(item.getItemId()==R.id.EditarItem){
                Fuente_mostrarPerfiles per = mostrar_perfiles.get(perfilselecionado);
                Intent in = new Intent(getApplicationContext(),Editar_Perfiles.class);
                in.putExtra("id",per.getId());
                startActivity(in);
                mode.finish();
                finish();
            }*/
            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {

        }
    };
}
