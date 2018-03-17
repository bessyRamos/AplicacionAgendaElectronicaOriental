package unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVAVirgilio;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.ConexionSQLiteHelper;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.R;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVAAlan.Panel_de_Control;

public class Login extends AppCompatActivity {
    public EditText usuario,contrasena;
    ConexionSQLiteHelper basedatos = new ConexionSQLiteHelper(this,"bdaeo",null,1);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        usuario = (EditText) findViewById(R.id.usuario_login);
        contrasena = (EditText) findViewById(R.id.contrasena_login);

    }

    public void Formulario_Registrarse_login(View v){   //metodo que habre el formulario para registrarse
        Intent intent = new Intent(this, FormularioRegistroLogin.class);
        usuario.setText("");
        contrasena.setText("");
        startActivity(intent);
    }
    public void Ingresar_Login(View v){ // metodo que verificaque se ingresen datos en los campos usuario y contraseña
        if(usuario.getText().toString().isEmpty() || contrasena.getText().toString().isEmpty()){
            Toast.makeText(getApplicationContext(),"Usuario o contraseña incorrecta",Toast.LENGTH_SHORT).show();

        }else{                          //si existe el usuario y la contraseña son correctas el accedera

            try {
                Cursor cursor = basedatos.ConsultarUsuarioPassword(usuario.getText().toString(),contrasena.getText().toString());

                if(cursor.getCount()> 0 ){
                    basedatos.close();
                    usuario.setText("");
                    contrasena.setText("");
                    Intent intent = new Intent(this,Panel_de_Control.class);
                    startActivity(intent);
                    finish();

                }else {
                    Toast.makeText(getApplicationContext(),"Usuario y/o contraseña incorrecto",Toast.LENGTH_SHORT).show();
                    usuario.findFocus();
                }

            }catch (android.database.SQLException e){
                e.printStackTrace();

            }

        }

    }//fin de boton


}
