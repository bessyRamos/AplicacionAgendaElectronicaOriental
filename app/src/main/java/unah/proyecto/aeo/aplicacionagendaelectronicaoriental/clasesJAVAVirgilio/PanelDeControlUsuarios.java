package unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVAVirgilio;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.R;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVAAlan.ActivityCategorias;

public class PanelDeControlUsuarios extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    //preferencia de administrador o usuario
    private Sesion sesion;
    private SesionUsuario sesionUsuario;
    //

    private Button salir;
    int id_usuario_resibido_usuario;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_panel_de_control_usuarios);
        //envio de clase actual para las preferencias
        sesion = new Sesion(this);
        sesionUsuario = new SesionUsuario(this);
        //

        salir = (Button) findViewById(R.id.salir);
        salir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sesion.logindim()) {
                    sesion.setLogin(false);
                    startActivity(new Intent(PanelDeControlUsuarios.this, Login.class));

                    finish();
                }else {
                    if(sesionUsuario.logindimUsuario()){
                        sesionUsuario.setLoginUsuario(false);
                        startActivity(new Intent(PanelDeControlUsuarios.this, Login.class));
                        finish();
                    }
                }
                //if (getIntent().getExtras()!=null){
                  //  id_usuario_resibido_usuario = getIntent().getExtras().getInt("usuario_ingreso");
                    //intent.putExtra("usuario_ingreso",id_usuario_resibido_usuario);
                //}
            }
        });



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

    @SuppressWarnings("StatementWithEmptyBody")
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.principaldos) {
            startActivity(new Intent(getBaseContext(), ActivityCategorias.class)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP));
            finish();

        } else if (id == R.id.acercadeinfodos) {
            Intent intent = new Intent(this,AcercaDe.class);
            startActivity(intent);

        }else if (id == R.id.login) {
            Intent intent = new Intent(this, Login.class);
            startActivity(intent);
            finish();
        }else if (id ==R.id.cerrarsecion){
            //
            if (sesion.logindim()) {
                sesion.setLogin(false);
                startActivity(new Intent(this, Login.class));
                finish();
            }else {
                //cerrar secion y borrado de preferencias
                if(sesionUsuario.logindimUsuario()){
                    sesionUsuario.setLoginUsuario(false);
                    startActivity(new Intent(this, Login.class));
                    finish();
                }
            }

            //
            /*editor_2.clear();
            editor_2.commit();
            finish();
            */
        }else if (id == R.id.ediciondeCuenta){
            Intent intent = new Intent(this,EditarUsuario.class);
            if (getIntent().getExtras()!=null){
                intent.putExtra("id",id_usuario_resibido_usuario);
                id_usuario_resibido_usuario = getIntent().getExtras().getInt("id");
            }

            startActivity(intent);

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
