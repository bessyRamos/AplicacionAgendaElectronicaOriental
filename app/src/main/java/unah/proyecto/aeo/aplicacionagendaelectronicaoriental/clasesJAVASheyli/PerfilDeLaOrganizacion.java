package unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVASheyli;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.ConexionSQLiteHelper;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.R;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVAAlan.ActivityCategorias;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVAVirgilio.AcercaDe;

public class PerfilDeLaOrganizacion extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private ImageView organizacion;
    private TextView nombre,direccion,telefono,email,descripcion;
    ConexionSQLiteHelper conn;



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
        email = (TextView) findViewById(R.id.e);
        descripcion = (TextView) findViewById(R.id.descripcion);


        conn = new ConexionSQLiteHelper(this,"bdaeo",null,1);

        SQLiteDatabase db = conn.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT imagen, nombre_organizacion,direccion,numero_fijo,e_mail,descripcion_organizacion FROM CONTACTOS WHERE id_contacto = 1",null );
        while(cursor.moveToNext())
        {
            organizacion.setImageResource(cursor.getInt(0));
            nombre.setText(cursor.getString(0));
            direccion.setText(cursor.getString(0));
            telefono.setText(cursor.getString(0));
            email.setText(cursor.getString(0));
            descripcion.setText(cursor.getString(0));

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

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
