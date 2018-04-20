package unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVAMelvin;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.util.ArrayList;

import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.entity.BufferedHttpEntity;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.util.EntityUtils;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.ConexionSQLiteHelper;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.R;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVAAlan.ActivityCategorias;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVAMelvin.AdaptadorPerfilBreve;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVAMelvin.PerfilBreve;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVAVirgilio.AcercaDe;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVAVirgilio.Login;

public class BusquedaAvanzada extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    TextInputEditText contactoABuscar;
    Spinner categoria, region;
    ImageButton btnbusqueda;
    ConexionSQLiteHelper conn;
    ArrayList<PerfilBreve> listaOrganizaciones;
    RecyclerView contenedor;
    SQLiteDatabase db;
    AdaptadorPerfilBreve adaptadorPerfilBreve;
    ArrayList<ModeloSpinner> listaCategorias, listaRegiones;
    boolean unaRegionSeleccionada, unaCategoriaSeleccionada;
    int id_categoria, id_region;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_busqueda_avanzada);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        contactoABuscar = findViewById(R.id.contacto_a_buscar);
        categoria = findViewById(R.id.spinercategoria);
        region = findViewById(R.id.spinerregionesbuscar);
        btnbusqueda = findViewById(R.id.boton_busqueda_avanzada);
        listaOrganizaciones = new ArrayList<PerfilBreve>();

        listaCategorias=new ArrayList<ModeloSpinner>();
        listaRegiones=new ArrayList<ModeloSpinner>();


        //Inicializacion del RecyclerView

        Toast.makeText(getApplicationContext(),"Cargando...",Toast.LENGTH_SHORT).show();

       // new llenarSpinnersBusqueda().execute();
        //new mostrarPerfilesRegistrados().execute();






        btnbusqueda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //filtrosOffline();
                
            }
        });
    }

    @Override
    public void onBackPressed() {
        conn.close();
        db.close();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

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
    }
/*
    private void consultarListaContactos(Cursor cursor,ArrayList<PerfilBreve> arrayList){

        //se obtienen los objetos de la consulta y se asignan a los componentes visuales
        while (cursor.moveToNext()){
            PerfilBreve perfilContacto = new PerfilBreve();
            perfilContacto.setNombre(cursor.getString(0));
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 2;
            perfilContacto.setImagen(BitmapFactory.decodeResource(getResources(),cursor.getInt(1),options));

            if(cursor.getString(2).isEmpty()) {
                perfilContacto.setNumeroTelefono(cursor.getString(3));
            }else{
                perfilContacto.setNumeroTelefono(cursor.getString(2));
            }
            perfilContacto.setDireccion(cursor.getString(5));
            perfilContacto.setId(cursor.getInt(4));

            //se añade los datos al array
            arrayList.add(perfilContacto);

        }
    }*/
/*
    //Filtros de búsqueda en base a nombre, numero de telefono, region y categoría
    public void filtrosOffline(){
        if(!contactoABuscar.getText().toString().isEmpty()){
            db=conn.getReadableDatabase();
            ArrayList<PerfilBreve> lista =new ArrayList<PerfilBreve>();
            Cursor cursorBusqueda;
            String [] argEntrada = new String[]{contactoABuscar.getText().toString()};

            if(unaCategoriaSeleccionada==false && unaRegionSeleccionada==false){
                cursorBusqueda =  db.rawQuery("SELECT c.nombre_organizacion, c.imagen, c.numero_fijo, c.numero_movil, c.id_contacto, a.nombre_region FROM CONTACTOS as c  JOIN REGIONES as a on c.id_region=a.id_region where c.nombre_organizacion like '%"+contactoABuscar.getText().toString()+"%' or c.numero_fijo like '%"+contactoABuscar.getText().toString()+"%' or numero_movil like '%"+contactoABuscar.getText().toString()+"%'",null);
                consultarListaContactos(cursorBusqueda,lista);
                adaptadorPerfilBreve.setFilter(lista);
            }else {
                cursorBusqueda =  db.rawQuery("SELECT c.nombre_organizacion, c.imagen, c.numero_fijo, c.numero_movil, c.id_contacto, a.nombre_region FROM CONTACTOS as c  JOIN REGIONES as a on c.id_region=a.id_region where c.nombre_organizacion like '%"+contactoABuscar.getText().toString()+"%' and c.id_region="+id_region+" and c.id_categoria="+id_categoria+" or c.numero_fijo like '%"+contactoABuscar.getText().toString()+"%' and c.id_region="+id_region+" and id_categoria="+id_categoria+" or numero_movil like '%"+contactoABuscar.getText().toString()+"%' and c.id_region="+id_region+"and id_categoria="+id_categoria,null);
                consultarListaContactos(cursorBusqueda,lista);
                adaptadorPerfilBreve.setFilter(lista);
            }


        }else{
            contactoABuscar.setError("No ha ingresado contacto a buscar");
        }

//        db.close();
    }
*/
 /*   private class llenarSpinnersBusqueda extends AsyncTask<String, Integer, Boolean> {
        private llenarSpinnersBusqueda(){}
        boolean resul = true;

        @Override
        protected Boolean doInBackground(String... strings) {

            try {

                JSONArray regionesWS = new JSONArray(EntityUtils.toString(new DefaultHttpClient().execute(new HttpPost("https://shessag.000webhostapp.com/consultarRegiones.php")).getEntity()));
                JSONArray categoriasWS = new JSONArray(EntityUtils.toString(new DefaultHttpClient().execute(new HttpPost("https://shessag.000webhostapp.com/consultarCategorias.php")).getEntity()));

                for (int i = 0; i < regionesWS.length(); i++) {
                    listaRegiones.add(new ModeloSpinner(regionesWS.getJSONObject(i).getString("nombre_region"),Integer.parseInt(regionesWS.getJSONObject(i).getString("id_region")))
                    );
                }
                for (int i=0;i<categoriasWS.length();i++){
                    listaCategorias.add(new ModeloSpinner(categoriasWS.getJSONObject(i).getString("nombre_categoria"), Integer.parseInt(categoriasWS.getJSONObject(i).getString("id_categoria"))));
                }

                resul = true;
            } catch (Exception ex) {
                Log.e("ServicioRest", "Error!", ex);
                resul = false;
            }
            return resul;

        }

        protected void onPostExecute(Boolean result) {
            if (resul) {
                AdaptadorPersonalizadoSpinner adaptadorCategorias = new AdaptadorPersonalizadoSpinner(BusquedaAvanzada.this,R.layout.plantilla_spiners_personalizados_id_nombre,R.id.item_id_spinner,listaCategorias);
                AdaptadorPersonalizadoSpinner adaptadorRegiones = new AdaptadorPersonalizadoSpinner(BusquedaAvanzada.this,R.layout.plantilla_spiners_personalizados_id_nombre,R.id.item_id_spinner,listaRegiones);
                categoria.setAdapter(adaptadorCategorias);
                region.setAdapter(adaptadorRegiones);
                /*for(int i=0; i < adaptadorCategorias.getCount(); i++) {
                    if(idcategoria_rec==adaptadorCategorias.getItem(i).getId()){
                        spcategorias.setSelection(i);
                        break;
                    }
                }

                for(int i=0; i < adaptadorRegiones.getCount(); i++) {
                    if(idregion_rec==adaptadorRegiones.getItem(i).getId()){
                        spregiones.setSelection(i);
                        break;
                    }
                }
            }else {
                Toast.makeText(getApplicationContext(), "Problemas de conexión", Toast.LENGTH_SHORT).show();
            }
        }
    }
*/
 /*   private class mostrarPerfilesRegistrados extends AsyncTask<String, Integer, Boolean> {
        private mostrarPerfilesRegistrados(){}

        @Override
        protected Boolean doInBackground(String... strings) {
            boolean resul = true;
            try{
                HttpGet httpGet =  new HttpGet("https://shessag.000webhostapp.com/consultarContactosParaMostrar.php");
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

                db = conn.getReadableDatabase();

                //Asignar la consulta sql
                Cursor cursorInicial =  db.rawQuery("SELECT c.nombre_organizacion, c.imagen, c.numero_fijo, c.numero_movil, c.id_contacto, a.nombre_region FROM CONTACTOS as c  JOIN REGIONES as a on c.id_region=a.id_region",null);

                consultarListaContactos(cursorInicial,listaOrganizaciones);
                //metodo contenedor de la pcicion de las pantallas horizontal y verical

                contenedor = (RecyclerView) findViewById(R.id.recyclerbusquedaAvanzada);
                contenedor.setHasFixedSize(true);
                LinearLayoutManager layout = new LinearLayoutManager(getApplicationContext());
                layout.setOrientation(LinearLayoutManager.VERTICAL);

                adaptadorPerfilBreve = new AdaptadorPerfilBreve(listaOrganizaciones);
                contenedor.setAdapter(adaptadorPerfilBreve);
                contenedor.setLayoutManager(layout);
                db.close();

                region.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        id_region = listaRegiones.get(position).getId();
                        unaRegionSeleccionada = true;
                        filtrosOffline();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        unaRegionSeleccionada = false;
                    }
                });

                categoria.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        unaCategoriaSeleccionada = true;
                        id_categoria = listaCategorias.get(position).getId();
                        filtrosOffline();

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        unaCategoriaSeleccionada=false;
                    }
                });
            }
        }

    }*/
}