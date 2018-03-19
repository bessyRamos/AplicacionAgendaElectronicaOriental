package unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVAMelvin;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

import java.text.ParseException;
import java.util.ArrayList;

import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.ConexionSQLiteHelper;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.R;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVAAlan.ActivityCategorias;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVAMelvin.AdaptadorPerfilBreve;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVAMelvin.PerfilBreve;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVAVirgilio.AcercaDe;

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

        //Conexión a la base de datos
        conn = new ConexionSQLiteHelper(this,"bdaeo",null,1);



        //Inicializacion del RecyclerView
        contenedor = (RecyclerView) findViewById(R.id.recyclerbusquedaAvanzada);
        contenedor.setHasFixedSize(true);
        LinearLayoutManager layout = new LinearLayoutManager(getApplicationContext());
        layout.setOrientation(LinearLayoutManager.VERTICAL);

        db = conn.getReadableDatabase();

        //Asignar la consulta sql
        Cursor cursorInicial =  db.rawQuery("SELECT c.nombre_organizacion, c.imagen, c.numero_fijo, c.numero_movil, c.id_contacto, a.nombre_region FROM CONTACTOS as c  JOIN REGIONES as a on c.id_region=a.id_region",null);

        consultarListaContactos(cursorInicial,listaOrganizaciones);

        //Declaracion y seteo del adaptador al contenedor
        adaptadorPerfilBreve = new AdaptadorPerfilBreve(listaOrganizaciones);
        contenedor.setAdapter(adaptadorPerfilBreve);
        contenedor.setLayoutManager(layout);
        db.close();
        region.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                parent.setSelection(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        categoria.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                parent.setSelection(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        btnbusqueda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                filtros();
                
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
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void consultarListaContactos(Cursor cursor,ArrayList<PerfilBreve> arrayList){

        //se obtienen los objetos de la consulta y se asignan a los componentes visuales
        while (cursor.moveToNext()){
            PerfilBreve perfilContacto = new PerfilBreve();
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
            arrayList.add(perfilContacto);

        }
    }

    //Filtros de búsqueda en base a nombre, numero de telefono, region y categoría
    public void filtros(){
        if(!contactoABuscar.getText().toString().isEmpty()){
            db=conn.getReadableDatabase();
            ArrayList<PerfilBreve> lista =new ArrayList<PerfilBreve>();
            Cursor cursorBusqueda;
            String [] argEntrada = new String[]{contactoABuscar.getText().toString()};

            if(categoria.getSelectedItem().toString().contains("Todas las Categorías")){
                if(region.getSelectedItem().toString().contains("Todas las Regiones")){
                    cursorBusqueda =  db.rawQuery("SELECT c.nombre_organizacion, c.imagen, c.numero_fijo, c.numero_movil, c.id_contacto, a.nombre_region FROM CONTACTOS as c  JOIN REGIONES as a on c.id_region=a.id_region where c.nombre_organizacion like '%"+contactoABuscar.getText().toString()+"%' or c.numero_fijo like '%"+contactoABuscar.getText().toString()+"%' or numero_movil like '%"+contactoABuscar.getText().toString()+"%'",null);
                    consultarListaContactos(cursorBusqueda,lista);
                    adaptadorPerfilBreve.setFilter(lista);
                }else if(region.getSelectedItem().toString().contains("El Paraíso")){
                    cursorBusqueda =  db.rawQuery("SELECT c.nombre_organizacion, c.imagen, c.numero_fijo, c.numero_movil, c.id_contacto, a.nombre_region FROM CONTACTOS as c  JOIN REGIONES as a on c.id_region=a.id_region where c.nombre_organizacion like '%"+contactoABuscar.getText().toString()+"%' and c.id_region=4 or c.numero_fijo like '%"+contactoABuscar.getText().toString()+"%' and c.id_region=4 or numero_movil like '%"+contactoABuscar.getText().toString()+"%' and c.id_region=4",null);
                    consultarListaContactos(cursorBusqueda,lista);
                    adaptadorPerfilBreve.setFilter(lista);
                }else if(region.getSelectedItem().toString().contains("Danlí")){
                    cursorBusqueda =  db.rawQuery("SELECT c.nombre_organizacion, c.imagen, c.numero_fijo, c.numero_movil, c.id_contacto, a.nombre_region FROM CONTACTOS as c  JOIN REGIONES as a on c.id_region=a.id_region where c.nombre_organizacion like '%"+contactoABuscar.getText().toString()+"%' and c.id_region=3 or c.numero_fijo like '%"+contactoABuscar.getText().toString()+"%' and c.id_region=3 or numero_movil like '%"+contactoABuscar.getText().toString()+"%' and c.id_region=3",null);
                    consultarListaContactos(cursorBusqueda,lista);
                    adaptadorPerfilBreve.setFilter(lista);
                }
            }else if(categoria.getSelectedItem().toString().contains("Emergencia")){
                if(region.getSelectedItem().toString().contains("Todas las Regiones")){
                    cursorBusqueda =  db.rawQuery("SELECT c.nombre_organizacion, c.imagen, c.numero_fijo, c.numero_movil, c.id_contacto, a.nombre_region FROM CONTACTOS as c  JOIN REGIONES as a on c.id_region=a.id_region where c.nombre_organizacion like '%"+contactoABuscar.getText().toString()+"%' and c.id_categoria=1 or c.numero_fijo like '%"+contactoABuscar.getText().toString()+"%' and c.id_categoria=1 or numero_movil like '%"+contactoABuscar.getText().toString()+"%' and c.id_categoria=1",null);
                    consultarListaContactos(cursorBusqueda,lista);
                    adaptadorPerfilBreve.setFilter(lista);
                }else if(region.getSelectedItem().toString().contains("El Paraíso")){
                    cursorBusqueda =  db.rawQuery("SELECT c.nombre_organizacion, c.imagen, c.numero_fijo, c.numero_movil, c.id_contacto, a.nombre_region FROM CONTACTOS as c  JOIN REGIONES as a on c.id_region=a.id_region where c.nombre_organizacion like '%"+contactoABuscar.getText().toString()+"%' and c.id_categoria=1 and c.id_region=4 or c.numero_fijo like '%"+contactoABuscar.getText().toString()+"%' and c.id_categoria=1 and c.id_region=4 or numero_movil like '%"+contactoABuscar.getText().toString()+"%' and c.id_categoria=1 and c.id_region=4",null);
                    consultarListaContactos(cursorBusqueda,lista);
                    adaptadorPerfilBreve.setFilter(lista);
                }else if(region.getSelectedItem().toString().contains("Danlí")){
                    cursorBusqueda =  db.rawQuery("SELECT c.nombre_organizacion, c.imagen, c.numero_fijo, c.numero_movil, c.id_contacto, a.nombre_region FROM CONTACTOS as c  JOIN REGIONES as a on c.id_region=a.id_region where c.nombre_organizacion like '%"+contactoABuscar.getText().toString()+"%' and c.id_categoria=1 and c.id_region=3 or c.numero_fijo like '%"+contactoABuscar.getText().toString()+"%' and c.id_categoria=1 and c.id_region=3 or numero_movil like '%"+contactoABuscar.getText().toString()+"%' and c.id_categoria=1 and c.id_region=3",null);
                    consultarListaContactos(cursorBusqueda,lista);
                    adaptadorPerfilBreve.setFilter(lista);
                }
            }else if(categoria.getSelectedItem().toString().contains("Educación")){
                if(region.getSelectedItem().toString().contains("Todas las Regiones")){
                    cursorBusqueda =  db.rawQuery("SELECT c.nombre_organizacion, c.imagen, c.numero_fijo, c.numero_movil, c.id_contacto, a.nombre_region FROM CONTACTOS as c  JOIN REGIONES as a on c.id_region=a.id_region where c.nombre_organizacion like '%"+contactoABuscar.getText().toString()+"%' and c.id_categoria=2 or c.numero_fijo like '%"+contactoABuscar.getText().toString()+"%' and c.id_categoria=2 or numero_movil like '%"+contactoABuscar.getText().toString()+"%' and c.id_categoria=2",null);
                    consultarListaContactos(cursorBusqueda,lista);
                    adaptadorPerfilBreve.setFilter(lista);
                }else if(region.getSelectedItem().toString().contains("El Paraíso")){
                    cursorBusqueda =  db.rawQuery("SELECT c.nombre_organizacion, c.imagen, c.numero_fijo, c.numero_movil, c.id_contacto, a.nombre_region FROM CONTACTOS as c  JOIN REGIONES as a on c.id_region=a.id_region where c.nombre_organizacion like '%"+contactoABuscar.getText().toString()+"%' and c.id_categoria=2 and c.id_region=4 or c.numero_fijo like '%"+contactoABuscar.getText().toString()+"%' and c.id_categoria=2 and c.id_region=4 or numero_movil like '%"+contactoABuscar.getText().toString()+"%' and c.id_categoria=2 and c.id_region=4",null);
                    consultarListaContactos(cursorBusqueda,lista);
                    adaptadorPerfilBreve.setFilter(lista);
                }else if(region.getSelectedItem().toString().contains("Danlí")){
                    cursorBusqueda =  db.rawQuery("SELECT c.nombre_organizacion, c.imagen, c.numero_fijo, c.numero_movil, c.id_contacto, a.nombre_region FROM CONTACTOS as c  JOIN REGIONES as a on c.id_region=a.id_region where c.nombre_organizacion like '%"+contactoABuscar.getText().toString()+"%' and c.id_categoria=2 and c.id_region=3 or c.numero_fijo like '%"+contactoABuscar.getText().toString()+"%' and c.id_categoria=2 and c.id_region=3 or numero_movil like '%"+contactoABuscar.getText().toString()+"%' and c.id_categoria=2 and c.id_region=3",null);
                    consultarListaContactos(cursorBusqueda,lista);
                    adaptadorPerfilBreve.setFilter(lista);
                }
            }else if(categoria.getSelectedItem().toString().contains("Centros Asistenciales")){
                if(region.getSelectedItem().toString().contains("Todas las Regiones")){
                    cursorBusqueda =  db.rawQuery("SELECT c.nombre_organizacion, c.imagen, c.numero_fijo, c.numero_movil, c.id_contacto, a.nombre_region FROM CONTACTOS as c  JOIN REGIONES as a on c.id_region=a.id_region where c.nombre_organizacion like '%"+contactoABuscar.getText().toString()+"%' and c.id_categoria=3 or c.numero_fijo like '%"+contactoABuscar.getText().toString()+"%' and c.id_categoria=3 or numero_movil like '%"+contactoABuscar.getText().toString()+"%' and c.id_categoria=3",null);
                    consultarListaContactos(cursorBusqueda,lista);
                    adaptadorPerfilBreve.setFilter(lista);
                }else if(region.getSelectedItem().toString().contains("El Paraíso")){
                    cursorBusqueda =  db.rawQuery("SELECT c.nombre_organizacion, c.imagen, c.numero_fijo, c.numero_movil, c.id_contacto, a.nombre_region FROM CONTACTOS as c  JOIN REGIONES as a on c.id_region=a.id_region where c.nombre_organizacion like '%"+contactoABuscar.getText().toString()+"%' and c.id_categoria=3 and c.id_region=4 or c.numero_fijo like '%"+contactoABuscar.getText().toString()+"%' and c.id_categoria=3 and c.id_region=4 or numero_movil like '%"+contactoABuscar.getText().toString()+"%' and c.id_categoria=3 and c.id_region=4",null);
                    consultarListaContactos(cursorBusqueda,lista);
                    adaptadorPerfilBreve.setFilter(lista);
                }else if(region.getSelectedItem().toString().contains("Danlí")){
                    cursorBusqueda =  db.rawQuery("SELECT c.nombre_organizacion, c.imagen, c.numero_fijo, c.numero_movil, c.id_contacto, a.nombre_region FROM CONTACTOS as c  JOIN REGIONES as a on c.id_region=a.id_region where c.nombre_organizacion like '%"+contactoABuscar.getText().toString()+"%' and c.id_categoria=3 and c.id_region=3 or c.numero_fijo like '%"+contactoABuscar.getText().toString()+"%' and c.id_categoria=3 and c.id_region=3 or numero_movil like '%"+contactoABuscar.getText().toString()+"%' and c.id_categoria=3 and c.id_region=3",null);
                    consultarListaContactos(cursorBusqueda,lista);
                    adaptadorPerfilBreve.setFilter(lista);
                }
            }else if(categoria.getSelectedItem().toString().contains("Bancos")){
                if(region.getSelectedItem().toString().contains("Todas las Regiones")){
                    cursorBusqueda =  db.rawQuery("SELECT c.nombre_organizacion, c.imagen, c.numero_fijo, c.numero_movil, c.id_contacto, a.nombre_region FROM CONTACTOS as c  JOIN REGIONES as a on c.id_region=a.id_region where c.nombre_organizacion like '%"+contactoABuscar.getText().toString()+"%' and c.id_categoria=4 or c.numero_fijo like '%"+contactoABuscar.getText().toString()+"%' and c.id_categoria=4 or numero_movil like '%"+contactoABuscar.getText().toString()+"%' and c.id_categoria=4",null);
                    consultarListaContactos(cursorBusqueda,lista);
                    adaptadorPerfilBreve.setFilter(lista);
                }else if(region.getSelectedItem().toString().contains("El Paraíso")){
                    cursorBusqueda =  db.rawQuery("SELECT c.nombre_organizacion, c.imagen, c.numero_fijo, c.numero_movil, c.id_contacto, a.nombre_region FROM CONTACTOS as c  JOIN REGIONES as a on c.id_region=a.id_region where c.nombre_organizacion like '%"+contactoABuscar.getText().toString()+"%' and c.id_categoria=4 and c.id_region=4 or c.numero_fijo like '%"+contactoABuscar.getText().toString()+"%' and c.id_categoria=4 and c.id_region=4 or numero_movil like '%"+contactoABuscar.getText().toString()+"%' and c.id_categoria=4 and c.id_region=4",null);
                    consultarListaContactos(cursorBusqueda,lista);
                    adaptadorPerfilBreve.setFilter(lista);
                }else if(region.getSelectedItem().toString().contains("Danlí")){
                    cursorBusqueda =  db.rawQuery("SELECT c.nombre_organizacion, c.imagen, c.numero_fijo, c.numero_movil, c.id_contacto, a.nombre_region FROM CONTACTOS as c  JOIN REGIONES as a on c.id_region=a.id_region where c.nombre_organizacion like '%"+contactoABuscar.getText().toString()+"%' and c.id_categoria=4 and c.id_region=3 or c.numero_fijo like '%"+contactoABuscar.getText().toString()+"%' and c.id_categoria=4 and c.id_region=3 or numero_movil like '%"+contactoABuscar.getText().toString()+"%' and c.id_categoria=4 and c.id_region=3",null);
                    consultarListaContactos(cursorBusqueda,lista);
                    adaptadorPerfilBreve.setFilter(lista);
                }
            }else if(categoria.getSelectedItem().toString().contains("Hotelería y Turismo")){
                if(region.getSelectedItem().toString().contains("Todas las Regiones")){
                    cursorBusqueda =  db.rawQuery("SELECT c.nombre_organizacion, c.imagen, c.numero_fijo, c.numero_movil, c.id_contacto, a.nombre_region FROM CONTACTOS as c  JOIN REGIONES as a on c.id_region=a.id_region where c.nombre_organizacion like '%"+contactoABuscar.getText().toString()+"%' and c.id_categoria=5 or c.numero_fijo like '%"+contactoABuscar.getText().toString()+"%' and c.id_categoria=5 or numero_movil like '%"+contactoABuscar.getText().toString()+"%' and c.id_categoria=5",null);
                    consultarListaContactos(cursorBusqueda,lista);
                    adaptadorPerfilBreve.setFilter(lista);
                }else if(region.getSelectedItem().toString().contains("El Paraíso")){
                    cursorBusqueda =  db.rawQuery("SELECT c.nombre_organizacion, c.imagen, c.numero_fijo, c.numero_movil, c.id_contacto, a.nombre_region FROM CONTACTOS as c  JOIN REGIONES as a on c.id_region=a.id_region where c.nombre_organizacion like '%"+contactoABuscar.getText().toString()+"%' and c.id_categoria=5 and c.id_region=4 or c.numero_fijo like '%"+contactoABuscar.getText().toString()+"%' and c.id_categoria=5 and c.id_region=4 or numero_movil like '%"+contactoABuscar.getText().toString()+"%' and c.id_categoria=5 and c.id_region=4",null);
                    consultarListaContactos(cursorBusqueda,lista);
                    adaptadorPerfilBreve.setFilter(lista);
                }else if(region.getSelectedItem().toString().contains("Danlí")){
                    cursorBusqueda =  db.rawQuery("SELECT c.nombre_organizacion, c.imagen, c.numero_fijo, c.numero_movil, c.id_contacto, a.nombre_region FROM CONTACTOS as c  JOIN REGIONES as a on c.id_region=a.id_region where c.nombre_organizacion like '%"+contactoABuscar.getText().toString()+"%' and c.id_categoria=5 and c.id_region=3 or c.numero_fijo like '%"+contactoABuscar.getText().toString()+"%' and c.id_categoria=5 and c.id_region=3 or numero_movil like '%"+contactoABuscar.getText().toString()+"%' and c.id_categoria=5 and c.id_region=3",null);
                    consultarListaContactos(cursorBusqueda,lista);
                    adaptadorPerfilBreve.setFilter(lista);
                }
            }else if(categoria.getSelectedItem().toString().contains("Instituciones Públicas")){
                if(region.getSelectedItem().toString().contains("Todas las Regiones")){
                    cursorBusqueda =  db.rawQuery("SELECT c.nombre_organizacion, c.imagen, c.numero_fijo, c.numero_movil, c.id_contacto, a.nombre_region FROM CONTACTOS as c  JOIN REGIONES as a on c.id_region=a.id_region where c.nombre_organizacion like '%"+contactoABuscar.getText().toString()+"%' and c.id_categoria=6 or c.numero_fijo like '%"+contactoABuscar.getText().toString()+"%' and c.id_categoria=6 or numero_movil like '%"+contactoABuscar.getText().toString()+"%' and c.id_categoria=6",null);
                    consultarListaContactos(cursorBusqueda,lista);
                    adaptadorPerfilBreve.setFilter(lista);
                }else if(region.getSelectedItem().toString().contains("El Paraíso")){
                    cursorBusqueda =  db.rawQuery("SELECT c.nombre_organizacion, c.imagen, c.numero_fijo, c.numero_movil, c.id_contacto, a.nombre_region FROM CONTACTOS as c  JOIN REGIONES as a on c.id_region=a.id_region where c.nombre_organizacion like '%"+contactoABuscar.getText().toString()+"%' and c.id_categoria=6 and c.id_region=4 or c.numero_fijo like '%"+contactoABuscar.getText().toString()+"%' and c.id_categoria=6 and c.id_region=4 or numero_movil like '%"+contactoABuscar.getText().toString()+"%' and c.id_categoria=6 and c.id_region=4",null);
                    consultarListaContactos(cursorBusqueda,lista);
                    adaptadorPerfilBreve.setFilter(lista);
                }else if(region.getSelectedItem().toString().contains("Danlí")){
                    cursorBusqueda =  db.rawQuery("SELECT c.nombre_organizacion, c.imagen, c.numero_fijo, c.numero_movil, c.id_contacto, a.nombre_region FROM CONTACTOS as c  JOIN REGIONES as a on c.id_region=a.id_region where c.nombre_organizacion like '%"+contactoABuscar.getText().toString()+"%' and c.id_categoria=6 and c.id_region=3 or c.numero_fijo like '%"+contactoABuscar.getText().toString()+"%' and c.id_categoria=6 and c.id_region=3 or numero_movil like '%"+contactoABuscar.getText().toString()+"%' and c.id_categoria=6 and c.id_region=3",null);
                    consultarListaContactos(cursorBusqueda,lista);
                    adaptadorPerfilBreve.setFilter(lista);
                }
            }else if(categoria.getSelectedItem().toString().contains("Comercio de Bienes")){
                if(region.getSelectedItem().toString().contains("Todas las Regiones")){
                    cursorBusqueda =  db.rawQuery("SELECT c.nombre_organizacion, c.imagen, c.numero_fijo, c.numero_movil, c.id_contacto, a.nombre_region FROM CONTACTOS as c  JOIN REGIONES as a on c.id_region=a.id_region where c.nombre_organizacion like '%"+contactoABuscar.getText().toString()+"%' and c.id_categoria=7 or c.numero_fijo like '%"+contactoABuscar.getText().toString()+"%' and c.id_categoria=7 or numero_movil like '%"+contactoABuscar.getText().toString()+"%' and c.id_categoria=7",null);
                    consultarListaContactos(cursorBusqueda,lista);
                    adaptadorPerfilBreve.setFilter(lista);
                }else if(region.getSelectedItem().toString().contains("El Paraíso")){
                    cursorBusqueda =  db.rawQuery("SELECT c.nombre_organizacion, c.imagen, c.numero_fijo, c.numero_movil, c.id_contacto, a.nombre_region FROM CONTACTOS as c  JOIN REGIONES as a on c.id_region=a.id_region where c.nombre_organizacion like '%"+contactoABuscar.getText().toString()+"%' and c.id_categoria=7 and c.id_region=4 or c.numero_fijo like '%"+contactoABuscar.getText().toString()+"%' and c.id_categoria=7 and c.id_region=4 or numero_movil like '%"+contactoABuscar.getText().toString()+"%' and c.id_categoria=7 and c.id_region=4",null);
                    consultarListaContactos(cursorBusqueda,lista);
                    adaptadorPerfilBreve.setFilter(lista);
                }else if(region.getSelectedItem().toString().contains("Danlí")){
                    cursorBusqueda =  db.rawQuery("SELECT c.nombre_organizacion, c.imagen, c.numero_fijo, c.numero_movil, c.id_contacto, a.nombre_region FROM CONTACTOS as c  JOIN REGIONES as a on c.id_region=a.id_region where c.nombre_organizacion like '%"+contactoABuscar.getText().toString()+"%' and c.id_categoria=7 and c.id_region=3 or c.numero_fijo like '%"+contactoABuscar.getText().toString()+"%' and c.id_categoria=7 and c.id_region=3 or numero_movil like '%"+contactoABuscar.getText().toString()+"%' and c.id_categoria=7 and c.id_region=3",null);
                    consultarListaContactos(cursorBusqueda,lista);
                    adaptadorPerfilBreve.setFilter(lista);
                }
            }else if(categoria.getSelectedItem().toString().contains("Comercio de Servicios")){
                if(region.getSelectedItem().toString().contains("Todas las Regiones")){
                    cursorBusqueda =  db.rawQuery("SELECT c.nombre_organizacion, c.imagen, c.numero_fijo, c.numero_movil, c.id_contacto, a.nombre_region FROM CONTACTOS as c  JOIN REGIONES as a on c.id_region=a.id_region where c.nombre_organizacion like '%"+contactoABuscar.getText().toString()+"%' and c.id_categoria=8 or c.numero_fijo like '%"+contactoABuscar.getText().toString()+"%' and c.id_categoria=8 or numero_movil like '%"+contactoABuscar.getText().toString()+"%' and c.id_categoria=8",null);
                    consultarListaContactos(cursorBusqueda,lista);
                    adaptadorPerfilBreve.setFilter(lista);
                }else if(region.getSelectedItem().toString().contains("El Paraíso")){
                    cursorBusqueda =  db.rawQuery("SELECT c.nombre_organizacion, c.imagen, c.numero_fijo, c.numero_movil, c.id_contacto, a.nombre_region FROM CONTACTOS as c  JOIN REGIONES as a on c.id_region=a.id_region where c.nombre_organizacion like '%"+contactoABuscar.getText().toString()+"%' and c.id_categoria=8 and c.id_region=4 or c.numero_fijo like '%"+contactoABuscar.getText().toString()+"%' and c.id_categoria=8 and c.id_region=4 or numero_movil like '%"+contactoABuscar.getText().toString()+"%' and c.id_categoria=8 and c.id_region=4",null);
                    consultarListaContactos(cursorBusqueda,lista);
                    adaptadorPerfilBreve.setFilter(lista);
                }else if(region.getSelectedItem().toString().contains("Danlí")){
                    cursorBusqueda =  db.rawQuery("SELECT c.nombre_organizacion, c.imagen, c.numero_fijo, c.numero_movil, c.id_contacto, a.nombre_region FROM CONTACTOS as c  JOIN REGIONES as a on c.id_region=a.id_region where c.nombre_organizacion like '%"+contactoABuscar.getText().toString()+"%' and c.id_categoria=8 and c.id_region=3 or c.numero_fijo like '%"+contactoABuscar.getText().toString()+"%' and c.id_categoria=8 and c.id_region=3 or numero_movil like '%"+contactoABuscar.getText().toString()+"%' and c.id_categoria=8 and c.id_region=3",null);
                    consultarListaContactos(cursorBusqueda,lista);
                    adaptadorPerfilBreve.setFilter(lista);
                }
            }else if(categoria.getSelectedItem().toString().contains("Bienes y Raices")){
                if(region.getSelectedItem().toString().contains("Todas las Regiones")){
                    cursorBusqueda =  db.rawQuery("SELECT c.nombre_organizacion, c.imagen, c.numero_fijo, c.numero_movil, c.id_contacto, a.nombre_region FROM CONTACTOS as c  JOIN REGIONES as a on c.id_region=a.id_region where c.nombre_organizacion like '%"+contactoABuscar.getText().toString()+"%' and c.id_categoria=9 or c.numero_fijo like '%"+contactoABuscar.getText().toString()+"%' and c.id_categoria=9 or numero_movil like '%"+contactoABuscar.getText().toString()+"%' and c.id_categoria=9",null);
                    consultarListaContactos(cursorBusqueda,lista);
                    adaptadorPerfilBreve.setFilter(lista);
                }else if(region.getSelectedItem().toString().contains("El Paraíso")){
                    cursorBusqueda =  db.rawQuery("SELECT c.nombre_organizacion, c.imagen, c.numero_fijo, c.numero_movil, c.id_contacto, a.nombre_region FROM CONTACTOS as c  JOIN REGIONES as a on c.id_region=a.id_region where c.nombre_organizacion like '%"+contactoABuscar.getText().toString()+"%' and c.id_categoria=9 and c.id_region=4 or c.numero_fijo like '%"+contactoABuscar.getText().toString()+"%' and c.id_categoria=9 and c.id_region=4 or numero_movil like '%"+contactoABuscar.getText().toString()+"%' and c.id_categoria=9 and c.id_region=4",null);
                    consultarListaContactos(cursorBusqueda,lista);
                    adaptadorPerfilBreve.setFilter(lista);
                }else if(region.getSelectedItem().toString().contains("Danlí")){
                    cursorBusqueda =  db.rawQuery("SELECT c.nombre_organizacion, c.imagen, c.numero_fijo, c.numero_movil, c.id_contacto, a.nombre_region FROM CONTACTOS as c  JOIN REGIONES as a on c.id_region=a.id_region where c.nombre_organizacion like '%"+contactoABuscar.getText().toString()+"%' and c.id_categoria=9 and c.id_region=3 or c.numero_fijo like '%"+contactoABuscar.getText().toString()+"%' and c.id_categoria=9 and c.id_region=3 or numero_movil like '%"+contactoABuscar.getText().toString()+"%' and c.id_categoria=9 and c.id_region=3",null);
                    consultarListaContactos(cursorBusqueda,lista);
                    adaptadorPerfilBreve.setFilter(lista);
                }
            }else if(categoria.getSelectedItem().toString().contains("Asesoría Legal")){
                if(region.getSelectedItem().toString().contains("Todas las Regiones")){
                    cursorBusqueda =  db.rawQuery("SELECT c.nombre_organizacion, c.imagen, c.numero_fijo, c.numero_movil, c.id_contacto, a.nombre_region FROM CONTACTOS as c  JOIN REGIONES as a on c.id_region=a.id_region where c.nombre_organizacion like '%"+contactoABuscar.getText().toString()+"%' and c.id_categoria=10 or c.numero_fijo like '%"+contactoABuscar.getText().toString()+"%' and c.id_categoria=10 or numero_movil like '%"+contactoABuscar.getText().toString()+"%' and c.id_categoria=10",null);
                    consultarListaContactos(cursorBusqueda,lista);
                    adaptadorPerfilBreve.setFilter(lista);
                }else if(region.getSelectedItem().toString().contains("El Paraíso")){
                    cursorBusqueda =  db.rawQuery("SELECT c.nombre_organizacion, c.imagen, c.numero_fijo, c.numero_movil, c.id_contacto, a.nombre_region FROM CONTACTOS as c  JOIN REGIONES as a on c.id_region=a.id_region where c.nombre_organizacion like '%"+contactoABuscar.getText().toString()+"%' and c.id_categoria=10 and c.id_region=4 or c.numero_fijo like '%"+contactoABuscar.getText().toString()+"%' and c.id_categoria=10 and c.id_region=4 or numero_movil like '%"+contactoABuscar.getText().toString()+"%' and c.id_categoria=10 and c.id_region=4",null);
                    consultarListaContactos(cursorBusqueda,lista);
                    adaptadorPerfilBreve.setFilter(lista);
                }else if(region.getSelectedItem().toString().contains("Danlí")){
                    cursorBusqueda =  db.rawQuery("SELECT c.nombre_organizacion, c.imagen, c.numero_fijo, c.numero_movil, c.id_contacto, a.nombre_region FROM CONTACTOS as c  JOIN REGIONES as a on c.id_region=a.id_region where c.nombre_organizacion like '%"+contactoABuscar.getText().toString()+"%' and c.id_categoria=10 and c.id_region=3 or c.numero_fijo like '%"+contactoABuscar.getText().toString()+"%' and c.id_categoria=10 and c.id_region=3 or numero_movil like '%"+contactoABuscar.getText().toString()+"%' and c.id_categoria=10 and c.id_region=3",null);
                    consultarListaContactos(cursorBusqueda,lista);
                    adaptadorPerfilBreve.setFilter(lista);
                }
            }else if(categoria.getSelectedItem().toString().contains("Funerarias")){
                if(region.getSelectedItem().toString().contains("Todas las Regiones")){
                    cursorBusqueda =  db.rawQuery("SELECT c.nombre_organizacion, c.imagen, c.numero_fijo, c.numero_movil, c.id_contacto, a.nombre_region FROM CONTACTOS as c  JOIN REGIONES as a on c.id_region=a.id_region where c.nombre_organizacion like '%"+contactoABuscar.getText().toString()+"%' and c.id_categoria=11 or c.numero_fijo like '%"+contactoABuscar.getText().toString()+"%' and c.id_categoria=11 or numero_movil like '%"+contactoABuscar.getText().toString()+"%' and c.id_categoria=11",null);
                    consultarListaContactos(cursorBusqueda,lista);
                    adaptadorPerfilBreve.setFilter(lista);
                }else if(region.getSelectedItem().toString().contains("El Paraíso")){
                    cursorBusqueda =  db.rawQuery("SELECT c.nombre_organizacion, c.imagen, c.numero_fijo, c.numero_movil, c.id_contacto, a.nombre_region FROM CONTACTOS as c  JOIN REGIONES as a on c.id_region=a.id_region where c.nombre_organizacion like '%"+contactoABuscar.getText().toString()+"%' and c.id_categoria=11 and c.id_region=4 or c.numero_fijo like '%"+contactoABuscar.getText().toString()+"%' and c.id_categoria=11 and c.id_region=4 or numero_movil like '%"+contactoABuscar.getText().toString()+"%' and c.id_categoria=11 and c.id_region=4",null);
                    consultarListaContactos(cursorBusqueda,lista);
                    adaptadorPerfilBreve.setFilter(lista);
                }else if(region.getSelectedItem().toString().contains("Danlí")){
                    cursorBusqueda =  db.rawQuery("SELECT c.nombre_organizacion, c.imagen, c.numero_fijo, c.numero_movil, c.id_contacto, a.nombre_region FROM CONTACTOS as c  JOIN REGIONES as a on c.id_region=a.id_region where c.nombre_organizacion like '%"+contactoABuscar.getText().toString()+"%' and c.id_categoria=11 and c.id_region=3 or c.numero_fijo like '%"+contactoABuscar.getText().toString()+"%' and c.id_categoria=11 and c.id_region=3 or numero_movil like '%"+contactoABuscar.getText().toString()+"%' and c.id_categoria=11 and c.id_region=3",null);
                    consultarListaContactos(cursorBusqueda,lista);
                    adaptadorPerfilBreve.setFilter(lista);
                }
            }

        }else{
            contactoABuscar.setError("No ha ingresado contacto a buscar");
        }

        db.close();
    }
}
