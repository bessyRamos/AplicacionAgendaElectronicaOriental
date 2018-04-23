package unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVAMelvin.PerfilesBreves;
import android.app.LoaderManager;

import android.content.Context;

import android.content.ContentResolver;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
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

import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.*;

import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVAAlan.Panel_de_Control;

import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVAMelvin.HerramientaBusquedaAvanzada.BusquedaAvanzada;

import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVAVirgilio.AcercaDe;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVAVirgilio.Login;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVAVirgilio.PanelDeControlUsuarios;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVAVirgilio.Sesion;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVAVirgilio.SesionUsuario;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.provider.*;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.sync.*;


public class ListaDeContactos extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, android.support.v7.widget.SearchView.OnQueryTextListener, LoaderManager.LoaderCallbacks<Cursor> {
    AEODbHelper dbHelper;
    int id_categoria;
    String nombre_categoria;
    AdaptadorPerfilBreve adaptadorPerfilBreve;
    RecyclerView contenedor;
    private static final int PERFIL_LOADER=0;

    //preferencias
    private Sesion sesion;
    private SesionUsuario sesionUsuario;
    int id_usu=-1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_de_contactos);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //Obtiene el id de la categoria de la cual se mostrarán los contactos
        Bundle extras = getIntent().getExtras();
        //envio de clase actual para las preferencias
        sesion = new Sesion(this);
        sesionUsuario = new SesionUsuario(this);
        SharedPreferences preferences = getSharedPreferences("credencial", Context.MODE_PRIVATE);
        id_usu  = preferences.getInt("usuario_ingreso",id_usu);
        //
        if (extras!=null){
            id_categoria = extras.getInt("id_categoria");
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


        contenedor = (RecyclerView) findViewById(R.id.recyclerViewPerfilBreve);
        contenedor.setHasFixedSize(true);
        adaptadorPerfilBreve = new AdaptadorPerfilBreve(this, null);

        LinearLayoutManager layout = new LinearLayoutManager(getApplicationContext());
        layout.setOrientation(LinearLayoutManager.VERTICAL);
        contenedor.setAdapter(adaptadorPerfilBreve);
        contenedor.setLayoutManager(layout);

        getLoaderManager().initLoader(PERFIL_LOADER,null,this);

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
                break;
            case R.id.sincronizar:
                SyncAdapter.syncImmediately(this);
                break;
        }
        return super.onOptionsItemSelected(item);
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
            if (sesion.logindim()){
                Intent intent = new Intent(ListaDeContactos.this,Panel_de_Control.class);
                intent.putExtra("usuario_ingreso",id_usu);
                //startActivity(new Intent(ActivityCategorias.this,Panel_de_Control.class));
                startActivity(intent);
                finish();
            }else{
                if (sesionUsuario.logindimUsuario()){
                    Intent intent = new Intent(ListaDeContactos.this,PanelDeControlUsuarios.class);
                    intent.putExtra("id",id_usu);
                    //startActivity(new Intent(ActivityCategorias.this,PanelDeControlUsuarios.class));
                    startActivity(intent);
                    finish();

                }else {
                    Intent intent = new Intent(this, Login.class);
                    startActivity(intent);
                    finish();
                }

            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection ={
                PerfilesContract.ContactosEntry.COLUMN_PERFILID,
                PerfilesContract.ContactosEntry.COLUMN_NOMBRE,
                PerfilesContract.ContactosEntry.COLUMN_NUMERO_TELEFONO,
                PerfilesContract.ContactosEntry.COLUMN_NUMERO_CELULAR,
                PerfilesContract.ContactosEntry.COLUMN_NOMBRE_REGION,
                PerfilesContract.ContactosEntry.COLUMN_IMAGEN_PATH
        };

        return new CursorLoader(this,
                PerfilesContract.ContactosEntry.CONTENT_URI,
                projection,
                PerfilesContract.ContactosEntry.COLUMN_CATEGORIA+" = ? AND id_estado=?",
                new String[]{""+id_categoria,"2"},
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        adaptadorPerfilBreve.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adaptadorPerfilBreve.swapCursor(null);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {

        Cursor contacts = getListOfContacts(query);

        AdaptadorPerfilBreve adaptador_perfil1 = new AdaptadorPerfilBreve(this,contacts);

        contenedor.setAdapter(adaptador_perfil1);
        return true;
    }

    public Cursor getListOfContacts(String searchText) {

        Cursor cur = null;
        ContentResolver cr = getContentResolver();

        String[] projection ={
                PerfilesContract.ContactosEntry.COLUMN_PERFILID,
                PerfilesContract.ContactosEntry.COLUMN_NOMBRE,
                PerfilesContract.ContactosEntry.COLUMN_NUMERO_TELEFONO,
                PerfilesContract.ContactosEntry.COLUMN_NUMERO_CELULAR,
                PerfilesContract.ContactosEntry.COLUMN_NOMBRE_REGION,
                PerfilesContract.ContactosEntry.COLUMN_IMAGEN_PATH
        };

        Uri uri = PerfilesContract.ContactosEntry.CONTENT_URI;

        String selection =
                PerfilesContract.ContactosEntry.COLUMN_NOMBRE + " LIKE ? AND "+
                PerfilesContract.ContactosEntry.COLUMN_CATEGORIA +" = ? AND " +
                PerfilesContract.ContactosEntry.COLUMN_ESTADO +" = ? or "+
                PerfilesContract.ContactosEntry.COLUMN_NUMERO_TELEFONO + " LIKE ? AND "+
                PerfilesContract.ContactosEntry.COLUMN_CATEGORIA +" = ? AND " +
                PerfilesContract.ContactosEntry.COLUMN_ESTADO +" = ? or "+
                PerfilesContract.ContactosEntry.COLUMN_NUMERO_CELULAR + " LIKE ? AND "+
                PerfilesContract.ContactosEntry.COLUMN_CATEGORIA +" = ? AND " +
                PerfilesContract.ContactosEntry.COLUMN_ESTADO +" = ? or "+
                PerfilesContract.ContactosEntry.COLUMN_NOMBRE_REGION + " LIKE ?  AND "+
                PerfilesContract.ContactosEntry.COLUMN_CATEGORIA +" = ? AND " +
                PerfilesContract.ContactosEntry.COLUMN_ESTADO +" = ? ";
        String[] selectionArgs = new String[]{"" +
                "%"+searchText+"%", ""+id_categoria, "2",
                "%"+searchText+"%", ""+id_categoria, "2",
                "%"+searchText+"%", ""+id_categoria, "2",
                "%"+searchText+"%", ""+id_categoria, "2"};

        cur = cr.query(uri, projection, selection, selectionArgs, null);

        return cur;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        Cursor contacts = getListOfContacts(newText);

        AdaptadorPerfilBreve adaptador_categoria1 = new AdaptadorPerfilBreve(this,contacts);

        contenedor.setAdapter(adaptador_categoria1);
        return true;
    }

}