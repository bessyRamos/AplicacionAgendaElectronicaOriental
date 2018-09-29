package unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVAVirgilio;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;
import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.entity.UrlEncodedFormEntity;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.message.BasicNameValuePair;
import cz.msebera.android.httpclient.util.EntityUtils;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.R;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVAAlan.ActivityCategorias;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVAAlan.Panel_de_Control;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVAAlan.SharedPrefManager;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVAMelvin.HerramientaBusquedaAvanzada.BusquedaAvanzada;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVAMelvin.PerfilesBreves.ListaDeContactos;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVASheyli.RecuperacionDePassword;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVAVirgilio.DatoT;

public class Login extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    public EditText usuario, contrasena;
    public Button button;
    private int contador = 0;
    private int id_usuario;
    private int rol;
    private int estado_usuario;
    //private Button acceder,registrarse;
    private static final String IP_TOKEN="http://aeo.web-hn.com/RegisterDevice.php";
    Context context=this;
    //preferencia de usuario
    private SesionUsuario  sessionUsuario;
    private Sesion session;


    int id_preferencia;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    private CircularProgressButton acceder;
    private MenuPreferencias menu;
    String nada,dato,tkasig;
    SharedPreferences logue;
    SharedPreferences.Editor editorLogueo;

    DatoT datoT = new DatoT(this);

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

        logue= getSharedPreferences("Nombre",Context.MODE_PRIVATE);
        editorLogueo = logue.edit();

        menu=new MenuPreferencias(this);
        acceder = (CircularProgressButton) findViewById(R.id.ingresar_login);
        acceder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                acceder.startAnimation();

                if (usuario.getText().toString().isEmpty() || contrasena.getText().toString().isEmpty()) {
                    //Toast.makeText(getApplicationContext(), "Favor ingresar todos los Campos", Toast.LENGTH_SHORT).show();
                    validar();
                    acceder.revertAnimation();
                    acceder.stopAnimation();
                } else {
                    new LoginValidadoWeb().execute();//si existe el usuario y la contraseña son correctas el accedera


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
            Intent intent = new Intent();
            setResult(ActivityCategorias.RESULT_CANCELED,intent);
            setResult(ListaDeContactos.RESULT_CANCELED,intent);
            setResult(BusquedaAvanzada.RESULT_CANCELED,intent);

            startActivity(new Intent(getBaseContext(), ActivityCategorias.class)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP));
            finish();
        }
    }
    @Override
    public void onRestart()
    {
        super.onRestart();
        finish();
        startActivity(getIntent());
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
            Intent intent = new Intent();
            setResult(ActivityCategorias.RESULT_CANCELED,intent);
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

                HttpClient httpclient;
                HttpPost httppost;
                ArrayList<NameValuePair> parametros;
                httpclient = new DefaultHttpClient();
                httppost = new HttpPost("http://aeo.web-hn.com/WebServices/validar_usuario.php");
                parametros = new ArrayList<NameValuePair>();
                parametros.add(new BasicNameValuePair("nombre_usuario",usu));
                parametros.add(new BasicNameValuePair("contrasena",pas));
                httppost.setEntity(new UrlEncodedFormEntity(parametros, "UTF-8"));

                JSONObject credencial = new JSONObject(EntityUtils.toString(httpclient.execute(httppost).getEntity()));
                    id_usuario = credencial.getInt("idUrs");
                    rol = credencial.getInt("rol");
                    estado_usuario = credencial.getInt("ste");
                    tkasig = credencial.getString("token");


                if (id_usuario != 0 && rol != 0 && estado_usuario != 0) {
                    resul = true;
                } else {
                        resul = false;
                }
                if (id_usuario==0){
                    resul = false;
                }
                if (credencial.equals("Credenciales incorrectos")){
                    nada="nada";
                }
                if (id_usuario!=0&& rol==1&&estado_usuario==2){
                    resul = false;
                }
                if (id_usuario!=0&& rol==2&&estado_usuario==2){
                    resul = false;
                }


            } catch (Exception ex) {
                Log.e("ServicioRest", "Error!", ex);
                resul = false;
            }


            return resul;
        }

        protected void onPostExecute(Boolean result) {

            if (resul) {
                dato = tkasig;
                id_preferencia = id_usuario;
                editor.putInt("usuario_ingreso",id_preferencia);
                editor.putInt("usuario_admin",id_preferencia);
                editor.commit();


                acceder.stopAnimation();
                acceder.revertAnimation();



                 if (rol == 1 && estado_usuario ==1) {
                    Intent intent = new Intent(Login.this,Panel_de_Control.class);

                    session.setLogin(true);
                    sessionUsuario.setLoginUsuario(false);


                    intent.putExtra("usuario_ingreso",id_preferencia);

                    editorLogueo.putInt("Admin",id_usuario);
                    editorLogueo.commit();
                    startActivity(intent);
                    //limpieza de variables
                    usuario.setText("");
                    contrasena.setText("");
                    //
                    finish();

                } else if (rol ==2 && estado_usuario ==1){

                    Intent intent = new Intent(Login.this,PanelDeControlUsuarios.class);
                    sessionUsuario.setLoginUsuario(true);
                    menu.setLoginMenu(true);
                    session.setLogin(false);

                    intent.putExtra("id",id_preferencia);
                    intent.putExtra("usuario_ingreso",id_preferencia);

                    editorLogueo.putInt("Normal",id_usuario);
                    editorLogueo.commit();

                    startActivity(intent);

                    //limpieza de variables
                    usuario.setText("");
                    contrasena.setText("");
                    //
                    finish();


                }

            } else {
                acceder.stopAnimation();
                acceder.revertAnimation();

                if (id_usuario==0){
                    contador=contador+1;
                    if (contador ==3){
                        usuario.setText("");
                        contrasena.setText("");
                        Toast.makeText(getApplicationContext(), "Limite de intentos agotados", Toast.LENGTH_SHORT).show();
                        finish();
                    }else {
                        Toast.makeText(getApplicationContext(), "Usuario y/o Contraseña incorrecta", Toast.LENGTH_SHORT).show();
                    }
                   // Toast.makeText(getApplicationContext(),"Usuario y/o Contraseña incorrecta 1",Toast.LENGTH_LONG).show();
                }else  if (id_usuario!=0&& rol==1&&estado_usuario==2){
                    contador=contador+1;
                    if (contador ==3){
                        usuario.setText("");
                        contrasena.setText("");
                        Toast.makeText(getApplicationContext(), "Limite de intentos agotados", Toast.LENGTH_SHORT).show();
                        finish();
                    }else {
                        Toast.makeText(getApplicationContext(), "Usuario y/o Contraseña incorrecta", Toast.LENGTH_SHORT).show();
                    }
                    //Toast.makeText(getApplicationContext(),"Usuario y/o Contraseña incorrecta 2",Toast.LENGTH_LONG).show();
                }else if (id_usuario!=0&& rol==2&&estado_usuario==2){
                    contador=contador+1;
                    if (contador ==3){
                        usuario.setText("");
                        contrasena.setText("");
                        Toast.makeText(getApplicationContext(), "Limite de intentos agotados", Toast.LENGTH_SHORT).show();
                        finish();
                    }else {
                        Toast.makeText(getApplicationContext(), "Usuario y/o Contraseña incorrecta", Toast.LENGTH_SHORT).show();
                    }
                    //Toast.makeText(getApplicationContext(),"Usuario y/o Contraseña incorrecta 3",Toast.LENGTH_LONG).show();
                } else if (nada.equals(nada)) {
                    //Toast.makeText(getApplicationContext(), "Usuario y/o Contraseña incorrecta ", Toast.LENGTH_LONG).show();
                }if (compruebaConexion()==false){
                    Toast.makeText(getApplicationContext(), "Problemas de conexion ", Toast.LENGTH_LONG).show();

                }

            }//fin de onPostExecute

        }//fin boolean result
        public boolean compruebaConexion() {

            Runtime runtime = Runtime.getRuntime();
            try {
                Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
                int     exitValue = ipProcess.waitFor();
                return (exitValue == 0);
            } catch (IOException e)          { e.printStackTrace(); }
            catch (InterruptedException e) { e.printStackTrace(); }
            return false;
        }

    }//fin boolean

    private void validar(){

        //id.setError(null);
        usuario.setError(null);
        contrasena.setError(null);

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

    private void sendTokenToServer() {
        //  progressDialog = new ProgressDialog(FormularioRegistroLogin.this);
        //  progressDialog.setMessage("Registering Device...");
        //  progressDialog.show();

        final String token = SharedPrefManager.getInstance(Login.this).getDeviceToken();
        // final String email = editTextEmail.getText().toString();

        if (token == null) {
            //  progressDialog.dismiss();
            Toast.makeText(Login.this, "Token not generated", Toast.LENGTH_LONG).show();
            return;
        }

        StringRequest stringRequest = new StringRequest(Request.Method.POST,IP_TOKEN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //  progressDialog.dismiss();
                        try {
                            JSONObject obj = new JSONObject(response);
                            // Toast.makeText(FormularioRegistroLogin.this, obj.getString("message"), Toast.LENGTH_LONG).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //   progressDialog.dismiss();
                        Toast.makeText(Login.this, error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id_usuario",String.valueOf(id_usuario));
                params.put("token", token);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(Login.this);
        requestQueue.add(stringRequest);
    }

}//todo:fin de subir token al server

