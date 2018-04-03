package unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVAVirgilio;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.ConexionSQLiteHelper;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.R;

public class FormularioRegistroLogin extends AppCompatActivity {
    private EditText id,nombre,nombre_usuario,contrasena,rol,estado_del_usuario;
    Button bottonvalidar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario_registro_login);

        bottonvalidar = (Button)findViewById(R.id.registrar_usuario);
        bottonvalidar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validar();

            }
        });


        nombre = (EditText) findViewById(R.id.txtnombre_registro_login);
        nombre_usuario = (EditText) findViewById(R.id.txtnombreUsuario_registro_login);
        contrasena = (EditText) findViewById(R.id.txtcontrasena_registro_login);
        rol = (EditText) findViewById(R.id.txtrol_registro_login);
        estado_del_usuario = (EditText) findViewById(R.id.txtestado_registro_login);
        rol.setText("1");
        estado_del_usuario.setText("1");


    }

    // public void registrar_usuario_login (View v){
    private void validar(){
        //id.setError(null);
        nombre.setError(null);
        nombre_usuario.setError(null);
        contrasena.setError(null);


        // String idd = id.getText().toString();
        String nombusus = nombre.getText().toString();
        String nomb = nombre_usuario.getText().toString();
        String cont = contrasena.getText().toString();


        if(TextUtils.isEmpty(nombusus)){
            nombre.setError(getString(R.string.error_nombre));
            nombre.requestFocus();
            return;
        }
        if(TextUtils.isEmpty(nomb)){
            nombre_usuario.setError(getString(R.string.error_nombre_usuario));
            nombre_usuario.requestFocus();
            return;

        }if(TextUtils.isEmpty(cont)){
            contrasena.setError(getString(R.string.error_contrasena));
            contrasena.requestFocus();
            return;

        }

        String nom,nombusuario,conta,ro,est;

        nom= nombre_usuario.getText().toString();
        nombusuario=nombre.getText().toString();
        conta = contrasena.getText().toString();
        ro = rol.getText().toString();
        est = estado_del_usuario.getText().toString();

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
                nombre.setText("");
                nombre_usuario.setText("");
                contrasena.setText("");
                startActivity(intent);
                finish();

            }else {
                Toast.makeText(FormularioRegistroLogin.this,"Nos se Agrego",Toast.LENGTH_SHORT).show();

            }
        }

    }

}


