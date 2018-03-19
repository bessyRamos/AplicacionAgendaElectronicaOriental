package unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVAAlan;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
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

/**
 * Created by alan fabricio on 15/03/2018.
 */

public class Editar_Usuarios extends AppCompatActivity {

    private int usuarioEditar;
    private EditText nombreusuario,nombrepropio,contraseña;
    Button bottonvalidar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editar_usuarios);
        bottonvalidar = (Button)findViewById(R.id.editar);
        bottonvalidar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validar();
                if (nombreusuario.getError()==null && nombrepropio.getError()==null && contraseña.getError()==null){
                     Intent intent = new Intent(Editar_Usuarios.this,Mostrar_Usuarios.class);
                    startActivity(intent);
                    finish();
                }




            }
        });

//manda la informacion de las clases pra ser editadas
        Bundle extras = this.getIntent().getExtras();
        if(extras!=null) {
            usuarioEditar = extras.getInt("id");

        }

        nombreusuario = (EditText)findViewById(R.id.EditUsuario);
        nombrepropio = (EditText)findViewById(R.id.EditNombre);
        contraseña= (EditText)findViewById(R.id.Editcontrasena);

        reflejarCampos();
    }
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



    private void validar(){
        //id.setError(null);
        nombreusuario.setError(null);
        nombrepropio.setError(null);
        contraseña.setError(null);


        // String idd = id.getText().toString();
        String nombusus = nombreusuario.getText().toString();
        String nomb = nombrepropio.getText().toString();
        String cont = contraseña.getText().toString();


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
            contraseña.setError(getString(R.string.error_contrasena));
            contraseña.requestFocus();
            return;

        }

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
    }





}


