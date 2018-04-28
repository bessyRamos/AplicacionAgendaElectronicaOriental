package unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVASheyli;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

import com.bumptech.glide.Glide;

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
import de.hdodenhof.circleimageview.CircleImageView;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.ConexionSQLiteHelper;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.R;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVAAlan.ActivityCategorias;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVAAlan.Fuente_Categoria;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVAAlan.Panel_de_Control;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVABessy.Mapa;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVAVirgilio.AcercaDe;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVAVirgilio.Login;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVAVirgilio.PanelDeControlUsuarios;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVAVirgilio.Sesion;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVAVirgilio.SesionUsuario;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.provider.AEODbHelper;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.provider.PerfilesContract;

public class PerfilDeLaOrganizacion extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private ImageView organizacion;
    private TextView nombre, direccion, telefono, email, descripcion, movil;
    AEODbHelper conn;
    int id_organizacion;
    String nombreOrganizacion;
    double x, y;
    FloatingActionButton ubicacion;
    String organizacionP, nombreP, direccionP, telefonoP, emailP, descripcionP, movilP;
    boolean imagenContacto;


    //preferencias
    private Sesion sesion;
    private SesionUsuario sesionUsuario;
    int id_usu=-1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_de_la_organizacion);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        //envio de clase actual para las preferencias
        sesion = new Sesion(this);
        sesionUsuario = new SesionUsuario(this);
        SharedPreferences preferences = getSharedPreferences("credencial", Context.MODE_PRIVATE);
        id_usu  = preferences.getInt("usuario_ingreso",id_usu);
        //

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        //recuperacion de variables
        organizacion =  findViewById(R.id.imagenOrganizacion);
        nombre = (TextView) findViewById(R.id.n);
        direccion = (TextView) findViewById(R.id.d);
        telefono = (TextView) findViewById(R.id.t);
        movil = (TextView) findViewById(R.id.tcelular);
        email = (TextView) findViewById(R.id.e);
        descripcion = (TextView) findViewById(R.id.descripcion);
        ubicacion = (FloatingActionButton) findViewById(R.id.fab);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            id_organizacion = extras.getInt("id_organizacion");
            nombreOrganizacion = extras.getString("nombre_organizacion");
            toolbar.setTitle(nombreOrganizacion);
        }
        setSupportActionBar(toolbar);
        conn = new AEODbHelper(this);

        String[] projection = {
                PerfilesContract.ContactosEntry.COLUMN_IMAGEN_PATH,
                PerfilesContract.ContactosEntry.COLUMN_NOMBRE,
                PerfilesContract.ContactosEntry.COLUMN_NUMERO_TELEFONO,
                PerfilesContract.ContactosEntry.COLUMN_NUMERO_CELULAR,
                PerfilesContract.ContactosEntry.COLUMN_E_MAIL,
                PerfilesContract.ContactosEntry.COLUMN_DIRECCION,
                PerfilesContract.ContactosEntry.COLUMN_DESCRIPCION,
                PerfilesContract.ContactosEntry.COLUMN_LATITUD,
                PerfilesContract.ContactosEntry.COLUMN_LONGITUD
        };

        SQLiteDatabase db = conn.getReadableDatabase();
        Cursor cursor = db.query(PerfilesContract.ContactosEntry.TABLE_NAME,
                projection,
                PerfilesContract.ContactosEntry.COLUMN_PERFILID + " = ?",
                new String[]{String.valueOf(id_organizacion)},
                null,
                null,
                null);


        while (cursor.moveToNext()) {
            if (!cursor.getString(cursor.getColumnIndex(PerfilesContract.ContactosEntry.COLUMN_IMAGEN_PATH)).isEmpty()) {
                Glide.with(this).
                        load(cursor.getString(cursor.getColumnIndex(PerfilesContract.ContactosEntry.COLUMN_IMAGEN_PATH))).
                        override(220,180).
                        into(organizacion);
            } else {
                Glide.with(this).
                        load(R.drawable.iconocontactowhite).
                        override(220,180).
                        into(organizacion);
            }

            nombre.setText(cursor.getString(cursor.getColumnIndex(PerfilesContract.ContactosEntry.COLUMN_NOMBRE)));
            if (cursor.getString(cursor.getColumnIndex(PerfilesContract.ContactosEntry.COLUMN_NUMERO_TELEFONO)).isEmpty()) {
                telefono.setText("No disponible");
            } else {
                telefono.setText(cursor.getString(cursor.getColumnIndex(PerfilesContract.ContactosEntry.COLUMN_NUMERO_TELEFONO)));
            }

            if (cursor.getString(cursor.getColumnIndex(PerfilesContract.ContactosEntry.COLUMN_NUMERO_CELULAR)).isEmpty()) {
                movil.setText("No disponible");
            } else {
                movil.setText(cursor.getString(cursor.getColumnIndex(PerfilesContract.ContactosEntry.COLUMN_NUMERO_CELULAR)));
            }

            if (cursor.getString(cursor.getColumnIndex(PerfilesContract.ContactosEntry.COLUMN_E_MAIL)).isEmpty()) {
                email.setText("No disponible");
            } else {
                email.setText(cursor.getString(cursor.getColumnIndex(PerfilesContract.ContactosEntry.COLUMN_E_MAIL)));
            }

            if (cursor.getString(cursor.getColumnIndex(PerfilesContract.ContactosEntry.COLUMN_DIRECCION)).isEmpty()) {
                direccion.setText("Ninguna");
            } else {
                direccion.setText(cursor.getString(cursor.getColumnIndex(PerfilesContract.ContactosEntry.COLUMN_DIRECCION)));
            }

            if (cursor.getString(cursor.getColumnIndex(PerfilesContract.ContactosEntry.COLUMN_DESCRIPCION)).isEmpty()) {
                descripcion.setText("No disponible");
            } else {
                descripcion.setText(cursor.getString(cursor.getColumnIndex(PerfilesContract.ContactosEntry.COLUMN_DESCRIPCION)));
            }

            x = Double.valueOf(cursor.getString(cursor.getColumnIndex(PerfilesContract.ContactosEntry.COLUMN_LATITUD)));
            y = Double.valueOf(cursor.getString(cursor.getColumnIndex(PerfilesContract.ContactosEntry.COLUMN_LONGITUD)));

        }
        ubicacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ubicacion1 = new Intent(getApplicationContext(), Mapa.class);
                ubicacion1.putExtra("latitud", x);
                ubicacion1.putExtra("longitud", y);
                ubicacion1.putExtra("nombre", nombre.getText().toString());
                startActivity(ubicacion1);
            }
        });
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
            Intent intent = new Intent(this, AcercaDe.class);
            startActivity(intent);

        } else if (id == R.id.login) {
            if (sesion.logindim()){
                Intent intent = new Intent(PerfilDeLaOrganizacion.this,Panel_de_Control.class);
                intent.putExtra("usuario_ingreso",id_usu);
                //startActivity(new Intent(ActivityCategorias.this,Panel_de_Control.class));
                startActivity(intent);

            }else{
                if (sesionUsuario.logindimUsuario()){
                    Intent intent = new Intent(PerfilDeLaOrganizacion.this,PanelDeControlUsuarios.class);
                    intent.putExtra("id",id_usu);
                    //startActivity(new Intent(ActivityCategorias.this,PanelDeControlUsuarios.class));
                    startActivity(intent);


                }else {
                    Intent intent = new Intent(this, Login.class);
                    startActivity(intent);

                }

            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}

