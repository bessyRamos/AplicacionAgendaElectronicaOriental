package unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVAVirgilio;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.entity.UrlEncodedFormEntity;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.message.BasicNameValuePair;
import cz.msebera.android.httpclient.util.EntityUtils;
import de.hdodenhof.circleimageview.CircleImageView;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.R;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVAAlan.ActivityCategorias;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVAAlan.Panel_de_Control;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVABessy.Ingresar_Ubicacion;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVAMelvin.AdministracionDePerfilesAdmin.AdaptadorPersonalizadoSpinner;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVAMelvin.AdministracionDePerfilesAdmin.ModeloSpinner;


public class FormularioNuevaOrganizacion extends AppCompatActivity  implements NavigationView.OnNavigationItemSelectedListener{

    EditText nombreOrganizacion;
    EditText telefonoFijo;
    EditText telefonoCelular;
    EditText direccionOrganizacion;
    EditText emailOrganizacion;
    EditText descrpcionOrganizacion;
    EditText latitudOrganizacion;
    EditText longitudOrganizacion;
    ImageView imagenOrganizacion,ubicacionOrganizacion;
    FloatingActionButton guardar;
    ImageButton imageButton;
    //int id_usuario;
    Button ubicacion;
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1 ;
    Bitmap imagenBitmap;

    ArrayList<ModeloSpinner> listaCategorias, listaRegiones, listaUsuarios;
    private Spinner  spinnerCategorias,spinnerRgiones;

    int id_categoria, id_region, id_usuario;
    private static final int PICK_IMAGE = 100;
    Uri imageUri;

    String encodeImagen;


    //preferencias
    private Sesion sesion;
    private SesionUsuario sesionUsuario;
    int id_usu=-1;
    //

    double latitud;
    double longitud;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario_nueva_organizacion);

        //envio de clase actual para las preferencias
        sesion = new Sesion(this);
        sesionUsuario = new SesionUsuario(this);
        SharedPreferences preferences = getSharedPreferences("credencial", Context.MODE_PRIVATE);
        id_usu  = preferences.getInt("usuario_ingreso",id_usu);
        //

        nombreOrganizacion = (EditText) findViewById(R.id.txtNombreOrganizacion);
        telefonoFijo = (EditText) findViewById(R.id.txtNumeroTelefonoFijo);
        telefonoCelular = (EditText) findViewById(R.id.txtNumeroTelefonoCelular);
        direccionOrganizacion = (EditText) findViewById(R.id.txtDireccion);
        emailOrganizacion = (EditText) findViewById(R.id.txtEmail);
        descrpcionOrganizacion = (EditText) findViewById(R.id.txtDescripcion);
        latitudOrganizacion = (EditText) findViewById(R.id.txtlatitudOrganizacion);
        imageButton = findViewById(R.id.imagenOrganizacionUsuario);

        latitudOrganizacion.setText("123123");  ///ingresar la latitud que el usuario selecciono

        longitudOrganizacion = (EditText) findViewById(R.id.txtlongitudOrganizacion);

        longitudOrganizacion.setText("-123334");    //ingresar la longitud que el usuario selecciono

        imagenOrganizacion = (ImageView) findViewById(R.id.imgimagenOrganizacion);
        guardar = (FloatingActionButton) findViewById(R.id.btnGuardar);
        spinnerCategorias = (Spinner) findViewById(R.id.spinercategoriaOrganizacion);
        spinnerRgiones = (Spinner) findViewById(R.id.spinerregionOrganizacion);
        ubicacion = (Button) findViewById(R.id.btnUbicacionOrganizacion);

        //Ingresar la ubicacion
        ubicacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FormularioNuevaOrganizacion.this, Ingresar_Ubicacion.class);


                startActivityForResult(intent,1);


            }
        });


        //llenado de spiner categorias y regiones
        listaCategorias=new ArrayList<ModeloSpinner>();
        listaRegiones=new ArrayList<ModeloSpinner>();
        //

        new llenarSpinnersNuevoPerfil().execute();


        if (getIntent().getExtras()!=null){
            id_usuario = getIntent().getExtras().getInt("id");

        }





        spinnerCategorias.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                id_categoria = listaCategorias.get(position).getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinnerRgiones.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                id_region = listaRegiones.get(position).getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestRead();
            }
        });

        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                imagenBitmap = ((BitmapDrawable)imagenOrganizacion.getDrawable()).getBitmap();

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

                validar();


                if (nombreOrganizacion.getError()==null && telefonoFijo.getError()==null && telefonoCelular.getError()==null && direccionOrganizacion.getError()==null && emailOrganizacion.getError()==null && descrpcionOrganizacion.getError()==null && latitudOrganizacion.getError()==null && longitudOrganizacion.getError()==null){
                    Toast.makeText(getApplicationContext(),"Procesando... Por favor espere",Toast.LENGTH_SHORT).show();
                    new crearPerfil().execute();
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

    }
    protected void onActivityResult(int requestCode,int resultCode, Intent data){
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {

                String latitud = data.getStringExtra("latitud");
                String longitud = data.getStringExtra("longitud");
                latitudOrganizacion.setText(latitud);
                longitudOrganizacion.setText(longitud);

            }
        }

        if(resultCode == RESULT_OK && requestCode == PICK_IMAGE){
            imageUri = data.getData();
            imagenOrganizacion.setImageURI(imageUri);
        }
    }

    public void requestRead() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
        } else {
            openGallery();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        if (requestCode == MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openGallery();
            } else {
                // Permission Denied
                Toast.makeText(getApplicationContext(), "Permission Denied", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void openGallery(){
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);
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
                if (sesionUsuario.logindimUsuario()){
                    Intent intent = new Intent(FormularioNuevaOrganizacion.this,PanelDeControlUsuarios.class);
                    intent.putExtra("id",id_usu);
                    //startActivity(new Intent(ActivityCategorias.this,PanelDeControlUsuarios.class));
                    startActivity(intent);
                    finish();

                }else {
                    Intent intent = new Intent(this, Login.class);
                    startActivity(intent);
                }

        }else if (id ==R.id.cerrarsecion){
                //cerrar secion y borrado de preferencias
                if(sesionUsuario.logindimUsuario()){
                    sesionUsuario.setLoginUsuario(false);
                    startActivity(new Intent(this, Login.class));
                    finish();
                }


        }else if (id == R.id.ediciondeCuenta){
            Intent intent = new Intent(this,EditarUsuario.class);
            if (getIntent().getExtras()!=null){
                //id_usuario_resibido_usuario = getIntent().getExtras().getInt("id");
                intent.putExtra("id",id_usu);
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


    private void validar(){

        //id.setError(null);
        nombreOrganizacion.setError(null);
        telefonoFijo.setError(null);
        telefonoCelular.setError(null);
        direccionOrganizacion.setError(null);
        emailOrganizacion.setError(null);
        descrpcionOrganizacion.setError(null);
        latitudOrganizacion.setError(null);
        longitudOrganizacion.setError(null);

        // String idd = id.getText().toString();
        String nombreOrganiza = nombreOrganizacion.getText().toString();
        String telefonoOrganiza = telefonoFijo.getText().toString();
        String celularOrganiza= telefonoCelular.getText().toString();
        String direccionOrganiza = direccionOrganizacion.getText().toString();
        String emailOrganiza = emailOrganizacion.getText().toString();
        String descripcionOrganiza = descrpcionOrganizacion.getText().toString();
        //String longitudOrganiza = String.valueOf(longitudOrganizacion).toString();
        //String latitudOrganiza = String.valueOf(latitudOrganizacion).toString();

        String longitudOrganiza = longitudOrganizacion.getText().toString();
        String latitudOrganiza = latitudOrganizacion.getText().toString();



        if(TextUtils.isEmpty(nombreOrganiza)){
            nombreOrganizacion.setError(getString(R.string.errNombreOrg));
            nombreOrganizacion.requestFocus();
            return;
        }
        if(TextUtils.isEmpty(telefonoOrganiza)){
            telefonoFijo.setError(getString(R.string.errNumero));
            telefonoFijo.requestFocus();
            return;
        }

        if(TextUtils.isEmpty(celularOrganiza)){
            telefonoCelular.setError(getString(R.string.errNumeroCelular));
            telefonoCelular.requestFocus();
            return;
        }
        if(TextUtils.isEmpty(direccionOrganiza)){
            direccionOrganizacion.setError(getString(R.string.errDir));
            direccionOrganizacion.requestFocus();
            return;
        }
        if(TextUtils.isEmpty(emailOrganiza)){
            emailOrganizacion.setError(getString(R.string.erremail));
            emailOrganizacion.requestFocus();
            return;
        }
        if(TextUtils.isEmpty(descripcionOrganiza)){
            descrpcionOrganizacion.setError(getString(R.string.errDesc));
            descrpcionOrganizacion.requestFocus();
            return;
        }
        if(TextUtils.isEmpty(latitudOrganiza)){
            latitudOrganizacion.setError(getString(R.string.errLat));
            latitudOrganizacion.requestFocus();
            return;
        }
        if(TextUtils.isEmpty(longitudOrganiza)){
           longitudOrganizacion.setError(getString(R.string.errLong));
            longitudOrganizacion.requestFocus();
            return;
        }


    }

    private class crearPerfil extends AsyncTask<String, Integer, Boolean> {
        private crearPerfil(){}
        boolean resul = true;

        @Override
        protected Boolean doInBackground(String... strings) {

            try {
                HttpClient httpclient;
                HttpPost httppost;
                ArrayList<NameValuePair> parametros;
                httpclient = new DefaultHttpClient();
                httppost = new HttpPost("https://shessag.000webhostapp.com/insertarPerfilUsuario.php");
                parametros = new ArrayList<NameValuePair>();
                parametros.add(new BasicNameValuePair("nomborg_rec",nombreOrganizacion.getText().toString()));
                parametros.add(new BasicNameValuePair("numtel_rec",telefonoFijo.getText().toString()));
                parametros.add(new BasicNameValuePair("numcel_rec",telefonoCelular.getText().toString()));
                parametros.add(new BasicNameValuePair("direccion_rec",direccionOrganizacion.getText().toString()));
                parametros.add(new BasicNameValuePair("email_rec",emailOrganizacion.getText().toString()));
                parametros.add(new BasicNameValuePair("desc_rec",descrpcionOrganizacion.getText().toString()));
                parametros.add(new BasicNameValuePair("lat_rec",latitudOrganizacion.getText().toString()));
                parametros.add(new BasicNameValuePair("longitud_rec",longitudOrganizacion.getText().toString()));
                parametros.add(new BasicNameValuePair("id_categoria",String.valueOf(id_categoria)));
                parametros.add(new BasicNameValuePair("id_region",String.valueOf(id_region)));
                parametros.add(new BasicNameValuePair("id_usuario",String.valueOf(id_usuario)));

                parametros.add(new BasicNameValuePair("imagen_rec",encodeImagen));

                httppost.setEntity(new UrlEncodedFormEntity(parametros, "UTF-8"));

                httpclient.execute(httppost);

               /* EntityUtils.toString(new DefaultHttpClient().execute(new HttpPost("?id_contacto="+id_perfilEditar
                        +"&nomborg_rec="+etnombreeorganizacion.getText().toString().replace(" ","%20")+
                        "&numtel_rec="+etnumerofijo.getText().toString()+"&numcel_rec="+etnumerocel.getText().toString()+"&direccion_rec="+
                        etdireccion.getText().toString().replace(" ","%20")+"&email_rec="+etemail.getText().toString()+"&desc_rec="
                        +etdescripcion.getText().toString().replace(" ","%20")+"&lat_rec="+etlatitud.getText().toString()+
                        "&longitud_rec="+etlongitud.getText().toString().replace("-","%2D")+"&id_categoria="+id_categoria+"&id_region="+id_region+"&imagen_rec="+  encodeImagen.toString())).getEntity());
                */
                resul = true;
            } catch (Exception ex) {
                Log.e("ServicioRest", "Error!", ex);
                resul = false;
            }
            return resul;

        }


        protected void onPostExecute(Boolean result) {
            if (resul) {
                validar();
                if (nombreOrganizacion.getError()==null && telefonoFijo.getError()==null && telefonoCelular.getError()==null && direccionOrganizacion.getError()==null && emailOrganizacion.getError()==null && descrpcionOrganizacion.getError()==null && latitudOrganizacion.getError()==null && longitudOrganizacion.getError()==null){
                    Toast.makeText(getApplicationContext(),"Perfil Creado Correctamente",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(FormularioNuevaOrganizacion.this,PanelDeControlUsuarios.class);
                    //startActivity(new Intent(FormularioNuevaOrganizacion.this,PanelDeControlUsuarios.class));
                    intent.putExtra("id",id_usuario);
                    startActivity(intent);
                    finish();
                }

            }else {
                Toast.makeText(getApplicationContext(), "Problemas de conexión", Toast.LENGTH_SHORT).show();
            }
        }

    }


    private class llenarSpinnersNuevoPerfil extends AsyncTask<String, Integer, Boolean> {
        private llenarSpinnersNuevoPerfil(){}
        boolean resul = true;

        @Override
        protected Boolean doInBackground(String... strings) {

            try {

                JSONArray regionesWS = new JSONArray(EntityUtils.toString(new DefaultHttpClient().execute(new HttpPost("https://shessag.000webhostapp.com/consultarRegiones.php")).getEntity()));
                JSONArray categoriasWS = new JSONArray(EntityUtils.toString(new DefaultHttpClient().execute(new HttpPost("https://shessag.000webhostapp.com/consultarCategorias.php")).getEntity()));
                //JSONArray usuariosWS = new JSONArray(EntityUtils.toString(new DefaultHttpClient().execute(new HttpPost("https://shessag.000webhostapp.com/ConsultarTodosLosUsuarios.php")).getEntity()));

                for (int i = 0; i < regionesWS.length(); i++) {
                    listaRegiones.add(new ModeloSpinner(regionesWS.getJSONObject(i).getString("nombre_region"),Integer.parseInt(regionesWS.getJSONObject(i).getString("id_region")))
                    );                }
                for (int i=0;i<categoriasWS.length();i++){
                    listaCategorias.add(new ModeloSpinner(categoriasWS.getJSONObject(i).getString("nombre_categoria"), Integer.parseInt(categoriasWS.getJSONObject(i).getString("id_categoria"))));
                }
               // for (int i=0;i<usuariosWS.length();i++){
               //     listaUsuarios.add(new ModeloSpinner(usuariosWS.getJSONObject(i).getString("nombre_usuario"),Integer.parseInt(usuariosWS.getJSONObject(i).getString("id_usuario"))));
               // }

                resul = true;
            } catch (Exception ex) {
                Log.e("ServicioRest", "Error!", ex);
                resul = false;
            }
            return resul;

        }

        protected void onPostExecute(Boolean result) {
            if (resul) {
                AdaptadorPersonalizadoSpinner adaptadorCategorias = new AdaptadorPersonalizadoSpinner(FormularioNuevaOrganizacion.this,R.layout.plantilla_spiners_personalizados_id_nombre,R.id.item_id_spinner,listaCategorias);
                AdaptadorPersonalizadoSpinner adaptadorRegiones = new AdaptadorPersonalizadoSpinner(FormularioNuevaOrganizacion.this,R.layout.plantilla_spiners_personalizados_id_nombre,R.id.item_id_spinner,listaRegiones);
                //AdaptadorPersonalizadoSpinner adaptadorUsuarios = new AdaptadorPersonalizadoSpinner(NuevoPerfil.this,R.layout.plantilla_spiners_personalizados_id_nombre,R.id.item_id_spinner,listaUsuarios);
                spinnerCategorias.setAdapter(adaptadorCategorias);
                spinnerRgiones.setAdapter(adaptadorRegiones);
                //spusuario.setAdapter(adaptadorUsuarios);

            }else {
                Toast.makeText(getApplicationContext(), "Problemas de conexión", Toast.LENGTH_SHORT).show();
            }
        }


    }


}
