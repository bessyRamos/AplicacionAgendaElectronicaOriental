package unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVAAlan;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.R;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVAMelvin.PerfilesBreves.ListaDeContactos;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVAMelvin.AdministracionDePerfilesAdmin.AdministracionDePerfiles;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVAVirgilio.AcercaDe;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVAVirgilio.Login;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVAVirgilio.PanelDeControlUsuarios;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVAVirgilio.Sesion;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVAVirgilio.SesionUsuario;

public class Panel_de_Control extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    ListView lista;

    private DrawerLayout mdrawerLayout;
    private ActionBarDrawerToggle mToggle;
    private int id_usuario_resibido;
    private SharedPreferences preferences_2;
    private SharedPreferences.Editor editor_2;
    Context context=this;

    //preferencias de administrador y usuario
    private Sesion sesion;
    private SesionUsuario sesionUsuario;
    //

    public Fuente_Panel_de_control[] fuente_panel_de_control;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_panel_de_control);

        //envio de la clase actual alas preferencias
        sesion =new Sesion(this);
        sesionUsuario = new SesionUsuario(this);
        //

        lista=(ListView)findViewById(R.id.listViewPneldeControl);



        Fuente_Panel_de_control fuente_panel_de_control[]={
                new Fuente_Panel_de_control(R.drawable.administracioncuenta,"Administración de  Cuenta",0),
                new Fuente_Panel_de_control(R.drawable.administracionperfil,"Administración de Perfil",0)
                // new Fuente_Panel_de_control(R.drawable.celular,"Solicitudes Nuevas",0),
                // new Fuente_Panel_de_control(R.drawable.celular,"Solicitudes Aprovadas",0),
                // new Fuente_Panel_de_control(R.drawable.celular,"Solicitudes Eliminadas",0)

        };
        final Adaptador_paneldeControl adapter  = new Adaptador_paneldeControl(this,fuente_panel_de_control);
        lista.setAdapter(adapter);
        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                if(position==0){
                    Intent intent = new Intent(view.getContext(),Mostrar_Usuarios.class);
                    if (getIntent().getExtras()!=null){
                        id_usuario_resibido = getIntent().getExtras().getInt("usuario_ingreso");
                        intent.putExtra("usuario_ingreso",id_usuario_resibido);
                    }
                    startActivity(intent);
                }else if(position==1){
                    Intent intent = new Intent(view.getContext(),AdministracionDePerfiles.class);
                    startActivity(intent);
                }else if(position==2){
                    Intent intent = new Intent(view.getContext(),ListaDeContactos.class);
                    startActivity(intent);
                }

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

        //
        preferences_2 = getSharedPreferences("Login", context.MODE_PRIVATE);
        editor_2 = preferences_2.edit();

        String usuario_2 = preferences_2.getString("usuarioP","null");
        String contrasena_2 = preferences_2.getString("contrasenaP","null");
        //


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
            if (sesion.logindim()){
                startActivity(new Intent(Panel_de_Control.this,Panel_de_Control.class));
                finish();
            }else{
                if (sesionUsuario.logindimUsuario()){
                    startActivity(new Intent(Panel_de_Control.this,PanelDeControlUsuarios.class));
                    finish();
                }else {
                    Intent intent = new Intent(this, Login.class);
                    startActivity(intent);
                }

            }
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
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



}




