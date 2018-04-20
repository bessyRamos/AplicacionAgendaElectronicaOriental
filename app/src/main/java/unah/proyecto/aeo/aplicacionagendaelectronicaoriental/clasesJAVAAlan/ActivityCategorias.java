package unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVAAlan;
import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Surface;
import android.view.WindowManager;

import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.R;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVAMelvin.BusquedaAvanzada;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVAVirgilio.AcercaDe;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVAVirgilio.Login;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.provider.CategoriasContract;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.sync.SyncAdapter;

public class ActivityCategorias extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, SearchView.OnQueryTextListener, LoaderManager.LoaderCallbacks<Cursor> {

    Adaptador_Categoria adaptadorCategoria;
    RecyclerView contenedor;
    private static final int CATEGORIA_LOADER=0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_categorias);



        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        contenedor = (RecyclerView) findViewById(R.id.contenedor);
        contenedor.setHasFixedSize(true);

        adaptadorCategoria = new Adaptador_Categoria(ActivityCategorias.this,null);


        if(getRotation(getApplicationContext())== "vertical"){
            LinearLayoutManager layout = new LinearLayoutManager(getApplicationContext());
            layout.setOrientation(LinearLayoutManager.VERTICAL);
            contenedor.setLayoutManager(layout);
        }else{

            contenedor.setLayoutManager(new GridLayoutManager(getApplicationContext(),2));
        }
        contenedor.setAdapter(adaptadorCategoria);



        SyncAdapter.initializeSyncAdapter(this);

        getLoaderManager().initLoader(CATEGORIA_LOADER,null,this);


    }


    public String getRotation(Context context){
        final int rotation = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getOrientation();
        switch (rotation) {
            case Surface.ROTATION_0:
                return "vertical";
            case Surface.ROTATION_90:
                return "horizontal";
            case Surface.ROTATION_180:
                return "vertical inversa";
            default:
                return "horizontal inversa";
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


    @SuppressWarnings("StatementWithEmptyBody")
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.principaldos) {
            // Handle the camera action

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


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection ={
                CategoriasContract.CategoriasEntry.COLUMN_ID_CATEGORIA,
                CategoriasContract.CategoriasEntry.COLUMN_NOMBRE_CATEGORIA,
                CategoriasContract.CategoriasEntry.COLUMN_CANTIDAD,
                CategoriasContract.CategoriasEntry.COLUMN_IMAGEN_CATEGORIA,
        };



        return new CursorLoader(this,
                CategoriasContract.CategoriasEntry.CONTENT_URI,
                projection,
                null,
                null,
                CategoriasContract.CategoriasEntry.COLUMN_ID_CATEGORIA);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        adaptadorCategoria.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adaptadorCategoria.swapCursor(null);
    }








    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
       /* newText = newText.toLowerCase();
        ArrayList<Fuente_Categoria> newList = new ArrayList<>();
        for (Fuente_Categoria fuentecategoria :  lista){
            String nombre = fuentecategoria.getTitulo().toLowerCase();


            if(nombre.contains(newText)){
                newList.add(fuentecategoria);
            }

        }
        adaptadorCategoria.setFilter(newList);
        return true;*/
       return false;
    }
}



