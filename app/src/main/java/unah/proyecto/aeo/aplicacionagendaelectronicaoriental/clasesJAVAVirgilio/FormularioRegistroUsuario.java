package unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVAVirgilio;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.util.EntityUtils;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.R;

public class FormularioRegistroUsuario extends AppCompatActivity {
    private EditText nombrepropio_isertar_usuario,nombreusuario_insertar_usuario,contrasena_insertar_usuario;
    String nombreusuariobar_usuario,nombrepropiobar_usuario,contrasenabar_usuario,respuesta1bar_usuario,respuesta2bar_usuario,respuesta3bar_usuario;
    int rol_insertar_usuario,estado_insertar_usuario;
    private Button insertar_usuario;
    private EditText respuesta1,respuesta2,respuesta3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario_registro_usuario);

        nombreusuario_insertar_usuario = (EditText) findViewById(R.id.txtnombreUsuario_registro_login_usuario);
        nombrepropio_isertar_usuario = (EditText) findViewById(R.id.txtnombre_registro_login_usuario);
        contrasena_insertar_usuario = (EditText) findViewById(R.id.txtcontrasena_registro_login_usuario);
        rol_insertar_usuario=2;
        respuesta1 = (EditText) findViewById(R.id.txtrespuesta1);
        respuesta2 = (EditText) findViewById(R.id.txtrespuesta2);
        respuesta3 = (EditText) findViewById(R.id.txtrespuesta3);

        insertar_usuario = (Button) findViewById(R.id.registrar_usuario_usuario);
        insertar_usuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //EJECUTAMOS LAS CLASE DE INSERCION DE UN USUARIO.
                new insertarUsuarios().execute();

            }
        });
    }

    // public void registrar_usuario_login (View v){
    private void validar(){
        //id.setError(null);
        nombrepropio_isertar_usuario.setError(null);
        nombreusuario_insertar_usuario.setError(null);
        contrasena_insertar_usuario.setError(null);
        respuesta1.setError(null);
        respuesta2.setError(null);
        respuesta3.setError(null);

//VARIABLES QUE SE USAN EN LA CONEXION DE LA BASE DE DATOS LOCAL.
        // String idd = id.getText().toString();
        String nombusus = nombrepropio_isertar_usuario.getText().toString();
        String nomb = nombreusuario_insertar_usuario.getText().toString();
        String cont = contrasena_insertar_usuario.getText().toString();
        String resp1=respuesta1.getText().toString();
        String resp2=respuesta2.getText().toString();
        String resp3=respuesta3.getText().toString();


        if(TextUtils.isEmpty(nombusus)){
            nombrepropio_isertar_usuario.setError(getString(R.string.error_nombre));
            nombrepropio_isertar_usuario.requestFocus();
            return;
        }
        if(TextUtils.isEmpty(nomb)){
            nombreusuario_insertar_usuario.setError(getString(R.string.error_nombre_usuario));
            nombreusuario_insertar_usuario.requestFocus();
            return;

        }if(TextUtils.isEmpty(cont)){
            contrasena_insertar_usuario.setError(getString(R.string.error_contrasena));
            contrasena_insertar_usuario.requestFocus();
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

        nom= nombreusuario_insertar_usuario.getText().toString();
        nombusuario=nombrepropio_isertar_usuario.getText().toString();
        conta = contrasena_insertar_usuario.getText().toString();


    }



    //METODO PARA INSERTAR USUARIOS DIRECTAMENTE DESDE EL SERVER.
    private class insertarUsuarios extends AsyncTask<String, Integer, Boolean> {
        private insertarUsuarios(){}

        boolean resul = true;

        @Override
        protected Boolean doInBackground(String... strings) {

            try {
                nombreusuariobar_usuario=nombreusuario_insertar_usuario.getText().toString();
                nombrepropiobar_usuario=nombrepropio_isertar_usuario.getText().toString().replace(" ","%20");
                contrasenabar_usuario=contrasena_insertar_usuario.getText().toString();
                respuesta1bar_usuario = respuesta1.getText().toString();
                respuesta2bar_usuario = respuesta2.getText().toString();
                respuesta3bar_usuario = respuesta3.getText().toString();

                EntityUtils.toString(new DefaultHttpClient().execute(new HttpPost("http://aeo.web-hn.com/insertarUsuarioRespDeSeguridad.php?nombre_usuario="+nombreusuariobar_usuario+"&nombre_propio="+nombrepropiobar_usuario+"&contrasena="+contrasenabar_usuario+"&rol="+rol_insertar_usuario+"&respuesta_uno="+respuesta1bar_usuario+"&respuesta_dos="+respuesta2bar_usuario+"&respuesta_tres="+respuesta3bar_usuario)).getEntity());

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
                if (nombreusuario_insertar_usuario.getError()==null && nombrepropio_isertar_usuario.getError()==null && contrasena_insertar_usuario.getError()==null && respuesta1.getError()==null && respuesta2.getError()==null && respuesta3.getError()==null){

                    Toast.makeText(getApplicationContext(),"Usuario agregado Correctamente",Toast.LENGTH_SHORT).show();
                    finish();

                }

            }else {
                Toast.makeText(getApplicationContext(), "Problemas de conexión", Toast.LENGTH_SHORT).show();
            }
        }


    }
}
