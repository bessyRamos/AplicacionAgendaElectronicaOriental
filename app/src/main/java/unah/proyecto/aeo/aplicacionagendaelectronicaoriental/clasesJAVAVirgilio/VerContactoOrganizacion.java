package unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVAVirgilio;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ColorSpace;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.util.EntityUtils;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.R;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVAAlan.ActivityCategorias;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVAAlan.Panel_de_Control;

public class VerContactoOrganizacion extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    TextView nombre,telefono,celular,correo,direccion,descripcion,categoria,region,latitud,longitud;
    String nombre_ver,telefono_ver,celular_ver,correo_ver,direccion_ver,descripcion_ver,categoria_ver,region_ver,latitud_ver,longitud_ver;
    int id_perfil=0;
    int id_perfilEditar=0;
    ImageView imagen;
    Bitmap imagenBitmap;
    String encodeImagen,imagen_rec;
    int categoriaEntero,regionEntero;
    Boolean tieneImagen;

    String nombreCategoria,nombreRegion;
    Toolbar toolbar;

    //preferencias
    private Sesion sesion;
    private SesionUsuario sesionUsuario;
    int id_usu=-1,id_usuario_resibido_usuario=0;
    int usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_bar_ver_contacto);

        //envio de clase actual para las preferencias
        sesion = new Sesion(this);
        sesionUsuario = new SesionUsuario(this);
        SharedPreferences preferences = getSharedPreferences("credencial", Context.MODE_PRIVATE);
        id_usu  = preferences.getInt("usuario_ingreso",id_usu);
        //

        boolean editarFoto=false;

        Bundle a = getIntent().getExtras();
        id_perfilEditar = a.getInt("id");

        id_perfil = id_perfilEditar;

        nombre = (TextView) findViewById(R.id.verNombreOrganizacion);
        telefono = (TextView) findViewById(R.id.verNumeroFijo);
        celular = (TextView) findViewById(R.id.verNumeroCelular);
        correo = (TextView) findViewById(R.id.verEmailOrganizacion);
        direccion = (TextView) findViewById(R.id.verDireccionOrganizacion);
        descripcion = (TextView) findViewById(R.id.verDescripcionOrganizacion);
        categoria = (TextView) findViewById(R.id.verCategoriaOrganizacion);
        region = (TextView) findViewById(R.id.verRegionOrganizacion);
        latitud = (TextView) findViewById(R.id.verLatitudOrganizacion);
        longitud = (TextView) findViewById(R.id.verLongitudOrganizacion);
        imagen = (ImageView)findViewById(R.id.verImagenOrganizacion);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        new llenarEditTexEditarPerfil().execute();


        //toolbar.setTitle("Contacto");
        setSupportActionBar(toolbar);
        //muestra el menu lateral
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


//
        if(editarFoto==true){
            imagenBitmap = ((BitmapDrawable)imagen.getDrawable()).getBitmap();

            new AsyncTask<Void, Void, String>(){
                @Override
                protected String doInBackground(Void... voids) {
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    imagenBitmap.compress(Bitmap.CompressFormat.JPEG,100,baos);
                    byte b []= baos.toByteArray();

                    encodeImagen = Base64.encodeToString(b,0);

                    return null;
                }
            }.execute();
        }//fin



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
            finish();

        }else if (id == R.id.login) {

            if (sesion.logindim()){
                Intent intent = new Intent(VerContactoOrganizacion.this,Panel_de_Control.class);
                intent.putExtra("usuario_ingreso",id_usu);
                //startActivity(new Intent(PanelDeControlUsuarios.this,Panel_de_Control.class));
                startActivity(intent);
                finish();
            }else{
                if (sesionUsuario.logindimUsuario()){
                    Intent intent = new Intent(VerContactoOrganizacion.this,PanelDeControlUsuarios.class);
                    intent.putExtra("id",id_usu);
                    //startActivity(new Intent(PanelDeControlUsuarios.this,PanelDeControlUsuarios.class));
                    startActivity(intent);
                    finish();
                }else {
                    Intent intent = new Intent(this, Login.class);
                    startActivity(intent);
                }

            }

        }else if (id ==R.id.cerrarsecion){
            //cerrar secion y borrado de preferencias
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

        }else if (id == R.id.ediciondeCuenta){
            Intent intent = new Intent(this,EditarUsuario.class);
            if (getIntent().getExtras()!=null){
                id_usuario_resibido_usuario = getIntent().getExtras().getInt("id_usuario");

                intent.putExtra("id",id_usuario_resibido_usuario);
                startActivity(intent);
                finish();

            }else {
                Toast.makeText(getApplicationContext(),"Error en id de usuario",Toast.LENGTH_SHORT).show();
            }



        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void editarOrganizacion(View v){
        Intent intent = new Intent(VerContactoOrganizacion.this,EditarPerfilOrganizacion.class);
        // intent.putExtra("usuario_ingreso",id_perfil);
        intent.putExtra("id_usuario",id_perfil);
        if (getIntent().getExtras()!=null){
            usuario = getIntent().getExtras().getInt("usuario");
        }
        intent.putExtra("latitudEnviar",latitud_ver);
        intent.putExtra("longitudEnviar",longitud_ver);
        intent.putExtra("usuario",usuario);
        startActivity(intent);
        finish();

        /*
        if (getIntent().getExtras()!=null){
            id_perfil = getIntent().getExtras().getInt("usuario_ingreso");
            intent.putExtra("usuario_ingreso",id_perfil);
        }
        intent.putExtra("usuario_ingreso",id_perfil);
         */

    }

    private class llenarDatosContacto extends AsyncTask<String, Integer, Boolean> {
        private llenarDatosContacto(){}
        boolean resul = true;

        @Override
        protected Boolean doInBackground(String... strings) {

            try {
                JSONObject respJSON = new JSONObject(EntityUtils.toString(new DefaultHttpClient().execute(new HttpPost("http://aeo.web-hn.com/consultarDatosDePerfilParaEditar.php?id_contacto="+id_perfilEditar)).getEntity()));
                JSONArray jsonArray = respJSON.getJSONArray("perfiles");
                for (int i = 0; i < jsonArray.length(); i++) {
                    nombre_ver = jsonArray.getJSONObject(i).getString("nombre_organizacion");

                    if (jsonArray.getJSONObject(i).getString("numero_fijo").isEmpty()){
                        telefono_ver = null;
                        telefono.setText("Numero no disponible");

                    }else {
                        telefono_ver = jsonArray.getJSONObject(i).getString("numero_fijo");
                    }
                    if (jsonArray.getJSONObject(i).getString("numero_movil").isEmpty()){
                        celular_ver = null;
                        celular.setText("Numero no disponible");

                    }else {
                        celular_ver = jsonArray.getJSONObject(i).getString("numero_movil");
                    }

                    correo_ver = jsonArray.getJSONObject(i).getString("e_mail");
                    direccion_ver = jsonArray.getJSONObject(i).getString("direccion");
                    descripcion_ver = jsonArray.getJSONObject(i).getString("descripcion_organizacion");
                    //
                    categoria_ver = String.valueOf(jsonArray.getJSONObject(i).getInt("id_categoria"));
                    region_ver = String.valueOf(jsonArray.getJSONObject(i).getInt("id_region"));
                    //
                    latitud_ver = jsonArray.getJSONObject(i).getString("latitud");
                    longitud_ver = jsonArray.getJSONObject(i).getString("longitud");

                    /*
                    if(jsonArray.getJSONObject(i).getString("imagen").isEmpty()){
                        tieneImagen=false;
                    }else{
                        imagen_rec = jsonArray.getJSONObject(i).getString("imagen");
                        tieneImagen=true;
                    }
                    */

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
                /*if(tieneImagen==true){
                    byte[] byteCode = Base64.decode(imagen_rec, Base64.DEFAULT);
                    imagenOrg.setImageBitmap(BitmapFactory.decodeByteArray(byteCode,0,byteCode.length));
                }else {
                    imagenOrg.setImageResource(R.drawable.iconocontactowhite);
                }*/


                nombre.setText(nombre_ver);



                telefono.setText(telefono_ver);

                celular.setText(celular_ver);


                correo.setText(correo_ver);
                direccion.setText(direccion_ver);
                descripcion.setText(descripcion_ver);
                //
                categoria.setText(categoria_ver);
                region.setText(region_ver);
                //
                latitud.setText(latitud_ver);
                longitud.setText(longitud_ver);

                /*
                if(tieneImagen==true){
                    Glide.with(getApplicationContext()).
                            load(imagen_rec).
                            into(imagenOrg);
                }else{
                    Glide.with(getApplicationContext()).
                            load(R.drawable.iconocontactowhite).
                            into(imagenOrg);
                }
                */


            }else {
                Toast.makeText(getApplicationContext(), "Problemas de conexión", Toast.LENGTH_SHORT).show();
            }
        }
    }
//metodo
    private class llenarEditTexEditarPerfil extends AsyncTask<String, Integer, Boolean> {
        private llenarEditTexEditarPerfil(){}
        boolean resul = true;

        @Override
        protected Boolean doInBackground(String... strings) {

            try {
                JSONObject respJSON = new JSONObject(EntityUtils.toString(new DefaultHttpClient().execute(new HttpPost("http://aeo.web-hn.com/consultarDatosDePerfilParaEditar.php?id_contacto="+id_perfilEditar)).getEntity()));
                JSONArray jsonArray = respJSON.getJSONArray("perfiles");
                for (int i = 0; i < jsonArray.length(); i++) {
                    nombre_ver = jsonArray.getJSONObject(i).getString("nombre_organizacion");
                    telefono_ver = jsonArray.getJSONObject(i).getString("numero_fijo");
                    celular_ver = jsonArray.getJSONObject(i).getString("numero_movil");
                    direccion_ver= jsonArray.getJSONObject(i).getString("direccion");
                    if(jsonArray.getJSONObject(i).getString("imagen").isEmpty()){
                        tieneImagen=false;
                    }else{
                        imagen_rec = jsonArray.getJSONObject(i).getString("imagen");
                        tieneImagen=true;
                    }

                    correo_ver = jsonArray.getJSONObject(i).getString("e_mail");
                    descripcion_ver = jsonArray.getJSONObject(i).getString("descripcion_organizacion");
                    latitud_ver = jsonArray.getJSONObject(i).getString("latitud");
                    longitud_ver = jsonArray.getJSONObject(i).getString("longitud");
                    regionEntero = jsonArray.getJSONObject(i).getInt("id_region");
                    categoriaEntero = jsonArray.getJSONObject(i).getInt("id_categoria");

                    //JSONArray regiones = new JSONArray(EntityUtils.toString(new DefaultHttpClient().execute(new HttpPost("http://aeo.web-hn.com/consultarRegiones.php")).getEntity()));


                        JSONObject categorias = new JSONObject(EntityUtils.toString(new DefaultHttpClient().execute(new HttpPost("https://shessag.000webhostapp.com/verNombreCategoria.php?categoria_id="+categoriaEntero)).getEntity()));
                        JSONArray jsonArraye = categorias.getJSONArray("datos");
                        for (int c = 0; c < categorias.length(); c++) {
                            nombreCategoria = jsonArraye.getJSONObject(c).getString("nombre_categoria");
                        }

                    JSONObject regiones = new JSONObject(EntityUtils.toString(new DefaultHttpClient().execute(new HttpPost("https://shessag.000webhostapp.com/verNombreRegiones.php?region_id="+regionEntero)).getEntity()));
                    JSONArray jsonArrayr = regiones.getJSONArray("datoss");
                    for (int c = 0; c < regiones.length(); c++) {
                        nombreRegion = jsonArrayr.getJSONObject(c).getString("nombre_region");
                    }





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
                if(tieneImagen==true){

                    Picasso.get().
                            load(imagen_rec).
                            networkPolicy(NetworkPolicy.NO_CACHE).
                            memoryPolicy(MemoryPolicy.NO_CACHE).placeholder(R.drawable.wait).
                            into(imagen);
                }else{
                    Picasso.get().
                            load(R.drawable.iconocontactowhite).
                            into(imagen);
                }

                if (nombre_ver.isEmpty()){
                    nombre.setText("No Disponible");
                    //toolbar.setTitle("Nombre No Disponible");
                    //setSupportActionBar(toolbar);
                }else {
                    nombre.setText(nombre_ver);
                    //toolbar.setTitle(nombre_ver);
                   // setSupportActionBar(toolbar);
                }

               if (telefono_ver.isEmpty()){
                   telefono.setText("No Disponible");
               }else {
                   telefono.setText(telefono_ver);
               }
               if (celular_ver.isEmpty()){
                   celular.setText("No Disponible");
               }else {
                   celular.setText(celular_ver);
               }
                if (correo_ver.isEmpty()){
                   correo.setText("No Disponible");
                }else {
                    correo.setText(correo_ver);
                }
                if (direccion_ver.isEmpty()){
                    direccion.setText("No Disponible");
                }else {
                    direccion.setText(direccion_ver);
                }
                if (direccion_ver.isEmpty()){
                    descripcion.setText("No Disponible");
                }else {
                    descripcion.setText(descripcion_ver);
                }


                categoria_ver= String.valueOf(categoriaEntero);
                if (categoria_ver.isEmpty()){
                    categoria.setText("No Disponinble");
                }else {
                    //
                    categoria.setText(nombreCategoria);
                }


                region_ver=String.valueOf(regionEntero);
                if (region_ver.isEmpty()){
                    region.setText("No Disponible");
                }else {
                    region.setText(nombreRegion);
                    //
                }
                if (latitud_ver.isEmpty()|| latitud_ver.equals(0)){
                    latitud.setText("No Disponible");

                }else {
                    latitud.setText(latitud_ver);
                }
                if (longitud_ver.isEmpty() || longitud_ver.equals(0)){
                    longitud.setText("No Disponible");

                }else {
                    longitud.setText(longitud_ver);
                }
            }else {
                Toast.makeText(getApplicationContext(), "Problemas de conexión", Toast.LENGTH_SHORT).show();
            }
        }
    }
    @Override
    public void onRestart()
    {
        super.onRestart();
        finish();
        startActivity(getIntent());
    }

}

