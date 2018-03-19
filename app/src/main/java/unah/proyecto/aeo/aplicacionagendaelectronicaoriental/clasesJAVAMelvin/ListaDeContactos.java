package unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVAMelvin;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import java.util.ArrayList;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.ConexionSQLiteHelper;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.R;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVAVirgilio.AcercaDe;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVAVirgilio.Login;

public class ListaDeContactos extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, android.support.v7.widget.SearchView.OnQueryTextListener {
    ArrayList<PerfilBreve> listaOrganizaciones;
    ConexionSQLiteHelper conn;
    int id_categoria;
    String nombre_categoria;
    AdaptadorPerfilBreve adaptadorPerfilBreve;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_de_contactos);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //Obtiene el id de la categoria de la cual se mostrarán los contactos
        Bundle extras = getIntent().getExtras();
        if (extras!=null){
            id_categoria = Integer.parseInt(extras.getString("id_categoria"));
            nombre_categoria = extras.getString("nombre_categoria");

            //Establece el texto del toolbar con el nombre de la categoria a la que se  entró
            toolbar.setTitle(nombre_categoria);
        }

        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        //Conexión a la base de datos
        conn = new ConexionSQLiteHelper(this,"bdaeo",null,1);

        //Inicializacion del array
        listaOrganizaciones = new ArrayList<PerfilBreve>();

        //Inicializacion del RecyclerView
        RecyclerView contenedor = (RecyclerView) findViewById(R.id.recyclerViewPerfilBreve);
        contenedor.setHasFixedSize(true);
        LinearLayoutManager layout = new LinearLayoutManager(getApplicationContext());
        layout.setOrientation(LinearLayoutManager.VERTICAL);


        //Llamada al método para consultar los contactos en la base de datos
        consultarListaContactos();

        //Declaracion y seteo del adaptador al contenedor
        adaptadorPerfilBreve = new AdaptadorPerfilBreve(listaOrganizaciones);
        contenedor.setAdapter(adaptadorPerfilBreve);
        contenedor.setLayoutManager(layout);

        //cierre de la conexión a la base de datos
        conn.close();

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.lista_de_contactos,menu);
        MenuItem menuItem = menu.findItem(R.id.accion_buscar);
        MenuItem itemBusquedaAvanzada   = menu.findItem(R.id.accion_buscarAvanzado);
        //Establecimeinto del SearchView para filtrar por nombre, numero de telefono o region
        android.support.v7.widget.SearchView searchView  = (android.support.v7.widget.SearchView) MenuItemCompat.getActionView(menuItem);
        searchView.setOnQueryTextListener(this);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            //Intent para pasar a la activity de búsqueda avanzada
            case R.id.accion_buscarAvanzado:
                Intent aBusquedaAvanzada= new Intent(getApplicationContext(),BusquedaAvanzada.class);
                startActivity(aBusquedaAvanzada);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    //Metodo que establece el filtro del adaptador según se va escribiendo en el SearchView
    public boolean onQueryTextChange(String newText) {
        newText = newText.toLowerCase();
        ArrayList<PerfilBreve> newList = new ArrayList<>();
        for (PerfilBreve perfilBreve: listaOrganizaciones){
            String nombre = perfilBreve.getNombre().toLowerCase();
            String numeroTel = perfilBreve.getNumeroTelefono().toLowerCase();
            String region = perfilBreve.getDireccion().toLowerCase();

            if(nombre.contains(newText) || numeroTel.contains(newText) || region.contains(newText))
                newList.add(perfilBreve);
        }
        adaptadorPerfilBreve.setFilter(newList);
        return true;
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
        }else if (id == R.id.login) {
            Intent intent = new Intent(this, Login.class);
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
        Cursor cursor =  db.rawQuery("SELECT c.nombre_organizacion, c.imagen, c.numero_fijo, c.numero_movil, c.id_contacto, a.nombre_region FROM CONTACTOS as c  JOIN REGIONES as a on c.id_region=a.id_region where id_categoria="+id_categoria,null);

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
        //cierra la conexión a la base de datos
        db.close();
    }


}
