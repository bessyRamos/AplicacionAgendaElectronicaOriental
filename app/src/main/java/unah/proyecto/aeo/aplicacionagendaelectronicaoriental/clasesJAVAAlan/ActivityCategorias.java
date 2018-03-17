package unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVAAlan;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.Surface;
import android.view.WindowManager;


import java.util.ArrayList;

import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.ConexionSQLiteHelper;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.R;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVAVirgilio.AcercaDe;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVAVirgilio.Login;

public class ActivityCategorias extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    ArrayList<Fuente_Categoria> lista;
    ConexionSQLiteHelper conn;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_categorias);
        lista= new ArrayList<Fuente_Categoria>();




        //Conexión a la base de datos
        conn = new ConexionSQLiteHelper(getApplicationContext(),"bdaeo",null,1);

        consultarListaCategorias();
//metodo contenedor de la pcicion de las pantallas horizontal y verical
        RecyclerView contenedor = (RecyclerView) findViewById(R.id.contenedor);
        Adaptador_Categoria myAdapter = new Adaptador_Categoria(this.lista);
        contenedor.setHasFixedSize(true);


       if(getRotation(getApplicationContext())== "vertical"){
           LinearLayoutManager layout = new LinearLayoutManager(getApplicationContext());
           layout.setOrientation(LinearLayoutManager.VERTICAL);
           contenedor.setAdapter(myAdapter);
           contenedor.setLayoutManager(layout);
       }else{

           contenedor.setLayoutManager(new GridLayoutManager(this,2));
           contenedor.setAdapter(myAdapter);

       }





        conn.close();


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
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



    //Metodo que consulta a la base de datos para ver las  categorias
    private void consultarListaCategorias(){

        //Obtener la base de datos
        SQLiteDatabase db = conn.getReadableDatabase();

        Fuente_Categoria fuente_categoria = null;

        //Asignar la consulta sql
        Cursor cursor =  db.rawQuery("SELECT A.id_categoria,A.nombre_categoria, A.imagen_categoria,COUNT(*) FROM CATEGORIAS AS A " +
                "JOIN CONTACTOS AS C ON A.id_categoria=C.id_categoria GROUP BY A.id_categoria ",null);

        //se obtienen los objetos de la consulta y se asignan a los componentes visuales
        while (cursor.moveToNext()){
            fuente_categoria = new Fuente_Categoria();
            fuente_categoria.setId(cursor.getInt(0));
            fuente_categoria.setTitulo(cursor.getString(1));
            fuente_categoria.setImagen(cursor.getInt(2));
            fuente_categoria.setCantidad(cursor.getInt(3));


            //se añade los datos al array
            lista.add(fuente_categoria);

        }
    }




}
