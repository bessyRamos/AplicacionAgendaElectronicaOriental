package unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVASheyli;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.entity.BufferedHttpEntity;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.util.EntityUtils;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.ConexionSQLiteHelper;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.R;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVAAlan.ActivityCategorias;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVAAlan.Fuente_Categoria;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVABessy.Mapa;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVAVirgilio.AcercaDe;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVAVirgilio.Login;

public class PerfilDeLaOrganizacion extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private ImageView organizacion;
    private TextView nombre,direccion,telefono,email,descripcion,movil;
    ConexionSQLiteHelper conn;
    int id_organizacion;
    double x,y;
    FloatingActionButton ubicacion;
    String organizacionP,nombreP,direccionP,telefonoP,emailP,descripcionP,movilP;
    boolean imagenContacto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_de_la_organizacion);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        new PerfilesEnBaseDeDatosWeb().execute();
        //new ObtenerUbicacionEnBaseDeDatosWeb().execute();

        //recuperacion de variables
        organizacion = (ImageView) findViewById(R.id.imagenOrganizacion);
        nombre = (TextView) findViewById(R.id.n);
        direccion = (TextView) findViewById(R.id.d);
        telefono = (TextView) findViewById(R.id.t);
        movil = (TextView) findViewById(R.id.tcelular);
        email = (TextView) findViewById(R.id.e);
        descripcion = (TextView) findViewById(R.id.descripcion);
        ubicacion = (FloatingActionButton) findViewById(R.id.fab);

       Bundle extras = getIntent().getExtras();
        if (extras!=null){
            id_organizacion = extras.getInt("id_organizacion");
        }
        conn = new ConexionSQLiteHelper(this,"bdaeo",null,1);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

  /*  @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.perfil_de_la_organizacion, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    */

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.principaldos) {
            // Handle the camera action
            startActivity(new Intent(getBaseContext(), ActivityCategorias.class)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP));
            finish();
        } else if (id == R.id.acercadeinfodos) {
            Intent intent = new Intent(this,AcercaDe.class);
            startActivity(intent);

        }else if (id == R.id.login) {
            Intent intent = new Intent(this, Login.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }//fin de boolean

    //obtener datos de perfiles de organizacion mediante la base de datos en la web
    private class PerfilesEnBaseDeDatosWeb extends AsyncTask<String, Integer, Boolean> {
        private PerfilesEnBaseDeDatosWeb(){}
        @Override
        protected Boolean doInBackground(String... strings) {
            boolean resul = true;

            try{
                // Parseamos la respuesta obtenida del servidor a un objeto JSON
                JSONObject jsonObject = new JSONObject(EntityUtils.toString(new DefaultHttpClient().execute(new HttpPost("https://shessag.000webhostapp.com/mostrar_perfil.php?id_contacto="+id_organizacion)).getEntity()));
                JSONArray jsonArray = jsonObject.getJSONArray("perfilOrganizacion");
                for(int i = 0; i < jsonArray.length(); i++) {

                    if (jsonArray.getJSONObject(i).getString("imagen").isEmpty()){
                        imagenContacto = false;
                    }else{
                        organizacionP = jsonArray.getJSONObject(i).getString("imagen");
                        imagenContacto=true;
                    }
                    nombreP = jsonArray.getJSONObject(i).getString("nombre_organizacion");
                    telefonoP = jsonArray.getJSONObject(i).getString("numero_fijo");
                    movilP = jsonArray.getJSONObject(i).getString("numero_movil");
                    emailP= jsonArray.getJSONObject(i).getString("e_mail");
                    direccionP = jsonArray.getJSONObject(i).getString("direccion");
                    descripcionP = jsonArray.getJSONObject(i).getString("descripcion_organizacion");
                    x = jsonArray.getJSONObject(i).getDouble("latitud");
                    y = jsonArray.getJSONObject(i).getDouble("longitud");

                    resul  = true;
                }
            }catch (Exception ex){
                ex.printStackTrace();
                resul = false;
            }

            return resul;
        }
        protected void onPostExecute(Boolean result) {
            if (result)
            {

                if (imagenContacto==true){
                  byte[]byteCode = Base64.decode(organizacionP,Base64.DEFAULT);
                  organizacion.setImageBitmap(BitmapFactory.decodeByteArray(byteCode,0,byteCode.length));
                }else{
                    organizacion.setImageResource(R.drawable.iconocontactowhite);
                }

                if(nombreP.isEmpty()){
                   nombre.setText("No disponible");
                }else{
                    nombre.setText(nombreP);
                }
                if(telefonoP.isEmpty()){
                    telefono.setText("No disponible");
                }else{
                    telefono.setText(telefonoP);
                }
                if(movilP.isEmpty()){
                    movil.setText("No disponible");
                }else{
                    movil.setText(movilP);
                }
                if(emailP.isEmpty()){
                    email.setText("No disponible");
                }else{
                    email.setText(emailP);
                }
                if(direccionP.isEmpty()){
                    direccion.setText("No disponible");
                }else{
                    direccion.setText(direccionP);
                }
                if(descripcionP.isEmpty()){
                    descripcion.setText("No disponible");
                }else{
                    descripcion.setText(descripcionP);
                }

               ubicacion.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                Intent ubicacion1 = new Intent(getApplicationContext(),Mapa.class);
                ubicacion1.putExtra("latitud",x);
                ubicacion1.putExtra("longitud",y);
                ubicacion1.putExtra("nombre",nombre.getText().toString());
                startActivity(ubicacion1);}
                });

            }else {
               Toast.makeText(getApplicationContext(), "Problemas de conexión \n Mostrando datos de base de datos local", Toast.LENGTH_SHORT).show();

                //llenado desde la base de datos local
                SQLiteDatabase db = conn.getReadableDatabase();
                Cursor cursor = db.rawQuery("SELECT imagen, nombre_organizacion,numero_fijo,numero_movil,e_mail,direccion,descripcion_organizacion FROM CONTACTOS WHERE id_contacto = "+id_organizacion,null );
                while(cursor.moveToNext())
                {
                    organizacion.setImageResource(cursor.getInt(0));
                    nombre.setText(cursor.getString(1));
                    if(cursor.getString(2).isEmpty()) {
                        telefono.setText("(No disponible)");
                    }else{
                        telefono.setText(cursor.getString(2));
                    }

                    if(cursor.getString(3).isEmpty()) {
                        movil.setText("(No disponible)");
                    }else{
                        movil.setText(cursor.getString(3));
                    }

                    if(cursor.getString(4).isEmpty()) {
                        email.setText("(No disponible)");
                    }else{
                        email.setText(cursor.getString(4));
                    }

                    if(cursor.getString(5).isEmpty()) {
                        direccion.setText("(Ninguna)");
                    }else{
                        direccion.setText(cursor.getString(5));
                    }

                    if(cursor.getString(6).isEmpty()) {
                        descripcion.setText("(No disponible)");
                    }else{
                        descripcion.setText(cursor.getString(6));
                    }
                }//fin de while

                //llenado desde la base de datos local
              ubicacion.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        SQLiteDatabase db = conn.getReadableDatabase();
                        Cursor cursor1 = db.rawQuery("SELECT latitud, longitud FROM CONTACTOS WHERE id_contacto = "+id_organizacion,null);
                        while(cursor1.moveToNext()){
                            x = cursor1.getDouble(0);
                            y = cursor1.getDouble(1);
                        }
                        Intent ubicacion1 = new Intent(getApplicationContext(),Mapa.class);
                        ubicacion1.putExtra("latitud",x);
                        ubicacion1.putExtra("longitud",y);
                        ubicacion1.putExtra("nombre",nombre.getText().toString());
                        startActivity(ubicacion1);
                    }
                });
            }//fin de else fallo conexion, buscar sqlite
        }//fin de onPostExecute
    }//fin de PerfilesEnBaseDeDatosWeb
}