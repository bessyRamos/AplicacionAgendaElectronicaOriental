package unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVABessy;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import java.util.ArrayList;

import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.ConexionSQLiteHelper;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.R;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVAMelvin.AdaptadorPerfilBreve;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVAMelvin.PerfilBreve;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVAVirgilio.AcercaDe;

/**
 * Created by melvinrivera on 10/3/18.
 */

public class Buscar extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener  {

    private  String busquedaDesdeContactos;
    String filtroRegionDesdeContactos;
    EditText busqueda;
    ImageButton btnBusqueda;
    ConexionSQLiteHelper conn;
    ArrayList<PerfilBreve> listaOrganizaciones;
    PerfilBreve perfilContacto = null;
    Cursor cursor;
    int id_region;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_de_contactos);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        busqueda = findViewById(R.id.abuscar);
        btnBusqueda = findViewById(R.id.btnBuscar);
        conn = new ConexionSQLiteHelper(this, "bdaeo", null, 1);
        listaOrganizaciones = new ArrayList<PerfilBreve>();
        RecyclerView contenedor = (RecyclerView) findViewById(R.id.recyclerViewPerfilBreve);
        contenedor.setHasFixedSize(true);
        LinearLayoutManager layout = new LinearLayoutManager(getApplicationContext());
        layout.setOrientation(LinearLayoutManager.VERTICAL);


        Bundle extras = getIntent().getExtras();
        if (extras != null) {

            SQLiteDatabase db = conn.getReadableDatabase();
            busquedaDesdeContactos = extras.getString("busqueda");
            filtroRegionDesdeContactos = extras.getString("regionBuscar");

            switch (filtroRegionDesdeContactos){
                case "Danli":
                    id_region=3;
                    cursor = db.rawQuery("SELECT c.nombre_organizacion, c.imagen, c.numero_fijo, c.numero_movil, c.id_contacto, a.nombre_region FROM CONTACTOS as c INNER JOIN REGIONES as a ON c.id_region=a.id_region where c.nombre_organizacion like '%"+busquedaDesdeContactos+"%' and c.id_region="+id_region+" or c.numero_fijo like '%"+busquedaDesdeContactos+"%' and c.id_region="+id_region+" or c.numero_movil like '%"+busquedaDesdeContactos+"%' and c.id_region="+id_region, null);
                    break;
                case "El Paraiso":
                    id_region=4;
                    cursor = db.rawQuery("SELECT c.nombre_organizacion, c.imagen, c.numero_fijo, c.numero_movil, c.id_contacto, a.nombre_region FROM CONTACTOS as c INNER JOIN REGIONES as a ON c.id_region=a.id_region where c.nombre_organizacion like '%"+busquedaDesdeContactos+"%' and c.id_region="+id_region+" or c.numero_fijo like '%"+busquedaDesdeContactos+"%' and c.id_region="+id_region+" or c.numero_movil like '%"+busquedaDesdeContactos+"%' and c.id_region="+id_region, null);
                    break;
                default:
                    cursor = db.rawQuery("SELECT c.nombre_organizacion, c.imagen, c.numero_fijo, c.numero_movil, c.id_contacto, a.nombre_region FROM CONTACTOS as c INNER JOIN REGIONES as a ON c.id_region=a.id_region where c.nombre_organizacion like '%"+busquedaDesdeContactos+"%'"+" or c.numero_fijo like '%"+busquedaDesdeContactos+"%'or c.numero_movil like '%"+busquedaDesdeContactos+"%' and c.id_region="+id_region, null);
                    break;

            }
            consultarListaContactos();


        }

       //Declaracion y seteo del adaptador al contenedor
        AdaptadorPerfilBreve adaptadorPerfilBreve = new AdaptadorPerfilBreve(listaOrganizaciones);
        contenedor.setAdapter(adaptadorPerfilBreve);
        contenedor.setLayoutManager(layout);

        conn.close();

        btnBusqueda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
        finish();
    }

    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.principaldos) {
            // Handle the camera action
            finish();
        } else if (id == R.id.acercadeinfodos) {
            Intent intent = new Intent(this,AcercaDe.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void consultarListaContactos(){


        //se obtienen los objetos de la consulta y se asignan a los componentes visuales
        while (cursor.moveToNext()){
            perfilContacto = new PerfilBreve();
            perfilContacto.setNombre(cursor.getString(0));
            perfilContacto.setImagen(cursor.getInt(1));
            if(cursor.getString(2).isEmpty()) {
                perfilContacto.setNumeroTelefono(cursor.getString(3));
            }else{
                perfilContacto.setNumeroTelefono(cursor.getString(2));
            }

            perfilContacto.setDireccion(cursor.getString(5));
            perfilContacto.setId(cursor.getInt(4));

            //se añade los datos al array
            listaOrganizaciones.add(perfilContacto);

        }
    }

}
