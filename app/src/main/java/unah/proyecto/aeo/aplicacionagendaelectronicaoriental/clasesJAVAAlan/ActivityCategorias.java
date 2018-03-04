package unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVAAlan;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import java.util.ArrayList;

import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.ConexionSQLiteHelper;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.R;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVAVirgilio.AcercaDe;

public class ActivityCategorias extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ListView lista;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_categorias);



        ConexionSQLiteHelper conn=new ConexionSQLiteHelper(this,"bd_aeo",null,1);

        ArrayList<Fuente_Categoria>lista= new ArrayList<Fuente_Categoria>();
        lista.add(new Fuente_Categoria("Emergencia", R.drawable.casa,65,0));
        lista.add(new Fuente_Categoria("Hospitales",R.drawable.ho,5,0));
        lista.add(new Fuente_Categoria("Clinicas",R.drawable.casa,23,0));
        lista.add(new Fuente_Categoria("Escuelas",R.drawable.casa,45,0));
        lista.add(new Fuente_Categoria("Univercidades",R.drawable.ho,6,0));
        lista.add(new Fuente_Categoria("Museos",R.drawable.casa,4,0));
        lista.add(new Fuente_Categoria("Ferreterias",R.drawable.casa,20,0));
        lista.add(new Fuente_Categoria("Agrocomerciales",R.drawable.ho,80,0));
        lista.add(new Fuente_Categoria("Laboratorios",R.drawable.casa,28,0));



        RecyclerView contenedor = (RecyclerView) findViewById(R.id.contenedor);
        contenedor.setHasFixedSize(true);
        LinearLayoutManager layout = new LinearLayoutManager(getApplicationContext());
        layout.setOrientation(LinearLayoutManager.VERTICAL);
        contenedor.setAdapter(new Adaptador_Categoria(lista));
        contenedor.setLayoutManager(layout);


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
        getMenuInflater().inflate(R.menu.main_categorias, menu);
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

        } else if (id == R.id.acercadeinfodos) {
            Intent intent = new Intent(this,AcercaDe.class);
            startActivity(intent);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



}
