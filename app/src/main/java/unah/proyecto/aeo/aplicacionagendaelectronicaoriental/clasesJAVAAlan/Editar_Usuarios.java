package unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVAAlan;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.entity.UrlEncodedFormEntity;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.message.BasicNameValuePair;
import cz.msebera.android.httpclient.util.EntityUtils;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.R;


/**
 * Created by alan fabricio on 15/03/2018.
 */


public class Editar_Usuarios extends AppCompatActivity {
    SharedPreferences datos;
    String  traidotk;

    private int usuarioEditar;
    private EditText nombreusuario,nombrepropio,correo;
    String nombreusuariobar,nombrepropiobar,correobar;
    Button bottonvalidar;
    String nombre_usuario,nombre_propio,correoo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editar_usuarios);

        bottonvalidar = (Button)findViewById(R.id.editar);
//
        datos= getSharedPreferences("datos", Context.MODE_PRIVATE);
        traidotk = datos.getString("usuariotraidotkn","no tkn");
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
        correo= (EditText)findViewById(R.id.Editcorreo);


        // reflejarCampos();
        //SE OPTIENEN LOS DATOS DEL SERVER Y SE PASAN A VARIABLES
        new llenarlosEditTextdelServer().execute();
        nombreusuariobar=nombreusuario.getText().toString();
        nombrepropiobar=nombrepropio.getText().toString();
        correobar=correo.getText().toString();

    }
    /*
    public void reflejarCampos(){
        ConexionSQLiteHelper bh = new ConexionSQLiteHelper(Editar_Usuarios.this,"bdaeo",null,1);
        if(bh!=null){
            SQLiteDatabase db = bh.getReadableDatabase();
            Cursor c = db.rawQuery("SELECT * FROM Usuarios WHERE id_usuario="+usuarioEditar,null);

            try {
                if(c.moveToNext()){

                    nombreusuario.setText(c.getString(1));
                    nombrepropio.setText(c.getString(2));
                    contraseña.setText(c.getString(3));
                }

            }finally {

            }
        }

    }
*/


    private void validar(){

        //id.setError(null);
        nombreusuario.setError(null);
        nombrepropio.setError(null);
        correo.setError(null);


        // String idd = id.getText().toString();
        String nombusus = nombreusuario.getText().toString();
        String nomb = nombrepropio.getText().toString();
       String cor = correo.getText().toString();




        if(TextUtils.isEmpty(nombusus) || nombusus.startsWith(" ")){
            nombreusuario.setError(getString(R.string.error_nombre_usuario));
            nombreusuario.requestFocus();
            return;
        }
        if(TextUtils.isEmpty(nomb) || nomb.startsWith(" ")){
            nombrepropio.setError(getString(R.string.error_nombre));
            nombrepropio.requestFocus();
            return;

        }if(TextUtils.isEmpty(cor) || cor.startsWith(" ")){
        }else{
            if(!correo.getText().toString().contains("@") && !correo.getText().toString().contains(".")){
                correo.setError(getString(R.string.error_contrasena));
                correo.requestFocus();
                return;
            }

        }
    }
    //ACTUALIZACION DE UN USUARIO DESDE EL WEB SERVER
    private class actualizarUsuarios extends AsyncTask<String, Integer, Boolean> {
        private actualizarUsuarios(){}
        boolean resul = true;

        @Override
        protected Boolean doInBackground(String... strings) {

            try {
                HttpClient httpclient;
                HttpPost httppost;
                ArrayList<NameValuePair> parametros;
                httpclient = new DefaultHttpClient();
                httppost = new HttpPost("http://aeo.web-hn.com/WebServices/actualizacion_de_un_usuario.php");
                parametros = new ArrayList<NameValuePair>();
                parametros.add(new BasicNameValuePair("usuario",String.valueOf(usuarioEditar)));
                parametros.add(new BasicNameValuePair("usuarionombre",nombreusuario.getText().toString()));
                parametros.add(new BasicNameValuePair("usuariopropio",nombrepropio.getText().toString()));
                parametros.add(new BasicNameValuePair("usuarioemail",correo.getText().toString()));
                parametros.add(new BasicNameValuePair("tkn",traidotk));


                httppost.setEntity(new UrlEncodedFormEntity(parametros, "UTF-8"));

                httpclient.execute(httppost);



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
                if (nombreusuario.getError()==null && nombrepropio.getError()==null && correo.getError()==null){
                    Toast.makeText(getApplicationContext(),"Usuario Realizado Correctamente",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent();
                    setResult(Mostrar_Usuarios.RESULT_OK,intent);
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
                HttpClient httpclient;
                HttpPost httppost;
                ArrayList<NameValuePair> parametros;
                httpclient = new DefaultHttpClient();
                httppost = new HttpPost("http://aeo.web-hn.com/WebServices/Mostar_Los_Usuarios_Editados.php");
                parametros = new ArrayList<NameValuePair>();
                parametros.add(new BasicNameValuePair("usuario",String.valueOf(usuarioEditar) ));
                parametros.add(new BasicNameValuePair("tkn",traidotk));

                httppost.setEntity(new UrlEncodedFormEntity(parametros, "UTF-8"));
                JSONArray respJSON = new JSONArray(EntityUtils.toString(( httpclient.execute(httppost)).getEntity()));

                //JSONArray respJSON = new JSONArray(EntityUtils.toString(new DefaultHttpClient().execute(new HttpPost("http://aeo.web-hn.com/WebServices/Mostar_Los_Usuarios_Editados.php?usuario="+usuarioEditar)).getEntity()));
                for (int i = 0; i < respJSON.length(); i++) {
                    nombre_usuario = respJSON.getJSONObject(i).getString("nombre_usuario");
                    nombre_propio = respJSON.getJSONObject(i).getString("nombre_propio");
                    correoo = respJSON.getJSONObject(i).getString("correo");
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
                correo.setText(correoo);
            }else {
                Toast.makeText(getApplicationContext(), "Problemas de conexión", Toast.LENGTH_SHORT).show();
            }
        }


    }

}

