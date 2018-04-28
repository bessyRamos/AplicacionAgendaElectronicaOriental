package unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVAVirgilio;

import android.content.Context;
import android.content.Entity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.renderscript.ScriptGroup;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

import javax.net.ssl.HttpsURLConnection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.*;
import org.json.JSONStringer;

import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.util.EntityUtils;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.ConexionSQLiteHelper;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.R;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVAAlan.ActivityCategorias;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVAAlan.Panel_de_Control;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVASheyli.RecuperacionDePassword;

public class Login extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    public EditText usuario, contrasena;
    public Button button;
    public TextView recuperar;//para recuperacion de contrasenia
    ConexionSQLiteHelper basedatos = new ConexionSQLiteHelper(this, "bdaeo", null, 1);
    SQLiteDatabase conexion;
    String usuarioPermiso, contrasenaPermiso;
    int idRol;

    private int contador = 0;
    private int id_usuario;
    private int rol;
    private int estado_usuario;
    private JSONObject jsonObject;
    private Button acceder,registrarse;
    String nombre_traido,contrasena_traida;

    // preferencia de administrador
    //private SharedPreferences preferences;
    //private SharedPreferences.Editor editor;
    Context context=this;
    String usuari;
    String contrase;

    //

    //preferencia de usuario
    private SesionUsuario  sessionUsuario;
    private Sesion session;
    private SharedPreferences preferencesUsuario;
    private SharedPreferences.Editor editorUsuario;

    //
    int id_preferencia;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    int id_usu=-1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        usuario = (EditText) findViewById(R.id.usuario_login);
        contrasena = (EditText) findViewById(R.id.contrasena_login);
        //Preferencias de administrador y usuario
        session = new Sesion(this);
        sessionUsuario = new SesionUsuario(this);

        preferences = getSharedPreferences("credencial",Context.MODE_PRIVATE);
        editor = preferences.edit();


        //
        acceder = (Button) findViewById(R.id.ingresar_login);
        recuperar = (TextView) findViewById(R.id.recuperacion);//para recuperacion de contrasenia
        acceder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    if (usuario.getText().toString().isEmpty() || contrasena.getText().toString().isEmpty()) {
                        //Toast.makeText(getApplicationContext(), "Favor ingresar todos los Campos", Toast.LENGTH_SHORT).show();
                        validar();
                    } else {                          //si existe el usuario y la contraseña son correctas el accedera

                        new LoginValidadoWeb().execute();

                    }//fin else
            }
        });


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //muestra el menu lateral
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    //abrira activity recuperar contrasenia
    public void recuperar1(View v){

        Intent password = new Intent(this, RecuperacionDePassword.class);
        startActivity(password);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.principaldos) {
            startActivity(new Intent(getBaseContext(), ActivityCategorias.class)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP));
            finish();
        } else if (id == R.id.acercadeinfodos) {
            Intent intent = new Intent(this, AcercaDe.class);
            startActivity(intent);

        } else if (id == R.id.login) {


        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

//Metodo para ingresar al formulario de registrar una nueva cuenta de usuario
    public void Formulario_Registrarse_login(View v) {   //metodo que habre el formulario para registrarse
        Intent intent = new Intent(this, FormularioRegistroUsuario.class);
        usuario.setText("");
        contrasena.setText("");
        startActivity(intent);
    }


    public void Ingresar_Login(View v) { // metodo que verificaque se ingresen datos en los campos usuario y contraseña


    }//fin de boton

    private Cursor ConsultarUsuarioPassword(String usuario, String password) throws SQLException {
        conexion = basedatos.getReadableDatabase();
        Cursor mcursor = null;
        int estado = 1;
        int rol = 1;
        mcursor = conexion.query("Usuarios", new String[]{"id_usuario", "nombre_usuario", "nombre_propio", "contrasena", "rol", "estado_usuario"}, "nombre_usuario like'" + usuario + "'and  contrasena like '" + password + "'and  estado_usuario like '" + estado + "'and  rol like '" + rol + "'", null, null, null, null);
        return mcursor;
    }

    private void consultaUsuContra(String nombre_usuario, String password_usuario) {
        String usuario_resibido, password_resibido;

        usuario_resibido = usuario.getText().toString();
        password_resibido = contrasena.getText().toString();

        //
        usuari = usuario_resibido;
        contrase = password_resibido;
        //

        Cursor fila = conexion.rawQuery("select nombre_usuario,contrasena,id_usuario,estado_usuario from Usuarios WHERE nombre_usuario= '" + usuario_resibido + "'and contrasena='" + password_resibido + "'", null);

        while (fila.moveToNext()) {
            String usua = fila.getString(0);
            String pass = fila.getString(1);
            if (usuario_resibido.equals(usua) && password_resibido.equals(pass)) {
                Intent intent = new Intent(this, Panel_de_Control.class);
                usuario.setText("");
                contrasena.setText("");
                startActivity(intent);
            } else {
                Toast.makeText(getApplicationContext(), "usuario o contrase;a erronea", Toast.LENGTH_SHORT).show();
            }
        }

    }
//METODO DE VERIFICADE DESDE EL SERVIDOR
    private class LoginValidadoWeb extends AsyncTask<String, Integer, Boolean> {
        private LoginValidadoWeb() {
        }

        boolean resul = true;

        @Override
        protected Boolean doInBackground(String... strings) {

            String usu=usuario.getText().toString();
            String pas=contrasena.getText().toString();

            try {
                // Parseamos la respuesta obtenida del servidor a un objeto JSON
                JSONObject jsonObject = new JSONObject(EntityUtils.toString(new DefaultHttpClient().execute(new HttpPost("http://aeo.web-hn.com/validar_usuario.php?nombre_usuario="+ usu + "&contrasena="+pas)).getEntity()));
                JSONArray jsonArray = jsonObject.getJSONArray("datos");
                for (int i = 0; i < jsonArray.length(); i++) {

                    id_usuario = jsonArray.getJSONObject(i).getInt("id_usuario");
                    rol = jsonArray.getJSONObject(i).getInt("rol");
                    estado_usuario = jsonArray.getJSONObject(i).getInt("estado_usuario");

                }
                if (id_usuario != 0 && rol != 0 && estado_usuario != 0) {
                    resul = true;
                } else {
                    if(   (    ((id_usuario != 0) && (rol==1 && estado_usuario==2)) ||  ( (id_usuario != 0 && rol==2) && (estado_usuario==2) ) ) ) {
                        resul = false;
                    }else if (id_usuario==0){
                        resul = false;
                    }else {
                        resul = false;
                    }
                }

            } catch (Exception ex) {
                ex.printStackTrace();
                resul = false;
            }


            return resul;
        }

    protected void onPostExecute(Boolean result) {
        if (resul) {
            id_preferencia = id_usuario;
            editor.putInt("usuario_ingreso",id_preferencia);
            editor.commit();

            if (rol == 1 && estado_usuario ==1) {
                //instancia y envio de usuario logeado
                Intent intent = new Intent(Login.this, Panel_de_Control.class);
                intent.putExtra("usuario_ingreso",id_preferencia);
                //preferencia logeado con exito
                session.setLogin(true);
                //
                usuario.setText("");
                contrasena.setText("");
                startActivity(intent);
                finish();
            } else if (rol ==2 && estado_usuario ==1){
                    //instancia y envio de usuario logeado
                    Intent intent = new Intent(Login.this,PanelDeControlUsuarios.class);
                    intent.putExtra("id",id_preferencia);
                    //preferencia logeado con exito usuario
                    sessionUsuario.setLoginUsuario(true);
                    //limpieza de variables
                    usuario.setText("");
                    contrasena.setText("");
                    //
                    startActivity(intent);
                    finish();
                }else {
                    Toast.makeText(getApplicationContext(),"Usuario y/o Contraseña incorrecta ",Toast.LENGTH_LONG).show();
                }


        } else {
            if ( (rol == 1 && estado_usuario==2) || (rol ==2 && estado_usuario ==2)) {

                //numero de intentos
                contador = contador + 1;
                if (contador == 3) {
                    usuario.setText("");
                    contrasena.setText("");
                    Toast.makeText(getApplicationContext(), "Limite de intentos agotados", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "Usuario y/o Contraseña incorrecta", Toast.LENGTH_SHORT).show();
                }

            }else if (id_usuario ==0){
                //numero de intentos
                contador=contador+1;
                if (contador ==3){
                    usuario.setText("");
                    contrasena.setText("");
                    Toast.makeText(getApplicationContext(), "Limite de intentos agotados", Toast.LENGTH_SHORT).show();
                    finish();
                }else {
                    Toast.makeText(getApplicationContext(), "Usuario y/o Contraseña incorrecta", Toast.LENGTH_SHORT).show();
                }

            }else  if((id_usuario != 0 && rol==1 && estado_usuario==2) || (id_usuario != 0 && rol==2 && estado_usuario==2)){
                Toast.makeText(getApplicationContext(), "Usuario y/o Contraseña incorrecta", Toast.LENGTH_SHORT).show();

             } else if (result!=true){
                Toast.makeText(getApplicationContext(), "Problemas de Conexion", Toast.LENGTH_SHORT).show();


            }//fin de else fallo conexion, buscar sqlite

        }//fin de onPostExecute

    }//fin boolean result

    }//fin boolean





    private void validar(){

        //id.setError(null);
        usuario.setError(null);
        contrasena.setError(null);


        // String idd = id.getText().toString();

        String us = usuario.getText().toString();
        String cont = contrasena.getText().toString();


        if(TextUtils.isEmpty(us)){
            usuario.setError(getString(R.string.error_usuario));
            usuario.requestFocus();
            return;
        }if(TextUtils.isEmpty(cont)){
            contrasena.setError(getString(R.string.error_contrasenaingresada));
            contrasena.requestFocus();
            return;

        }


    }



    public void permisoAdmin() {
        SQLiteDatabase permiso = basedatos.getReadableDatabase();
        idRol = 1;
        Cursor cursorP = permiso.rawQuery("SELECT nombre_usuario, contrasena FROM USUARIOS WHERE rol = " + idRol, null);
        while (cursorP.moveToNext()) {
            usuarioPermiso = cursorP.getString(0);
            contrasenaPermiso = cursorP.getString(1);
        }

        if (usuarioPermiso != "Admin") {
            Toast.makeText(getApplicationContext(), "Permiso denegado", Toast.LENGTH_SHORT).show();
        } else {
            Intent p = new Intent(getApplicationContext(), Panel_de_Control.class);
            startActivity(p);
        }
    }

    public int obtenerDatosJson(String respuest) {
        int res = 0;
        try {
            JSONArray json = new JSONArray(respuest);
            if (json.length() > 0) {
                res = 1;

            }

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Error inesperado en respuesta ", Toast.LENGTH_SHORT).show();
        }

        return res;
    }

    public int informacion(String response) {
        int res = 0;
        try {
            final JSONArray json = new JSONArray(response);
            if (json.length() > 0) {
                for (int i = 0; i < response.length(); i++) {
                    String[] hola = new String[0];
                    //hola [i]= response.json.getString("id_usuario");
                    jsonObject = new JSONObject(String.valueOf(jsonObject.getInt("id_usuario")));

                }
                res = Integer.parseInt(String.valueOf(jsonObject));

            }

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Error inesperado en respuesta ", Toast.LENGTH_SHORT).show();
        }

        return res;

    }

    private void cargarPreferencia (){

        SharedPreferences preferences = getSharedPreferences("credencial",Context.MODE_PRIVATE);
        int id_usu  = preferences.getInt("usuario_ingreso",-1);
        // intent.putExtra("usuario_ingreso",id_preferencia);


    }

}