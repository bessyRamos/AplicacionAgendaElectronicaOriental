package unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVAMelvin;

import android.app.LoaderManager;
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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.*;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVAVirgilio.AcercaDe;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVAVirgilio.Login;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_de_contactos);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //Obtiene el id de la categoria de la cual se mostrarán los contactos
        Bundle extras = getIntent().getExtras();
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

        //perfilesDbHelper = new AEODbHelper(this);


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
        /*newText = newText.toLowerCase();
        ArrayList<PerfilBreve> newList = new ArrayList<>();
        for (PerfilBreve perfilBreve: listaOrganizaciones){
            String nombre = perfilBreve.getNombre().toLowerCase();
            String numeroTel = perfilBreve.getNumeroTelefono().toLowerCase();
            String region = perfilBreve.getDireccion().toLowerCase();

            if(nombre.contains(newText) || numeroTel.contains(newText) || region.contains(newText))
                newList.add(perfilBreve);
        }
        //adaptadorPerfilBreve.setFilter(newList);*/
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

    //Metodo que consulta a la base de datos para ver los contactos
   /* private void consultarListaContactos(){

        //Obtener la base de datos
        SQLiteDatabase db = conn.getReadableDatabase();

        PerfilBreve perfilContacto = null;

        //Asignar la consulta sql
        Cursor cursor =  db.rawQuery("SELECT "
                +PerfilesContract.ContactosEntry.COLUMN_NOMBRE+","
                + PerfilesContract.ContactosEntry.COLUMN_IMAGEN_PATH+","
                + PerfilesContract.ContactosEntry.COLUMN_NUMERO_TELEFONO+","
                + PerfilesContract.ContactosEntry.COLUMN_NUMERO_CELULAR+","
                + PerfilesContract.ContactosEntry._ID+","
                +PerfilesContract.ContactosEntry.COLUMN_REGION+" FROM "
                +PerfilesContract.ContactosEntry.TABLE_NAME/*+ as c  JOIN REGIONES as a on c.id_region=a.id_region where id_categoria="+id_categoria+" and id_estado=2"*//*,null);

       //se obtienen los objetos de la consulta y se asignan a los componentes visuales
        while (cursor.moveToNext()){
            perfilContacto = new PerfilBreve();
            perfilContacto.setNombre(cursor.getString(0));
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 1;
            perfilContacto.setImagen(cursor.getString(1));
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
    }*/
/*
    private class mostrarPerfilesRegistrados extends AsyncTask<String, Integer, Boolean> {
        private mostrarPerfilesRegistrados(){}

        @Override
        protected Boolean doInBackground(String... strings) {
            boolean resul = true;
            try{
                HttpGet httpGet =  new HttpGet("https://shessag.000webhostapp.com/consultarContactosParaMostrar.php?id_categoria="+id_categoria);
                HttpClient httpClient = new DefaultHttpClient();
                HttpResponse response = (HttpResponse)httpClient.execute(httpGet);
                HttpEntity entity = response.getEntity();
                BufferedHttpEntity buffer = new BufferedHttpEntity(entity);
                InputStream iStream = buffer.getContent();

                String aux = "";

                BufferedReader r = new BufferedReader(new InputStreamReader(iStream));
                StringBuilder total = new StringBuilder();
                String line;
                while ((line = r.readLine()) != null) {
                    aux += line;
                }

                // Parseamos la respuesta obtenida del servidor a un objeto JSON
                JSONObject jsonObject = new JSONObject(aux);
                JSONArray perfiles = jsonObject.getJSONArray("perfiles");


                for(int i = 0; i < perfiles.length(); i++) {
                    JSONObject perfil = perfiles.getJSONObject(i);

                    // Creamos el objeto City
                    PerfilBreve c = new PerfilBreve();
                    c.setNombre(perfil.getString("nombre_organizacion"));

                    if(!perfil.getString("numero_fijo").isEmpty()){
                        c.setNumeroTelefono(perfil.getString("numero_fijo"));
                    }else{
                        c.setNumeroTelefono(perfil.getString("numero_movil"));
                    }
                    c.setDireccion(perfil.getString("nombre_region"));

                    if(!perfil.getString("imagen").isEmpty()){
                        c.setDato(perfil.getString("imagen"));
                    }else {
                        c.setImagen(BitmapFactory.decodeResource(getResources(),R.drawable.iconocontactowhite));
                    }

                    c.setId(Integer.parseInt(perfil.getString("id_contacto")));
                    // Almacenamos el objeto en el array que hemos creado anteriormente
                    listaOrganizaciones.add(c);
                }
            }catch (Exception ex){
                ex.printStackTrace();
                resul = false;
            }

            return resul;

        }

        protected void onPostExecute(Boolean result) {
            if (result.booleanValue()) {
                adaptadorPerfilBreve = new AdaptadorPerfilBreve(listaOrganizaciones);
                contenedor = (RecyclerView) findViewById(R.id.recyclerViewPerfilBreve);
                contenedor.setHasFixedSize(true);

                LinearLayoutManager layout = new LinearLayoutManager(getApplicationContext());
                layout.setOrientation(LinearLayoutManager.VERTICAL);
                contenedor.setAdapter(adaptadorPerfilBreve);
                contenedor.setLayoutManager(layout);
                return;
            }else {
                Toast.makeText(getApplicationContext(), "Problemas de conexión \n Mostrando datos de base de datos local", Toast.LENGTH_SHORT).show();

                //Conexión a la base de datos
                conn = new ConexionSQLiteHelper(getApplicationContext(),"bdaeo",null,1);

                consultarListaContactos();
                conn.close();
                //metodo contenedor de la pcicion de las pantallas horizontal y verical


            }
        }

    }*/
}