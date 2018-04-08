package unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVAVirgilio;

import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.ConexionSQLiteHelper;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.R;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVAAlan.ActivityCategorias;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVAAlan.Panel_de_Control;

public class Login extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    public EditText usuario,contrasena;
    public Button button;
    ConexionSQLiteHelper basedatos = new ConexionSQLiteHelper(this,"bdaeo",null,1);
    SQLiteDatabase conexion ;
    String usuarioPermiso,contrasenaPermiso;
    int idRol;

    private int contador=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        usuario = (EditText) findViewById(R.id.usuario_login);
        contrasena = (EditText) findViewById(R.id.contrasena_login);


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
            Intent intent = new Intent(this,AcercaDe.class);
            startActivity(intent);

        }else if (id == R.id.login) {
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public void Formulario_Registrarse_login(View v){   //metodo que habre el formulario para registrarse
        Intent intent = new Intent(this, FormularioRegistroLogin.class);
        usuario.setText("");
        contrasena.setText("");
        startActivity(intent);
    }
    public void Ingresar_Login(View v){ // metodo que verificaque se ingresen datos en los campos usuario y contraseña
        if(usuario.getText().toString().isEmpty() || contrasena.getText().toString().isEmpty()){
            Toast.makeText(getApplicationContext(),"Favor ingresar todos los Campos",Toast.LENGTH_SHORT).show();

        }else{                          //si existe el usuario y la contraseña son correctas el accedera

            try {

               Cursor cursor = ConsultarUsuarioPassword(usuario.getText().toString(),contrasena.getText().toString());

                if(cursor.getCount()> 0 ){
                    if (cursor.moveToFirst()==true){
                        Intent intent = new Intent(getApplicationContext(),Panel_de_Control.class);
                        int id_enviar = (cursor.getInt(0));
                        intent.putExtra("id_usuario_enviado",id_enviar);
                        basedatos.close();
                        usuario.setText("");
                        contrasena.setText("");
                        startActivity(intent);
                        finish();
                    }


                }else {
                    contador = contador+1;
                    if (contador >3){
                        Toast.makeText(getApplicationContext(),"Limite de Intentos Excedido",Toast.LENGTH_LONG).show();
                        finish();
                    }else {
                        Toast.makeText(getApplicationContext(),"Usuario y/o contraseña incorrecto",Toast.LENGTH_SHORT).show();
                        usuario.findFocus();
                    }
                }

            }catch (SQLException e){
                e.printStackTrace();
            }

        }//fin else

    }//fin de boton
    private Cursor ConsultarUsuarioPassword(String usuario, String password) throws SQLException {
       conexion = basedatos.getReadableDatabase();
        Cursor mcursor = null;
        int estado=1;
        mcursor = conexion.query("Usuarios", new String[]{"id_usuario", "nombre_usuario", "nombre_propio", "contrasena", "rol", "estado_usuario"}, "nombre_usuario like'" + usuario + "'and  contrasena like '" + password + "'and  estado_usuario like '" + estado + "'", null, null, null, null);
        return mcursor;
    }
    private void consultaUsuContra(String nombre_usuario, String password_usuario){
        String usuario_resibido,password_resibido;

        usuario_resibido = usuario.getText().toString();
        password_resibido = contrasena.getText().toString();

        Cursor fila = conexion.rawQuery("select nombre_usuario,contrasena,id_usuario,estado_usuario from Usuarios WHERE nombre_usuario= '" + usuario_resibido + "'and contrasena='" + password_resibido + "'", null);

        while (fila.moveToNext()){
            String usua = fila.getString(0);
            String pass = fila.getString(1);
            if (usuario_resibido.equals(usua) && password_resibido.equals(pass)){
                Intent intent = new Intent(this,Panel_de_Control.class);
                usuario.setText("");
                contrasena.setText("");
                startActivity(intent);
            }else{
                Toast.makeText(getApplicationContext(),"usuario o contrase;a erronea",Toast.LENGTH_SHORT).show();
            }
        }

    }

    public void permisoAdmin(){
        SQLiteDatabase permiso = basedatos.getReadableDatabase();
        idRol = 1;
        Cursor cursorP = permiso.rawQuery("SELECT nombre_usuario, contrasena FROM USUARIOS WHERE rol = "+ idRol,null);
        while (cursorP.moveToNext()){
            usuarioPermiso = cursorP.getString(0);
            contrasenaPermiso = cursorP.getString(1);
        }

        if (usuarioPermiso != "Admin"){
            Toast.makeText(getApplicationContext(),"Permiso denegado",Toast.LENGTH_SHORT).show();
        }else{
            Intent p = new Intent(getApplicationContext(),Panel_de_Control.class);
            startActivity(p);
        }
    }


}
