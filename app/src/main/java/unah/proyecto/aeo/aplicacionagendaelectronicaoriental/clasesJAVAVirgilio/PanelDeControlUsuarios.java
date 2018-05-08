package unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVAVirgilio;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONArray;

import java.util.ArrayList;

import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.util.EntityUtils;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.R;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVAAlan.ActivityCategorias;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVAAlan.Panel_de_Control;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVAMelvin.AdministracionDePerfilesAdmin.EditarPerfil;

public class PanelDeControlUsuarios extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    //preferencia de administrador o usuario
    private Sesion sesion;
    private SesionUsuario sesionUsuario;
    //
    private Button salir;
    int id_usuario_resibido_usuario;
    private int id;
    String estadoOrganizacion;
    private ListView lista;
    //
    AdaptadorOrganizacion adaptadorMostrarPerfiles;
    ArrayList<EntidadOrganizacion> mostrar_perfiles;
    String nombre_organizacion;
    int id_contacto;
    int perfilselecionado=-1;
    int idperf;
    String imagen;

    int id_usu=-1;
    int id_usuario;
    ProgressBar barraProgreso;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_panel_de_control_usuarios);

        //envio de clase actual para las preferencias
        sesion = new Sesion(this);
        sesionUsuario = new SesionUsuario(this);
        SharedPreferences preferences = getSharedPreferences("credencial", Context.MODE_PRIVATE);
        id_usu  = preferences.getInt("usuario_ingreso",id_usu);
        //
        mostrar_perfiles= new ArrayList<EntidadOrganizacion>();
        FloatingActionButton agregar = (FloatingActionButton) findViewById(R.id.agregarContacto);
        lista = (ListView) findViewById(R.id.lista_pefil_empresa);

        //barra de progreso
        barraProgreso = (ProgressBar) findViewById(R.id.progresoPerfilesUsuario);
        barraProgreso.setProgress(0);
        //fin barra de progreso


        //se asegura que el extra no este vacio
        if (getIntent().getExtras()!=null){
            id_usuario_resibido_usuario = getIntent().getExtras().getInt("id");

        }else {
            Toast.makeText(getApplicationContext(),"Error en id de usuario",Toast.LENGTH_SHORT).show();
        }

        //llamado de que se ejecute el metodo llenarLista
        new llenarLista().execute();

        //boton flotante que lleva ala actividad de formulario para una nueva aplicacion

        agregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PanelDeControlUsuarios.this,FormularioNuevaOrganizacion.class);
                //se asegura que el extra no este vacio
                if (getIntent().getExtras()!=null){
                    id_usuario_resibido_usuario = getIntent().getExtras().getInt("id");
                    id = getIntent().getExtras().getInt("id");
                    //Toast.makeText(getApplicationContext(),""+id_usuario_resibido_usuario,Toast.LENGTH_SHORT).show();

                    intent.putExtra("id",id_usuario_resibido_usuario);
                    startActivity(intent);
                    finish();

                }

                //startActivity(intent);
            }
        });

        // metodo para el llenado de la lista de perfiles
        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int masterposition, long id) {
                perfilselecionado = masterposition;

                AlertDialog.Builder builder = new AlertDialog.Builder(PanelDeControlUsuarios.this);
                ListView modeloListView = new ListView(PanelDeControlUsuarios.this);
                String[] modes = new String[] {"Ver Contacto ","Borrar Contacto"};
                ArrayAdapter<String> modeAdapter  = new ArrayAdapter<String>(PanelDeControlUsuarios.this,android.R.layout.simple_list_item_1,android.R.id.text1,modes);

                modeloListView.setAdapter(modeAdapter);
                builder.setView(modeloListView);
                final Dialog dialog = builder.create();
                dialog.show();
                //metodo para el llenado de la lista al darle tap a cada perfil editar y borrar
                modeloListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        if (position == 0){  //Editar Contacto

                            EntidadOrganizacion per = mostrar_perfiles.get(perfilselecionado);
                            Intent intent= new Intent(getApplicationContext(),VerContactoOrganizacion.class);
                            intent.putExtra("id",per.getId());
                            intent.putExtra("id_usuario",id_usuario_resibido_usuario);
                            intent.putExtra("usuario",id_usuario_resibido_usuario);
                            startActivity(intent);

                        }else if(position ==1){ //Borrar Contacto

                            AlertDialog.Builder builder = new AlertDialog.Builder(PanelDeControlUsuarios.this);
                            builder.setTitle("Eliminar Perfil");
                            String fmt= getResources().getString(R.string.eliminarPerfil);
                            builder.setMessage(String.format(fmt,mostrar_perfiles.get(perfilselecionado).getTitulo()));
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

                        dialog.dismiss();
                    }
                });

                //Toast.makeText(getApplicationContext(),""+position,Toast.LENGTH_SHORT).show();
            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

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

    @SuppressWarnings("StatementWithEmptyBody")
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.principaldos) {
            startActivity(new Intent(getBaseContext(), ActivityCategorias.class)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP));
            finish();

        } else if (id == R.id.acercadeinfodos) {
            Intent intent = new Intent(this,AcercaDe.class);
            startActivity(intent);
            finish();

        }else if (id == R.id.login) {

            if (sesion.logindim()){
                Intent intent = new Intent(PanelDeControlUsuarios.this,Panel_de_Control.class);
                intent.putExtra("usuario_ingreso",id_usu);
                //startActivity(new Intent(PanelDeControlUsuarios.this,Panel_de_Control.class));
                startActivity(intent);
                finish();
            }else{
                if (sesionUsuario.logindimUsuario()){
                    Intent intent = new Intent(PanelDeControlUsuarios.this,PanelDeControlUsuarios.class);
                    intent.putExtra("id",id_usu);
                    //startActivity(new Intent(PanelDeControlUsuarios.this,PanelDeControlUsuarios.class));
                    startActivity(intent);
                    finish();
                }else {
                    Intent intent = new Intent(this, Login.class);
                    startActivity(intent);
                }

            }

        }else if (id ==R.id.cerrarsecion){
            //cerrar secion y borrado de preferencias
            if (sesion.logindim()) {
                sesion.setLogin(false);
                startActivity(new Intent(this, Login.class));
                finish();
            }else {
                //cerrar secion y borrado de preferencias
                if(sesionUsuario.logindimUsuario()){
                    sesionUsuario.setLoginUsuario(false);
                    startActivity(new Intent(this, Login.class));
                    finish();
                }
            }

        }else if (id == R.id.ediciondeCuenta){
            Intent intent = new Intent(this,EditarUsuario.class);
            if (getIntent().getExtras()!=null){
                id_usuario_resibido_usuario = getIntent().getExtras().getInt("id");

                intent.putExtra("id",id_usuario_resibido_usuario);
                startActivity(intent);
                finish();

            }else {
                Toast.makeText(getApplicationContext(),"Error en id de usuario",Toast.LENGTH_SHORT).show();
            }


        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //metodo de llenado de el listView
    private class llenarLista extends AsyncTask<String, Integer, Boolean> {
        private llenarLista(){}
        //variable booleana para controlar el resultado de las ejecuciones
        boolean resul = true;

        int progreso=0;

        @Override
        protected Boolean doInBackground(String... strings) {


            //verifica que el id no este vacio
            if (getIntent().getExtras()!=null){
                id_usuario_resibido_usuario = getIntent().getExtras().getInt("id");
            }

            try {
                JSONArray respJSON = new JSONArray(EntityUtils.toString(new DefaultHttpClient().execute(new HttpPost("http://aeo.web-hn.com/consultarOrganizacionesUsuarioLogeados.php?id_usuario="+id_usuario_resibido_usuario)).getEntity()));
                //recorre el array para asignar los resultados a las variables
                for (int i = 0; i < respJSON.length(); i++) {

                    id_contacto = respJSON.getJSONObject(i).getInt("id_contacto");
                    nombre_organizacion = respJSON.getJSONObject(i).getString("nombre_organizacion");
                    estadoOrganizacion = respJSON.getJSONObject(i).getString("id_estado");
                    imagen =respJSON.getJSONObject(i).getString("imagen");
                    id_usuario = respJSON.getJSONObject(i).getInt("id_usuario");

                    //comprueba el estado de la organizacion
                    if(1 == Integer.parseInt(estadoOrganizacion)){
                        estadoOrganizacion ="Pendiente";
                    }else if (2 == Integer.parseInt(estadoOrganizacion)){
                        estadoOrganizacion ="Activo";
                    }else if(3 == Integer.parseInt(estadoOrganizacion)){
                        estadoOrganizacion = "Rechazado";
                    }else if (4 == Integer.parseInt(estadoOrganizacion)){
                        estadoOrganizacion ="Eliminado";
                    }
                    //envia los datos ala clase pojo de el item lista

                    if (respJSON.getJSONObject(i).getInt("id_usuario")!=1){

                    }
                    mostrar_perfiles.add(new EntidadOrganizacion(id_contacto , nombre_organizacion, estadoOrganizacion,imagen));

                }
            } catch (Exception ex) {
                Log.e("ServicioRest", "Error!", ex);
                resul = false;
            }
            //barra de progreso
            while (progreso<100){
                progreso++;
                publishProgress(progreso);
                SystemClock.sleep(20);
            }
            //fin de barra de progreso

            return resul;

        }

        //barrra de progreso
        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            barraProgreso.setProgress(values[0]);

        }//fin de barra de progreso

        protected void onPostExecute(Boolean result) {

            if (result) {

                //ingresa en el adapter cada uno de los campos que provienen de el json
                adaptadorMostrarPerfiles = new AdaptadorOrganizacion(mostrar_perfiles,PanelDeControlUsuarios.this);
                //llena la lista con el item creado junto con la informacion de el json
                lista.setAdapter(adaptadorMostrarPerfiles);
                //barra de progreso
                barraProgreso.setVisibility(View.INVISIBLE);
                //fin de barra de progreso

                return;
            }else{
                //muestra mensaje si se produce un error al ejercutar la consulta al webservice
                Toast.makeText(getApplicationContext(), "Problemas de conexión", Toast.LENGTH_SHORT).show();
            }

        }


    }


    //clase AsyncTask que se conecta al webservice que ejecuta la consulta para borrar el perfil

    private class eliminarPerfil extends AsyncTask<String, Integer, Boolean> {
        private eliminarPerfil(){}
        boolean resul = true;

        @Override
        protected Boolean doInBackground(String... strings) {

            try {
                //Se obtiene el id del perfil que se va a eliminar
                EntidadOrganizacion perf = mostrar_perfiles.get(perfilselecionado);
                idperf=perf.getId();
                //se ejecuta la consulta al webservice y se pasa el id del perfil seleccionado
                EntityUtils.toString(new DefaultHttpClient().execute(new HttpPost("http://aeo.web-hn.com/eliminarPerfil.php?id_contacto="+idperf)).getEntity());
                resul = true;
            } catch (Exception ex) {
                Log.e("ServicioRest", "Error!", ex);
                resul = false;
            }
            return resul;

        }

        protected void onPostExecute(Boolean result) {

            if (resul) {
                //barra de progreso
                barraProgreso.setVisibility(View.VISIBLE);
                //fin de barra de progreso
                Toast.makeText(getApplicationContext(),"Perfil Eliminado",Toast.LENGTH_SHORT).show();
                mostrar_perfiles.removeAll(mostrar_perfiles);
                new llenarLista().execute();
                adaptadorMostrarPerfiles.notifyDataSetChanged();
            }else {
                Toast.makeText(getApplicationContext(), "Problemas de conexión", Toast.LENGTH_SHORT).show();
            }
        }


    }




}
