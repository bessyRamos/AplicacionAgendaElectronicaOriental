package unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVASheyli;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.ConexionSQLiteHelper;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.R;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVAAlan.ActivityCategorias;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVABessy.Mapa;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVAVirgilio.AcercaDe;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVAVirgilio.Login;

public class PerfilDeLaOrganizacion extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private ImageView organizacion;
    private TextView nombre,direccion,telefono,email,descripcion,movil;
    ConexionSQLiteHelper conn;
    int id_organizacion;
    double x,y;

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

        //recuperacion de variables
        organizacion = (ImageView) findViewById(R.id.imagenOrganizacion);
        nombre = (TextView) findViewById(R.id.n);
        direccion = (TextView) findViewById(R.id.d);
        telefono = (TextView) findViewById(R.id.t);
        movil = (TextView) findViewById(R.id.tcelular);
        email = (TextView) findViewById(R.id.e);
        descripcion = (TextView) findViewById(R.id.descripcion);

        Bundle extras = getIntent().getExtras();
        if (extras!=null){
            id_organizacion = extras.getInt("id_organizacion");
        }
        conn = new ConexionSQLiteHelper(this,"bdaeo",null,1);

       FloatingActionButton ubicacion = (FloatingActionButton) findViewById(R.id.fab);
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

        //llenado desde la base de datos
        SQLiteDatabase db = conn.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT imagen, nombre_organizacion,numero_fijo,numero_movil,e_mail,direccion,descripcion_organizacion FROM CONTACTOS WHERE id_contacto = "+id_organizacion,null );
        while(cursor.moveToNext())
        {
            organizacion.setImageResource(cursor.getInt(0));
            nombre.setText(cursor.getString(1));
            telefono.setText(cursor.getString(2));
            movil.setText(cursor.getString(3));
            email.setText(cursor.getString(4));
            direccion.setText(cursor.getString(5));
            descripcion.setText(cursor.getString(6));
        }
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
}
