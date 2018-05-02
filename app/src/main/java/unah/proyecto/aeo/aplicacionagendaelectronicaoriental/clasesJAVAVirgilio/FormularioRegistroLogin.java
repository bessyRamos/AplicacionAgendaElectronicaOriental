package unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVAVirgilio;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.util.EntityUtils;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.ConexionSQLiteHelper;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.EntidadesBD.Roles;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.EntidadesBD.Usuarios;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.R;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVAAlan.Editar_Usuarios;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVAAlan.Mostrar_Usuarios;

public class FormularioRegistroLogin extends AppCompatActivity {
    private EditText id,nombrepropio_isertar,nombreusuario_insertar,contrasena_insertar,rol_insertar,estado_del_usuario;
    String nombreusuariobar,nombrepropiobar,contrasenabar,respuesta1bar_usuario,respuesta2bar_usuario,respuesta3bar_usuario;
    int id_rol;
    private Spinner  tipousu;
    private AsyncHttpClient cliente;
    Button bottonvalidar;
    ArrayList lista = new ArrayList<>();
    private EditText respuesta1,respuesta2,respuesta3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario_registro_login);

        //flecha atras
        android.support.v7.app.ActionBar actionBar= getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        //fin de flecha

        bottonvalidar = (Button)findViewById(R.id.registrar_usuario);
        bottonvalidar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //EJECUTAMOS LAS CLASE DE INSERCION DE UN USUARIO.
                new insertarUsuarios().execute();
               // validar();

            }
        });

        tipousu=(Spinner)findViewById(R.id.tipousuario);
        nombrepropio_isertar = (EditText) findViewById(R.id.txtnombre_registro_login);
        nombreusuario_insertar = (EditText) findViewById(R.id.txtnombreUsuario_registro_login);
        contrasena_insertar = (EditText) findViewById(R.id.txtcontrasena_registro_login);
        rol_insertar = (EditText) findViewById(R.id.txtrol_registro_login);
        estado_del_usuario = (EditText) findViewById(R.id.txtestado_registro_login);
        respuesta1 = (EditText) findViewById(R.id.txtrespuesta1);
        respuesta2 = (EditText) findViewById(R.id.txtrespuesta2);
        respuesta3 = (EditText) findViewById(R.id.txtrespuesta3);


//EJECUTAMOS LA CLASE DE LLENADO DE EL SPINNER
     new llenarEspinner().execute();
     tipousu.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
         @Override
         public void onItemSelected(AdapterView<?> adapterView, View view, int posicion, long l) {
             switch (posicion) {
                 case 0:
                     id_rol =1;
                     break;
                 case  1:
                     id_rol = 2;
                     break;

             }
         }

         @Override
         public void onNothingSelected(AdapterView<?> adapterView) {

         }
     });


    }

    // public void registrar_usuario_login (View v){
    private void validar(){
        //id.setError(null);
        nombrepropio_isertar.setError(null);
        nombreusuario_insertar.setError(null);
        contrasena_insertar.setError(null);
        respuesta1.setError(null);
        respuesta2.setError(null);
        respuesta3.setError(null);

//VARIABLES QUE SE USAN EN LA CONEXION DE LA BASE DE DATOS LOCAL.
        // String idd = id.getText().toString();
        String nombusus = nombrepropio_isertar.getText().toString();
        String nomb = nombreusuario_insertar.getText().toString();
        String cont = contrasena_insertar.getText().toString();
        String resp1=respuesta1.getText().toString();
        String resp2=respuesta2.getText().toString();
        String resp3=respuesta3.getText().toString();


//server
//RECIVIMOS LOS USUARIOS QUE VIENEN DEL SERVIDOR Y LOS ADAPTAMOS ALOS COMPONENTES VISIALUES
        nombreusuariobar=nombreusuario_insertar.getText().toString();
        nombrepropiobar=nombrepropio_isertar.getText().toString();
        contrasenabar=contrasena_insertar.getText().toString();


        if(TextUtils.isEmpty(nombusus)){
            nombrepropio_isertar.setError(getString(R.string.error_nombre));
            nombrepropio_isertar.requestFocus();
            return;
        }
        if(TextUtils.isEmpty(nomb)){
            nombreusuario_insertar.setError(getString(R.string.error_nombre_usuario));
            nombreusuario_insertar.requestFocus();
            return;

        }if(TextUtils.isEmpty(cont)){
            contrasena_insertar.setError(getString(R.string.error_contrasena));
            contrasena_insertar.requestFocus();
            return;

        }if(TextUtils.isEmpty(resp1)){
            respuesta1.setError(getString(R.string.error_respuesta1));
            respuesta1.requestFocus();
            return;

        }if(TextUtils.isEmpty(resp2)){
            respuesta2.setError(getString(R.string.error_respuesta2));
            respuesta2.requestFocus();
            return;

        }if(TextUtils.isEmpty(resp3)){
            respuesta3.setError(getString(R.string.error_respuesta3));
            respuesta3.requestFocus();
            return;

        }

        String nom,nombusuario,conta,ro,est;

        nom= nombreusuario_insertar.getText().toString();
        nombusuario=nombrepropio_isertar.getText().toString();
        conta = contrasena_insertar.getText().toString();
        ro = rol_insertar.getText().toString();
        est = estado_del_usuario.getText().toString();
/*
//INSERCION DE USUARIOS DESDE UNA BASE DE  DATOS LOCAL.
        ConexionSQLiteHelper bh = new ConexionSQLiteHelper(FormularioRegistroLogin.this,"bdaeo",null,1);
        if(bh!=null){
            SQLiteDatabase db = bh.getWritableDatabase();
            ContentValues valores = new ContentValues();
            //  valores.put("id_usuario",idd);
            valores.put("nombre_usuario",nom);
            valores.put("nombre_propio",nombusuario);
            valores.put("contrasena",conta);
            valores.put("rol",ro);
            valores.put("estado_usuario",est);
            long insertado = db.insert("USUARIOS",null,valores);
            db.close();
            if(insertado>0 ){

                Toast.makeText(FormularioRegistroLogin.this,"Agregado con exito",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this,Login.class);
                nombrepropio_isertar.setText("");
                nombreusuario_insertar.setText("");
                contrasena_insertar.setText("");
                startActivity(intent);
                finish();

            }else {
                Toast.makeText(FormularioRegistroLogin.this,"Nos se Agrego",Toast.LENGTH_SHORT).show();

            }
        }
*/
    }

    //METODO PARA LLENAR EL ESPINNER DESDE EL WEB SERVER
    private class llenarEspinner extends AsyncTask<String, Integer, Boolean> {
        private llenarEspinner(){}
        boolean resul = true;

        @Override
        protected Boolean doInBackground(String... strings) {

            try {
                JSONArray respJSON = new JSONArray(EntityUtils.toString(new DefaultHttpClient().execute(new HttpPost("http://aeo.web-hn.com/consultar_los_roles.php")).getEntity()));

                for (int i = 0; i < respJSON.length(); i++) {
                    String roles ;
                    roles=respJSON.getJSONObject(i).getString("descripcion_rol");

                    lista.add(roles);
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
                ArrayAdapter a = new ArrayAdapter<>(getApplicationContext(),R.layout.adaptacion_spinner,lista);
                tipousu.setAdapter(a);

            }else {
                Toast.makeText(getApplicationContext(), "Problemas de conexión", Toast.LENGTH_SHORT).show();
            }
        }


    }
    //METODO PARA INSERTAR USUARIOS DIRECTAMENTE DESDE EL SERVER.
    private class insertarUsuarios extends AsyncTask<String, Integer, Boolean> {
        private insertarUsuarios(){}

        boolean resul = true;

        @Override
        protected Boolean doInBackground(String... strings) {

            try {
                nombreusuariobar=nombreusuario_insertar.getText().toString();
                nombrepropiobar=nombrepropio_isertar.getText().toString().replace(" ","%20");
                contrasenabar=contrasena_insertar.getText().toString();
                respuesta1bar_usuario = respuesta1.getText().toString();
                respuesta2bar_usuario = respuesta2.getText().toString();
                respuesta3bar_usuario = respuesta3.getText().toString();

                EntityUtils.toString(new DefaultHttpClient().execute(new HttpPost("http://aeo.web-hn.com/insertarUsuarioRespDeSeguridad.php?nombre_usuario="+nombreusuariobar+"&nombre_propio="+nombrepropiobar+"&contrasena="+contrasenabar+"&rol="+id_rol+"&respuesta_uno="+respuesta1bar_usuario+"&respuesta_dos="+respuesta2bar_usuario+"&respuesta_tres="+respuesta3bar_usuario)).getEntity());

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
                //VALIDACION DE QUE LOS CAMPOS NO ESTEN VACIOS ANTES DE INGRESAR UN USUARIO
                if (nombreusuario_insertar.getError()==null && nombrepropio_isertar.getError()==null && contrasena_insertar.getError()==null){

                    Toast.makeText(getApplicationContext(),"Usuario agregado Correctamente",Toast.LENGTH_SHORT).show();
                    finish();

                }

            }else {
                Toast.makeText(getApplicationContext(), "Problemas de conexión", Toast.LENGTH_SHORT).show();
            }
        }


    }

}






