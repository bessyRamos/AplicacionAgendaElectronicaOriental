package unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVAAlan;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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

import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.util.EntityUtils;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.ConexionSQLiteHelper;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.R;


/**
 * Created by alan fabricio on 15/03/2018.
 */

public class Editar_Usuarios extends AppCompatActivity {

    private int usuarioEditar;
    private EditText nombreusuario,nombrepropio,contrasena;
    String nombreusuariobar,nombrepropiobar,contrasenabar;
    Button bottonvalidar;
    String nombre_usuario,nombre_propio,contra;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editar_usuarios);

        bottonvalidar = (Button)findViewById(R.id.editar);

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


/*
        ConexionSQLiteHelper bh = new ConexionSQLiteHelper(Editar_Usuarios.this,"bdaeo",null,1);
        if (bh!=null){
            SQLiteDatabase db = bh.getWritableDatabase();
            ContentValues values = new ContentValues();

            // values.put("cod_clases",Integer.parseInt(codigo.getText().toString()));
            values.put("nombre_usuario",nombreusuario.getText().toString());
            values.put("nombre_propio",nombrepropio.getText().toString());
            values.put("contrasena",contraseña.getText().toString());
            long respuesta = db.update("USUARIOS",values,"id_usuario="+usuarioEditar,null);
            if(respuesta >0){
                Toast.makeText(Editar_Usuarios.this,"Editado con exito",Toast.LENGTH_LONG).show();
                nombreusuario.setText("");
                nombrepropio.setText("");
                contraseña.setText("");

            }else  {
                Toast.makeText(Editar_Usuarios.this,"Ocurrio un error",Toast.LENGTH_LONG).show();
            }

        }
        */
    }



    //ACTUALIZACION DE UN USUARIO DESDE EL WEB SERVER
    private class actualizarUsuarios extends AsyncTask<String, Integer, Boolean> {
        private actualizarUsuarios(){}
        boolean resul = true;

        @Override
        protected Boolean doInBackground(String... strings) {

            try {
                nombreusuariobar=nombreusuario.getText().toString();
                nombrepropiobar=nombrepropio.getText().toString();
                contrasenabar=contrasena.getText().toString();

                EntityUtils.toString(new DefaultHttpClient().execute(new HttpPost("https://shessag.000webhostapp.com/actualizacion_de_un_usuario.php?id_usuario="+usuarioEditar+"&nombre_usuario="+nombreusuariobar+"&nombre_propio="+nombrepropiobar+"&contrasena="+contrasenabar)).getEntity());

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
                    Intent intent = new Intent(Editar_Usuarios.this,Mostrar_Usuarios.class);
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
                JSONArray respJSON = new JSONArray(EntityUtils.toString(new DefaultHttpClient().execute(new HttpPost("https://shessag.000webhostapp.com/Mostar_Los_Usuarios_Editados.php?id_usuario="+usuarioEditar)).getEntity()));
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


