package unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVAMelvin;

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
import android.widget.TextView;

import java.util.ArrayList;

import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.ConexionSQLiteHelper;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.R;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVAVirgilio.AcercaDe;

public class ListaDeContactos extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    ArrayList<PerfilBreve> listaOrganizaciones;
    ConexionSQLiteHelper conn;
    int id_categoria;
    String nombre_categoria;
    TextView nombreCategoria;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_de_contactos);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        nombreCategoria = (TextView)findViewById(R.id.nombreCategoriaMostrada);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        Bundle extras = getIntent().getExtras();
        if (extras!=null){
            id_categoria = Integer.parseInt(extras.getString("id_categoria"));
            nombre_categoria = extras.getString("nombre_categoria");
        }

        nombreCategoria.setText(nombre_categoria);

        //Conexión a la base de datos
        conn = new ConexionSQLiteHelper(this,"bdaeo",null,1);
        conn.getReadableDatabase().setForeignKeyConstraintsEnabled(true);

        //Inicializacion del array
        listaOrganizaciones = new ArrayList<PerfilBreve>();

        //Inicializacion del RecyclerView
        RecyclerView contenedor = (RecyclerView) findViewById(R.id.recyclerViewPerfilBreve);
        contenedor.setHasFixedSize(true);
        LinearLayoutManager layout = new LinearLayoutManager(getApplicationContext());
        layout.setOrientation(LinearLayoutManager.VERTICAL);


        //Llamada al método para consultar la base de datos
        consultarListaContactos();

        //Declaracion y seteo del adaptador al contenedor
        AdaptadorPerfilBreve adaptadorPerfilBreve = new AdaptadorPerfilBreve(listaOrganizaciones);
        contenedor.setAdapter(adaptadorPerfilBreve);
        contenedor.setLayoutManager(layout);

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
/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.lista_de_contactos, menu);
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



    //Metodo que consulta a la base de datos para ver los contactos
    private void consultarListaContactos(){

        //Obtener la base de datos
        SQLiteDatabase db = conn.getReadableDatabase();

        PerfilBreve perfilContacto = null;

        //Asignar la consulta sql
        Cursor cursor =  db.rawQuery("SELECT nombre_organizacion, imagen, numero_fijo, direccion, id_contacto FROM CONTACTOS where id_categoria="+id_categoria,null);

       //se obtienen los objetos de la consulta y se asignan a los componentes visuales
        while (cursor.moveToNext()){
            perfilContacto = new PerfilBreve();
            perfilContacto.setNombre(cursor.getString(0));
            perfilContacto.setImagen(cursor.getInt(1));
            perfilContacto.setNumeroTelefono(cursor.getString(2));
            perfilContacto.setDireccion(cursor.getString(3));
            perfilContacto.setId(cursor.getInt(4));

            //se añade los datos al array
            listaOrganizaciones.add(perfilContacto);

        }
    }


}
