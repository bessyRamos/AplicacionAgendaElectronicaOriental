package unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVAVirgilio;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;

import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.util.EntityUtils;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.R;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVAAlan.ActivityCategorias;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVAAlan.Panel_de_Control;

public class EditarUsuario extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private int usuarioEditar;
    private EditText nombreusuario,nombrepropio,contrasena;
    String nombreusuariobar,nombrepropiobar,contrasenabar;
    Button bottonvalidar;
    String nombre_usuario,nombre_propio,contra;

    private Sesion sesion;
    private SesionUsuario sesionUsuario;
    int id_usu=-1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_usuario);

        bottonvalidar = (Button)findViewById(R.id.editar);

        //envio de clase actual para las preferencias
        sesion = new Sesion(this);
        sesionUsuario = new SesionUsuario(this);
        SharedPreferences preferences = getSharedPreferences("credencial", Context.MODE_PRIVATE);

            id_usu  = preferences.getInt("usuario_ingreso",id_usu);
            //




        //RECIVIMOS EL ID QUE VIENE DE LA CLASE MOSTRAR USUARIOS.
        Bundle extras = this.getIntent().getExtras();
        if(extras!=null) {
            usuarioEditar = extras.getInt("id");

        }
        bottonvalidar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new actualizarUsuarios().execute();


            }
        });

        nombreusuario = (EditText)findViewById(R.id.EditUsuario);
        nombrepropio = (EditText)findViewById(R.id.EditNombre);
        contrasena= (EditText)findViewById(R.id.Editcontrasena);


        // reflejarCampos();
        //SE OPTIENEN LOS DATOS DEL SERVER Y SE PASAN A VARIABLES
        new llenarlosEditTextdelServer().execute();
        nombreusuariobar=nombreusuario.getText().toString();
        nombrepropiobar=nombrepropio.getText().toString();
        contrasenabar=contrasena.getText().toString();


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
                Intent intent = new Intent(EditarUsuario.this,Panel_de_Control.class);
                intent.putExtra("usuario_ingreso",id_usu);
                //startActivity(new Intent(ActivityCategorias.this,Panel_de_Control.class));
                startActivity(intent);
                finish();

            }else{
                if (sesionUsuario.logindimUsuario()){
                    Intent intent = new Intent(EditarUsuario.this,PanelDeControlUsuarios.class);
                    intent.putExtra("id",id_usu);
                    //startActivity(new Intent(ActivityCategorias.this,PanelDeControlUsuarios.class));
                    startActivity(intent);
                    finish();

                }else {
                    Intent intent = new Intent(this, Login.class);
                    startActivity(intent);
                    finish();
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

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }




    private void validar(){

        //id.setError(null);
        nombreusuario.setError(null);
        nombrepropio.setError(null);
        contrasena.setError(null);


        // String idd = id.getText().toString();
        String nombusus = nombreusuario.getText().toString();
        String nomb = nombrepropio.getText().toString();
        String cont = contrasena.getText().toString();


        if(TextUtils.isEmpty(nombusus)){
            nombreusuario.setError(getString(R.string.error_nombre_usuario));
            nombreusuario.requestFocus();
            return;
        }
        if(TextUtils.isEmpty(nomb)){
            nombrepropio.setError(getString(R.string.error_nombre));
            nombrepropio.requestFocus();
            return;

        }if(TextUtils.isEmpty(cont)){
            contrasena.setError(getString(R.string.error_contrasena));
            contrasena.requestFocus();
            return;

        }

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



    //ACTUALIZACION DE UN USUARIO DESDE EL WEB SERVER
    private class actualizarUsuarios extends AsyncTask<String, Integer, Boolean> {
        private actualizarUsuarios(){}
        boolean resul = true;

        @Override
        protected Boolean doInBackground(String... strings) {

            try {
                nombreusuariobar=nombreusuario.getText().toString();
                nombrepropiobar=nombrepropio.getText().toString().replace(" ","%20");
                contrasenabar=contrasena.getText().toString();

                EntityUtils.toString(new DefaultHttpClient().execute(new HttpPost("http://aeo.web-hn.com/actualizacion_de_un_usuario.php?id_usuario="+usuarioEditar+"&nombre_usuario="+nombreusuariobar+"&nombre_propio="+nombrepropiobar+"&contrasena="+contrasenabar)).getEntity());

                resul = true;
            } catch (Exception ex) {
                Log.e("ServicioRest", "Error!", ex);
                resul = false;
            }
            return resul;

        }

        protected void onPostExecute(Boolean result) {
            validar();


            if (resul) {
                if (nombreusuario.getError()==null && nombrepropio.getError()==null && contrasena.getError()==null){
                    Toast.makeText(getApplicationContext(),"Usuario Realizado Correctamente",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(EditarUsuario.this,PanelDeControlUsuarios.class);
                    intent.putExtra("id",id_usu);
                    startActivity(intent);
                    finish();
                }

            }else {
                Toast.makeText(getApplicationContext(), "Problemas de conexión", Toast.LENGTH_SHORT).show();
            }
        }


    }
    //METODO PARA LLENAR CUANDO SE ACTUALIZARON LOS USUARIOS DESDE EL WEB SERVER.
    private class llenarlosEditTextdelServer extends AsyncTask<String, Integer, Boolean> {
        private llenarlosEditTextdelServer(){}
        boolean resul = true;

        @Override
        protected Boolean doInBackground(String... strings) {

            try {
                JSONArray respJSON = new JSONArray(EntityUtils.toString(new DefaultHttpClient().execute(new HttpPost("http://aeo.web-hn.com/Mostar_Los_Usuarios_Editados.php?id_usuario="+usuarioEditar)).getEntity()));
                for (int i = 0; i < respJSON.length(); i++) {
                    nombre_usuario = respJSON.getJSONObject(i).getString("nombre_usuario");
                    nombre_propio = respJSON.getJSONObject(i).getString("nombre_propio");
                    contra = respJSON.getJSONObject(i).getString("contrasena");
                }

                resul = true;
            } catch (Exception ex) {
                Log.e("ServicioRest", "Error!", ex);
                resul = false;
            }
            return resul;

        }

        protected void onPostExecute(Boolean result) {

            if (resul) {
                nombreusuario.setText(nombre_usuario);
                nombrepropio.setText(nombre_propio);
                contrasena.setText(contra);
            }else {
                Toast.makeText(getApplicationContext(), "Problemas de conexión", Toast.LENGTH_SHORT).show();
            }
        }


    }

}

