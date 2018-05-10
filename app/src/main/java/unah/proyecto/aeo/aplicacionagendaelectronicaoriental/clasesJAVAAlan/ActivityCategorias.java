package unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVAAlan;
import android.app.Activity;
import android.app.Application;
import android.app.LoaderManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
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
import android.widget.Toast;

import com.squareup.picasso.LruCache;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.R;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVAMelvin.HerramientaBusquedaAvanzada.BusquedaAvanzada;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVAVirgilio.AcercaDe;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVAVirgilio.Login;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVAVirgilio.MenuPreferencias;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVAVirgilio.PanelDeControlUsuarios;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVAVirgilio.Sesion;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVAVirgilio.SesionUsuario;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.provider.AEODbHelper;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.provider.CategoriasContract;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.provider.PerfilesContract;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.sync.SyncAdapter;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.sync.SyncService;

import static kotlin.text.Typography.amp;

public class ActivityCategorias extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        SearchView.OnQueryTextListener,
        LoaderManager.LoaderCallbacks<Cursor> {

    Adaptador_Categoria adaptadorCategoria;
    RecyclerView contenedor;
    private static final int CATEGORIA_LOADER = 0;

    //preferencias
    private Sesion sesion;
    private SesionUsuario sesionUsuario;
    int id_usu=-0;
    int id_usu2;

    private Picasso picasso;
    private LruCache picassoLruCache;

//va ala preferencia
  SharedPreferences preferencia ;



    //variables en la cual se almacenara la preferencia
    //String usario = preferencia.getString("usarioAdmin","null");
    //String contrasena = preferencia.getString("contrasenaAdmin","null");
    //int id_usuario = preferencia.getInt("idAdmin",0);

    int id_usuario_normal;
    private MenuPreferencias menu;

    SharedPreferences logue;
    int id_administrador,id_normal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_categorias);

        preferencia = getSharedPreferences("acceso", Context.MODE_PRIVATE);
        id_usuario_normal = preferencia.getInt("idNormal",0);



        logue = getSharedPreferences("Nombre",Context.MODE_PRIVATE);
        id_administrador =logue.getInt("Admin",0);
        id_normal = logue.getInt("Normal",0);

        //envio de clase actual para las preferencias
        sesion = new Sesion(this);
        sesionUsuario = new SesionUsuario(this);
        SharedPreferences preferences = getSharedPreferences("credencial",Context.MODE_PRIVATE);
        id_usu  = preferences.getInt("usuario_ingreso",0);
        id_usu2  = preferences.getInt("usuario_admin",0);
        //
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        menu = new MenuPreferencias(this);

        if (sesion.logindim() || sesionUsuario.logindimUsuario()){
                NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
                navigationView.inflateMenu(R.menu.menu_tercero);
                navigationView.setNavigationItemSelectedListener(this);
        }else {
            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.inflateMenu(R.menu.activity_principal_drawer);
            navigationView.setNavigationItemSelectedListener(this);
        }


        contenedor = (RecyclerView) findViewById(R.id.contenedor);
        contenedor.setHasFixedSize(true);


        adaptadorCategoria = new Adaptador_Categoria(ActivityCategorias.this, null);


        if (getRotation(getApplicationContext()) == "vertical") {
            LinearLayoutManager layout = new LinearLayoutManager(getApplicationContext());
            layout.setOrientation(LinearLayoutManager.VERTICAL);
            contenedor.setLayoutManager(layout);
        } else {

            contenedor.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
        }
        contenedor.setAdapter(adaptadorCategoria);


        SyncAdapter.initializeSyncAdapter(this);

        getLoaderManager().initLoader(CATEGORIA_LOADER,null,this);


    }


    public String getRotation(Context context) {
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
        getMenuInflater().inflate(R.menu.lista_de_contactos, menu);
        MenuItem menuItem = menu.findItem(R.id.accion_buscar);
        MenuItem itemBusquedaAvanzada = menu.findItem(R.id.accion_buscarAvanzado);
        //Establecimeinto del SearchView para filtrar por nombre, numero de telefono o region
        android.support.v7.widget.SearchView searchView = (android.support.v7.widget.SearchView) MenuItemCompat.getActionView(menuItem);
        searchView.setOnQueryTextListener(this);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            //Intent para pasar a la activity de búsqueda avanzada
            case R.id.accion_buscarAvanzado:
                Intent aBusquedaAvanzada = new Intent(getApplicationContext(), BusquedaAvanzada.class);
                startActivity(aBusquedaAvanzada);
                break;
            case R.id.sincronizar:
                if( compruebaConexion()){
                    Snackbar.make(findViewById(R.id.drawer_layout),"Actualizando datos...",Snackbar.LENGTH_SHORT).show();
                    borrarCacheImagenes();
                    SyncAdapter.syncImmediately(this);
                }else{
                    Snackbar.make(findViewById(R.id.drawer_layout),"Actualmente no cuentas con conexión a internet",Snackbar.LENGTH_SHORT).show();

                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
       this.recreate();
    }

    private void borrarCacheImagenes(){



    }

    @SuppressWarnings("StatementWithEmptyBody")
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.principaldos) {

            startActivity(new Intent(getBaseContext(), ActivityCategorias.class)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP));

        } else if (id == R.id.acercadeinfodos) {
            Intent intent = new Intent(this,AcercaDe.class);
            startActivity(intent);

        }else if (id == R.id.login) {
            if (sesion.logindim()){
                Intent intent = new Intent(ActivityCategorias.this,Panel_de_Control.class);
                Toast.makeText(getApplicationContext(),""+id_administrador,Toast.LENGTH_SHORT).show();
                intent.putExtra("usuario_ingreso",id_administrador);

                sesionUsuario.setLoginUsuario(false);
                startActivityForResult(intent,100);

            }else{
                if (sesionUsuario.logindimUsuario()){
                    Intent intent = new Intent(ActivityCategorias.this,PanelDeControlUsuarios.class);

                    intent.putExtra("id",id_normal);
                    Toast.makeText(getApplicationContext(),""+id_normal,Toast.LENGTH_SHORT).show();
                    //startActivity(new Intent(ActivityCategorias.this,PanelDeControlUsuarios.class));

                    sesion.setLogin(false);
                    startActivityForResult(intent,300);


                }else {
                    Intent intent = new Intent(this, Login.class);
                    startActivityForResult(intent,100);
                    //finish();

                }

            }
        }else if (id == R.id.cerrarsecion){
            if (sesion.logindim()) {
                sesion.setLogin(false);
                //startActivity(new Intent(this, Login.class));
                Intent intent = new Intent(this,Login.class);
                startActivityForResult(intent,200);

            }else {
                if(sesionUsuario.logindimUsuario()){
                    sesionUsuario.setLoginUsuario(false);
                    Intent intent = new Intent(this,Login.class);
                    startActivityForResult(intent,200);

                }
            }
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {
                CategoriasContract.CategoriasEntry.COLUMN_ID_CATEGORIA,
                CategoriasContract.CategoriasEntry.COLUMN_NOMBRE_CATEGORIA,
                CategoriasContract.CategoriasEntry.COLUMN_CANTIDAD,
                CategoriasContract.CategoriasEntry.COLUMN_IMAGEN_CATEGORIA
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

        Cursor contacts = getListOfContacts(query);

       Adaptador_Categoria adaptador_categoria1 = new Adaptador_Categoria(this,contacts);

        contenedor.setAdapter(adaptador_categoria1);
        return true;
    }

    public Cursor getListOfContacts(String searchText) {

        Cursor cur = null;
        ContentResolver cr = getContentResolver();

        String[] projection = {
                CategoriasContract.CategoriasEntry.COLUMN_ID_CATEGORIA,
                CategoriasContract.CategoriasEntry.COLUMN_NOMBRE_CATEGORIA,
                CategoriasContract.CategoriasEntry.COLUMN_CANTIDAD,
                CategoriasContract.CategoriasEntry.COLUMN_IMAGEN_CATEGORIA
        };

        Uri uri = CategoriasContract.CategoriasEntry.CONTENT_URI;

        String selection = CategoriasContract.CategoriasEntry.COLUMN_NOMBRE_CATEGORIA + " LIKE ?";
        String[] selectionArgs = new String[]{"%"+searchText+"%"};

        cur = cr.query(uri, projection, selection, selectionArgs, CategoriasContract.CategoriasEntry.COLUMN_ID_CATEGORIA);

        return cur;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        Cursor contacts = getListOfContacts(newText);


        Adaptador_Categoria adaptador_categoria1 = new Adaptador_Categoria(this,contacts);

        contenedor.setAdapter(adaptador_categoria1);
        return true;
    }

    public boolean compruebaConexion() {

        Runtime runtime = Runtime.getRuntime();
        try {
            Process ipProcess = runtime.exec("/system/bin/ping -w 1 8.8.8.8");
            int     exitValue = ipProcess.waitFor();
            return (exitValue == 0);
        } catch (IOException e)          { e.printStackTrace(); }
        catch (InterruptedException e) { e.printStackTrace(); }
        return false;
    }
}