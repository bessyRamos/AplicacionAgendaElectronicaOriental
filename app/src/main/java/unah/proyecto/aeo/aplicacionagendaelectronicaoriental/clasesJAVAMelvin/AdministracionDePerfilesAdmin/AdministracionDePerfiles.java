package unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVAMelvin.AdministracionDePerfilesAdmin;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.view.ActionMode;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
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

public class AdministracionDePerfiles extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    //Declaración de atributos
    ArrayList<Fuente_mostrarPerfiles> mostrar_perfiles;
    private ListView lista;
    private int perfilselecionado = -1;
    int id_contacto;
    String nombre_organizacion;
    AdaptadorMostrarPerfiles adaptadorMostrarPerfiles;
    private  Object mActionMode;
    int idperf;
    ProgressBar barra;
    FloatingActionButton botonNuevoPerfil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_administracion_de_perfiles);
        //inicialización de componentes gráficos
        barra = findViewById(R.id.progressBarPerfiles);
        lista = (ListView) findViewById(R.id.listviewperfiles);
        mostrar_perfiles= new ArrayList<Fuente_mostrarPerfiles>();

        //Ejecución de la clase AsyncTask llenarLista
        new llenarLista().execute();

        //Se establece el listener al mantener presionado un item del listview
        lista.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                //se inicializa la variable con la posición del item seleccionado del listview
                perfilselecionado = position;
                //se inicia la construcción del menú superior
                mActionMode =AdministracionDePerfiles.this.startActionMode(amc);
                view.setSelected(true);
                return true;
            }
        });

        //se establece el listener del boton agregar perfil
        botonNuevoPerfil = (FloatingActionButton) findViewById(R.id.botonNuevoPerfil);
        botonNuevoPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),NuevoPerfil.class));
                finish();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, null, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.administracion_de_perfiles, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.menusolicitudesnuevas) {
            Intent i = new Intent(getApplicationContext(),NuevasSolicitudes.class);
            startActivity(i);

        } else if (id == R.id.menusolicitudesrechazadas) {
           Intent i = new Intent(getApplicationContext(),SolicitudesRechazadas.class);
            startActivity(i);
        } else if (id == R.id.menuperfileliminados) {
            Intent i = new Intent(getApplicationContext(),PerfilesEliminados.class);
            startActivity(i);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    //clase que se conecta al webservice y trae los registros solicitados
    private class llenarLista extends AsyncTask<String, Integer, Boolean> {
        private llenarLista(){}
        //variable booleana para controlar el resultado de las ejecuciones
        boolean resul = true;

        @Override
        protected Boolean doInBackground(String... strings) {

            try {
                //se conecta al webservice y almacena el resultado en un array tipo json
                JSONArray respJSON = new JSONArray(EntityUtils.toString(new DefaultHttpClient().execute(new HttpPost("https://shessag.000webhostapp.com/consultarPerfilesParaAdministracionPerfiles.php?id_estado=2")).getEntity()));
                //recorre el array para asignar los resultados a las variables
                for (int i = 0; i < respJSON.length(); i++) {
                    id_contacto = respJSON.getJSONObject(i).getInt("id_contacto");
                    nombre_organizacion = respJSON.getJSONObject(i).getString("nombre_organizacion");
                    //mostrar_perfiles.add(new Fuente_mostrarPerfiles(id_contacto, nombre_organizacion));

                }
            } catch (Exception ex) {
                Log.e("ServicioRest", "Error!", ex);
                resul = false;
            }
            return resul;

        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            //muestra el progreso en la barra de progreso
            barra.setProgress(values[0]);
        }

        protected void onPostExecute(Boolean result) {

            if (result) {
                //hace invisible la barra
                barra.setVisibility(View.INVISIBLE);
                //inicializa el adaptador
                adaptadorMostrarPerfiles = new AdaptadorMostrarPerfiles( mostrar_perfiles,getApplicationContext());
                //establece el adaptador al listview
                lista.setAdapter(adaptadorMostrarPerfiles);
                return;
            }else{
                //muestra mensaje si se produce un error al ejercutar la consulta al webservice
                Toast.makeText(getApplicationContext(), "Problemas de conexión", Toast.LENGTH_SHORT).show();
            }

        }


    }

    //metodo para eliminar un perfil

    public void removerperfil(final int pos) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Eliminar Perfil");
        String fmt= getResources().getString(R.string.eliminarPerfil);
        builder.setMessage(String.format(fmt,mostrar_perfiles.get(pos).getPerfil()));
        builder.setPositiveButton(R.string.eliminar, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //  llama a la clase que borra el perfil de la base de datos remota


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

    //clase AsyncTask que se conecta al webservice que ejecuta la consulta para borrar el perfil

    private class eliminarPerfil extends AsyncTask<String, Integer, Boolean> {
        private eliminarPerfil(){}
        boolean resul = true;

        @Override
        protected Boolean doInBackground(String... strings) {

            try {
                //Se obtiene el id del perfil que se va a eliminar
                Fuente_mostrarPerfiles perf = mostrar_perfiles.get(perfilselecionado);
                idperf=perf.getId();
                //se ejecuta la consulta al webservice y se pasa el id del perfil seleccionado
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

            }else if(item.getItemId()==R.id.EditarItem){
                Fuente_mostrarPerfiles per = mostrar_perfiles.get(perfilselecionado);
                Intent in = new Intent(getApplicationContext(),EditarPerfil.class);
                in.putExtra("id",per.getId());
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
}
